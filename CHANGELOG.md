# 更新日志（Changelog）

本文件记录 eap-common 各版本的对外变更，格式参考 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.1.0/)，
版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

- **发布记录（本文件）**：这一版**变了什么**，简洁、按版本倒序。
- **升级指南 [`doc/升级指南.md`](doc/升级指南.md)**：**怎么升级、要注意什么**，跨版本常青（版本矩阵、升级步骤、下游 Checklist）。

发布新版本时：把 `[Unreleased]` 提为 `[版本号] - 日期`，并把该版本小节直接作为代码托管平台的 Release Notes，避免两份内容漂移。

## [Unreleased]

## [2.9.2] - 2026-09-15

> ⚠️ 无破坏性 API 变更。下游升级前请核对[升级指南](doc/升级指南.md) §四：
> RabbitMQ 受信转换器（Spring AMQP ≥ 3.2.11）、jsqlparser 4.9 配方、easypoi 的 POI 钉版、Connector/J DATETIME。

### Added

- `eap-spring-boot-starter-mq`：新增 `org.openea.eap.framework.mq.rabbitmq.core.TrustedRootJackson2JavaTypeMapper`，
  实现“根包覆盖子包”的 RabbitMQ 可信类型映射，替代“逐包枚举、新增消息包就复发”的写法（配套 5 个单测）。
- 新增文档：[升级指南](doc/升级指南.md)、[2.9.2 升级下游回迁评估](doc/2.9.2-升级下游回迁评估.md)。

### Fixed

- `eap-spring-boot-starter-mq`：默认 `Jackson2JsonMessageConverter` 增加
  `@ConditionalOnMissingBean(MessageConverter.class)`。应用自定义转换器时默认 Bean 自动退让，
  避免 `ObjectProvider#getIfUnique()` 因候选不唯一返回 `null`，导致监听容器静默退回 `SimpleMessageConverter`。
- `eap-dependencies`：`poi-ooxml-schemas` 管理版本由 `${poi.version}=5.4.0` 修正为独立属性 `4.1.2`。
  该构件自 POI 5.x 起已停止发布（最后一个真实版本为 4.1.2），原管理项指向不存在的坐标，
  导致依赖它的 `easypoi-base` 4.4.0 解析失败。`poi`/`poi-ooxml` 仍保持 5.4.0（easyexcel 4.0.3 需要 POI 5.x）。

### Changed

- `eap-dependencies`、`eap-cloud-dependencies`：standalone pom（无 parent，不继承根 `${revision}`）的 `revision`
  同步升级到 2.9.2。不同步会让根 pom 以 `${revision}` 导入 BOM 时出现两个版本，`mvn` 读取 POM 阶段即报
  `Non-resolvable import POM`。
- README 版本徽标、示例配置、`doc/技术架构分析.md` 项目版本同步为 2.9.2。

## [2.9.1] - 2026-08-30

JDK 17 基线版本：未做包名或自动配置机制变更，属 minor 升级。

### Added

- 共享框架与 BOM 升级到 JDK 17 基线、Spring Boot 3.5.16（Spring Framework 6.2.x）、Spring Boot Admin 3.5.10。

### Changed

- `eap-dependencies`、`eap-cloud-dependencies` 与根 pom 的 `revision` 统一到 2.9.1 版本线。
- 补发 **2.9.1b**、**2.9.1c**：仅 `revision` 与开发环境 Nexus 仓库地址调整，无功能变更。

### Fixed

- OSSRH 部署配置迁移到 Central Publishing Portal。

## [2.8.5] - 2025-10-24

### Changed

- Spring Boot 升级到 3.5.6。
- 移除 dev106 profile 配置。

## 更早版本

2.8.5 之前的版本记录见 git tag 与提交历史。
