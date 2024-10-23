package dataaccess.interfaces.memory;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;


public class MemoryDatabase {
    protected Map<String, UserData> user = new HashMap<>();     //maps username String to UserData object
    protected Map<Integer, GameData> game = new HashMap<>();    //gameID int to GameData
    protected Map<String, AuthData> auth = new HashMap<>();     //authToken String to AuthData

    public void clearAll() {
        auth.clear();
        game.clear();
        user.clear();
    }
    public int numGames() {
        return game.size();
    }
}
