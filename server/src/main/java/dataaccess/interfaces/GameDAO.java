package dataaccess.interfaces;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    public int createGame(GameData gameData) throws DataAccessException; //returns gameID
    public GameData getGame(int gameID) throws DataAccessException;
    public void deleteGame(int gameID) throws DataAccessException;
    public void updateGame(GameData gameData, int gameID) throws DataAccessException;
    public Collection<GameData> listGames() throws DataAccessException;
    public void clear() throws DataAccessException;
}
