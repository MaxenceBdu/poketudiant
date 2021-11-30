import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class MapPanel extends JPanel {


    public MapPanel(List<JLabel> map){
        setVisible(true);
        setBounds(200, 100,750, 750);
        GridLayout gl = new GridLayout(15, 15,0,0);
        gl.preferredLayoutSize(this);
        setLayout(gl);

        for(JLabel c: map){
            add(c);
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
