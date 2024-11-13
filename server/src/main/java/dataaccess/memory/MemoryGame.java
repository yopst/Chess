package dataaccess.memory;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.interfaces.GameDAO;
import model.GameData;

import java.util.*;

public class MemoryGame implements GameDAO {
    private static final HashMap<Integer, GameData> GAMES = new HashMap<>();

    @Override
    public int createGame(String gameName) throws DataAccessException {
        int gameID = GAMES.size() + 1;
        GameData gameData = new GameData(gameID, null,null, gameName, new ChessGame());
        GAMES.put(gameID, gameData);
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return GAMES.get(gameID);
    }

//    @Override
//    public void deleteGame(int gameID) throws DataAccessException {
//        if (getGame(gameID) == null) throw new DataAccessException("no such game to delete");
//        GAMES.remove(gameID);
//    }

    @Override
    public void updateGame(int gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException {
        GameData currentGameData = GAMES.get(gameID);
        if (currentGameData == null) {
            throw new DataAccessException("no such game to update");
        }
        GameData newGameData;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            newGameData = new GameData(gameID, username,
                    currentGameData.blackUsername(), currentGameData.gameName(), currentGameData.game());
        }
        else {
            newGameData = new GameData(gameID, currentGameData.whiteUsername(),
                    username, currentGameData.gameName(), currentGameData.game());
        }
        GAMES.put(gameID, newGameData);
    }

    @Override
    public Collection<model.GameDataListItem> listGames() {
        ArrayList<model.GameDataListItem> list = new ArrayList<>();
        for (GameData game : GAMES.values()) {
            list.add(new model.GameDataListItem(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return list;
    }

    @Override
    public void clear() throws DataAccessException {
        GAMES.clear();
    }
}
