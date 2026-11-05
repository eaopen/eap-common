package org.openea.eap.framework.mq.rabbitmq.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link EapRabbitMQAutoConfiguration} 默认转换器 Bean 的“退让”行为测试。
 *
 * <p><b>背景：</b>该 Bean 原为无条件注册，会与业务应用自定义的 {@link MessageConverter} 同时存在；
 * Spring Boot 装配监听容器工厂时用 {@code ObjectProvider#getIfUnique()} 挑选转换器，
 * 多候选且无 {@code @Primary} 时返回 {@code null}，容器便静默退回
 * {@code SimpleMessageConverter}，POJO 载荷反序列化失败且极难定位。
 *
 * <p>本测试锁定两条：应用未自定义时 starter 仍注册默认转换器（行为不变）；
 * 应用自定义时必须退让，保证候选唯一。
 */
class EapRabbitMQAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(EapRabbitMQAutoConfiguration.class));

    /**
     * 应用未自定义转换器：starter 继续提供默认转换器，对存量应用行为不变。
     */
    @Test
    void shouldRegisterDefaultConverterWhenApplicationHasNone() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(MessageConverter.class);
            assertThat(context.getBean(MessageConverter.class)).isInstanceOf(Jackson2JsonMessageConverter.class);
        });
    }

    /**
     * 关键回归点：应用自定义转换器后，starter 的默认转换器必须退让，不能出现两个候选。
     * 修复前（无 {@code @ConditionalOnMissingBean}）此处会有两个 Bean，断言失败。
     */
    @Test
    void shouldBackOffWhenApplicationDefinesItsOwnConverter() {
        contextRunner
                .withBean("customMessageConverter", MessageConverter.class, Jackson2JsonMessageConverter::new)
                .run(context -> {
                    assertThat(context).hasSingleBean(MessageConverter.class);
                    assertThat(context.getBean(MessageConverter.class))
                            .isSameAs(context.getBean("customMessageConverter"));
                });
    }

    /**
     * 直接锁定故障机理：候选必须唯一，{@code getIfUnique()} 才能选到应用的转换器。
     * 修复前这里拿到的是 {@code null}，监听容器工厂随即退回 {@code SimpleMessageConverter}。
     */
    @Test
    void shouldKeepConverterUniqueSoGetIfUniqueResolves() {
        contextRunner
                .withBean("customMessageConverter", MessageConverter.class, Jackson2JsonMessageConverter::new)
                .run(context -> assertThat(context.getBeanProvider(MessageConverter.class).getIfUnique())
                        .as("转换器候选不唯一时 getIfUnique() 返回 null，监听容器会静默退回 SimpleMessageConverter")
                        .isNotNull()
                        .isSameAs(context.getBean("customMessageConverter")));
    }
}
