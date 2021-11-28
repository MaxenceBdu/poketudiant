import java.awt.*;

public class GrassCell extends Cell{

    public GrassCell(){
        super();
        setBackground(Color.GREEN);
        setForeground(Color.GREEN);
    }
    @Override
    public CellType getType() {
        return CellType.GRASS;
    }
}
