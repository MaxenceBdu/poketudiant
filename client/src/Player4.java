import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Player4 extends JLabel {

    public Player4(){
        try {
            Image image = new ImageIcon(ImageIO.read(new File("src/assets/pink.jpg"))).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(image));
        } catch (IOException e) {
            System.out.println("grass image not found");
        }
    }
}
