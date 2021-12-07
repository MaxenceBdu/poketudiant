import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TeamPanel extends JPanel {

    public TeamPanel(List<TeamItem> team, int width, int height){
        //System.out.println(width);
        setVisible(true);
        setBounds(0,0, width, height);
        GridLayout gl = new GridLayout(3,1,0,0);
        gl.preferredLayoutSize(this);
        setLayout(gl);

        if(team != null){
            for(TeamItem ti: team){
                add(ti);
            }
        }
    }

    public void refreshTeam(List<TeamItem> team){
        removeAll();
        for(TeamItem ti: team){
            add(ti);
        }
        validate();
        repaint();
    }
}
