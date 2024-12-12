package websocket.messages;

public class LoadGameMessage extends ServerMessage{
    public String gameState;

    public LoadGameMessage(String gameState) {
        super(ServerMessageType.LOAD_GAME);
        this.gameState = gameState;

    }
}
