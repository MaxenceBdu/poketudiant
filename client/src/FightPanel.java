import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FightPanel extends JLayeredPane {

    public FightPanel(int size, int xOffset, int yOffset){
        setLayout(null);
        setVisible(true);
        setBounds(xOffset, yOffset, size, size);

        try{
            JLabel background = new JLabel(new ImageIcon(new ImageIcon(ImageIO.read(new File("src/assets/FOND-COMBAT.png"))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
            background.setSize(background.getMaximumSize());
            add(background, DEFAULT_LAYER);
        }catch(IOException e){
            System.out.println("Fond pour combat non trouvé");
        }
        validate();
        repaint();
    }

    public void setDisplay(){
        //setVisible(true);
        //System.out.println("setdisplay");
    }
}
