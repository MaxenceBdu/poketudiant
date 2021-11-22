import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class HomePanel extends JPanel {

    public HomePanel(int width, int height){
        setLayout(null);
        setSize(width, height);
        JButton play = new JButton("Play");
        play.setSize(play.getMaximumSize());
        play.setLocation(100,100);
        play.addActionListener(new PlayButtonListener());

        /*
        try{
            Image image = new ImageIcon(ImageIO.read(new File("src/assets/menu-bg.jpg"))).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            JLabel background = new JLabel(new ImageIcon(image));
            background.setSize(width, height);
            background.setLocation(0,0);
            //background.setVisible(true);
            add(background);
        }catch(IOException e){
            e.printStackTrace();
        }*/
        this.add(play);
    }


    static class PlayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DisplayWindow.getInstance().leaveHomeForMenu();
        }
    }
}
