package org.openea.eap.framework.quartz.config;


import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openea.eap.framework.quartz.config.properties.XxlJobProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;


/**
 * 分布式任务调度支持
 *
 * 集成 xxl-job
 * https://github.com/eaopen/xxl-job
 */
@AutoConfiguration
@EnableConfigurationProperties(XxlJobProperties.class)
@RequiredArgsConstructor
@Slf4j
public class EapXxlJobConfig {

    private final XxlJobProperties xxlJobProperties;

    @Bean
    @ConditionalOnProperty(prefix = "xxl.job", name = "enable", havingValue = "true")
    public XxlJobSpringExecutor xxlJobExecutor() {
        if (StringUtils.isEmpty(this.xxlJobProperties.getAdminAddresses())) {
            return null;
        }
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAccessToken(this.xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setAdminAddresses(this.xxlJobProperties.getAdminAddresses());

        xxlJobSpringExecutor.setAppname(this.xxlJobProperties.getExecutor().getAppName()); // 执行器AppName：执行器心跳注册分组依据；为空则关闭自动注册
        xxlJobSpringExecutor.setAddress(this.xxlJobProperties.getExecutor().getAddress());
        xxlJobSpringExecutor.setIp(this.xxlJobProperties.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(this.xxlJobProperties.getExecutor().getPort());
        xxlJobSpringExecutor.setLogPath(this.xxlJobProperties.getExecutor().getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(this.xxlJobProperties.getExecutor().getLogRetentionDays());

        return xxlJobSpringExecutor;
    }
}
