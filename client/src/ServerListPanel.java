import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class ServerListPanel extends JPanel {

    private JList<InetAddress> serversList;
    private DefaultListModel<InetAddress> serversListModel;
    private RefreshButton refreshButton;

    public ServerListPanel(){
        setLayout(null);
        setSize(650, 900);
        setBackground(new Color(0,0,255));

        JLabel label = new JLabel("Servers");
        label.setSize(label.getMaximumSize());
        label.setLocation(0,0);
        this.add(label);

        serversListModel = new DefaultListModel<InetAddress>();
        serversList = new JList<InetAddress>(serversListModel);
        serversList.addListSelectionListener(new ServerListSelectionListener(serversList));
        serversList.setLayoutOrientation(JList.VERTICAL_WRAP);
        serversList.setBounds(0, 300, 200, 400);
        this.add(serversList);

        refreshButton = new RefreshButton(this);
        refreshButton.setSize(refreshButton.getMaximumSize());
        refreshButton.setLocation(0, 100);
        this.add(refreshButton);

        ClearButton cb = new ClearButton(serversListModel);
        cb.setSize(cb.getMaximumSize());
        cb.setLocation(100, 0);
        this.add(cb);

        this.add(new CreateButton());
    }

    public void clearServersList(){
        this.serversListModel.clear();
    }

    public void addServer(InetAddress address){
        //ServerButton serverButton = new ServerButton(address);
        this.serversListModel.addElement(address);
        //this.add(serverButton);
    }

    static class ServerListSelectionListener implements ListSelectionListener{
        private final JList<InetAddress> serverList;

        public ServerListSelectionListener(JList<InetAddress> serverList){
            this.serverList = serverList;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            GameWindow.getInstance().chooseServer(serverList.getSelectedValue());
            //System.out.println(serverList.getSelectedValue());
        }
    }

    static class ClearButton extends JButton{

        public ClearButton(DefaultListModel<InetAddress> listModel){
            setText("Clear");
            this.addActionListener(new ClearButtonListener(listModel));
        }

        static class ClearButtonListener implements ActionListener{
            private final DefaultListModel<InetAddress> listModel;
            public ClearButtonListener(DefaultListModel<InetAddress> listModel){
                this.listModel = listModel;
            }
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                listModel.clear();
            }
        }
    }

}
