package org.openea.eap.framework.schedule.powerjob;

import lombok.extern.slf4j.Slf4j;
import org.openea.eap.framework.schedule.config.properties.PowerJobProperties;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.Scheduled;
import tech.powerjob.worker.annotation.PowerJobHandler;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.extension.processor.ProcessorBean;
import tech.powerjob.worker.extension.processor.ProcessorDefinition;
import tech.powerjob.worker.processor.impl.AbstractBuildInSpringProcessorFactory;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * PowerJob 的 ProcessorFactory 实现类
 * support powerJob, quartz, spring schedule
 */
@Slf4j
public class EapSpringProcessorFactory extends AbstractBuildInSpringProcessorFactory {

    protected static final List<String> jobHandlerRepository = new LinkedList<>();

    protected final static String DELIMITER = "#";

    protected PowerJobProperties properties;  // extend properties

    public EapSpringProcessorFactory(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    public EapSpringProcessorFactory(ApplicationContext applicationContext, PowerJobProperties properties) {
        super(applicationContext);
        this.properties = properties;
    }

    /**
     * 根据处理器定义构建处理器对象
     * 注意：Processor 为单例对象，即 PowerJob 对每一个 ProcessorBean 只调用一次 build 方法
     *
     * @param processorDefinition 处理器定义
     * @return null or ProcessorBean
     */
    @Override
    public ProcessorBean build(ProcessorDefinition processorDefinition) {
        try {
            // 1 prepare
            boolean canLoad = checkCanLoad();
            if (!canLoad) {
                log.info("[ProcessorFactory] can't find Spring env, this processor can't load by 'BuiltInSpringProcessorFactory'");
                return null;
            }
            boolean checkQuartzJob = this.properties.getWorker().isCheckQuartzJob();
            boolean checkSpringSchedule = this.properties.getWorker().isCheckSpringSchedule();

            String processorInfo = processorDefinition.getProcessorInfo();
            String type = "";
            //用于区分方法级别的参数
            String[] split = processorInfo.split(DELIMITER);
            String methodName = null;
            String className = split[0];
            if(split.length>1){
                methodName = split[1];
            }
            Object bean = getBean(className, applicationContext);

            // 2 check class or method
            // 2.1 check BasicProcessor
            if(bean.getClass().isAssignableFrom(ProcessorBean.class)){
                BasicProcessor basicProcessor = (BasicProcessor) bean;
                return new ProcessorBean()
                        .setProcessor(basicProcessor)
                        .setClassLoader(basicProcessor.getClass().getClassLoader());
            }

            // 2.2 check QuartzJobBean
            if(checkQuartzJob){
                try{
                    Class quartzJobClazz = Class.forName("org.springframework.scheduling.quartz.QuartzJobBean");
                    if(bean.getClass().isAssignableFrom(quartzJobClazz)){
                        type = "QuartzJobBean";
                        methodName = "executeInternal";
                    }
                }catch (Exception e){
                    log.warn(e.getMessage());
                }
            }

            // 2.3 check methods
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                // 2.4 check PowerJobHandler
                if(checkPowerJobHandler(methodName, method, bean)){
                    method.setAccessible(true);
                    registerJobHandler(methodName);

                    type = "@PowerJobHandle";
                    MethodBasicProcessor processor = new MethodBasicProcessor(bean, method, type);
                    return new ProcessorBean()
                            .setProcessor(processor)
                            .setClassLoader(processor.getClass().getClassLoader());
                }
                // 2.5 check quartz job
                if("QuartzJobBean".equalsIgnoreCase(type)
                        && checkQuartzJob(method, bean)){
                    method.setAccessible(true);
                    //type = "QuartzJobBean";
                    MethodBasicProcessor processor = new MethodBasicProcessor(bean, method, type);
                    return new ProcessorBean()
                            .setProcessor(processor)
                            .setClassLoader(processor.getClass().getClassLoader());
                }

                // 2.6 check spring schedule
                if(checkSpringSchedule && checkSpringScheduled(methodName, method)){
                    method.setAccessible(true);
                    type = "@Scheduled";
                    MethodBasicProcessor processor = new MethodBasicProcessor(bean, method, type);
                    return new ProcessorBean()
                            .setProcessor(processor)
                            .setClassLoader(processor.getClass().getClassLoader());
                }
            }
        } catch (NoSuchBeanDefinitionException ignore) {
            log.warn("[ProcessorFactory] can't find the processor in SPRING");
        } catch (Throwable t) {
            log.warn("[ProcessorFactory] load by EapSpringProcessorFactory failed. If you are using Spring, make sure this bean was managed by Spring", t);
        }

        return null;
    }

    protected boolean checkQuartzJob(Method method, Object bean){
        if(method.getName().equals("executeInternal")){
            return true;
        }
        return false;
    }

    protected boolean checkPowerJobHandler(String methodName, Method method, Object bean){
        PowerJobHandler powerJob = method.getAnnotation(PowerJobHandler.class);
        // CGLib代理对象拿不到该注解, 通过 AnnotationUtils.findAnnotation()可以获取到注解 by GitHub@zhangxiang0907 https://github.com/PowerJob/PowerJob/issues/770
        if (powerJob == null) {
            powerJob = AnnotationUtils.findAnnotation(method, PowerJobHandler.class);
        }

        if (powerJob == null) {
            return false;
        }
        String name = powerJob.name();
        //匹配到和页面定义相同的methodName
        if (!name.equals(methodName)) {
            return false;
        }
        if (name.trim().length() == 0) {
            throw new RuntimeException("method-jobhandler name invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
        }
        if (containsJobHandler(name)) {
            throw new RuntimeException("jobhandler[" + name + "] naming conflicts.");
        }
        return true;
    }

    protected boolean checkSpringScheduled(String methodName, Method method){
        try{
            Scheduled scheduled = method.getAnnotation(Scheduled.class);
            // CGLib代理对象拿不到该注解, 通过 AnnotationUtils.findAnnotation()可以获取到注解
            if (scheduled == null) {
                scheduled = AnnotationUtils.findAnnotation(method, Scheduled.class);
            }
            if (scheduled == null) {
                return false;
            }
            String name = method.getName();
            if (!name.equals(methodName)) {
                return false;
            }
            return true;
        }catch(Throwable t){
            log.warn("checkSpringScheduled fail: methodName="+methodName+", msg="+t.getMessage(), t);
            return false;
        }
    }

    public static void registerJobHandler(String name) {
        jobHandlerRepository.add(name);
    }


    protected boolean containsJobHandler(String name) {
        return jobHandlerRepository.contains(name);
    }
}
