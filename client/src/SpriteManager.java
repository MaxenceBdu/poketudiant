import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class SpriteManager {

    private static Map<PoketudiantSpriteSource, ImageIcon> map;
    private static int finalSize = 0;

    public static ImageIcon generatePoketudiant(PoketudiantSpriteSource variety, int size){
        if(finalSize == 0){
            finalSize = size;
        }

        if(map == null) {
            map = new EnumMap<>(PoketudiantSpriteSource.class);
            for (PoketudiantSpriteSource pss : PoketudiantSpriteSource.values()) {
                map.put(pss, null);
            }
        }

        if(map.get(variety) == null){
            map.remove(variety);
            try{
                map.put(variety, new ImageIcon(new ImageIcon(ImageIO.read(new File("src/assets/"+variety.name()+".png"))).getImage().getScaledInstance(finalSize, finalSize, Image.SCALE_SMOOTH)));
            }catch (IOException e){
                //System.out.println("src/assets/"+variety.name()+".jpg not found");
                e.printStackTrace();
            }
        }

        return map.get(variety);
    }
}
