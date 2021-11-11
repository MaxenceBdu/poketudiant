import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RefreshButton extends JButton {

    public RefreshButton(){
        this.setText("Refresh");
        this.addActionListener(new RefreshButtonListener());
    }

    static class RefreshButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JoinCreate.getInstance().getServerList().clearServersList();
            GameWindow.getInstance().askForNewServerList();
        }
    }
}
