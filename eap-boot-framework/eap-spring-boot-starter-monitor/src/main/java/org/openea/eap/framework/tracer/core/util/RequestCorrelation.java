package org.openea.eap.framework.tracer.core.util;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import java.util.UUID;
import java.util.regex.Pattern;

/** Request correlation for opt-in propagation to trusted downstream services. */
public final class RequestCorrelation {
    public static final String HEADER = "X-Request-Id";
    public static final String MDC_KEY = "request.id";
    private static final Pattern VALID = Pattern.compile("[A-Za-z0-9._-]{1,64}");

    private RequestCorrelation() {
    }

    public static String resolve(String value) {
        return value != null && VALID.matcher(value).matches() ? value : UUID.randomUUID().toString();
    }

    public static void propagate(HttpHeaders headers) {
        String current = MDC.get(MDC_KEY);
        if (current != null && VALID.matcher(current).matches() && !headers.containsHeader(HEADER)) {
            headers.set(HEADER, current);
        }
    }
}
