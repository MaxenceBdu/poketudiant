import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameList extends JPanel {

    private List<GameListItem> listOfGames;
    //private JButton createButton;
    private int nbGames;
    private final JLabel noGameMessage = new JLabel("No game on this server");
    private final JLabel title = new JLabel("Games");

    public GameList(){
        setBounds(700, 0, 600, 700);
        setBackground(new Color(255,0,0));
        add(title);
        //setVisible(false);
        nbGames = 0;
        listOfGames = new ArrayList<>();
        //createButton = new JButton("Create a game");

        noGameMessage.setVisible(false);
        this.add(noGameMessage);
    }

    public void displayGameList(List<GameListItem> gameList){
        if(listOfGames != null){
            clearGameList();
        }

        nbGames = gameList.size();
        listOfGames = gameList;
        if(listOfGames.size() == 0){
            noGameMessage.setVisible(true);
        }else{
            for(GameListItem gameButton: gameList){
                this.add(gameButton);
            }
        }
    }

    private void clearGameList(){
        for(JButton gameButton: listOfGames){
            this.remove(gameButton);
        }
        listOfGames.clear();
        nbGames = 0;
    }
}
