package websocket;

import websocketmessages.Notification;

public interface NotificationHandler {
    void notify(Notification notification);
    String help();
}
