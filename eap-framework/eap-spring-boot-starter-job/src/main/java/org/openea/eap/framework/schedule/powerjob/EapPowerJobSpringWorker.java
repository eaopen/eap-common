package org.openea.eap.framework.schedule.powerjob;

import com.google.common.collect.Lists;
import org.openea.eap.framework.schedule.config.properties.PowerJobProperties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import tech.powerjob.worker.PowerJobSpringWorker;
import tech.powerjob.worker.common.PowerJobWorkerConfig;
import tech.powerjob.worker.extension.processor.ProcessorFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EapPowerJobSpringWorker extends PowerJobSpringWorker {

    protected final PowerJobWorkerConfig workerConfig; // same as config in PowerJobSpringWorker
    protected PowerJobProperties properties;  // extend properties

    public EapPowerJobSpringWorker(PowerJobWorkerConfig config) {
        super(config);
        this.workerConfig = config;
    }

    public EapPowerJobSpringWorker(PowerJobWorkerConfig config, PowerJobProperties properties) {
        super(config);
        this.workerConfig = config;
        this.properties = properties;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        EapSpringProcessorFactory springProcessorFactory = new EapSpringProcessorFactory(applicationContext, this.properties);
        List<ProcessorFactory> processorFactories = Lists.newArrayList(
                Optional.ofNullable(this.workerConfig.getProcessorFactoryList())
                        .orElse(Collections.emptyList()));
        processorFactories.add(springProcessorFactory);
        this.workerConfig.setProcessorFactoryList(processorFactories);
    }
}
