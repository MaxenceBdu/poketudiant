import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class HealCell extends JLabel {

    public HealCell(){
        super();
        try{
            Image image = new ImageIcon(ImageIO.read(new File("src/assets/rouge.jpg"))).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(image));
        }catch (IOException e){
            System.out.println("heal image not found");
        }
    }

}
