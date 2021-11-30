import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ClientDaemon extends Thread{

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
                //System.out.println(data);

                if(data.startsWith("map")){
                    //System.out.println("startsWith working");
                    String[] split = data.split(" ");

                    int lines = Integer.parseInt(split[1]);
                    int columns = Integer.parseInt(split[2]);
                    //System.out.println(lines + " " + columns);

                    List<String> map = new ArrayList<>(lines);
                    for(int i =0; i < lines; i++){
                        String s = socketReader.readLine();
                        //System.out.println("dans for :"+s);
                        map.add(s);
                    }
                    //System.out.println("map size = "+map.size());
                    ClientBack.getInstance().generateMap(lines, columns, map);
                }else{
                    ClientBack.getInstance().interpretMessage(data);
                }
            }catch(SocketTimeoutException e){
                // Do nothing
            }catch(IOException e){
                System.out.println(e.getMessage());
            }

        }


    }
}
