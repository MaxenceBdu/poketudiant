import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TeamPanel extends JPanel {

    public TeamPanel(List<JLabel> team, int width, int height){
        //System.out.println(width);
        setVisible(true);
        setBounds(0,0, width, height);
        GridLayout gl = new GridLayout(3,1,0,0);
        gl.preferredLayoutSize(this);
        setLayout(gl);

        if(team != null){
            for(JLabel j: team){
                add(j);
            }
        }
    }

    public void refreshTeam(List<JLabel> team){
        removeAll();
        for(JLabel j: team){
            add(j);
        }
        validate();
        repaint();
    }
}
