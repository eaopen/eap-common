package org.openea.eap.framework.security.config;

import org.openea.eap.framework.common.biz.system.oauth2.OAuth2TokenCommonApi;
import org.openea.eap.framework.common.biz.system.permission.PermissionCommonApi;
import org.openea.eap.framework.security.core.rpc.LoginUserRequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * Security 使用到 Feign 的配置项
 *
 */
@AutoConfiguration
@EnableFeignClients(clients = {OAuth2TokenCommonApi.class, // 主要是引入相关的 API 服务
        PermissionCommonApi.class})
public class EapSecurityRpcAutoConfiguration {

    @Bean
    public LoginUserRequestInterceptor loginUserRequestInterceptor() {
        return new LoginUserRequestInterceptor();
    }

}
