/*
    Class used in menu panel, in list of games
 */
public class GameListItem {
    private final String gameName;
    private final int players;

    public GameListItem(String gameName, int players){
        this.gameName = gameName;
        this.players = players;
    }

    public String getName() {
        return gameName;
    }

    @Override
    public String toString() {
        return gameName + " (" + players + ")";
    }
}
