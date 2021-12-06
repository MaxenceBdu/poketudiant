import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class SpriteManager {

    private static Map<PoketudiantVariety, ImageIcon> map;
    private static int finalSize = 0;

    public static JLabel generatePoketudiant(PoketudiantVariety variety, int size){
        if(finalSize == 0){
            finalSize = size;
        }

        if(map == null){
            map = new EnumMap<PoketudiantVariety, ImageIcon>(PoketudiantVariety.class);

            map.put(PoketudiantVariety.PARLFOR,null);
            map.put(PoketudiantVariety.ISMAR, null);
            map.put(PoketudiantVariety.RIGOLAMOR,null);
            map.put(PoketudiantVariety.PROCRASTINO,null);
            map.put(PoketudiantVariety.COUCHTAR,null);
            map.put(PoketudiantVariety.NUIDEBOU,null);
            map.put(PoketudiantVariety.ALABOURRE, null);
            map.put(PoketudiantVariety.BUCHAFON,null);
            map.put(PoketudiantVariety.BELMENTION,null);
            map.put(PoketudiantVariety.PROMOMAJOR,null);
            map.put(PoketudiantVariety.ENSEIGNANT, null);
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

        return new JLabel(map.get(variety));
    }
}
