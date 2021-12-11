import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PoketudiantMoveButton extends JButton {

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

    public enum MoveDirection{
        monter,descendre
    }
}
