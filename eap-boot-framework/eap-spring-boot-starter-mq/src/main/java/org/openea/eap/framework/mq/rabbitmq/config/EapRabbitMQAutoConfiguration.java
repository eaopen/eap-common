package org.openea.eap.framework.mq.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * RabbitMQ 消息队列配置类
 *
 */
@AutoConfiguration
@Slf4j
@ConditionalOnClass(name = "org.springframework.amqp.rabbit.core.RabbitTemplate")
public class EapRabbitMQAutoConfiguration {

    /**
     * Jackson2JsonMessageConverter Bean：使用 jackson 序列化消息
     *
     * <p><b>修改逻辑：</b>加 {@link ConditionalOnMissingBean}，仅当应用未自行定义
     * {@link MessageConverter} 时才注册本默认转换器；应用自定义转换器后本 Bean 自动退让。
     *
     * <p><b>修改原因：</b>本 Bean 原为无条件注册，会与应用自定义的转换器同时存在，
     * 而 Spring Boot 装配 {@code SimpleRabbitListenerContainerFactory} 时通过
     * {@code ObjectProvider#getIfUnique()} 挑选转换器：候选不唯一且无 {@code @Primary} 时返回
     * {@code null}，监听容器工厂会静默退回 {@link org.springframework.amqp.support.converter.SimpleMessageConverter}，
     * 导致 POJO 载荷反序列化失败且难以定位。
     *
     * <p>同时，Spring AMQP 自 3.2.11 起把无参 {@code Jackson2JsonMessageConverter} 的默认可信包
     * 由 {@code *} 收紧为 {@code [java.util, java.lang]}，各应用需要按自身消息包配置受信转换器
     * （并通常标记 {@code @Primary}）。若本 Bean 不退让，这些自定义转换器就会与之并存而失效。
     */
    @Bean
    @ConditionalOnMissingBean(MessageConverter.class)
    public MessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
