package org.openea.eap.framework.schedule.config;

import com.alibaba.ttl.TtlRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务 Configuration
 */
@AutoConfiguration
@EnableAsync
@Slf4j
public class EapAsyncAutoConfiguration
//        implements AsyncConfigurer
{

    @Bean
    public BeanPostProcessor threadPoolTaskExecutorBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (!(bean instanceof ThreadPoolTaskExecutor)) {
                    return bean;
                }
                // 修改提交的任务，接入 TransmittableThreadLocal
                ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) bean;
                executor.setTaskDecorator(TtlRunnable::get);
                return executor;
            }

        };
    }

    @Primary
    @Bean("threadPoolTaskExecutor")
    //@Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置线程池核心容量
        executor.setCorePoolSize(10);
        // 设置线程池最大容量
        executor.setMaxPoolSize(50);
        // 设置任务队列长度
        executor.setQueueCapacity(2000);
        // 设置线程超时时间
        executor.setKeepAliveSeconds(30);
        // 设置线程名称前缀
        executor.setThreadNamePrefix("sysTaskExecutor");
        // 设置任务丢弃后的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setTaskDecorator( r ->{
            //实现线程上下文穿透， 异步线程内无法获取之前的Request，租户信息等， 如有新的上下文对象在此处添加
            //此方法在请求结束后在无法获取request, 下方完整异步Servlet请求
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
//            String dataSourceId = DataSourceContextHolder.getDatasourceId();
//            String dataSourceName = DataSourceContextHolder.getDatasourceName();
//            Boolean isAssign = DataSourceContextHolder.isAssignDataSource();
//            UserInfo userInfo = UserProvider.getUser();
            return () -> {
                try {
                    if(attributes!= null) {
                        RequestContextHolder.setRequestAttributes(attributes);
                    }
//                    if(dataSourceId != null || dataSourceName != null){
//                        DataSourceContextHolder.setDatasource(dataSourceId, dataSourceName, isAssign);
//                    }
//                    UserProvider.setLocalLoginUser(userInfo);
                    r.run();
                } finally {
//                    UserProvider.clearLocalUser();
                    RequestContextHolder.resetRequestAttributes();
//                    DataSourceContextHolder.clearDatasourceType();
                }
            };
        });
        return executor;
    }

//    @Bean("defaultExecutor")
//    public ThreadPoolTaskExecutor getAsyncExecutorDef(@Qualifier("threadPoolTaskExecutor") Executor executor) {
//        return (ThreadPoolTaskExecutor) executor;
//    }

}
