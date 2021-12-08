import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class DisplayWindow extends JFrame {

    private static DisplayWindow displayWindowInstance;
    private HomePanel homePanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private MapPanel mapPanel;
    private TeamPanel teamPanel;

    private boolean inGame;
    private boolean inChat;

    private DisplayWindow() {
        super("Pokétudiant - Boisédu & Gaudissard");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dim);
        //System.out.println("Window height: "+dim.height);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        homePanel = new HomePanel(dim);
        this.setContentPane(homePanel);
        setVisible(true);
        setFocusable(true);
    }

    public static DisplayWindow getInstance(){
        if(DisplayWindow.displayWindowInstance == null){
            DisplayWindow.displayWindowInstance = new DisplayWindow();
        }

        return DisplayWindow.displayWindowInstance;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void leaveHomeForMenu(){
        if(menuPanel == null) {
            menuPanel = new MenuPanel(this.getWidth(), this.getHeight());
        }
        setContentPane(menuPanel);
        homePanel = null;
    }

    public void displayMap(List<JLabel> cells) {
        if(gamePanel == null){
            menuPanel.setVisible(false);
            menuPanel = null;

            gamePanel = new GamePanel(cells, getWidth(), getHeight());
            setContentPane(gamePanel);
        }else{
            gamePanel.repaintMap(cells);
        }
    }

    public void displayTeam(List<TeamItem> team){
        gamePanel.displayTeam(team);
    }

    public void goToGame(){
        menuPanel.setVisible(false);
        menuPanel = null;

        gamePanel = new GamePanel(getWidth(), getHeight());
        setContentPane(gamePanel);

        /* KeyListener here because not working in gamePanel */
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent keyEvent) {
                // Empty
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                // Empty
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                /* Appui touches pour déplacement */
                //System.out.println("key listener");
                switch (keyEvent.getKeyCode()){
                    case KeyEvent.VK_Z:
                        ClientBack.getInstance().playerMoveUp();
                        break;
                    case KeyEvent.VK_Q:
                        ClientBack.getInstance().playerMoveLeft();
                        break;
                    case KeyEvent.VK_S:
                        ClientBack.getInstance().playerMoveDown();
                        break;
                    case KeyEvent.VK_D:
                        ClientBack.getInstance().playerMoveRight();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void displayFight(boolean wild){
        if(gamePanel != null){
            gamePanel.displayFight(wild);
            //this.removeKeyListener(this.getKeyListeners()[0]);
        }
    }
}