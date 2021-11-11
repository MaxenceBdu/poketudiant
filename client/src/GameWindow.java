import javax.swing.*;
import java.net.InetAddress;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GameWindow extends JFrame{

    private ClientBack clientBack;
    private static GameWindow gameWindowInstance;

    private GameWindow() {
        super("Pokétudiant - Boisédu & Gaudissard");
        setSize(1200, 900);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static GameWindow getInstance(){
        if(GameWindow.gameWindowInstance == null){
            GameWindow.gameWindowInstance = new GameWindow();
        }

        return GameWindow.gameWindowInstance;
    }

    protected void setClientBack(ClientBack clientBack){
        this.clientBack = clientBack;
    }

    public void sendAddressToFront(InetAddress address){
        JoinCreate.getInstance().addServer(address);
        this.setContentPane(JoinCreate.getInstance());
    }

    public void askForNewServerList(){
        this.clientBack.askForServers();
    }

    public void chooseServer(InetAddress serverAdress){
        this.clientBack.askForGameList(serverAdress);
    }

    public void sendGameListToFront(String gameList){
        HashMap<Integer, String> games = new HashMap<>();
        String[] lines = gameList.split("\n");
        int gameNumber = lines[0].charAt(lines[0].length()-1);
        if(gameNumber > 0){
            for(int i = 1; i < gameNumber; i++){
                String[] gameInfos = lines[i].split(" ");
                int nbPlayers = Integer.parseInt(gameInfos[0]);
                String gameName = gameInfos[1];
                games.put(nbPlayers, gameName);
            }
        }
        JoinCreate.getInstance().displayGameList(games);
    }
}