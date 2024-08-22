package org.openea.eap.framework.websocket.core.sender.local;

import org.openea.eap.framework.websocket.core.sender.AbstractWebSocketMessageSender;
import org.openea.eap.framework.websocket.core.sender.WebSocketMessageSender;
import org.openea.eap.framework.websocket.core.session.WebSocketSessionManager;

/**
 * 本地的 {@link WebSocketMessageSender} 实现类
 *
 * 注意：仅仅适合单机场景！！！
 *
 */
public class LocalWebSocketMessageSender extends AbstractWebSocketMessageSender {

    public LocalWebSocketMessageSender(WebSocketSessionManager sessionManager) {
        super(sessionManager);
    }

}
