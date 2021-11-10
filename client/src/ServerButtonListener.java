import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class ServerButtonListener implements ActionListener {

    private InetAddress serverAdress;

    protected ServerButtonListener(InetAddress serverAdress){
        this.serverAdress = serverAdress;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        GameWindow.getInstance().chooseServer(this.serverAdress);
    }
}
