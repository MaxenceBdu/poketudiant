import java.awt.*;

public class NeutralCell extends Cell{

    public NeutralCell(){
        super();
        setBackground(Color.LIGHT_GRAY);
        setForeground(Color.LIGHT_GRAY);
    }

    @Override
    public CellType getType() {
        return CellType.NEUTRAL;
    }
}
