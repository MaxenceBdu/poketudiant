import java.awt.*;

public class Player extends Cell{

    public Player() {

        setForeground(Color.WHITE);
        setBackground(Color.WHITE);
    }

    @Override
    public CellType getType() {
        return CellType.PLAYER;
    }
}
