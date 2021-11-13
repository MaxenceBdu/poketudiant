import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RefreshButton extends JButton {

    public RefreshButton(ServerList serverList){
        this.setText("Refresh");
        this.addActionListener(new RefreshButtonListener(serverList));
    }

    static class RefreshButtonListener implements ActionListener{
        private ServerList serverList;

        public RefreshButtonListener(ServerList serverList){
            this.serverList = serverList;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            serverList.clearServersList();
            GameWindow.getInstance().askForNewServerList();
        }
    }
}
