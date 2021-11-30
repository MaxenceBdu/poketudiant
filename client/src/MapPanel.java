import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class MapPanel extends JPanel implements KeyListener {

    public MapPanel(List<JLabel> map){
        setVisible(true);
        setBounds(200, 100,750, 750);
        setLayout(new GridLayout(15, 15));

        for(JLabel c: map){
            add(c);
        }

    }
    @Override
    public void keyTyped(KeyEvent keyEvent) {
        // Empty
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // Empty
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        System.out.println("key listener");
        switch (keyEvent.getKeyCode()){
            case KeyEvent.VK_Z:
                ClientBack.getInstance().playerMoveUp();
                break;
            case KeyEvent.VK_Q:
                ClientBack.getInstance().playerMoveRight();
                break;
            case KeyEvent.VK_S:
                ClientBack.getInstance().playerMoveDown();
                break;
            case KeyEvent.VK_D:
                ClientBack.getInstance().playerMoveLeft();
                break;
            default:
                break;
        }
    }

    public void repaintMap(List<JLabel> map){
        removeAll();
        for(JLabel c: map){
            add(c);
        }
        revalidate();
        repaint();
    }
}
