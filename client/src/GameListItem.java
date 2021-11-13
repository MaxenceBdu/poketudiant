import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameListItem extends JButton {
    private String gameName;
    private int players;

    public GameListItem(String gameName, int players){
        this.gameName = gameName;
        this.players = players;
    }

    public String getGameName() {
        return gameName;
    }

    public int getPlayers() {
        return players;
    }

    static class GameListItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
