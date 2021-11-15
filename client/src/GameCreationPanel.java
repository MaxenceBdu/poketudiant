import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameCreationPanel extends JPanel {

    private JTextArea textArea;
    private JButton validateButton;
    private JButton cancelButton;

    GameCreationPanel(){
        setLayout(null);
        setBounds(600,600,500,200);
        setBackground(new Color(0,0,0));
        System.out.println("constructor gamecreationpanel");
        textArea = new JTextArea();
        textArea.setBounds(20,20, 100, 50);
        add(textArea);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelButtonListener(this));
        cancelButton.setSize(cancelButton.getMaximumSize());
        cancelButton.setLocation(200,100);
        add(cancelButton);

        validateButton = new JButton("Validate");
        validateButton.addActionListener(new ValidateButtonListener(textArea));
        validateButton.setSize(validateButton.getMaximumSize());
        validateButton.setLocation(30, 100);
        add(validateButton);

        this.setVisible(true);
    }

    static class CancelButtonListener implements ActionListener{
        private GameCreationPanel gameCreationPanel;
        public CancelButtonListener(GameCreationPanel gameCreationPanel) {
            this.gameCreationPanel = gameCreationPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gameCreationPanel.setVisible(false);
        }
    }

    static class ValidateButtonListener implements ActionListener{
        private JTextArea textArea;

        public ValidateButtonListener(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            GameWindow.getInstance().sendGameCreationRequest(this.textArea.getText());
        }
    }
}
