import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateButton extends JButton {

    CreateButton(){
        setText("Create game");
        this.addActionListener(new CreateButtonListener());
    }

    static class CreateButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("click create");
            GameWindow.getInstance().getJoinCreatePanel().showGameCreationPanel();
        }
    }
}
