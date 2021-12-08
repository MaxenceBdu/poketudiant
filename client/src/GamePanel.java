import javax.swing.*;
import java.util.List;

public class GamePanel extends JPanel {

    private TeamPanel teamPanel;
    private final MapPanel mapPanel;
    private FightPanel fightPanel;
    //private ChatPanel chatPanel;
    private final int mainPanelSize,yOffset, xOffset;

    public GamePanel(List<JLabel> map, int width, int height){
        setVisible(true);
        setLayout(null);
        setBounds(0,0,width, height);

        mainPanelSize = this.calculateMainPanelSize(width, height);
        yOffset = (height- mainPanelSize) / 2;
        xOffset = (width - mainPanelSize) / 2;

        mapPanel = new MapPanel(map, mainPanelSize, xOffset, yOffset);
        add(mapPanel);


    }

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
        fightPanel.setEnabledButtons(wild);
        fightPanel.setVisible(true);
    }

    public void displayMap(){
        fightPanel.setVisible(false);
        mapPanel.setVisible(true);
    }
}
