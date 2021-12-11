import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PoketudiantFreeButton extends JButton {

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
