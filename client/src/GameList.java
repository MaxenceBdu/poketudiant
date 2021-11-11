import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameList extends JPanel {

    private List<JButton> listOfGames;
    private JButton createButton;

    public GameList(){
        createButton = new JButton("Create a game");
    }

    public void displayGameList(HashMap<Integer, String> gameList){
        if(listOfGames != null){
            for(int i: gameList.keySet()){
                JButton gameButton = new JButton(gameList.get(i));
            }
        }else{

        }
    }
}
