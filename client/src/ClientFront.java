import javax.swing.*;
import java.net.InetAddress;
import java.util.List;

public class ClientFront{

    private JFrame mainFrame;
    private JPanel panel;
    private ClientBack clientBack;

    public ClientFront(){
        mainFrame = new JFrame();
        panel = new JPanel();
        mainFrame.add(panel);
    }

    protected void setClientBack(ClientBack clientBack){
        this.clientBack = clientBack;
    }

    protected void displayServers(List<InetAddress> addresses){
        for(InetAddress addr:addresses){
            JButton button = new JButton(addr.getHostAddress());
            panel.add(button);
        }
    }
}