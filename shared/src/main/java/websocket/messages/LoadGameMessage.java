package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage{
    public String GameState;

    public LoadGameMessage() {
        super(ServerMessageType.LOAD_GAME);
    }
}
