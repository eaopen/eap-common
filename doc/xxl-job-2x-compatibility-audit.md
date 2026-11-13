# 2.x XXL-JOB 依赖兼容审计（2026-11-13）

## 当前结论

- 正式版 EAP 2.9.3.1 使用 xxl-job-core 2.4.2，2.x 开发 BOM 使用 3.2.0（由依赖更新引入）。
- 对本仓直接使用的 XxlJobExecutor、XxlJobSpringExecutor、XxlJobContext、XxlJobHelper、@XxlJob，已用 Java 17 javap 对照 2.4.2/3.2.0：EAP 调用的公开方法签名均存在。
- 新增 XxlJobClientCompatibilityTest，验证配置 Setter、ThreadLocal Job 上下文、成功/失败状态、注解声明；不会访问正式调度中心。
- 上游 3.0.0 发行说明声明 2.4.x 及以上客户端的协议/数据模型向前兼容；3.2.0 带来 SSRF/越权修复，但这不是“2.4.x 调度中心 + 3.2.0 执行器已完成实机验收”的证明。
- 因此暂保留 3.2.0，不在维护线上盲目降回 2.4.2；2.9.4 发布前须完成真实调度中心的兼容验证。

上游资料：https://github.com/xuxueli/xxl-job/releases/tag/3.2.0
相关变更：https://github.com/xuxueli/xxl-job/releases

## 2.9.4 放行门禁（独立隔离环境，不连接生产执行器）

1. 记录实际调度中心版本及执行器版本，覆盖旧中心 2.4.2 + 新 core 3.2.0；若所有中心都已升级，也应记录现网配置。
2. 验证 accessToken/执行器注册、心跳、发现与摘除，禁止使用真实生产调度地址或密钥做自动化测试。
3. 以无害任务验证触发、任务参数、执行结果回调、日志分页/rollinglog、失败重试、停止和任务超时行为。
4. 验证 @TenantJob 的多租户上下文、异常后失败状态和租户间隔离；不能只验证代码能编译。
5. 若升级**调度中心**到 3.2.0，必须另行审查账号表的密码列与 token 字段及密码重置流程；只升级 executor core 不应修改中心账号库。
6. 版本不兼容时优先单独调整 executor/admin 配对，保留已修复漏洞，不在 EAP 整个 2.x BOM 中做未经审核的降级。

## 其他范围边界

- 这次是依赖兼容性测试与审计文档，不改变版本号、不升级调度中心、不执行真实业务任务。
- 3.x/dev 仍是 Java 21 / Boot 4 技术主干；本次只针对 2.x Java 17 维护线。
