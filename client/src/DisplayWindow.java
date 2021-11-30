import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class DisplayWindow extends JFrame{

    private static DisplayWindow displayWindowInstance;
    private HomePanel homePanel;
    private MenuPanel menuPanel;
    private MapPanel mapPanel;

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

    public void displayMap(List<JLabel> cells) {
        if (mapPanel == null) {
            menuPanel.setVisible(false);
            menuPanel = null;

            mapPanel = new MapPanel(cells);
            setContentPane(mapPanel);
        }else{
            mapPanel.repaintMap(cells);
        }
    }

}