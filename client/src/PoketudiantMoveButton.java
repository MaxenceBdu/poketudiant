import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PoketudiantMoveButton extends JButton {

    PoketudiantMoveButton(int id, MoveDirection direction){
        setText(direction.name().toUpperCase());
        addActionListener(new PoketudiantMoveListener(id, direction));
    }

    static class PoketudiantMoveListener implements ActionListener {
        private int id;
        private MoveDirection direction;
        PoketudiantMoveListener(int id, MoveDirection direction){
            this.id = id;
            this.direction = direction;

        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ClientBack.getInstance().sendPoketudiantMove(id,direction.name());
        }
    }

    public enum MoveDirection{
        monter,descendre
    }
}
