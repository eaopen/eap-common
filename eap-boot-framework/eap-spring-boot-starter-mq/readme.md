### 简介

消息队列默认采用redis stream实现，可扩展消息队列，如kafka、rabbitmq等

### RabbitMQ 转换器约定

`EapRabbitMQAutoConfiguration#createMessageConverter` 注册的是带 `@ConditionalOnMissingBean(MessageConverter.class)`
的默认 `Jackson2JsonMessageConverter`：

- 应用**未**自定义 `MessageConverter` 时，由本 starter 提供默认转换器；
- 应用**已**自定义 `MessageConverter`（例如按自身消息包配置受信包）时，本默认 Bean 自动退让。

这样做的原因：Spring Boot 装配监听容器工厂时用 `ObjectProvider#getIfUnique()` 挑选转换器，
若同时存在两个候选且无 `@Primary`，`getIfUnique()` 会返回 `null`，容器将静默退回
`SimpleMessageConverter`，导致 POJO 载荷反序列化失败且难以定位。

另注意 Spring AMQP 自 3.2.11 起，无参 `Jackson2JsonMessageConverter` 的默认可信包已由 `*`
收紧为 `[java.util, java.lang]`；业务应用若收发自定义消息类型，需自行配置可信包并标记 `@Primary`。

### 基础知识
- 集群消费和广播消费  
https://help.aliyun.com/zh/apsaramq-for-rocketmq/cloud-message-queue-rocketmq-4-x-series/developer-reference/clustering-consumption-and-broadcasting-consumption


- RabbitMQ
https://yu0doc.openea.site/Spring-Boot/RabbitMQ/?yudao


### 消息队列
- 消息队列（Redis）
https://yu1doc.openea.site/message-queue/redis/


- 消息队列（RabbitMQ）
https://yu1doc.openea.site/message-queue/rabbitmq



