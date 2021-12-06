import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class MapPanel extends JPanel {


    public MapPanel(List<JLabel> map, int size, int xOffset, int yOffset){
        setVisible(true);
        setBounds(xOffset,yOffset, size, size);
        GridLayout gl = new GridLayout(15, 15,0,0);
        gl.preferredLayoutSize(this);
        setLayout(gl);

        if(map != null){
            for(JLabel c: map){
                add(c);
            }
        }
    }

    public void repaintMap(List<JLabel> map){
        removeAll();
        for(JLabel c: map){
            add(c);
        }
        validate();
        repaint();
    }
}
