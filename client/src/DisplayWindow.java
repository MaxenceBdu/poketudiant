import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/*
    Application frame, also a singleton
 */
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

    /*
        Creates the panel for the menu and displays it
     */
    public void goToMenu(boolean disconnected){
        if(menuPanel == null) {
            menuPanel = new MenuPanel(this.getWidth(), this.getHeight());
        }
        setContentPane(menuPanel);
        homePanel = null;
        if(disconnected){
            JOptionPane.showMessageDialog(this,"Vous avez été déconnecté du serveur");
        }
    }

    /*
        Creates the game panel and displays it
     */
    public void displayGame(List<JLabel> cells) {
        gamePanel.repaintMap(cells);
    }

    /*
        Function called to update the display of the sidebar
     */
    public void displayTeam(List<TeamItem> team){
        gamePanel.displayTeam(team);
    }

    /*
        Displays the game panel and the map then adds keyboard listener
     */
    public void goToGame(){
        menuPanel.setVisible(false);
        menuPanel = null;

        if(gamePanel == null)
            gamePanel = new GamePanel(getWidth(), getHeight());
        setContentPane(gamePanel);

        /* KeyListener here because not working in gamePanel */
        if(getKeyListeners().length == 0){
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

    }

    /*
        Swap to the fight panel
        wild parameter is to disable 'capturer' and 'fuir' buttons
     */
    public void displayFight(boolean wild){
        if(gamePanel != null){
            gamePanel.displayFight(wild);
        }
    }
}