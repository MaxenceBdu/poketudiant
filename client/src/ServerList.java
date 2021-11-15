import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerList extends JPanel {

    private List<ServerButton> servers;
    private RefreshButton refreshButton;

    public ServerList(){
        setSize(600, 700);
        setBackground(new Color(0,0,255));

        this.servers = new ArrayList<>();
        JLabel label = new JLabel("Servers");
        label.setLocation(0,0);
        this.add(label);

        this.refreshButton = new RefreshButton(this);
        this.add(this.refreshButton);

        this.add(new CreateButton());
    }

    public void clearServersList(){
        for(JButton serverButton: this.servers){
            this.remove(serverButton);
        }
        this.servers.clear();
    }

    public void addServer(InetAddress address){
        ServerButton serverButton = new ServerButton(address);
        this.servers.add(serverButton);
        this.add(serverButton);
    }

}
