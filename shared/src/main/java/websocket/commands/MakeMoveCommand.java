package websocket.commands;

import chess.*;
import com.google.gson.Gson;

public class MakeMoveCommand extends UserGameCommand {
    public ChessMove move;

    public MakeMoveCommand(String authToken, Integer gameID,
                           String chessMove) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = deserializeChessMove(chessMove);
    }

    private ChessMove deserializeChessMove(String move) {
        Gson gson = new Gson();
        return gson.fromJson(move, ChessMove.class);
    }


}