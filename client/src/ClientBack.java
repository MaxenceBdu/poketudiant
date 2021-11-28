import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ClientBack implements ConstantMessages {
    final int UDP_PORT = 9000;
    final int TCP_PORT = 9001;

    private DatagramSocket udpSocket;

    private Socket tcpSocket;
    private BufferedReader socketReader;
    private PrintStream socketPrinter;

    private static ClientBack clientBackInstance;

    private ClientBack(){}

    public static ClientBack getInstance(){
        if(clientBackInstance == null){
            clientBackInstance = new ClientBack();
        }

        return clientBackInstance;
    }

    public Socket getTcpSocket(){
        return tcpSocket;
    }

    public BufferedReader getSocketReader() {
        return socketReader;
    }

    protected ArrayList<InetAddress> askForServers() {
        if(udpSocket == null){
            try {
                udpSocket = new DatagramSocket();
                udpSocket.setBroadcast(true);
                udpSocket.setSoTimeout(1000);
            }catch(SocketException e){
                e.printStackTrace();
                return null;
            }
        }

        byte[] buf1 = LOOKING_FOR_SERVERS.getBytes();
        byte[] buf2 = new byte[50];
        try {
            ArrayList<InetAddress> serverList = new ArrayList<>();
            InetAddress address = InetAddress.getByName("255.255.255.255");

            DatagramPacket send = new DatagramPacket(buf1, buf1.length, address, UDP_PORT);
            DatagramPacket receive = new DatagramPacket(buf2, buf2.length);

            try{
                udpSocket.send(send);
            }catch(IOException e){
                e.printStackTrace();
            }

            try{
                while (true) {
                    udpSocket.receive(receive);
                    String realResponse = new String(receive.getData(), 0, receive.getLength());
                    if (realResponse.equals(SERVER_FOUND)) {
                        serverList.add(receive.getAddress());
                    }
                }
            }catch(SocketTimeoutException e){
                return serverList;
            }
            catch(IOException e){
                e.printStackTrace();
                return null;
            }
        }catch(UnknownHostException e){
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> askForGameList(InetAddress serverAddress){
        ArrayList<String> games = new ArrayList<>();
        try{
            if (tcpSocket != null) {
                tcpSocket.close();
            }

            tcpSocket = new Socket(serverAddress, TCP_PORT);
            tcpSocket.setSoTimeout(5000);

            socketReader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            socketPrinter = new PrintStream(tcpSocket.getOutputStream());

            socketPrinter.println(ASK_GAME_LIST);
            String response = socketReader.readLine();

            String[] tab = response.split(" ");
            int nbGames = Integer.parseInt(tab[tab.length-1]);
            if(nbGames > 0){
                for(int i = 0; i < nbGames; i++){
                    games.add(socketReader.readLine());
                }
            }

            return games;
        }catch(IOException e){
            e.printStackTrace();
            return games;
        }
    }

    public boolean askForGameCreation(String gameName){

        try{
            socketReader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            socketPrinter = new PrintStream(tcpSocket.getOutputStream());

            socketPrinter.println(CREATE_GAME+gameName);
            String response = socketReader.readLine();

            return response.equals(GAME_CREATED);
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public void generateMap(int lines, int columns, List<String> map){
        if(!DisplayWindow.getInstance().getContentPane().isVisible()){
            List<Cell> cells = new ArrayList<>(15*15);

            // récupérer uniquement les cases "autour" du joueur
            int xplayer =0, yplayer = 0, xlimit1, xlimit2, ylimit1, ylimit2;
            for(int i = 0; i < lines; i++){
                String[] split = map.get(i).split("");
                for(int j = 0; j < columns; j++){
                    if(split[j].equals("0")){
                        xplayer = j;
                        yplayer = i;
                    }
                }
            }

            ylimit1= xplayer-7;
            ylimit2= 15-ylimit;
            xlimit1 = xplayer;
            xlimit2=0;

            for(int i = ylimit1; i < ylimit2; i++){
                String[] split = map.get(i).split("");
                for(int j = xlimit1; j < xlimit2; j++){
                    switch (split[j]) {
                        case "0":
                            System.out.println("player cell");
                            cells.add(new Player());
                            break;
                        case "+":
                            System.out.println("heal cell");
                            cells.add(new HealCell());
                            break;
                        case "*":
                            System.out.println("grass cell");
                            cells.add(new GrassCell());
                            break;
                        default:
                            System.out.println("neutral cell");
                            cells.add(new NeutralCell());
                            break;
                    }
                }
            }

            for(String s1: map){
                for(String s2: split){
                    //System.out.println(s2);

                }
            }
            DisplayWindow.getInstance().setContentPane(new MapPanel(cells));
            DisplayWindow.getInstance().getContentPane().setVisible(true);
        }else{
            System.out.println("Actualiser map !");
        }
    }

    public void interpretMessage(String message){
        System.out.println(message);
    }
}