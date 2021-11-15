import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class ServerButton extends JButton {

    protected ServerButton(InetAddress serverAdress){
        this.setText("Join "+serverAdress.getHostAddress());
        this.addActionListener(new ServerButtonListener(serverAdress, this));
    }

    static class ServerButtonListener implements ActionListener{

        private final InetAddress serverAdress;
        private ServerButton serverButton;
        protected ServerButtonListener(InetAddress serverAdress, ServerButton serverButton){
            this.serverAdress = serverAdress;
            this.serverButton = serverButton;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            serverButton.setBackground(new Color(0,0,0));
            GameWindow.getInstance().chooseServer(this.serverAdress);
        }
    }
}
