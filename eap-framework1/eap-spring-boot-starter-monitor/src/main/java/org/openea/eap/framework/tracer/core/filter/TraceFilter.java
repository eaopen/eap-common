package org.openea.eap.framework.tracer.core.filter;

import cn.hutool.core.util.StrUtil;
import org.openea.eap.framework.common.util.monitor.TracerUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Trace 过滤器，打印 traceId 到 header 中返回
 *
 */
public class TraceFilter extends OncePerRequestFilter {

    /**
     * Header 名 - 链路追踪编号
     */
    private static final String HEADER_NAME_TRACE_ID = "trace-id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 设置响应 traceId, 允许外部传入
        String traceId = request.getHeader(HEADER_NAME_TRACE_ID);
        if(StrUtil.isEmpty(traceId)){
            traceId = TracerUtils.getTraceId();
            response.addHeader(HEADER_NAME_TRACE_ID, traceId);
        }
        // 继续过滤
        chain.doFilter(request, response);
    }

}
