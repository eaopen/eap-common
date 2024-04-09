package org.openea.eap.framework.quartz.core.powerjob;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import tech.powerjob.worker.PowerJobSpringWorker;
import tech.powerjob.worker.common.PowerJobWorkerConfig;

public class EapPowerJobSpringWorker extends PowerJobSpringWorker {

    protected PowerJobWorkerConfig workerConfig; // same as config in PowerJobSpringWorker

    public EapPowerJobSpringWorker(PowerJobWorkerConfig config) {
        super(config);
        workerConfig = config;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        super.setApplicationContext(applicationContext);
        // todo spring schedule @Scheduled
        // todo quartz Job/QuartzJobBean
        // 增加quartz job 的处理器
        // workerConfig.getProcessorFactoryList().add(new QuartzJobProcessorFactory(applicationContext));
    }
}
