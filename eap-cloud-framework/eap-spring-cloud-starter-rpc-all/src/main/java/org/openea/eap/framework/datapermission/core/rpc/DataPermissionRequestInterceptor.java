package org.openea.eap.framework.datapermission.core.rpc;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.openea.eap.framework.datapermission.core.annotation.DataPermission;
import org.openea.eap.framework.datapermission.core.aop.DataPermissionContextHolder;

/**
 * DataPermission 的 RequestInterceptor 实现类：Feign 请求时，将 {@link DataPermission} 设置到 header 中，继续透传给被调用的服务
 *
 * 注意：由于 {@link DataPermission} 不支持序列化和反序列化，所以暂时只能传递它的 enable 属性
 *
 */
public class DataPermissionRequestInterceptor implements RequestInterceptor {

    public static final String ENABLE_HEADER_NAME = "data-permission-enable";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        DataPermission dataPermission = DataPermissionContextHolder.get();
        if (dataPermission != null && Boolean.FALSE.equals(dataPermission.enable())) {
            requestTemplate.header(ENABLE_HEADER_NAME, "false");
        }
    }

}
