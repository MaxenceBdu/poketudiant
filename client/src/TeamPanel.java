import javax.swing.*;
import java.awt.*;
import java.util.List;

/*
    Corresponds to the sidebar that displays player's poketudiants
 */
public class TeamPanel extends JPanel {

    public TeamPanel(List<TeamItem> team, int width, int height){
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

    /*
        Updates the sidebar
     */
    public void refreshTeam(List<TeamItem> team){
        removeAll();
        for(TeamItem ti: team){
            add(ti);
        }
        validate();
        repaint();
    }
}
