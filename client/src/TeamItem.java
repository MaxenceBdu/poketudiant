import javax.swing.*;

public class TeamItem extends JPanel {

    public TeamItem(JLabel poketudiant, int id, String lvl, String currentXp, String xpNextLevel, String currentPV, String maxPV){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(poketudiant);
        add(new JLabel("Niveau "+lvl+ ":"+currentXp+"/"+xpNextLevel));
        add(new JLabel(currentPV+"/"+maxPV));
        switch(id){
            case 0:
                add(new PoketudiantMoveButton(id, PoketudiantMoveButton.MoveDirection.descendre));
                break;
            case 1:
                add(new PoketudiantMoveButton(id, PoketudiantMoveButton.MoveDirection.descendre));
                add(new PoketudiantMoveButton(id, PoketudiantMoveButton.MoveDirection.monter));
                break;
            case 2:
                add(new PoketudiantMoveButton(id, PoketudiantMoveButton.MoveDirection.monter));
                break;
            default:
                break;
        }
        add(new PoketudiantFreeButton(id));
    }
}
