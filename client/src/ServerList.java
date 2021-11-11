import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerList extends JPanel {

    private List<JButton> servers;
    private RefreshButton refreshButton;

    private final JLabel label = new JLabel("Servers");

    public ServerList(){
        this.servers = new ArrayList<>();
        this.label.setLocation(0,0);
        this.add(label);

        this.refreshButton = new RefreshButton();
        this.add(this.refreshButton);
    }

    public void clearServersList(){
        for(JButton serverButton: this.servers){
            this.remove(serverButton);
        }
        this.servers.clear();
    }

    public void addServer(InetAddress address){
        JButton serverButton = new JButton("Join " + address.getHostAddress());
        serverButton.addActionListener(new ServerButtonListener(address));
        this.servers.add(serverButton);
        this.add(serverButton);
    }

}
