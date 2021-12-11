import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class HomePanel extends JLayeredPane {

    public HomePanel(Dimension dim){
        setLayout(null);
        setSize(dim);

        JLabel title = new JLabel("<html>Bienvenue sur le pokétudiant <br/> de Florian Gaudissard et Maxence Boisédu</html>");
        title.setFont(new Font(Font.SERIF, Font.PLAIN, 50));
        title.setForeground(Color.WHITE);
        title.setLocation(dim.width/15, dim.height/5);
        title.setSize(title.getMaximumSize());
        add(title, PALETTE_LAYER);

        JButton play = new JButton("Explorer les serveurs");
        play.setContentAreaFilled(false);
        play.setOpaque(false);
        play.setLocation(dim.width/6,dim.height/2);
        play.addActionListener(new PlayButtonListener());
        play.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
        play.setForeground(Color.WHITE);
        play.setSize(play.getMaximumSize());
        play.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        add(play, PALETTE_LAYER);
        ;
        try{
            Image image = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/FOND-ACCUEIL.jpg")))).getImage().getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH);
            JLabel background = new JLabel(new ImageIcon(image));
            background.setSize(dim.width, dim.height);
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
