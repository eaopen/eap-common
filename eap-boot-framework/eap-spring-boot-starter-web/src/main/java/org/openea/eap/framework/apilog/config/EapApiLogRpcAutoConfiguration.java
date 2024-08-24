package org.openea.eap.framework.apilog.config;

import org.openea.eap.module.infra.api.logger.ApiAccessLogApi;
import org.openea.eap.module.infra.api.logger.ApiErrorLogApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * API 日志使用到 Feign 的配置项
 *
 */
@AutoConfiguration
@EnableFeignClients(clients = {ApiAccessLogApi.class, // 主要是引入相关的 API 服务
        ApiErrorLogApi.class})
public class EapApiLogRpcAutoConfiguration {
}
