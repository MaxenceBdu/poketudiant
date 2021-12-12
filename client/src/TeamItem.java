import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
    Each iteam of sidebar (team display)
 */
public class TeamItem extends JPanel {

    public TeamItem(ImageIcon poketudiant, int id, String lvl, String currentXp, String xpNextLevel, String currentPV, String maxPV){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new JLabel(poketudiant));
        add(new JLabel("Niveau "+lvl+ ": "+currentXp+"/"+xpNextLevel));
        add(new JLabel("PV: "+ currentPV+"/"+maxPV));
        switch(id){
            case 0:
                add(new PoketudiantMoveButton(id, MoveDirection.descendre));
                break;
            case 1:
                add(new PoketudiantMoveButton(id, MoveDirection.monter));
                add(new PoketudiantMoveButton(id, MoveDirection.descendre));
                break;
            case 2:
                add(new PoketudiantMoveButton(id, MoveDirection.monter));
                break;
            default:
                break;
        }
        add(new PoketudiantFreeButton(id));
    }

    /*
        Button to free the poketudiant
     */
    static class PoketudiantFreeButton extends JButton {

        public PoketudiantFreeButton(int id){
            setText("LIBERER");
            addActionListener(new PoketudiantFreeButtonListener(id));
        }

        static class PoketudiantFreeButtonListener implements ActionListener {
            private final int id;
            public PoketudiantFreeButtonListener(int id){
                this.id = id;
            }

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ClientBack.getInstance().sendPoketudiantFree(this.id);
            }
        }
    }

    /*
        Button to change the id of a poketudiant
     */
    static class PoketudiantMoveButton extends JButton {

        PoketudiantMoveButton(int id, MoveDirection direction){
            setText(direction.name().toUpperCase());
            addActionListener(new PoketudiantMoveListener(id, direction));
        }

        static class PoketudiantMoveListener implements ActionListener {
            private final int id;
            private final String direction;
            PoketudiantMoveListener(int id, MoveDirection direction){
                this.id = id;
                if(direction == MoveDirection.descendre){
                    this.direction = "down";
                }else{
                    this.direction = "up";
                }

            }

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ClientBack.getInstance().sendPoketudiantMove(id,direction);
            }
        }
    }
    public enum MoveDirection{
        monter,descendre
    }
}
