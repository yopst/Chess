package repl;

import chess.ChessGame;
import client.Client;
import client.State;
import ui.ChessBoardUI;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;
import static ui.EscapeSequences.*;

public class GameRepl extends Repl implements NotificationHandler {
    private final Client client;
    private final String visitorUsername;
    private final Integer gameID;
    private final ChessGame.TeamColor color;

    public GameRepl(Client client, String visitorUsername, Integer gameID, ChessGame.TeamColor color) {
        this.client = client;
        this.gameID = gameID;
        this.visitorUsername = visitorUsername;
        this.color = color;

        if (client.state == State.GAMING) {
            String strColor = (color == ChessGame.TeamColor.WHITE) ? "White:": "Black";
            System.out.println("You are playing as " + strColor);
        }
        else {
            System.out.println("Observing game number " + gameID);
        }
    }

    @Override
    public String help() {
        if (client.state == State.GAMING) {
            return """
                - highlight
                - resign
                - move
                - leave
                - redraw
                - help
                """;
        }
        else {
            return """
                - leave
                - redraw
                - help
                """;
        }
    }

    @Override
    public void notify(ServerMessage notification) {
        System.out.println(SET_BG_COLOR_RED + "notification.message()");
    }

    private void printInitBoard() {
        System.out.print(new ChessBoardUI().createChessBoard(color, client.blankInitializedBoard));
    }
}
