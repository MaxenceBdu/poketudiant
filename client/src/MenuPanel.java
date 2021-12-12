import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Objects;

public class MenuPanel extends JLayeredPane {
    private GameCreationPanel gameCreationPanel;

    private JList<GameListItem> gameList;
    private final DefaultListModel<GameListItem> gameListModel;

    private JList<InetAddress> serversList;
    private final DefaultListModel<InetAddress> serversListModel;

    public MenuPanel(int width, int height){
        setLayout(null);
        setSize(width, height);
        this.setBackground(new Color(115,115,255));

        try{
            Image image = new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/FOND-ACCUEIL.jpg")))).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            JLabel background = new JLabel(new ImageIcon(image));
            background.setSize(width, height);
            background.setLocation(0,0);
            add(background, DEFAULT_LAYER);
        }catch(IOException e){
            e.printStackTrace();
        }

        /* Servers part */
        JLabel label = new JLabel("Serveurs");
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
        label.setSize(label.getMaximumSize());
        label.setLocation(width/4-label.getWidth(),10);
        add(label, PALETTE_LAYER);

        // Button to refresh servers' list
        RefreshButton rb = new RefreshButton(this);
        rb.setSize(rb.getMaximumSize());
        rb.setLocation(width/4-rb.getWidth(), label.getY()+80);
        add(rb, PALETTE_LAYER);

        // List of servers
        serversListModel = new DefaultListModel<>();
        serversList = new JList<>(serversListModel);
        serversList.addListSelectionListener(new ServerSelectionListener(serversList, this));
        serversList.setLayoutOrientation(JList.VERTICAL_WRAP);
        serversList.setSize((int)(width/2*0.7), (int)(height*0.6));
        serversList.setLocation(width/4 - serversList.getWidth()/2, rb.getY()+150);
        serversList.setFixedCellWidth(serversList.getWidth());
        serversList.setFixedCellHeight(serversList.getHeight()/10);
        serversList.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
        add(serversList,PALETTE_LAYER);

        /* Games part */
        JLabel title = new JLabel("Parties");
        title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
        title.setSize(title.getMaximumSize());
        title.setLocation(width/4*3 - title.getWidth()/2, 10);
        add(title,PALETTE_LAYER);

        CreateButton cr = new CreateButton(this);
        cr.setSize(cr.getMaximumSize());
        cr.setLocation(width/4*3 - cr.getWidth()/2, title.getY()+80);
        add(cr,PALETTE_LAYER);

        // List of games
        gameListModel = new DefaultListModel<>();
        gameList = new JList<>(gameListModel);
        gameList.addListSelectionListener(new GameSelectionListener(gameList));
        gameList.setLayoutOrientation(JList.VERTICAL_WRAP);
        gameList.setSize((int)(width/2*0.7), (int)(height*0.6));
        gameList.setLocation(width/4*3 - gameList.getWidth()/2, cr.getY()+150);
        gameList.setFixedCellWidth(gameList.getWidth());
        gameList.setFixedCellHeight(gameList.getHeight()/4);
        gameList.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 45));
        add(gameList,PALETTE_LAYER);

    }

    public void showGameCreationPanel(){
        if(gameCreationPanel == null){
            gameCreationPanel = new GameCreationPanel(getWidth(), getHeight());
            this.add(gameCreationPanel, POPUP_LAYER);
        }else{
            gameCreationPanel.setVisible(true);
        }
    }

    public void addServers(ArrayList<InetAddress> addresses){
        serversListModel.clear();
        serversListModel.addAll(addresses);
    }

    public void displayGames(ArrayList<GameListItem> games){
        gameListModel.clear();
        gameListModel.addAll(games);
    }

    static class ServerSelectionListener implements ListSelectionListener {
        private final MenuPanel menuPanel;
        private final JList<InetAddress> serverList;

        public ServerSelectionListener(JList<InetAddress> serverList, MenuPanel menuPanel){
            this.menuPanel = menuPanel;
            this.serverList = serverList;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            menuPanel.displayGames(ClientBack.getInstance().askForGameList(serverList.getSelectedValue()));
        }
    }

    static class GameSelectionListener implements ListSelectionListener {
        private final JList<GameListItem> gameList;

        public GameSelectionListener(JList<GameListItem> gameList){
            this.gameList = gameList;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            ClientBack.getInstance().askForGameJoin(gameList.getSelectedValue().getName());
        }
    }

    static class RefreshButton extends JButton {
        public RefreshButton(MenuPanel menuPanel){
            this.setText("Chercher des serveurs");
            this.addActionListener(new RefreshButtonListener(menuPanel));
        }

        static class RefreshButtonListener implements ActionListener {
            private final MenuPanel menuPanel;

            public RefreshButtonListener(MenuPanel menuPanel){
                this.menuPanel =menuPanel;
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPanel.addServers(ClientBack.getInstance().askForServers());
            }
        }
    }

    static class CreateButton extends JButton {
        /*
            Displays a pop-up to create a new game
         */
        CreateButton(MenuPanel menuPanel){
            setText("Créer une partie");
            this.addActionListener(new CreateButtonListener(menuPanel));
        }

        static class CreateButtonListener implements ActionListener{
            private final MenuPanel menuPanel;
            public CreateButtonListener(MenuPanel menuPanel){
                this.menuPanel = menuPanel;
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPanel.showGameCreationPanel();
            }
        }
    }
}
