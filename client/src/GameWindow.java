import javax.swing.*;
import java.net.InetAddress;
import java.awt.*;
import java.util.ArrayList;

public class GameWindow extends JFrame{

    private ClientBack clientBack;
    private static GameWindow gameWindowInstance;

    private GameWindow() {
        super("Pokétudiant - Boisédu & Gaudissard");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0,screen.width, screen.height);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static GameWindow getInstance(){
        if(GameWindow.gameWindowInstance == null){
            GameWindow.gameWindowInstance = new GameWindow();
        }

        return GameWindow.gameWindowInstance;
    }

    protected void setClientBack(ClientBack clientBack){
        this.clientBack = clientBack;
    }

    public void sendAddressToFront(InetAddress address){
        JoinCreate.getInstance().displayServers(address);
        this.setContentPane(JoinCreate.getInstance());
    }


    public void chooseServer(InetAddress serverAdress){
        this.clientBack.askForGameList(serverAdress);
    }

    public void sendGameListToFront(String gameList){
        JoinCreate.getInstance().displayGameList(gameList);
    }
}