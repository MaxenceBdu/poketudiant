import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ClientDaemon extends Thread implements ConstantMessages{

    private final BufferedReader socketReader;

    public ClientDaemon(BufferedReader socketReader){
        this.socketReader = socketReader;
        setDaemon(true);
    }

    @Override
    public void run() {
        while(true){
            try{
                String data = socketReader.readLine();
                System.out.println("Reçu :"+data);

                if(data.startsWith("map")){
                    //System.out.println("map");
                    String[] split = data.split(" ");

                    int lines = Integer.parseInt(split[1]);
                    int columns = Integer.parseInt(split[2]);
                    //System.out.println(lines + " " + columns);

                    List<String> map = new ArrayList<>(lines);
                    for(int i =0; i < lines; i++){
                        String s = socketReader.readLine();
                        //System.out.println(s);
                        map.add(s);
                    }
                    //System.out.println("nb lignes map = "+map.size());
                    //System.out.println(map.get(0).length()+" "+map.get(0).split("").length);
                    ClientBack.getInstance().generateMap(lines, columns, map);
                }else if(data.startsWith("team")){
                    //System.out.println("Team reçue");
                    String[] split = data.split(" ");
                    int nbPoketudiants = Integer.parseInt(split[2]);
                    List<String> team = new ArrayList<>();
                    for(int i = 0; i < nbPoketudiants; i++){
                        String s = socketReader.readLine();
                        System.out.println("Reçu: "+s);
                        team.add(s);
                    }
                    ClientBack.getInstance().generateTeamDisplay(team);
                }else{
                    // It's an 'encounter' message (or blank message ????? (issue with teacher's server))
                    if(data.equals(ConstantMessages.ASK_POKE_INDEX)){
                        int nbPoke = Integer.parseInt(socketReader.readLine().split(" ")[2]);
                        String[] team = new String[nbPoke];
                        for(int i = 0; i < nbPoke; i++){
                            team[i] = i+" "+socketReader.readLine();
                        }
                        DisplayWindow.getInstance().getGamePanel().chooseNewPoketudiant(team);
                    }
                    ClientBack.getInstance().interpretFightMessage(data);
                }
            }catch(SocketTimeoutException e){
                // Do nothing
            }catch(IOException e){
                //System.out.println(e.getMessage());
            }

        }
    }
}
