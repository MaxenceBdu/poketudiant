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

    public ClientBack(GameWindow gameWindow){
        gameWindow.setClientBack(this);
        try {
            udpSocket = new DatagramSocket();
            udpSocket.setBroadcast(true);
            udpSocket.setSoTimeout(5000);
            this.askForServers();
        }catch(SocketException e){
            e.printStackTrace();
        }
    }

    /**
     * Send message in broadcast to check the existence of poketudiant servers (UDP)
     *
     */
    protected void askForServers() {

        byte[] buf1 = LOOKING_FOR_SERVERS.getBytes();
        byte[] buf2 = new byte[50];
        try {
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
                        GameWindow.getInstance().sendAddressToFront(receive.getAddress());
                    }
                }
            }catch(SocketTimeoutException e){}
            catch(IOException e){
                e.printStackTrace();
            }
        }catch(UnknownHostException e){
            e.printStackTrace();
        }
    }

    private boolean newTcpSocketNeeded(InetAddress serverAddress){
        return tcpSocket == null || !serverAddress.getHostAddress().equals(tcpSocket.getInetAddress().getHostAddress());
    }

    /**
     * Ask to the chosen server the list of games (TCP)
     *
     * @param serverAddress InetAdress of the chosen server
     */
    public void askForGameList(InetAddress serverAddress){

        if(newTcpSocketNeeded(serverAddress)){
            try{
                tcpSocket = new Socket(serverAddress, TCP_PORT);
                tcpSocket.setSoTimeout(5000);

                socketReader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
                socketPrinter = new PrintStream(tcpSocket.getOutputStream());

                socketPrinter.println(ASK_GAME_LIST);
                String response = socketReader.readLine();

                // socketPrinter.flush();
                // socketReader.reset();
                System.out.println(response);
                GameWindow.getInstance().sendGameListToFront(response);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void askForGameCreation(String gameName){

        try{
            socketReader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            socketPrinter = new PrintStream(tcpSocket.getOutputStream());

            socketPrinter.println(CREATE_GAME+gameName);
            String response = socketReader.readLine();

            // socketPrinter.flush();
            // socketReader.reset();
            System.out.println(response);
            GameWindow.getInstance().sendGameListToFront(response);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}