package org.openea.eap.framework.schedule.powerjob;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.openea.eap.framework.schedule.config.properties.PowerJobProperties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import tech.powerjob.common.PowerJobDKey;
import tech.powerjob.worker.PowerJobSpringWorker;
import tech.powerjob.worker.common.PowerJobWorkerConfig;
import tech.powerjob.worker.extension.processor.ProcessorFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
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

        String localAddress = this.properties.getWorker().getLocalAddress();
        if(ObjectUtil.isNotEmpty(localAddress)){
            System.setProperty(PowerJobDKey.BIND_LOCAL_ADDRESS, localAddress);
        }

        String externalAddress = this.properties.getWorker().getExternalAddress();
        if(ObjectUtil.isNotEmpty(externalAddress)){
            System.setProperty(PowerJobDKey.NT_EXTERNAL_ADDRESS, externalAddress);
            if(this.properties.getWorker().getProtocol().name().equalsIgnoreCase("HTTP")){
//                String localAddress = System.getProperty(PowerJobDKey.BIND_LOCAL_ADDRESS);
//                if(ObjectUtil.isEmpty(localAddress)){
//                    System.setProperty(PowerJobDKey.BIND_LOCAL_ADDRESS, externalAddress);
//                }
            }
        }
        String externalPort = this.properties.getWorker().getExternalPort();
        if(ObjectUtil.isNotEmpty(externalPort)){
            System.setProperty(PowerJobDKey.NT_EXTERNAL_PORT, externalPort);
        }
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        log.info("[EapPowerJobSpringWorker] init success");
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
