import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RefreshButton extends JButton {

    public RefreshButton(ServerListPanel serverListPanel){
        this.setText("Refresh");
        this.addActionListener(new RefreshButtonListener(serverListPanel));
    }

    static class RefreshButtonListener implements ActionListener{
        private ServerListPanel serverListPanel;

        public RefreshButtonListener(ServerListPanel serverListPanel){
            this.serverListPanel = serverListPanel;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            serverListPanel.clearServersList();
            GameWindow.getInstance().askForServerList();
        }
    }
}
