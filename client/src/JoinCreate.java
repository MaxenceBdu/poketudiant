import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JoinCreate extends JLayeredPane {

    // Panel with list of servers + refresh button
    private final ServerList serverList;
    private final GameList gameList;
    private GameCreationPanel gameCreationPanel;
    //private final CreateButton createButton;

    public JoinCreate(int width, int height){
        //setLayout(null);
        setSize(width, height);
        setForeground(new Color(0,255,0));

        serverList = new ServerList();
        this.add(serverList,0);

        this.gameList = new GameList();
        this.add(gameList,0);

        this.setVisible(true);
    }

    public GameCreationPanel getGameCreationPanel(){
        return gameCreationPanel;
    }

    public ServerList getServerList() {
        return serverList;
    }

    public void showGameCreationPanel(){
        if(gameCreationPanel == null){
            gameCreationPanel = new GameCreationPanel();
            //putLayer(gameCreationPanel,POPUP_LAYER);
            this.add(gameCreationPanel);
            setLayer(gameCreationPanel, POPUP_LAYER);
            //moveToFront(gameCreationPanel);
        }else{
            gameCreationPanel.setVisible(true);
        }
    }

    public void clearServersList(){
        this.serverList.clearServersList();
    }

    public void addServer(InetAddress serverAddress){
        this.serverList.addServer(serverAddress);
    }

    public void displayGameList(List<GameListItem> gameList){
        this.gameList.setVisible(true);
        this.gameList.displayGameList(gameList);
    }

}

