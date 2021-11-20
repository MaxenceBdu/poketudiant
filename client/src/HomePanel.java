import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePanel extends JPanel {

    public HomePanel(int width, int height){
        setLayout(null);
        setSize(width, height);
        JButton play = new JButton("Play");
        play.setSize(play.getMaximumSize());
        play.setLocation(100,100);
        play.addActionListener(new PlayButtonListener());

        /*
        ImageIcon img = new ImageIcon("assets/menu-bg.jpg");
        JLabel background = new JLabel(img);
        background.setSize(width, height);
        background.setLocation(0,0);
        add(background);
*/
        this.add(play);
    }


    static class PlayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DisplayWindow.getInstance().leaveHomeForMenu();
        }
    }
}
