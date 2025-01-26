package org.openea.eap.framework.datapermission.core.rpc;

import org.openea.eap.framework.datapermission.core.aop.DataPermissionContextHolder;
import org.openea.eap.framework.datapermission.core.util.DataPermissionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 针对 {@link DataPermissionRequestInterceptor} 的 RPC 调用，设置 {@link DataPermissionContextHolder} 的上下文
 *
 */
public class DataPermissionRpcWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String enable = request.getHeader(DataPermissionRequestInterceptor.ENABLE_HEADER_NAME);
        if (Objects.equals(enable, Boolean.FALSE.toString())) {
            DataPermissionUtils.executeIgnore(() -> {
                try {
                    chain.doFilter(request, response);
                } catch (IOException | ServletException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            chain.doFilter(request, response);
        }
    }

}
