import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.List;

public class JoinCreate extends JLayeredPane {

    // Panel with list of servers + refresh button
    private final ServerListPanel serverListPanel;
    private final GameListPanel gameListPanel;
    private GameCreationPanel gameCreationPanel;
    private final CreateButton createButton;

    public JoinCreate(int width, int height){
        //setLayout(null);
        setSize(width, height);
        setForeground(new Color(0,255,0));

        serverListPanel = new ServerListPanel();
        this.add(serverListPanel,0);

        this.gameListPanel = new GameListPanel();
        this.add(gameListPanel,0);

        createButton = new CreateButton();
        createButton.setSize(createButton.getMaximumSize());
        createButton.setLocation(200, 0);
        this.add(createButton);

        this.setVisible(true);
    }

    public GameCreationPanel getGameCreationPanel(){
        return gameCreationPanel;
    }

    public ServerListPanel getServerList() {
        return serverListPanel;
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
        this.serverListPanel.clearServersList();
    }

    public void addServer(InetAddress serverAddress){
        this.serverListPanel.addServer(serverAddress);
    }

    public void displayGameList(List<GameListItem> gameList){
        this.gameListPanel.setVisible(true);
        this.gameListPanel.displayGameList(gameList);
    }

}

