import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ClientDaemon extends Thread implements ConstantMessages{

    private BufferedReader socketReader;
    private boolean canRun;

    public ClientDaemon(BufferedReader socketReader){
        this.socketReader = socketReader;
        canRun = true;
        setDaemon(true);
    }

    public void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }

    public void setSocketReader(BufferedReader socketReader) {
        this.socketReader = socketReader;
    }

    @Override
    public void run() {
        /*
            When the messages are about the map, the team or an index ask,
            we have to read incoming lines, so they are special cases
         */
        while(canRun){
            try{
                String data = socketReader.readLine();

                if(data == null){
                    throw new IOException();
                }

                if(data.startsWith("map")){ // If it's map information
                    String[] split = data.split(" ");

                    int lines = Integer.parseInt(split[1]);
                    int columns = Integer.parseInt(split[2]);

                    List<String> map = new ArrayList<>(lines);
                    for(int i =0; i < lines; i++){
                        String s = socketReader.readLine();
                        map.add(s);
                    }
                    ClientBack.getInstance().generateMap(lines, columns, map);
                }else if(data.startsWith("team")){ // If it's team information
                    String[] split = data.split(" ");
                    int nbPoketudiants = Integer.parseInt(split[2]);
                    List<String> team = new ArrayList<>();
                    for(int i = 0; i < nbPoketudiants; i++){
                        String s = socketReader.readLine();
                        team.add(s);
                    }
                    ClientBack.getInstance().generateTeamDisplay(team);
                }else{
                    // It's an 'encounter' message
                    if(data.equals(ConstantMessages.ASK_POKE_INDEX)){ // 'encounter enter poketudiant index'
                        int nbPoke = Integer.parseInt(socketReader.readLine().split(" ")[2]);
                        String[] team = new String[nbPoke];
                        for(int i = 0; i < nbPoke; i++){
                            team[i] = i+" "+socketReader.readLine();
                        }
                        DisplayWindow.getInstance().getGamePanel().chooseNewPoketudiant(team);
                    }
                    ClientBack.getInstance().interpretFightMessage(data);
                }
            } catch(SocketTimeoutException e){
                // Do nothing
            }catch (IOException e){
                try {
                    socketReader.close();
                } catch (IOException ex) {
                    //ex.printStackTrace();
                }
                DisplayWindow.getInstance().goToMenu(true);
                ClientBack.getInstance().closeTCPCommunication();
            }

        }
    }
}
