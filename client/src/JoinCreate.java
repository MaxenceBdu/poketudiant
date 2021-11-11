import javax.swing.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JoinCreate extends JPanel {

    // Panel with list of servers + refresh button
    private final ServerList serverList;
    private final GameList gameList;

    private static JoinCreate joinCreateInstance;

    private JoinCreate(){
        this.serverList = new ServerList();
        this.add(this.serverList);

        this.gameList = new GameList();
        this.add(this.gameList);
    }

    public final static JoinCreate getInstance() {
        if(JoinCreate.joinCreateInstance == null){
            JoinCreate.joinCreateInstance = new JoinCreate();
        }

        JoinCreate.joinCreateInstance.setVisible(true);
        return JoinCreate.joinCreateInstance;
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

    public void displayGameList(HashMap<Integer, String> gameList){
        this.gameList.displayGameList(gameList);
    }
}

