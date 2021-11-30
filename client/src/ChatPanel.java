import javax.swing.*;

public class ChatPanel extends JPanel {
    private JTextArea input;

    public ChatPanel(){
        setVisible(true);
        setBounds(1000, 100, 250, 450);
        input = new JTextArea();
        add(input);
    }
}
