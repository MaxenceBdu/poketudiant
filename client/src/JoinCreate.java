import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JoinCreate extends JPanel {

    // Panel with list of servers + refresh button
    private final ServerList serverList;
    private final GameList gameList;

    public JoinCreate(){
        setLayout(null);
        serverList = new ServerList();
        setBackground(new Color(0,255,0));
        //serverList.setBounds(10,10,300,500);
        this.add(this.serverList);

        this.gameList = new GameList();
        this.add(this.gameList);

        this.setVisible(true);
    }

    public ServerList getServerList() {
        return serverList;
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

