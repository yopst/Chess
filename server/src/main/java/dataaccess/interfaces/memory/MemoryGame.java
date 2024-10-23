package dataaccess.interfaces.memory;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.interfaces.GameDAO;
import model.GameData;

import java.util.*;

public class MemoryGame implements GameDAO {
    MemoryDatabase db;

    public MemoryGame(MemoryDatabase mdb) {
        db = mdb;
    }
    @Override
    public int createGame(String gameName) throws DataAccessException {
        int gameID = db.numGames() + 1;
        GameData gameData = new GameData(gameID, "","", gameName, new ChessGame());
        db.game.put(gameID, gameData);
        return db.numGames();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return db.game.get(gameID);
    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {
        db.game.remove(gameID);
    }

    @Override
    public void updateGame(int gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException {
        GameData currentGameData = db.game.get(gameID);
        GameData newGameData;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            newGameData = new GameData(gameID, username,
                    currentGameData.blackUsername(), currentGameData.gameName(), currentGameData.game());
        }
        else {
            newGameData = new GameData(gameID, currentGameData.whiteUsername(),
                    username, currentGameData.gameName(), currentGameData.game());
        }
        db.game.put(gameID, newGameData);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return new ArrayList<>(db.game.values());
    }

    @Override
    public void clear() throws DataAccessException {
        db.game.clear();
    }
}
