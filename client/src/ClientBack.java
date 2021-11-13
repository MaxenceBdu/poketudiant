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
    private DatagramPacket send;
    private DatagramPacket receive;

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

            DatagramPacket packet1 = new DatagramPacket(buf1, buf1.length, address, UDP_PORT);
            DatagramPacket packet2 = new DatagramPacket(buf2, buf2.length);

            try{
                udpSocket.send(packet1);
            }catch(IOException e){
                e.printStackTrace();
            }

            try{
                while (true) {
                    udpSocket.receive(packet2);
                    String realResponse = new String(packet2.getData(), 0, packet2.getLength());
                    if (realResponse.equals(SERVER_FOUND)) {
                        GameWindow.getInstance().sendAddressToFront(packet2.getAddress());
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

                socketPrinter.println("require game list");
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

    private boolean newTcpSocketNeeded(InetAddress serverAddress){
        return tcpSocket == null || !serverAddress.getHostAddress().equals(tcpSocket.getInetAddress().getHostAddress());
    }
}