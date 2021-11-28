import javax.swing.*;

public abstract class Cell extends JLabel {
    public Cell(){
        setVisible(true);
    }

    public abstract CellType getType();
}
