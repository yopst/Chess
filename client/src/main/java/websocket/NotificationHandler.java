package websocket;

import websocket.messages.ServerMessage;

public interface NotificationHandler {
    
    String help();

    void notify(ServerMessage notification);
}
