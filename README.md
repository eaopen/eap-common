# Open EAP

<div align="center">

![Open EAP Logo](https://raw.githubusercontent.com/eaopen/openea-eap/dev/doc/yudao/ruoyi-vue-pro-architecture.png)

**开放企业应用平台 - 基于Spring Boot 3.x的企业级应用开发框架**

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE.txt)
[![Java](https://img.shields.io/badge/Java-17+-green.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Version](https://img.shields.io/badge/Version-2.8.5-orange.svg)](https://github.com/eaopen/openea-eap)

</div>

## 📖 项目简介

Open EAP（开放企业应用平台）是一个基于Spring Boot 3.x的企业级应用开发框架，整合了多个开源项目，为企业应用快速开发提供基础框架支持。

### 🎯 设计理念

- **eap-common**：基础框架共用依赖包，不可单独部署，为eap-boot和eap-cloud提供基础支持
- **eap-boot**：单体应用框架，包含完整的企业应用功能模块
- **eap-cloud**：微服务框架，支持从单体到微服务的无缝迁移

### ✨ 核心特性

- 🚀 **前后端分离**：Spring Boot 3.x + Vue2/Element-UI
- 🏢 **多租户支持**：完整的SaaS多租户解决方案
- 🌍 **国际化支持**：完整的i18n国际化框架
- 🔄 **工作流引擎**：集成Flowable/Activiti工作流
- 🛠️ **低代码平台**：在线表单设计、流程设计、代码生成
- ☁️ **微服务就绪**：单体架构可无缝切换为微服务
- 🔒 **安全可靠**：Spring Security + JWT + 数据权限
- 📊 **监控完善**：SkyWalking链路追踪 + Spring Boot Admin

## 🏗️ 架构设计

### 模块结构

```
eap-common-pom (根模块)
├── eap-dependencies          # Maven BOM依赖版本管理
├── eap-boot-common          # 基础框架库 (不可单独部署)
│   ├── eap-common           # 核心工具类、异常处理、通用POJO
│   └── eap-common-biz-api   # 业务模块间共享API接口
├── eap-boot-framework       # Spring Boot Starter组件集合
│   ├── eap-spring-boot-starter-web
│   ├── eap-spring-boot-starter-security
│   ├── eap-spring-boot-starter-mybatis
│   └── ... (更多组件)
└── eap-cloud-framework      # 微服务框架组件
    ├── eap-spring-boot-starter-rpc
    └── eap-spring-cloud-starter-rpc-all
```

### 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17+ | 编程语言 |
| Spring Boot | 3.5.3 | 应用框架 |
| Spring Security | 5.7.6+ | 安全框架 |
| MyBatis Plus | 3.5.12 | ORM框架 |
| Redis | 6.0+ | 缓存数据库 |
| MySQL | 8.0+ | 关系数据库 |
| Flowable | 7.0.1 | 工作流引擎 |
| Vue.js | 2.x | 前端框架 |
| Element UI | - | UI组件库 |

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Node.js 16+ (前端开发)

### 本地开发

1. **克隆项目**
```bash
git clone https://github.com/eaopen/openea-eap.git
cd openea-eap
```

2. **数据库初始化**
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE eap DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入初始化脚本
mysql -u root -p eap < sql/eap.sql
```

3. **修改配置**
```yaml
# application-local.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/eap
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
```

4. **启动应用**
```bash
# 编译项目
mvn clean install -DskipTests

# 启动后端服务
cd eap-server
mvn spring-boot:run

# 启动前端 (另开终端)
cd eap-ui-admin
npm install
npm run dev
```

5. **访问系统**
- 后端API: http://localhost:48080
- 前端界面: http://localhost:8080
- API文档: http://localhost:48080/swagger-ui.html

### Docker部署

```bash
# 使用docker-compose一键启动
docker-compose up -d
```

## 📚 主要功能

### 系统管理
- **用户权限管理**：用户、角色、菜单、部门、岗位管理
- **系统配置**：字典、参数、国际化、租户管理
- **监控审计**：操作日志、登录日志、在线用户、系统监控

### 工作流程
- **流程引擎**：Flowable/OpenBPM双引擎支持
- **流程设计**：可视化流程设计器、表单设计器
- **流程管理**：待办、已办、抄送、委托、监控

### 低代码平台
- **数据建模**：业务实体设计、对象关系建模
- **表单设计**：拖拽式表单设计、列表配置
- **代码生成**：前后端代码自动生成、单元测试

### 基础设施
- **文件管理**：本地存储、云存储(OSS/COS/七牛云)、MinIO
- **消息通知**：短信、邮件、站内信、消息队列
- **任务调度**：Quartz、XXL-Job、PowerJob

### 2.2 模块架构

#### 2.2.1 模块分类说明

**基础框架模块（不可单独部署）**
- `eap-dependencies`：Maven BOM依赖版本管理
- `eap-boot-common`：通用基础库，为其他模块提供基础支持
  - `eap-common`：核心工具类、异常处理、通用POJO等基础框架
  - `eap-common-biz-api`：业务模块间共享的API接口定义

**框架组件模块（Spring Boot Starter）**
- `eap-boot-framework`：Boot框架组件集合，提供各种技术能力
- `eap-cloud-framework`：Cloud框架组件，提供微服务支持

**业务应用模块（可部署）**
- `eap-module-system`：系统管理模块
- `eap-module-infra`：基础设施模块  
- `eap-module-lowcode`：低代码平台模块
- `eap-server`：应用启动模块

#### 2.2.2 核心模块结构

```
eap-common-pom (根模块)
├── eap-dependencies (依赖管理)
├── eap-boot-common (通用基础库 - 不可单独部署)
│   ├── eap-common (核心工具类和基础框架)
│   └── eap-common-biz-api (业务共享API)
├── eap-boot-framework (Boot框架组件)
│   ├── eap-spring-boot-starter-web (Web组件)
│   ├── eap-spring-boot-starter-security (安全组件)
│   ├── eap-spring-boot-starter-mybatis (数据访问组件)
│   ├── eap-spring-boot-starter-redis (缓存组件)
│   ├── eap-spring-boot-starter-mq (消息队列组件)
│   ├── eap-spring-boot-starter-job (任务调度组件)
│   ├── eap-spring-boot-starter-monitor (监控组件)
│   ├── eap-spring-boot-starter-protection (服务保障组件)
│   ├── eap-spring-boot-starter-excel (Excel处理组件)
│   ├── eap-spring-boot-starter-websocket (WebSocket组件)
│   ├── eap-spring-boot-starter-test (测试组件)
│   ├── eap-spring-boot-starter-biz-tenant (多租户组件)
│   ├── eap-spring-boot-starter-biz-data-permission (数据权限组件)
│   └── eap-spring-boot-starter-biz-ip (IP地址组件)
└── eap-cloud-framework (Cloud框架组件)
    ├── eap-spring-boot-starter-env (环境配置)
    ├── eap-spring-boot-starter-rpc (RPC组件)
    └── eap-spring-cloud-starter-rpc-all (Cloud RPC组件)
```

#### 2.2.2 部署架构

**单体架构模式**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   前端应用       │    │   后端服务       │    │   数据存储       │
│  Vue2 + Element │◄──►│ Spring Boot 3.x │◄──►│ MySQL + Redis   │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

**微服务架构模式**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   前端应用       │    │   API网关        │    │   注册中心       │
│  Vue2 + Element │◄──►│    APISIX       │◄──►│    Nacos        │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                ┌───────────────┼───────────────┐
                │               │               │
        ┌───────▼──────┐ ┌──────▼──────┐ ┌─────▼──────┐
        │  系统服务     │ │  工作流服务  │ │  业务服务   │
        │ System Service│ │ BPM Service │ │Biz Service │
        └──────────────┘ └─────────────┘ └────────────┘
```

## 3. 核心技术栈

### 3.1 后端技术栈

| 技术组件 | 版本 | 用途说明 |
|---------|------|----------|
| Spring Boot | 3.5.3 | 应用开发框架 |
| Spring Security | 5.7.6+ | 安全认证框架 |
| MyBatis Plus | 3.5.12 | ORM框架 |
| Druid | 1.2.25 | 数据库连接池 |
| Redis | 5.0/6.0+ | 缓存数据库 |
| Redisson | 3.41.0 | Redis客户端 |
| MySQL | 5.7/8.0+ | 关系型数据库 |
| Flowable | 7.0.1 | 工作流引擎 |
| Quartz | 2.3.2 | 任务调度 |
| Knife4j | 4.6.0 | API文档 |
| Hutool | 5.8.35 | Java工具库 |
| MapStruct | 1.6.3 | Bean转换 |
| Lombok | 1.18.38 | 代码简化 |

### 3.2 前端技术栈

| 技术组件 | 版本 | 用途说明 |
|---------|------|----------|
| Vue.js | 2.x | 前端框架 |
| Element UI | - | UI组件库 |
| Axios | - | HTTP客户端 |
| Vue Router | - | 路由管理 |
| Vuex | - | 状态管理 |

### 3.3 数据库支持

- **MySQL** 5.7/8.0+（主要支持）
- **Oracle** 
- **PostgreSQL**
- **SQL Server**
- **MariaDB**
- **TiDB**
- **达梦数据库** (DM8)
- **人大金仓** (KingBase)
- **OpenGauss**
- **TDengine**

## 4. 系统功能模块

### 4.1 系统管理功能

#### 4.1.1 用户权限管理
- **用户管理**：用户信息维护、状态管理
- **角色管理**：角色权限分配、数据范围权限
- **菜单管理**：系统菜单配置、按钮权限标识
- **部门管理**：组织机构管理、树形结构展示
- **岗位管理**：用户职务配置

#### 4.1.2 系统配置管理
- **字典管理**：系统数据字典维护
- **参数配置**：系统动态参数配置
- **国际化管理**：多语言支持、词条翻译
- **租户管理**：多租户配置、套餐管理
- **应用管理**：SSO单点登录应用管理

#### 4.1.3 监控审计功能
- **操作日志**：系统操作记录和查询
- **登录日志**：用户登录记录、异常监控
- **在线用户**：当前活跃用户监控
- **系统监控**：Java/Redis/MySQL性能监控
- **错误码管理**：系统错误码统一管理

### 4.2 工作流程管理

#### 4.2.1 流程引擎
- **Flowable引擎**：标准BPMN 2.0流程引擎
- **OpenBPM引擎**：定制化Activiti引擎
- **流程设计器**：可视化流程设计工具
- **表单设计器**：动态表单设计工具

#### 4.2.2 流程管理
- **待办事宜**：个人待处理任务
- **已办事宜**：已处理任务历史
- **抄送事宜**：抄送任务管理
- **我发起的**：个人发起流程跟踪
- **流程委托**：任务委托处理
- **流程监控**：流程执行状态监控

### 4.3 低代码平台

#### 4.3.1 数据建模
- **业务实体设计**：数据模型设计
- **业务对象设计**：对象关系建模
- **数据字典**：业务数据字典管理
- **数据源配置**：多数据源管理

#### 4.3.2 表单设计
- **表单设计器**：拖拽式表单设计
- **列表设计器**：数据列表配置
- **对话框设计**：弹窗表单设计
- **表单模板**：可复用表单模板

#### 4.3.3 代码生成
- **基于表单生成**：表单优先代码生成
- **基于数据表生成**：数据库表结构生成
- **前后端代码**：完整的CRUD代码生成
- **单元测试**：自动生成测试代码

### 4.4 基础设施服务

#### 4.4.1 文件管理
- **本地存储**：本地文件系统存储
- **云存储**：阿里云OSS、腾讯云COS、七牛云
- **MinIO**：私有云对象存储
- **FTP存储**：FTP文件服务器

#### 4.4.2 消息通知
- **短信服务**：阿里云、腾讯云短信平台
- **邮件服务**：SMTP邮件发送
- **站内信**：系统内部消息通知
- **消息队列**：Redis Stream/Pub-Sub

#### 4.4.3 任务调度
- **Quartz调度**：标准任务调度
- **XXL-Job**：分布式任务调度
- **PowerJob**：新一代分布式任务调度

## 5. 系统特色功能

### 5.1 多租户架构
- **租户隔离**：数据完全隔离
- **租户套餐**：灵活的功能权限配置
- **租户管理**：租户生命周期管理

### 5.2 国际化支持
- **多语言**：支持中英文等多语言
- **词条管理**：统一的国际化词条管理
- **自动翻译**：集成翻译服务
- **前后端一体化**：前后端统一国际化方案

### 5.3 数据权限
- **部门权限**：基于组织架构的数据权限
- **角色权限**：基于角色的访问控制
- **自定义权限**：灵活的权限规则配置

### 5.4 服务保障
- **分布式锁**：基于Redis的分布式锁
- **幂等组件**：防重复请求处理
- **限流熔断**：基于Resilience4j的服务保障
- **链路追踪**：SkyWalking链路追踪

## 6. 部署架构

### 6.1 单体部署
```yaml
# 推荐配置
服务器配置:
  CPU: 4核心
  内存: 8GB
  存储: 100GB SSD

软件环境:
  JDK: 17+
  MySQL: 8.0+
  Redis: 6.0+
  Nginx: 1.20+
```

### 6.2 微服务部署
```yaml
# 微服务组件
基础组件:
  - API网关: APISIX
  - 注册中心: Nacos
  - 配置中心: Nacos
  - 链路追踪: SkyWalking
  - 监控: Spring Boot Admin

业务服务:
  - 系统服务: eap-module-system
  - 基础设施服务: eap-module-infra
  - 工作流服务: eap-module-bpm
  - 低代码服务: eap-module-lowcode
```

## 7. 开发指南

### 7.1 项目结构
```
openea-eap/
├── eap-dependencies/          # 依赖版本管理 (BOM)
├── eap-boot-common/          # 通用基础库 (不可单独部署，为其他模块提供基础支持)
│   ├── eap-common/           # 核心工具类、异常处理、通用POJO等
│   └── eap-common-biz-api/   # 业务模块间共享的API接口
├── eap-boot-framework/       # Boot框架组件 (Spring Boot Starter集合)
├── eap-cloud-framework/      # Cloud框架组件 (微服务支持)
├── eap-module-system/        # 系统管理模块 (用户、角色、权限等)
├── eap-module-infra/         # 基础设施模块 (文件、短信、邮件等)
├── eap-module-lowcode/       # 低代码平台模块 (表单设计、代码生成等)
└── eap-server/              # 应用启动模块 (可部署的应用程序)
```

### 7.2 开发规范
- **代码规范**：遵循阿里巴巴Java开发规范
- **数据库规范**：统一的表结构设计规范
- **API规范**：RESTful API设计规范
- **文档规范**：完整的接口文档和开发文档

### 7.3 扩展开发
- **自定义组件**：基于Spring Boot Starter机制
- **业务模块**：标准的模块化开发
- **插件机制**：支持插件化扩展
- **主题定制**：前端主题和样式定制

## 8. 性能与安全

### 8.1 性能优化
- **数据库优化**：索引优化、SQL优化
- **缓存策略**：多级缓存、缓存预热
- **异步处理**：消息队列、异步任务
- **CDN加速**：静态资源CDN分发

### 8.2 安全保障
- **认证授权**：JWT Token + Spring Security
- **数据加密**：敏感数据加密存储
- **SQL注入防护**：MyBatis参数化查询
- **XSS防护**：输入输出过滤
- **CSRF防护**：CSRF Token验证

## 9. 运维监控

### 9.1 系统监控
- **应用监控**：Spring Boot Admin
- **性能监控**：JVM、数据库、Redis监控
- **链路追踪**：SkyWalking分布式追踪
- **日志管理**：统一日志收集和分析

### 9.2 告警机制
- **阈值告警**：性能指标阈值监控
- **异常告警**：系统异常实时告警
- **业务告警**：业务指标监控告警

## 10. 总结

Open EAP是一个功能完整、架构清晰的企业级应用开发平台，具有以下优势：

1. **技术先进**：基于最新的Spring Boot 3.x技术栈
2. **架构灵活**：支持单体到微服务的平滑演进
3. **功能丰富**：涵盖企业应用开发的各个方面
4. **扩展性强**：良好的模块化设计和插件机制
5. **开发效率高**：低代码平台和代码生成器
6. **国际化支持**：完整的多语言解决方案
7. **安全可靠**：完善的安全机制和服务保障

该平台适合中大型企业的信息化建设，可以快速构建各类企业应用系统。
## 
附录A：详细技术组件清单

### A.1 Spring Boot Starter组件详情

| 组件名称 | 功能描述 | 主要依赖 |
|---------|----------|----------|
| eap-spring-boot-starter-web | Web层基础组件，包含MVC、异常处理、参数校验等 | Spring MVC, Validation, Jackson |
| eap-spring-boot-starter-security | 安全认证组件，JWT Token、权限控制 | Spring Security, JWT |
| eap-spring-boot-starter-mybatis | 数据访问层组件，MyBatis Plus增强 | MyBatis Plus, Dynamic DataSource |
| eap-spring-boot-starter-redis | Redis缓存组件，分布式锁、缓存管理 | Redisson, Spring Data Redis |
| eap-spring-boot-starter-mq | 消息队列组件，Redis Stream/Pub-Sub | Redis, RocketMQ |
| eap-spring-boot-starter-job | 任务调度组件，定时任务管理 | Quartz, XXL-Job |
| eap-spring-boot-starter-monitor | 监控组件，性能监控、链路追踪 | SkyWalking, Spring Boot Admin |
| eap-spring-boot-starter-protection | 服务保障组件，限流、熔断、降级 | Resilience4j, Lock4j |
| eap-spring-boot-starter-excel | Excel处理组件，导入导出 | EasyExcel, POI |
| eap-spring-boot-starter-websocket | WebSocket组件，实时通信 | Spring WebSocket |
| eap-spring-boot-starter-test | 测试组件，单元测试、集成测试 | JUnit 5, Mockito, TestContainers |

### A.2 业务组件详情

| 组件名称 | 功能描述 | 应用场景 |
|---------|----------|----------|
| eap-spring-boot-starter-biz-tenant | 多租户组件，数据隔离、租户管理 | SaaS应用、多租户系统 |
| eap-spring-boot-starter-biz-data-permission | 数据权限组件，行级数据权限控制 | 企业应用、权限管理 |
| eap-spring-boot-starter-biz-ip | IP地址组件，IP归属地查询 | 用户行为分析、地域统计 |

## 附录B：数据库设计规范

### B.1 表命名规范
```sql
-- 系统表前缀：system_
-- 基础设施表前缀：infra_
-- 工作流表前缀：bpm_
-- 业务表前缀：biz_

-- 示例表结构
CREATE TABLE system_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(30) NOT NULL COMMENT '用户账号',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(30) NOT NULL COMMENT '用户昵称',
    email VARCHAR(50) COMMENT '邮箱',
    mobile VARCHAR(11) COMMENT '手机号码',
    sex TINYINT COMMENT '用户性别',
    avatar VARCHAR(512) COMMENT '头像地址',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '帐号状态',
    login_ip VARCHAR(50) COMMENT '最后登录IP',
    login_date DATETIME COMMENT '最后登录时间',
    creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted BIT NOT NULL DEFAULT FALSE COMMENT '是否删除',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号'
) ENGINE=InnoDB COMMENT='用户信息表';
```

### B.2 字段设计规范
- **主键**：统一使用 `id` 作为主键，类型为 `BIGINT AUTO_INCREMENT`
- **时间字段**：创建时间 `create_time`，更新时间 `update_time`
- **操作人字段**：创建者 `creator`，更新者 `updater`
- **逻辑删除**：使用 `deleted` 字段，类型为 `BIT`
- **多租户**：使用 `tenant_id` 字段，类型为 `BIGINT`

## 附录C：API设计规范

### C.1 RESTful API规范
```java
// 控制器示例
@RestController
@RequestMapping("/admin-api/system/user")
@Tag(name = "管理后台 - 用户")
@Validated
public class UserController {

    @GetMapping("/page")
    @Operation(summary = "获得用户分页列表")
    public CommonResult<PageResult<UserRespVO>> getUserPage(@Valid UserPageReqVO pageReqVO) {
        // 实现逻辑
    }

    @PostMapping("/create")
    @Operation(summary = "新增用户")
    public CommonResult<Long> createUser(@Valid @RequestBody UserSaveReqVO createReqVO) {
        // 实现逻辑
    }

    @PutMapping("/update")
    @Operation(summary = "修改用户")
    public CommonResult<Boolean> updateUser(@Valid @RequestBody UserSaveReqVO updateReqVO) {
        // 实现逻辑
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户")
    public CommonResult<Boolean> deleteUser(@RequestParam("id") Long id) {
        // 实现逻辑
    }
}
```

### C.2 统一响应格式
```java
// 统一响应结果
@Data
public class CommonResult<T> implements Serializable {
    public static Integer CODE_SUCCESS = 0;
    
    private Integer code;
    private String msg;
    private T data;
    
    // 成功响应
    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = CODE_SUCCESS;
        result.data = data;
        result.msg = "success";
        return result;
    }
    
    // 错误响应
    public static <T> CommonResult<T> error(Integer code, String message) {
        CommonResult<T> result = new CommonResult<>();
        result.code = code;
        result.msg = message;
        return result;
    }
}
```

## 附录D：配置文件示例

### D.1 application.yml配置
```yaml
server:
  port: 48080

spring:
  application:
    name: eap-server
  profiles:
    active: local
  
  # 数据源配置
  datasource:
    druid:
      initial-size: 5
      min-idle: 10
      max-active: 20
      max-wait: 600000
      time-between-eviction-runs-millis: 60000
      min-evictable-idle-time-millis: 300000
      max-evictable-idle-time-millis: 900000
      validation-query: SELECT 1 FROM DUAL
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
      
  # Redis配置
  redis:
    host: 127.0.0.1
    port: 6379
    database: 0
    timeout: 6000ms
    password:
    lettuce:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0

# MyBatis Plus配置
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: AUTO
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
  mapper-locations: classpath*:mapper/*.xml

# 日志配置
logging:
  level:
    root: INFO
    org.openea.eap: DEBUG
  pattern:
    console: "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}"

# Swagger配置
springdoc:
  api-docs:
    enabled: true
    path: /v3/api-docs
  swagger-ui:
    enabled: true
    path: /swagger-ui.html

# 业务配置
eap:
  info:
    version: 2.8.5
    base-package: org.openea.eap
  web:
    admin-api:
      prefix: /admin-api
      controller: org.openea.eap.module.**.controller.admin.**
  security:
    permit-all-urls:
      - /admin-api/system/auth/login
      - /admin-api/system/auth/logout
      - /admin-api/system/auth/refresh-token
  tenant:
    enable: true
    ignore-urls:
      - /admin-api/system/tenant/**
      - /admin-api/system/auth/login
```

### D.2 logback-spring.xml配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="30 seconds" debug="false">
    <springProfile name="dev,test">
        <logger name="org.openea.eap" level="DEBUG"/>
    </springProfile>
    
    <springProfile name="prod">
        <logger name="org.openea.eap" level="INFO"/>
        <logger name="org.springframework" level="WARN"/>
        <logger name="org.apache.ibatis" level="WARN"/>
    </springProfile>
    
    <!-- 控制台输出 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- 文件输出 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/eap-server.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/eap-server.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

## 附录E：部署脚本示例

### E.1 Docker部署
```dockerfile
# Dockerfile
FROM openjdk:17-jdk-slim

LABEL maintainer="openea@github.com"

# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone

# 创建应用目录
WORKDIR /app

# 复制应用文件
COPY target/eap-server.jar app.jar

# 暴露端口
EXPOSE 48080

# 启动应用
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

```yaml
# docker-compose.yml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: eap-mysql
    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: eap
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    command: --default-authentication-plugin=mysql_native_password

  redis:
    image: redis:6.2
    container_name: eap-redis
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data

  eap-server:
    build: .
    container_name: eap-server
    ports:
      - "48080:48080"
    depends_on:
      - mysql
      - redis
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/eap?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: 123456
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PORT: 6379

volumes:
  mysql_data:
  redis_data:
```

### E.2 Kubernetes部署
```yaml
# k8s-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: eap-server
  labels:
    app: eap-server
spec:
  replicas: 2
  selector:
    matchLabels:
      app: eap-server
  template:
    metadata:
      labels:
        app: eap-server
    spec:
      containers:
      - name: eap-server
        image: eap-server:2.8.5
        ports:
        - containerPort: 48080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:mysql://mysql-service:3306/eap"
        - name: SPRING_REDIS_HOST
          value: "redis-service"
        resources:
          requests:
            memory: "1Gi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 48080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 48080
          initialDelaySeconds: 30
          periodSeconds: 10

---
apiVersion: v1
kind: Service
metadata:
  name: eap-server-service
spec:
  selector:
    app: eap-server
  ports:
  - protocol: TCP
    port: 80
    targetPort: 48080
  type: LoadBalancer
```

---

**文档版本**：v1.0  
**最后更新**：2025年1月  
**维护者**：Open EAP开发团队