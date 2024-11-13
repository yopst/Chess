package dataaccess.memory;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.interfaces.GameDAO;
import model.GameData;

import java.util.*;

public class MemoryGame implements GameDAO {
    private static final HashMap<Integer, GameData> games = new HashMap<>();
    private record GameDataListItem(int gameID, String whiteUsername, String blackUsername, String gameName){};

    @Override
    public int createGame(String gameName) throws DataAccessException {
        int gameID = games.size() + 1;
        GameData gameData = new GameData(gameID, null,null, gameName, new ChessGame());
        games.put(gameID, gameData);
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {
        if (getGame(gameID) == null) throw new DataAccessException("no such game to delete");
        games.remove(gameID);
    }

    @Override
    public void updateGame(int gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException {
        GameData currentGameData = games.get(gameID);
        if (currentGameData == null) throw new DataAccessException("no such game to update");
        GameData newGameData;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            newGameData = new GameData(gameID, username,
                    currentGameData.blackUsername(), currentGameData.gameName(), currentGameData.game());
        }
        else {
            newGameData = new GameData(gameID, currentGameData.whiteUsername(),
                    username, currentGameData.gameName(), currentGameData.game());
        }
        games.put(gameID, newGameData);
    }

    @Override
    public Collection<model.GameDataListItem> listGames() throws DataAccessException {
        ArrayList<model.GameDataListItem> list = new ArrayList<>();
        for (GameData game : games.values()) {
            list.add(new model.GameDataListItem(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return list;
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}
