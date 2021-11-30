import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameListItem {
    private String gameName;
    private int players;

    public GameListItem(String gameName, int players){
        this.gameName = gameName;
        this.players = players;
    }

    public String getName() {
        return gameName;
    }

    public int getPlayers() {
        return players;
    }
}
