package org.openea.eap.framework.mq.rabbitmq.core;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * {@link TrustedRootJackson2JavaTypeMapper} 的“根包覆盖子包”语义与安全边界测试。
 *
 * <p><b>背景：</b>Spring AMQP 3.2.11 起无参转换器默认可信包收紧为 {@code [java.util, java.lang]}，
 * 应用升级到 Spring Boot 3.5.x（spring-amqp 3.2.12）后业务消息类反序列化失败。本测试锁定框架侧
 * 提供的“根包覆盖子包”能力：根包之下的类必须放行，根包之外的类必须继续拒绝，
 * 且 {@code org.openea.eapx} 这类“前缀相同但不是子包”的包名不能被误放行。
 */
class TrustedRootJackson2JavaTypeMapperTest {

    private static final String ROOT_PACKAGE = "org.openea.eap";

    /**
     * 根包之下（本测试类所在包）的消息类，应当被放行。
     */
    static class TrustedMessage {
    }

    @Test
    void shouldTrustClassUnderRootPackage() {
        TrustedRootJackson2JavaTypeMapper mapper = new TrustedRootJackson2JavaTypeMapper(ROOT_PACKAGE);
        MessageProperties properties = new MessageProperties();
        properties.setHeader(mapper.getClassIdFieldName(), TrustedMessage.class.getName());

        assertThat(mapper.toJavaType(properties).getRawClass()).isEqualTo(TrustedMessage.class);
    }

    @Test
    void shouldRejectClassOutsideRootPackage() {
        TrustedRootJackson2JavaTypeMapper mapper = new TrustedRootJackson2JavaTypeMapper(ROOT_PACKAGE);
        MessageProperties properties = new MessageProperties();
        // com.fasterxml.jackson.databind 不在受信根包之下，必须继续拒绝，避免放开为信任 *
        properties.setHeader(mapper.getClassIdFieldName(), "com.fasterxml.jackson.databind.JsonNode");

        assertThatThrownBy(() -> mapper.toJavaType(properties))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("trusted packages");
    }

    @Test
    void shouldStillTrustBuiltInJavaPackages() {
        TrustedRootJackson2JavaTypeMapper mapper = new TrustedRootJackson2JavaTypeMapper(ROOT_PACKAGE);
        MessageProperties properties = new MessageProperties();
        // java.lang 属于父类内置可信包；选非容器类型，避免触发 __ContentTypeId__ 解析
        properties.setHeader(mapper.getClassIdFieldName(), String.class.getName());

        assertThat(mapper.toJavaType(properties).getRawClass()).isEqualTo(String.class);
    }

    /**
     * 边界锁定：{@code org.openea.eapx} 与 {@code org.openea.eap} 前缀相同但不是其子包，
     * 不能被误判为受信，否则会意外放宽信任范围。
     */
    @Test
    void shouldNotTreatSiblingPrefixAsSubPackage() {
        assertThat(TrustedRootJackson2JavaTypeMapper.isUnderRoot("org.openea.eap", ROOT_PACKAGE)).isTrue();
        assertThat(TrustedRootJackson2JavaTypeMapper.isUnderRoot("org.openea.eap.obpm.lite.mq", ROOT_PACKAGE)).isTrue();
        assertThat(TrustedRootJackson2JavaTypeMapper.isUnderRoot("org.openea.eapx", ROOT_PACKAGE)).isFalse();
        assertThat(TrustedRootJackson2JavaTypeMapper.isUnderRoot("org.openea", ROOT_PACKAGE)).isFalse();
    }

    /**
     * 端到端：把映射器装配到 {@link Jackson2JsonMessageConverter} 后，
     * {@code __TypeId__} 指向根包之下的类可以被正常解析为对应类型。
     */
    @Test
    void shouldResolveClassThroughConverter() {
        TrustedRootJackson2JavaTypeMapper mapper = new TrustedRootJackson2JavaTypeMapper(ROOT_PACKAGE);
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setJavaTypeMapper(mapper);
        MessageProperties properties = new MessageProperties();
        properties.setHeader(mapper.getClassIdFieldName(), TrustedMessage.class.getName());

        assertThat(converter.getJavaTypeMapper().toJavaType(properties).getRawClass())
                .isEqualTo(TrustedMessage.class);
    }
}
