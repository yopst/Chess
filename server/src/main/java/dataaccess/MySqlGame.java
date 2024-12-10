package dataaccess;

import chess.ChessGame;
import dataaccess.interfaces.GameDAO;
import model.GameData;
import model.GameDataListItem;

import java.util.Collection;
import java.util.List;

public class MySqlGame implements GameDAO {

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException {

    }

    @Override
    public Collection<GameDataListItem> listGames() {
        return List.of();
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
