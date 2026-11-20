package org.openea.eap.framework.tracer.core.filter;

import org.openea.eap.framework.common.util.monitor.TracerUtils;
import org.openea.eap.framework.tracer.core.util.RequestCorrelation;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
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
        // 设置响应 traceId
        response.addHeader(HEADER_NAME_TRACE_ID, TracerUtils.getTraceId());
        String requestId = RequestCorrelation.resolve(request.getHeader(RequestCorrelation.HEADER));
        String previous = MDC.get(RequestCorrelation.MDC_KEY);
        MDC.put(RequestCorrelation.MDC_KEY, requestId);
        try {
            response.setHeader(RequestCorrelation.HEADER, requestId);
            chain.doFilter(request, response);
        } finally {
            if (previous == null) {
                MDC.remove(RequestCorrelation.MDC_KEY);
            } else {
                MDC.put(RequestCorrelation.MDC_KEY, previous);
            }
        }
    }

}
