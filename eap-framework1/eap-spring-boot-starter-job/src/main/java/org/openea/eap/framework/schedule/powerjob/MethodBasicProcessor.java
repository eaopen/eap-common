package org.openea.eap.framework.schedule.powerjob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.impl.JobExecutionContextImpl;
import tech.powerjob.common.serialize.JsonUtils;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
class MethodBasicProcessor implements BasicProcessor {

    private final Object bean;

    private final Method method;

    private final String type;  //   BasicProcessor/QuartzJobBean/@PowerJobHandle/@Scheduled

    public MethodBasicProcessor(Object bean, Method method, String type) {
        this.bean = bean;
        this.method = method;
        this.type = type;
    }

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        try {
            Object result = null;
            if("QuartzJobBean".equalsIgnoreCase(type)){
                result = invokeQuartzJob(bean, method, context);
            }else if("@Scheduled".equalsIgnoreCase(type)){
                result = method.invoke(bean);
            }else{
                result = method.invoke(bean, context);
            }
            return new ProcessResult(true, JsonUtils.toJSONString(result));
        } catch (InvocationTargetException ite) {
            ExceptionUtils.rethrow(ite.getTargetException());
        }
        return new ProcessResult(false, "IMPOSSIBLE");
    }

    protected Object invokeQuartzJob(Object bean, Method method, TaskContext context){
        Object result = null;
        try {
            JobExecutionContextImpl quartzContext = new JobExecutionContextImpl(null, null, null);
            JSONObject json = JSONUtil.parseObj(context);
            for(String key : json.keySet()){
                quartzContext.put(key, json.get(key));
            }
            result = method.invoke(bean, quartzContext);
        } catch (Throwable t) {
            log.warn("invokeQuartzJob fail, msg="+t.getMessage()+", bean="+bean.getClass().getName());
        }
        return result;
    }

}
