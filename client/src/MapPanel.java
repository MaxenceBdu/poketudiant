import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MapPanel extends JLayeredPane {

    public MapPanel(List<Cell> cells){
        setVisible(true);
        setBounds(200, 100,750, 750);
        setLayout(new GridLayout(15, 15));
        for(int i = 0; i < 225; i++){
            add(cells.get(i));
        }
    }
}
