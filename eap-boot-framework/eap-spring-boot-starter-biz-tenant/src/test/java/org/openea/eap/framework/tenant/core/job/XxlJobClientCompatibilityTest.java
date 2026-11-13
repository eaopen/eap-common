package org.openea.eap.framework.tenant.core.job;

import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Smoke checks for EAP's existing XXL-JOB executor API when upgrading xxl-job-core.
 * This is intentionally offline: do not initialize the executor or contact a real admin.
 */
class XxlJobClientCompatibilityTest {

    @Test
    void historicalExecutionContextAndCallbackStatusArePreserved() {
        XxlJobContext previous = XxlJobContext.getXxlJobContext();
        XxlJobContext context = new XxlJobContext(123L, "tenant-job", "/tmp/xxl-job-compat.log", 0, 1);
        try {
            XxlJobContext.setXxlJobContext(context);
            assertSame(context, XxlJobContext.getXxlJobContext());
            assertEquals(123L, XxlJobHelper.getJobId());
            assertEquals("tenant-job", XxlJobHelper.getJobParam());

            assertTrue(XxlJobHelper.handleSuccess("ok"));
            assertEquals(XxlJobContext.HANDLE_CODE_SUCCESS, context.getHandleCode());
            assertEquals("ok", context.getHandleMsg());

            assertTrue(XxlJobHelper.handleFail("failed"));
            assertEquals(XxlJobContext.HANDLE_CODE_FAIL, context.getHandleCode());
            assertEquals("failed", context.getHandleMsg());
        } finally {
            XxlJobContext.setXxlJobContext(previous);
        }
    }

    @Test
    void legacyExecutorConfigurationSettersRemainCallable() {
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        assertDoesNotThrow(() -> {
            executor.setAdminAddresses("http://localhost:9090/xxl-job-admin");
            executor.setAccessToken("test-only");
            executor.setIp("127.0.0.1");
            executor.setPort(9999);
            executor.setAppname("eap-compat-test");
            executor.setLogPath("/tmp/xxl-job-compat");
            executor.setLogRetentionDays(7);
        });
        // Deliberately do NOT start() the executor: that would register externally.
    }

    @Test
    void annotatedJobHandlerContractRemainsDiscoverable() throws Exception {
        Method method = ExampleHandler.class.getDeclaredMethod("run");
        XxlJob annotation = method.getAnnotation(XxlJob.class);
        assertNotNull(annotation);
        assertEquals("eapCompatibilitySmoke", annotation.value());
    }

    static class ExampleHandler {
        @XxlJob("eapCompatibilitySmoke")
        public void run() { }
    }
}
