# Boot4 稳定版与近期分支修复对齐（2026-11-05）

> 从原 `boot4` HEAD `9d58a2d` 派生独立候选分支 `feature/boot4-stable-refresh-20261105`。本轮 JDK21 整仓本地测试和配对跨仓测试通过后，经用户授权可合入 **仅用于继续研发的 boot4 分支**；`dev`、维护分支、生产环境均不修改。

## 版本矩阵

| 技术 | 原 boot4 | 本轮候选 | 决策依据 |
| --- | --- | --- | --- |
| JDK | 21 | 21 | Java 21 LTS，升级技术基线不强制改变业务数据 |
| Spring Boot | 4.1.0 | 4.1.2 | 2026-10-23 官方稳定维护发布 |
| Spring Framework / Security / Jackson | 随 Boot BOM | 随 Boot 4.1.2 BOM | 不单独覆盖，避免混用 Spring 版本 |
| springdoc-openapi | 3.0.3 | 3.2.0 | 官方 Boot4.1.x 兼容线 |
| MyBatis-Plus / Generator | 3.5.17 | 3.5.18.1 | 2026-10-27 补丁修复 Boot4 Starter 隔离与全表操作检查 |
| MyBatis-Spring | 4.1.0 | 4.1.0 | 已是 Boot4.1 稳定线 |
| Spring Cloud | 2025.1.2 | 2025.1.4 | 2026-10-23 同系列稳定维护发布 |
| Spring Cloud Alibaba | 2025.1.0.0 | 2025.1.0.0 | 官方仍为此 Boot4 支持系列；需 Nacos 实测 |
| Druid | 1.2.28 | 1.2.28 | Boot4 已支持，暂不扩大变更 |
| EAP 构件 revision | 3.0.0 | 3.0.1-SNAPSHOT | 隔离候选，避免覆盖原 3.0.0 制品 |

## 从近期其他分支移植的必要改动

- `dev` 2026-09 / 11 月 JWT 签发随机 jti、判断历史缺少 jti 的 JWT、移除运行时对 MapStruct 编译期内部包的依赖；保留旧 JWT 签名识别能力，不让重复登录生成相同凭证。
- `jjwt-jackson` 作为 `runtime` 依赖加入 BOM 及 Security Starter，防止打包后签发/解析旧格式失败；补 JWT 相关回归测试。
- 已移植 `dev` 的 RabbitMQ 默认转换器 `@ConditionalOnMissingBean`、`TrustedRootJackson2JavaTypeMapper`、使用说明和两组回归测试；本轮在 Boot4/Java21 编译且新增 8 项单测通过。**仅证明代码/单测兼容，不能替代真实 RabbitMQ 生产消息的序列化、消费与历史消息回放验证。**
- 已从 `dev` 同步 `poi-ooxml-schemas` 无效版本修复：POI 5.x 仍由 5.4.0 管理，旧 `poi-ooxml-schemas` 固定为最后真实发布的 4.1.2，不再假设存在 5.4.0 制品；涉及 Easypoi 4.x 的旧业务仍需实际 Excel 读写测试。
- `maintenance/2.8.5` / `maintenance/2.9.0` 的其他更改属于旧技术线回迁与版本管理，不直接整分支合并到 Boot4。

## 兼容边界

Boot4 技术升级必须在保留旧数据库、旧表单和历史业务数据的前提下完成。旧表单、业务对象、权限、流程与动态列表仍须完整读写；Schema v2 是可选增量，不是 Java/Boot 版本切换的前置条件。

旧 Token 处理单独遵守安全原则：不符合新令牌唯一性、类型边界要求的旧凭证可以要求重新登录，但不能因兼容而允许 refresh token 被当作 access bearer。

## 版本与测试门禁

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
# 在 eap-common:
mvn -B -Dflatten.skip=true test
mvn -B -DskipTests install
# 若 eap-cloud-dependencies 未随根 reactor 构建，须单独验证并安装 Cloud BOM
mvn -f eap-cloud-dependencies/pom.xml -B -DskipTests install
# 然后在同一版本线的 eap-boot 运行完整测试及 compile
```

开发基线合并与生产发布验收分离：本轮本机测试通过后可合并到 `boot4` 供持续开发；必须另外补齐干净 Maven CI、依赖冲突审计、认证/权限/JWT 两格式、真实消息序列化、旧业务 DB 和流程完整读写回归及 EAP 实际启动等证据。**未完成这些项目不得合并 dev 或部署生产，不能据本轮 boot4 合并宣称旧数据业务兼容已验收。**

## 2026-11-05 本机验证记录

- macOS / GraalVM JDK 21.0.7 / Maven 3.9.13；`mvn -B -Dflatten.skip=true test` 整仓 **25/25 Reactor 模块 SUCCESS**。
- 新增 JWT 测试 4 项、RabbitMQ 转换器与可信类型映射器测试共 8 项，均全部通过；其他已有模块单测也通过。
- `mvn -B -DskipTests install` 成功：使用正常 Flatten 生成可供跨仓消费的真实 POM；`eap-cloud-dependencies` 单独安装成功。
- 尚未对真实 RabbitMQ、生产身份服务、旧数据库与复杂业务历史表单做端到端验证；生产发布仍不允许。

## 官方版本来源

- Spring Boot: https://spring.io/blog/2026/10/23/spring-boot-4-1-2-available-now/
- Springdoc: https://springdoc.org/faq.html
- MyBatis-Plus: https://github.com/baomidou/mybatis-plus/releases/tag/v3.5.18.1
- Spring Cloud: https://github.com/spring-cloud/spring-cloud-release/releases/tag/v2025.1.4
- Spring Cloud Alibaba: https://github.com/alibaba/spring-cloud-alibaba/releases/tag/2025.1.0.0