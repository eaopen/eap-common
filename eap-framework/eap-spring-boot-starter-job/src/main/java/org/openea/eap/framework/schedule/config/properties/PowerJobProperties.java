package org.openea.eap.framework.schedule.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import tech.powerjob.common.enums.Protocol;
import tech.powerjob.worker.common.constants.StoreStrategy;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.WorkflowContext;

/**
 * PowerJob properties configuration class.
 *
 */
@ConfigurationProperties(prefix = "powerjob")
public class PowerJobProperties {

    private final Worker worker = new Worker();

    public Worker getWorker() {
        return worker;
    }

    /**
     * Powerjob worker configuration properties.
     */
    @Setter
    @Getter
    public static class Worker {

        /**
         * Whether to enable PowerJob Worker
         */
        private boolean enabled = true;

        /**
         * Name of application, String type. Total length of this property should be no more than 255
         * characters. This is one of the required properties when registering a new application. This
         * property should be assigned with the same value as what you entered for the appName.
         */
        private String appName;
        /**
         * port
         */
        private Integer port;

        /**
         * 绑定地址，一般填写本机网卡地址
         * JVM 参数
         * BIND_LOCAL_ADDRESS = "powerjob.network.local.address"
         */
        private String localAddress;
        /**
         * 扩展外部访问地址，用于类似容器内环境
         * 当存在 NAT 等场景时可通过单独传递外部地址来实现通讯
         * JVM 参数
         * NT_EXTERNAL_ADDRESS = "powerjob.network.external.address"
         * NT_EXTERNAL_PORT = "powerjob.network.external.port"
         */
        private String externalAddress;
        private String externalPort;
        /**
         * Address(es) of Powerjob-server node(s). Ip:port or domain.
         * Example of single Powerjob-server node:
         * <p>
         * 127.0.0.1:7700
         * </p>
         * Example of Powerjob-server cluster:
         * <p>
         * 192.168.0.10:7700,192.168.0.11:7700,192.168.0.12:7700
         * </p>
         */
        private String serverAddress;
        /**
         * Protocol for communication between WORKER and server
         */
        private Protocol protocol = Protocol.AKKA;
        /**
         * Local store strategy for H2 database. {@code disk} or {@code memory}.
         */
        private StoreStrategy storeStrategy = StoreStrategy.DISK;
        /**
         * Max length of response result. Result that is longer than the value will be truncated.
         * {@link ProcessResult} max length for #msg
         */
        private int maxResultLength = 8192;
        /**
         * If allowLazyConnectServer is set as true, PowerJob worker allows launching without a direct connection to the server.
         * allowLazyConnectServer is used for conditions that your have no powerjob-server in your develop env so you can't startup the application
         */
        private boolean allowLazyConnectServer = false;
        /**
         * Max length of appended workflow context value length. Appended workflow context value that is longer than the value will be ignored.
         * {@link WorkflowContext} max length for #appendedContextData
         */
        private int maxAppendedWfContextLength = 8192;

        private String tag;
        /**
         * Max numbers of LightTaskTacker
         */
        private Integer maxLightweightTaskNum = 1024;
        /**
         * Max numbers of HeavyTaskTacker
         */
        private Integer maxHeavyweightTaskNum = 64;
        /**
         * Interval(s) of worker health report
         */
        private Integer healthReportInterval = 10;

        /**
         * Whether to check quartz job
         */
        private boolean checkQuartzJob = false;

        /**
         * Whether to check spring schedule
         */
        private boolean checkSpringSchedule = false;
    }

}
