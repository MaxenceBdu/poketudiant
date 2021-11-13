import javax.swing.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class GameWindow extends JFrame{

    private ClientBack clientBack;
    private static GameWindow gameWindowInstance;
    private JoinCreate joinCreatePanel;

    private GameWindow() {
        super("Pokétudiant - Boisédu & Gaudissard");
        setSize(1300, 900);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        joinCreatePanel = new JoinCreate();
        joinCreatePanel.setSize(this.getWidth(),this.getHeight());
        setVisible(true);
    }

    public static GameWindow getInstance(){
        if(GameWindow.gameWindowInstance == null){
            GameWindow.gameWindowInstance = new GameWindow();
        }

        return GameWindow.gameWindowInstance;
    }

    public JoinCreate getJoinCreatePanel() {
        return joinCreatePanel;
    }

    protected void setClientBack(ClientBack clientBack){
        this.clientBack = clientBack;
    }

    public void sendAddressToFront(InetAddress address){
        joinCreatePanel.addServer(address);
        this.setContentPane(joinCreatePanel);
    }

    public void askForNewServerList(){
        this.clientBack.askForServers();
    }

    public void chooseServer(InetAddress serverAdress){
        this.clientBack.askForGameList(serverAdress);
    }

    public void sendGameListToFront(String gameList){
        List<GameListItem> games = new ArrayList<>();

        String[] lines = gameList.split("\n");
        int gameNumber = Integer.parseInt(lines[0].substring(lines[0].length()-1));
        if(gameNumber > 0){
            for(int i = 1; i < gameNumber; i++){
                String[] gameInfos = lines[i].split(" ");
                int nbPlayers = Integer.parseInt(gameInfos[0]);
                String gameName = gameInfos[1];
                games.add(new GameListItem(gameName, nbPlayers));
            }
        }
        joinCreatePanel.displayGameList(games);
    }
}