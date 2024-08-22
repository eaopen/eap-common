package org.openea.eap.framework.datapermission.config;

import org.openea.eap.framework.datapermission.core.rpc.DataPermissionRequestInterceptor;
import org.openea.eap.framework.datapermission.core.rpc.DataPermissionRpcWebFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import static org.openea.eap.framework.common.enums.WebFilterOrderEnum.TENANT_CONTEXT_FILTER;

/**
 * 数据权限针对 RPC 的自动配置类
 *
 */
@AutoConfiguration
@ConditionalOnClass(name = "feign.RequestInterceptor")
public class EapDataPermissionRpcAutoConfiguration {

    @Bean
    public DataPermissionRequestInterceptor dataPermissionRequestInterceptor() {
        return new DataPermissionRequestInterceptor();
    }

    @Bean
    public FilterRegistrationBean<DataPermissionRpcWebFilter> dataPermissionRpcFilter() {
        FilterRegistrationBean<DataPermissionRpcWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DataPermissionRpcWebFilter());
        registrationBean.setOrder(TENANT_CONTEXT_FILTER - 1); // 顺序没有绝对的要求，在租户 Filter 前面稳妥点
        return registrationBean;
    }

}
