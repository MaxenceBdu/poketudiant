import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FightPanel extends JLayeredPane {

    private final JButton leaveButton,catchButton,attack1, attack2, switchButton;

    private final JLabel poketudiantPlayer,poketudiantOpponent;


    public FightPanel(int size, int xOffset, int yOffset){
        //System.out.println("FightPanel size: "+size);
        setLayout(null);
        setVisible(true);
        setBounds(xOffset, yOffset, size, size);
        setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        setBackground(Color.GREEN);

        /*
        try{
            JLabel background = new JLabel(new ImageIcon(new ImageIcon(ImageIO.read(new File("src/assets/FOND-COMBAT.png"))).getImage().getScaledInstance(size, size-40, Image.SCALE_SMOOTH)));
            background.setSize(background.getMaximumSize());
            add(background, DEFAULT_LAYER);
        }catch(IOException e){
            System.out.println("Fond pour combat non trouvé");
        }*/

        leaveButton = new JButton("FUIR");
        leaveButton.setLocation((int)(size*0.8), (int) (size*0.9));
        leaveButton.setSize(leaveButton.getMaximumSize());
        leaveButton.addActionListener(new LeaveButtonListener());
        add(leaveButton, PALETTE_LAYER);

        catchButton = new JButton("CAPTURER");
        catchButton.setLocation((int)(size*0.8), (int) (size*0.8));
        catchButton.setSize(catchButton.getMaximumSize());
        catchButton.addActionListener(new CatchButtonListener());
        add(catchButton, PALETTE_LAYER);

        poketudiantPlayer = new JLabel();
        poketudiantPlayer.setLocation(50,500);
        add(poketudiantPlayer, PALETTE_LAYER);

        poketudiantOpponent = new JLabel();
        poketudiantOpponent.setLocation(500,50);
        add(poketudiantOpponent, PALETTE_LAYER);

        attack1 = new JButton();
        attack1.setLocation(size/10,(int) (size*0.8));
        attack1.addActionListener(new Attack1ButtonListener());
        add(attack1, PALETTE_LAYER);

        attack2 = new JButton();
        attack2.addActionListener(new Attack2ButtonListener());
        add(attack2, PALETTE_LAYER);

        switchButton = new JButton("CHANGER");
        switchButton.setSize(switchButton.getMaximumSize());
        switchButton.setLocation(size/9, (int) (size*0.9));
        switchButton.addActionListener(new SwitchButtonListener());
        add(switchButton, PALETTE_LAYER);
    }

    public void setEnabledButtons(boolean wild){
        leaveButton.setEnabled(wild);
        catchButton.setEnabled(wild);
    }

    public void displayPlayerPoketudiant(ImageIcon poketudiant, String variety, String hp, String lvl, String attack1, String attack2){
        poketudiantPlayer.setText("<html>"+variety+"<br/> PV: "+hp+"<br/> Nv: "+lvl+"</html>");
        poketudiantPlayer.setIcon(poketudiant);
        poketudiantPlayer.setSize(poketudiantPlayer.getMaximumSize());

        this.attack1.setText(attack1);
        this.attack1.setSize(this.attack1.getMaximumSize());

        this.attack2.setSize(this.attack2.getMaximumSize());
        this.attack2.setLocation(this.attack1.getX()+this.attack1.getWidth()+10, this.attack1.getY());
        this.attack2.setText(attack2);
        validate();
        repaint();
    }

    public void displayOpponentPoketudiant(ImageIcon poketudiant, String variety, String hp, String lvl){
        poketudiantOpponent.setText("<html>"+variety+"<br/> PV: "+hp+"<br/> Nv: "+lvl+"</html>");
        poketudiantOpponent.setIcon(poketudiant);
        poketudiantOpponent.setSize(poketudiantOpponent.getMaximumSize());
        validate();
        repaint();
    }

    static class Attack1ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ClientBack.getInstance().sendPlayerActionFight("attack1");
        }
    }

    static class Attack2ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ClientBack.getInstance().sendPlayerActionFight("attack2");
        }
    }

    static class SwitchButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ClientBack.getInstance().sendPlayerActionFight("switch");
        }
    }

    static class CatchButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ClientBack.getInstance().sendPlayerActionFight("catch");
        }
    }

    static class LeaveButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ClientBack.getInstance().sendPlayerActionFight("leave");
        }
    }
}
