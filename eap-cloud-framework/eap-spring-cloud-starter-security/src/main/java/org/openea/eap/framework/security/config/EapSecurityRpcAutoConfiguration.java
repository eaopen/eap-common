package org.openea.eap.framework.security.config;

import org.openea.eap.framework.security.core.rpc.LoginUserRequestInterceptor;
import org.openea.eap.module.system.api.oauth2.OAuth2TokenApiClient;
import org.openea.eap.module.system.api.permission.PermissionApiClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * Security 使用到 Feign 的配置项
 *
 */
@AutoConfiguration
@EnableFeignClients(clients = {OAuth2TokenApiClient.class, // 主要是引入相关的 API 服务
        PermissionApiClient.class})
public class EapSecurityRpcAutoConfiguration {

    @Bean
    public LoginUserRequestInterceptor loginUserRequestInterceptor() {
        return new LoginUserRequestInterceptor();
    }

}
