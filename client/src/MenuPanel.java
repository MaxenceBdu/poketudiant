import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;

public class MenuPanel extends JLayeredPane {
    private GameCreationPanel gameCreationPanel;

    private JList<GameListItem> gameList;
    private final DefaultListModel<GameListItem> gameListModel;

    private JList<InetAddress> serversList;
    private final DefaultListModel<InetAddress> serversListModel;

    public MenuPanel(int width, int height){
        setLayout(null);
        setSize(width, height);
        setBackground(new Color(115,115,255));

        /* Servers part */
        JLabel label = new JLabel("Serveurs");
        label.setSize(label.getMaximumSize());
        label.setLocation(0,0);
        add(label);

        serversListModel = new DefaultListModel<>();
        serversList = new JList<>(serversListModel);
        serversList.addListSelectionListener(new ServerSelectionListener(serversList, this));
        serversList.setLayoutOrientation(JList.VERTICAL_WRAP);
        serversList.setBounds(0, 300, 400, 400);
        add(serversList);

        RefreshButton rb = new RefreshButton(this);
        rb.setSize(rb.getMaximumSize());
        rb.setLocation(0, 100);
        add(rb);

        /* Games part */
        JLabel title = new JLabel("Parties");
        title.setSize(title.getMaximumSize());
        title.setLocation(700, 10);
        add(title);

        gameListModel = new DefaultListModel<>();
        gameList = new JList<>(gameListModel);
        gameList.addListSelectionListener(new GameSelectionListener(gameList));
        gameList.setLayoutOrientation(JList.VERTICAL_WRAP);
        gameList.setBounds(700, 300, 200, 400);
        add(gameList);

        CreateButton cr = new CreateButton(this);
        cr.setSize(cr.getMaximumSize());
        cr.setLocation(800, 0);
        add(cr);

    }

    public void showGameCreationPanel(){
        if(gameCreationPanel == null){
            gameCreationPanel = new GameCreationPanel();
            this.add(gameCreationPanel);
            setLayer(gameCreationPanel, POPUP_LAYER);
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
}
