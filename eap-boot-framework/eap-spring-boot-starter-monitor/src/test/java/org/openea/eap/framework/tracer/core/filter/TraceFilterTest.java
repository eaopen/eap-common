package org.openea.eap.framework.tracer.core.filter;

import org.junit.jupiter.api.Test;
import org.openea.eap.framework.tracer.core.util.RequestCorrelation;
import org.springframework.http.HttpHeaders;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class TraceFilterTest {
    @Test
    void validIdIsReturnedAndContextIsRestoredOnFailure() {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        request.addHeader("X-Request-Id", "gateway-01");
        MDC.put("request.id", "outer");
        try {
            assertThrows(IllegalStateException.class, () -> new TraceFilter().doFilter(request, response, (req, res) -> {
                assertEquals("gateway-01", MDC.get("request.id"));
                HttpHeaders headers = new HttpHeaders();
                RequestCorrelation.propagate(headers);
                assertEquals("gateway-01", headers.getFirst("X-Request-Id"));
                headers.set("X-Request-Id", "explicit");
                RequestCorrelation.propagate(headers);
                assertEquals("explicit", headers.getFirst("X-Request-Id"));
                throw new IllegalStateException();
            }));
            assertEquals("gateway-01", response.getHeader("X-Request-Id"));
            assertEquals("outer", MDC.get("request.id"));
        } finally {
            MDC.remove("request.id");
        }
    }

    @Test
    void missingOrInvalidIdsAreReplaced() throws Exception {
        for (String value : new String[]{null, "bad id", "x".repeat(65)}) {
            var request = new MockHttpServletRequest();
            var response = new MockHttpServletResponse();
            if (value != null) request.addHeader("X-Request-Id", value);
            new TraceFilter().doFilter(request, response, (req, res) ->
                    assertEquals(response.getHeader("X-Request-Id"), MDC.get("request.id")));
            UUID.fromString(response.getHeader("X-Request-Id"));
            assertNull(MDC.get("request.id"));
        }
    }
}
