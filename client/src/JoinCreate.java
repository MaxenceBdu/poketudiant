import javax.swing.*;
import java.net.InetAddress;

public class JoinCreate extends JPanel {

    private static JoinCreate joinCreateInstance;

    private JoinCreate(){}

    public final static JoinCreate getInstance() {
        if(JoinCreate.joinCreateInstance == null){
            JoinCreate.joinCreateInstance = new JoinCreate();
        }

        JoinCreate.joinCreateInstance.setVisible(true);
        return JoinCreate.joinCreateInstance;
    }

    public void displayServers(InetAddress address){
        JButton addrButton = new JButton("Join " + address.getHostAddress());
        addrButton.addActionListener(new ServerButtonListener(address));
        JoinCreate.getInstance().add(addrButton);
    }

    public void displayGameList(String gameList){
        JLabel list = new JLabel(gameList);
        JoinCreate.getInstance().add(list);
    }
}

