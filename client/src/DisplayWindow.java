import javax.swing.*;
import java.awt.*;

public class DisplayWindow extends JFrame{

    private static DisplayWindow displayWindowInstance;
    private HomePanel homePanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;

    private DisplayWindow() {
        super("Pokétudiant - Boisédu & Gaudissard");
        setSize(1300, 900);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        homePanel = new HomePanel(this.getWidth(), this.getHeight());
        this.setContentPane(homePanel);
        setVisible(true);
    }

    public static DisplayWindow getInstance(){
        if(DisplayWindow.displayWindowInstance == null){
            DisplayWindow.displayWindowInstance = new DisplayWindow();
        }

        return DisplayWindow.displayWindowInstance;
    }

    public void leaveHomeForMenu(){
        if(menuPanel == null) {
            menuPanel = new MenuPanel(this.getWidth(), this.getHeight());
        }
        setContentPane(menuPanel);
        homePanel = null;
    }
}