package org.openea.eap.framework.schedule.powerjob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.extension.processor.ProcessorBean;
import tech.powerjob.worker.extension.processor.ProcessorDefinition;
import tech.powerjob.worker.processor.impl.BuiltInSpringProcessorFactory;

/**
 * PowerJob 的 ProcessorFactory 实现类，接管Quartz的Job
 *
 */
@Slf4j
public class QuartzJobProcessorFactory extends BuiltInSpringProcessorFactory {
    protected QuartzJobProcessorFactory(ApplicationContext applicationContext) {
        super(applicationContext);
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
        // todo 集成quartz  QuartzJobBean => BasicProcessor
        try {
            boolean canLoad = checkCanLoad();
            if (!canLoad) {
                log.info("[ProcessorFactory] can't find Spring env, this processor can't load by 'BuiltInSpringProcessorFactory'");
                return null;
            }
            String processorInfo = processorDefinition.getProcessorInfo();
            //用于区分方法级别的参数
            if (processorInfo.contains("#")) {
                return null;
            }
            // QuartzJobBean => BasicProcessor
            BasicProcessor basicProcessor = getBean(processorInfo, applicationContext);
            return new ProcessorBean()
                    .setProcessor(basicProcessor)
                    .setClassLoader(basicProcessor.getClass().getClassLoader());
        } catch (NoSuchBeanDefinitionException ignore) {
            log.warn("[ProcessorFactory] can't find the processor in SPRING");
        } catch (Throwable t) {
            log.warn("[ProcessorFactory] load by BuiltInSpringProcessorFactory failed. If you are using Spring, make sure this bean was managed by Spring", t);
        }

        return null;
    }
}
