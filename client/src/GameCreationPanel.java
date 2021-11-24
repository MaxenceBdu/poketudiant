import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameCreationPanel extends JPanel {

    private JTextArea textArea;
    private JButton validateButton;
    private JButton cancelButton;
    private JLabel infoMessage;

    GameCreationPanel(){
        setLayout(null);
        setBounds(600,600,500,200);
        setBackground(new Color(0,0,0));
        textArea = new JTextArea();
        textArea.setBounds(20,20, 100, 50);
        add(textArea);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelButtonListener(this));
        cancelButton.setSize(cancelButton.getMaximumSize());
        cancelButton.setLocation(200,100);
        add(cancelButton);

        validateButton = new JButton("Validate");
        validateButton.addActionListener(new ValidateButtonListener(this));
        validateButton.setSize(validateButton.getMaximumSize());
        validateButton.setLocation(30, 100);
        add(validateButton);

        infoMessage = new JLabel();
        infoMessage.setVisible(false);
        infoMessage.setSize(100, 30);
        infoMessage.setLocation(300, 20);
        add(infoMessage);

        this.setVisible(true);
    }


    static class CancelButtonListener implements ActionListener{
        private final GameCreationPanel gameCreationPanel;
        public CancelButtonListener(GameCreationPanel gameCreationPanel) {
            this.gameCreationPanel = gameCreationPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            gameCreationPanel.setVisible(false);
        }
    }

    static class ValidateButtonListener implements ActionListener{
        private final GameCreationPanel gameCreationPanel;

        public ValidateButtonListener(GameCreationPanel gameCreationPanel) {
            this.gameCreationPanel = gameCreationPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(ClientBack.getInstance().askForGameCreation(gameCreationPanel.textArea.getText())){
                // PASSER AU PANEL DE JEU
                DisplayWindow.getInstance().getContentPane().setVisible(false);
                new ClientDeamon(ClientBack.getInstance().getTcpSocket()).start();
                //DisplayWindow.getInstance().setContentPane(new MapPanel());
            }else{
                gameCreationPanel.infoMessage.setForeground(Color.RED);
                gameCreationPanel.infoMessage.setText("Game not created");
            }
            gameCreationPanel.infoMessage.setVisible(true);
        }
    }
}
