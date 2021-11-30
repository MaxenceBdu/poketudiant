import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MapPanel extends JPanel {
    private final List<JLabel> map;

    public MapPanel(List<JLabel> map){
        setVisible(true);
        setBounds(200, 100,750, 750);
        setLayout(new GridLayout(15, 15));

        this.map = map;
        for(JLabel c: this.map){
            add(c);
        }

    }

    public void repaintMap(List<JLabel> map){
        for(JLabel c: this.map){
            remove(c);
        }

        for(JLabel c: map){
            add(c);
        }
        revalidate();
        repaint();
    }
}
