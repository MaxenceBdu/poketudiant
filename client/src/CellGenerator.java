import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class CellGenerator {

    private static Map<CellType, ImageIcon> map;
    public static JLabel generateCell(CellType cellType, int size){

        if(map == null){
            map = new EnumMap<>(CellType.class);
            map.put(CellType.HEAL, null);
            map.put(CellType.GRASS, null);
            map.put(CellType.NEUTRAL, null);
            map.put(CellType.PLAYER0, null);
            map.put(CellType.PLAYER1, null);
            map.put(CellType.PLAYER2, null);
            map.put(CellType.PLAYER3, null);
        }

        if(map.get(cellType) == null){
            map.remove(cellType);
            try{
                map.put(cellType, new ImageIcon(new ImageIcon(ImageIO.read(new File("src/assets/"+cellType.name()+".png"))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return new JLabel(map.get(cellType));
    }

    public enum CellType{
        HEAL,NEUTRAL,GRASS,PLAYER0,PLAYER1,PLAYER2,PLAYER3
    }
}
