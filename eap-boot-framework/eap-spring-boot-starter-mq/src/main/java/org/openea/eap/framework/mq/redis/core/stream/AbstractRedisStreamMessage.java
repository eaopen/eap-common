package org.openea.eap.framework.mq.redis.core.stream;

import org.openea.eap.framework.mq.redis.core.message.AbstractRedisMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Redis Stream Message 抽象类
 *
 */
public abstract class AbstractRedisStreamMessage extends AbstractRedisMessage {

    /**
     * 获得 Redis Stream Key，默认使用类名
     *
     * @return Channel
     */
    @JsonIgnore // 避免序列化
    public String getStreamKey() {
        return getClass().getSimpleName();
    }

    /**
     * 消息最大长度（防止消息队列无限增长撑爆内存进行裁剪使用，默认不裁剪）
     */
    @JsonIgnore // 避免序列化
    private long msgMaxLen;

    @JsonIgnore // 避免序列化
    public long getMsgMaxLen() {
        return msgMaxLen;
    }

    @JsonIgnore // 避免序列化
    public void setMsgMaxLen(long msgMaxLen) {
        this.msgMaxLen = msgMaxLen;
    }
}
