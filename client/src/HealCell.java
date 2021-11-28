import java.awt.*;

public class HealCell extends Cell{

    public HealCell(){
        super();
        setBackground(Color.CYAN);
        setForeground(Color.CYAN);
    }

    @Override
    public CellType getType() {
        return CellType.HEAL;
    }
}
