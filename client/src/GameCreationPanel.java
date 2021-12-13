import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
    Panel to enter the name of the new game
 */
public class GameCreationPanel extends JPanel {

    private final JTextArea textArea;
    private final JLabel infoMessage;

    GameCreationPanel(int parentWidth, int parentHeight){
        setLayout(null);
        setSize(parentWidth/4, parentHeight/4);
        setLocation(parentWidth/2-parentWidth/8,parentHeight/2-parentHeight/8);
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.BLACK,3));

        // Text area to enter the name of the game
        textArea = new JTextArea();
        textArea.setBounds(20,20, getWidth()-40, (int)(getHeight()*0.3));
        textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        add(textArea);

        infoMessage = new JLabel();
        infoMessage.setVisible(false);
        infoMessage.setBounds(20, textArea.getY()+textArea.getHeight()+10, getWidth()-40, textArea.getHeight()-10);
        infoMessage.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 30));
        add(infoMessage);

        // Validate button will send the tcp request when clicked
        JButton validateButton = new JButton("Valider");
        validateButton.addActionListener(new ValidateButtonListener(this));
        validateButton.setSize(getWidth()/3, getHeight()/4);
        validateButton.setLocation(20,getHeight() - (validateButton.getHeight()+20));
        validateButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        add(validateButton);

        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(new CancelButtonListener(this));
        cancelButton.setSize(getWidth()/3, getHeight()/4);
        cancelButton.setLocation(getWidth()-20-cancelButton.getWidth(), getHeight() - (cancelButton.getHeight()+20));
        cancelButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        add(cancelButton);

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
                DisplayWindow.getInstance().goToGame();
                // Start the daemon
                ClientBack.getInstance().startDaemon();
            }else{
                // Display error message
                gameCreationPanel.infoMessage.setForeground(Color.RED);
                gameCreationPanel.infoMessage.setText("Game not created");
                gameCreationPanel.infoMessage.setVisible(true);
            }
        }
    }
}
