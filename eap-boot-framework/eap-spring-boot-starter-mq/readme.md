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

#### 根包覆盖子包（推荐写法）

`DefaultJackson2JavaTypeMapper` 的可信包判断是“包名精确相等”，既不支持父包前缀，也不支持
`org.openea.eap.*` 通配。逐个枚举精确包名会带来“新增消息包就漏配置、再次反序列化失败”的复发风险，
因此本 starter 提供 `TrustedRootJackson2JavaTypeMapper`：配置一个根包，即可覆盖其下所有子包，
且仍只放行根包之下的类型（安全面远窄于信任 `*`）。

```java
@Bean
@Primary // 应用内存在多个候选时，保证监听容器工厂的 getIfUnique() 能选中本转换器
public Jackson2JsonMessageConverter rabbitMessageConverter() {
    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
    converter.setJavaTypeMapper(new TrustedRootJackson2JavaTypeMapper("org.openea.eap"));
    return converter;
}
```

生产端 `RabbitTemplate` 与消费端 `@RabbitListener` 应共用同一个转换器（对称读写 `__TypeId__`），
避免“能发不能收”或反之。

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



