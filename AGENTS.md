# EAP Common 协作指南

## 适用范围与入口

本文件适用于整个 `eap-common` 仓库，是开发人员、Codex、Claude Code 及其他编码智能体的统一协作约定。规则仅在此维护，避免不同工具的说明重复或冲突。

- 人工开发：开始修改前阅读本文件与 [README.md](README.md)。
- Codex：将本文件作为仓库级工作指引。
- Claude Code：从 [CLAUDE.md](CLAUDE.md) 跳转到本文件，不在 `CLAUDE.md` 重复维护规则。

## 仓库定位

- `eap-common` 是 EAP 的 Java 17 共享框架与 Maven BOM，不是可独立部署的应用。
- 保持现有模块目录和 `org.openea.eap` 包名不变；历史 `com.fhs` 兼容包未经明确迁移计划不得重命名。
- 对外提供的 API 以兼容为先。涉及破坏性改动时，必须明确迁移方式、影响范围和验证结果。
- 三方依赖版本集中维护在 `eap-dependencies/pom.xml`；平台与构建插件版本维护在根 `pom.xml`。不要在子模块重复声明版本。

## 修改流程

1. 先定位所属模块、调用方和已有测试，再做最小范围修改。
2. 参考 yudao 时仅吸收能力与实现思路，保留 EAP 的模块、包名、扩展点和兼容行为；禁止引入 `cn.iocoder.yudao` 包名。
3. 不覆盖、回退或格式化无关的工作区改动；不修改 `target/`、`.flattened-pom.xml` 等生成文件。
4. 修改共享行为、依赖版本或模块职责时，同步更新 README 和相关技术文档；文档中的版本、构建命令和部署边界必须与代码一致。

## 代码与测试约定

- 框架行为变化应补充或调整测试；MyBatis 非持久化字段、JDBC 空值规范化等场景要在断言中显式处理。
- 优先执行受影响模块测试；共享框架或 BOM 变更必须执行完整验证。
- 提交结论时说明：修改范围、验证命令、结果，以及尚未验证的风险（如有）。

## 构建与验证

```bash
# 在 eap-common 根目录
mvn -q -Dflatten.skip=true test
mvn -q -DskipTests install
```

修改后的 `eap-common` 必须先安装到本地 Maven 仓库，供同级 `eap-boot` 编译和测试。
