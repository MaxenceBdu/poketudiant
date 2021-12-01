import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.KeyEvent;

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

    public ArrayList<GameListItem> askForGameList(InetAddress serverAddress){
        ArrayList<GameListItem> games = new ArrayList<>();
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
                    String[] game = socketReader.readLine().split(" ");
                    games.add(new GameListItem(game[1], Integer.parseInt(game[0])));
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

    public void askForGameJoin(String game){
        try{
            socketReader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            socketPrinter = new PrintStream(tcpSocket.getOutputStream());

            socketPrinter.println(JOIN_GAME+game);
            String response = socketReader.readLine();

            if(response.equals(JOIN_SUCCESS)){
                new ClientDaemon(ClientBack.getInstance().getSocketReader()).start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void generateMap(int lines, int columns, List<String> map){

        boolean found = false;

        // récupérer uniquement les cases "autour" du joueur
        int xplayer =0, yplayer = 0, xlimit1, xlimit2, ylimit1, ylimit2;

        for(int i = 0; i < lines; i++){
            String[] split = map.get(i).split("");
            for(int j = 0; j < columns; j++){
                if(split[j].equals("0")) {
                    xplayer = j;
                    yplayer = i;
                    found = true;
                    break;
                }
            }
            if(found){
                break;
            }
        }
        System.out.println("xplayer= "+xplayer+", yplayer= "+yplayer);

        if(xplayer < 7) {
            xlimit1 = 0;
            xlimit2 = 15;
        }else if(xplayer >= columns-8){
            xlimit1 = columns - 15;
            xlimit2 = columns;
        }else{
            xlimit1 = xplayer-7;
            xlimit2 = xplayer+8;
        }

        if(yplayer < 7) {
            ylimit1 = 0;
            ylimit2 = 15;
        }else if(yplayer >= lines-8){
            ylimit1 = lines - 15;
            ylimit2 = lines;
        }else{
            ylimit1 = yplayer-7;
            ylimit2 = yplayer+8;
        }

        //System.out.println("ylimit1: "+ylimit1);
        //System.out.println("ylimit2: "+ylimit2);
        //System.out.println("xlimit1: "+xlimit1);
        //System.out.println("xlimit2: "+xlimit2);
        List<JLabel> cells = new ArrayList<>(15*15);

        for(int i = ylimit1; i < ylimit2; i++){
            String[] split = map.get(i).split("");
            for(int j = xlimit1; j < xlimit2; j++){
                switch (split[j]) {
                    case "0":
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.PLAYER0));
                        break;
                    case "1":
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.PLAYER1));
                        break;
                    case "2":
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.PLAYER2));
                        break;
                    case "3":
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.PLAYER3));
                        break;
                    case "+":
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.HEAL));
                        break;
                    case "*":
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.GRASS));
                        break;
                    default:
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.NEUTRAL));
                        break;
                }
            }
        }
        DisplayWindow.getInstance().displayMap(cells);
    }

    public void playerMoveUp(){
        sendPlayerAction(MOVE_UP);
    }

    public void playerMoveDown(){
        sendPlayerAction(MOVE_DOWN);
    }

    public void playerMoveLeft(){
        sendPlayerAction(MOVE_LEFT);
    }

    public void playerMoveRight(){
        sendPlayerAction(MOVE_RIGHT);
    }

    public void sendPlayerAction(String move){
        socketPrinter.println(move);
    }

    public void generateTeamDisplay(List<String> team){
        List<JLabel> pokeTeamf
        for(String poke : team){
            String[] infosPoke = poke.split(" ");
        }
    }

    public void interpretMessage(String message){

    }
}