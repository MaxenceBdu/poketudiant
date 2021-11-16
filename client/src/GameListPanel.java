import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameListPanel extends JPanel {

    private JList<String> gameList;
    private DefaultListModel<String> gameListModel;
    //private JButton createButton;
    private int nbGames;
    private final JLabel title = new JLabel("Games");

    public GameListPanel(){
        setBounds(650, 0, 650, 900);
        setBackground(new Color(255,0,0));
        add(title);
        nbGames = 0;

        gameListModel = new DefaultListModel<String>();
        gameList = new JList<>(gameListModel);
        this.add(gameList);
        //listOfGames = new ArrayList<>();
        //createButton = new JButton("Create a game");
    }

    public void displayGameList(List<GameListItem> gameList){
        for(GameListItem gameButton: gameList){
            gameListModel.addElement(gameButton.getGameName());
        }
    }

    private void clearGameList(){
        this.gameListModel.clear();
        /*
        for(JButton gameButton: listOfGames){
            this.remove(gameButton);
        }
        listOfGames.clear();
        nbGames = 0;*/
    }
}
