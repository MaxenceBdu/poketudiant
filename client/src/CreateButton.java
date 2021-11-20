import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateButton extends JButton {

    CreateButton(MenuPanel menuPanel){
        setText("Create game");
        this.addActionListener(new CreateButtonListener(menuPanel));
    }

    static class CreateButtonListener implements ActionListener{
        private MenuPanel menuPanel;
        public CreateButtonListener(MenuPanel menuPanel){
            this.menuPanel = menuPanel;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            menuPanel.showGameCreationPanel();
        }
    }
}
