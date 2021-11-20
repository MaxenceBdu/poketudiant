import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;

public class MenuPanel extends JLayeredPane {
    private GameCreationPanel gameCreationPanel;

    private JList<String> gameList;
    private DefaultListModel<String> gameListModel;

    private JList<InetAddress> serversList;
    private DefaultListModel<InetAddress> serversListModel;

    public MenuPanel(int width, int height){
        setLayout(null);
        setSize(width, height);

        /* Servers part */
        JLabel label = new JLabel("Servers");
        label.setSize(label.getMaximumSize());
        label.setLocation(0,0);
        add(label);

        serversListModel = new DefaultListModel<>();
        serversList = new JList<>(serversListModel);
        serversList.addListSelectionListener(new ServerListSelectionListener(serversList, this));
        serversList.setLayoutOrientation(JList.VERTICAL_WRAP);
        serversList.setBounds(0, 300, 200, 400);
        add(serversList);

        RefreshButton rb = new RefreshButton(this);
        rb.setSize(rb.getMaximumSize());
        rb.setLocation(0, 100);
        add(rb);


        /* Games part */
        JLabel title = new JLabel("Games");
        title.setSize(title.getMaximumSize());
        title.setLocation(700, 10);
        add(title);

        gameListModel = new DefaultListModel<>();
        gameList = new JList<>(gameListModel);
        gameList.setSize(gameList.getMaximumSize());
        gameList.setLocation(700, 300);
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
        this.serversListModel.clear();
        for(InetAddress addr: addresses){
            serversListModel.addElement(addr);
        }
    }

    public void displayGames(ArrayList<String> games){
        gameListModel.clear();
        for(String s: games){
            gameListModel.addElement(s);
        }
    }

    static class ServerListSelectionListener implements ListSelectionListener {
        private MenuPanel menuPanel;
        private final JList<InetAddress> serverList;

        public ServerListSelectionListener(JList<InetAddress> serverList, MenuPanel menuPanel){
            this.menuPanel = menuPanel;
            this.serverList = serverList;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            menuPanel.displayGames(ClientBack.getInstance().askForGameList(serverList.getSelectedValue()));
        }
    }

    static class RefreshButton extends JButton {
        public RefreshButton(MenuPanel menuPanel){
            this.setText("Search for servers");
            this.addActionListener(new RefreshButtonListener(menuPanel));
        }

        static class RefreshButtonListener implements ActionListener {
            private MenuPanel menuPanel;

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
