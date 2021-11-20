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

    /*
    public void sendGameListToFront(String gameList){
        List<GameListItem> games = new ArrayList<>();

        String[] lines = gameList.split("\n");
        int gameNumber = Integer.parseInt(lines[0].substring(lines[0].length()-1));
        if(gameNumber > 0){
            for(int i = 1; i < gameNumber; i++){
                String[] gameInfos = lines[i].split(" ");
                int nbPlayers = Integer.parseInt(gameInfos[0]);
                String gameName = gameInfos[1];
                games.add(new GameListItem(gameName, nbPlayers));
            }
        }
        joinCreatePanel.displayGameList(games);
    }*/

    public void leaveHomeForMenu(){
        if(menuPanel == null) {
            menuPanel = new MenuPanel(this.getWidth(), this.getHeight());
        }
        setContentPane(menuPanel);
        homePanel = null;
    }
}