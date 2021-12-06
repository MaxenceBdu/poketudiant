import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class HomePanel extends JLayeredPane {

    public HomePanel(Dimension dim){
        setLayout(null);
        setSize(dim);
        JButton play = new JButton("Explorer les serveurs");
        play.setContentAreaFilled(false);
        play.setOpaque(false);
        play.setLocation(getWidth()/6,getHeight()/2);
        play.addActionListener(new PlayButtonListener());
        play.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
        play.setForeground(Color.WHITE);
        play.setSize(play.getMaximumSize());
        add(play, PALETTE_LAYER);

        try{
            Image image = new ImageIcon(ImageIO.read(new File("src/assets/menu-bg.jpg"))).getImage().getScaledInstance(getWidth()/15, getHeight()/15, Image.SCALE_SMOOTH);
            JLabel background = new JLabel(new ImageIcon(image));
            background.setSize(getWidth()/15, getHeight()/15);
            background.setLocation(0,0);
            add(background, DEFAULT_LAYER);
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    static class PlayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DisplayWindow.getInstance().leaveHomeForMenu();
        }
    }
}
