import javax.swing.*;
import java.util.List;

/*
    Panel that contains team display, map and fight scene
 */
public class GamePanel extends JLayeredPane {

    private final TeamPanel teamPanel;
    private final MapPanel mapPanel;
    private FightPanel fightPanel;
    private final int mainPanelSize,yOffset, xOffset;

    public GamePanel(int width, int height){
        setVisible(true);
        setLayout(null);
        setBounds(0,0,width, height);

        mainPanelSize = this.calculateMainPanelSize(width, height);
        yOffset = (height- mainPanelSize) / 2;
        xOffset = (width - mainPanelSize) / 2;

        mapPanel = new MapPanel(null, mainPanelSize, xOffset, yOffset);
        add(mapPanel);

        teamPanel = new TeamPanel(null, xOffset, height);
        add(teamPanel);
    }

    public int getMainPanelSize() {
        return mainPanelSize;
    }

    public int getxOffset(){
        return xOffset;
    }

    public FightPanel getFightPanel() {
        return fightPanel;
    }

    // Useful to fix the size of map's cells
    private int calculateMainPanelSize(int width, int height){
        int maxWidth = width/5*3;
        boolean found = false;
        int size = 600;
        while(!found){
            int temp = size+15;
            if(temp < maxWidth && size < height){
                size=temp;
            }else{
                found = true;
            }
        }
        return size;
    }

    public void repaintMap(List<JLabel> map){
        mapPanel.repaintMap(map);
    }

    public void displayTeam(List<TeamItem> team){
        teamPanel.refreshTeam(team);
    }

    public void displayFight(boolean wild){
        if(fightPanel == null){
            fightPanel = new FightPanel(mainPanelSize, xOffset, yOffset);
            add(fightPanel);
        }
        mapPanel.setVisible(false);
        fightPanel.enableButtons(wild);
        fightPanel.setVisible(true);
    }

    /*
        Exits the fight scene and displays notifications related to the previous fight
     */
    public void backToMap(int leaveType, boolean win){
        fightPanel.setVisible(false);
        mapPanel.setVisible(true);

        switch (leaveType){
            case 0:
                JOptionPane.showMessageDialog(this,"Pok??tudiant captur?? !");
                break;
            case 1:
                JOptionPane.showMessageDialog(this,"Vous avez pris la fuite");
                break;
            case 2:
                if(win)
                    JOptionPane.showMessageDialog(this,"Vous avez gagn?? votre combat !");
                else
                    JOptionPane.showMessageDialog(this,"Vous avez perdu votre combat...");
                break;
            default:
                break;
        }
    }

    public void xpNotification(String idPoketudiant, String xp){
        JOptionPane.showMessageDialog(this,"Votre pok??tudiant ?? la place "+idPoketudiant+" a gagn?? "+xp+" point d'exp??rience !");
    }

    public void lvlUpNotification(String idPoketudiant, String lvls){
        JOptionPane.showMessageDialog(this,"Votre pok??tudiant ?? la place "+idPoketudiant+" est mont?? de "+lvls+" niveaux !");
    }

    public void evolutionNotification(String oldPoke, String newPoke){
        JOptionPane.showMessageDialog(this,"Votre "+oldPoke+" a ??volu?? en "+newPoke+" !");
    }

    public void chooseNewPoketudiant(String[] tab){
        Object selectedPoke = JOptionPane.showOptionDialog(this,"Choisissez un nouveau pok??tudiant","Pok??tudiant",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,tab,tab[0]);
        if(selectedPoke != null){
            ClientBack.getInstance().sendSwitchPoketudiant(selectedPoke.toString().split(" ")[0]);
        }
    }

    public void forbiddenNotification(){
        JOptionPane.showMessageDialog(this,"Vous ne pouvez pas faire ??a !");
    }

    public void koNotification(boolean forPlayer){
        if(forPlayer){
            JOptionPane.showMessageDialog(this,"Votre pok??tudiant est KO.");
        }else{
            JOptionPane.showMessageDialog(this,"Le pok??tudiant adverse est KO.");
        }
    }

    public void failEscapeNotification(){
        JOptionPane.showMessageDialog(this,"Tentative de fuite ??chou??e.");
    }

    public void failCatchNotification(){
        JOptionPane.showMessageDialog(this,"Tentative de capture ??chou??e.");
    }

}
