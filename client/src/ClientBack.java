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

    private Socket tcpSocket;
    private BufferedReader socketReader;
    private PrintStream socketPrinter;

    public ClientBack(GameWindow gameWindow){
        gameWindow.setClientBack(this);
        this.askForServers();
    }

    /**
     * Send udp message in broadcast to check the existence of poketudiant servers
     *
     */
    protected void askForServers() {

        byte[] buf1 = LOOKING_FOR_SERVERS.getBytes();
        byte[] buf2 = new byte[50];
        try {
            InetAddress address = InetAddress.getByName("255.255.255.255");

            try{
                DatagramSocket socket = new DatagramSocket();
                socket.setBroadcast(true);
                socket.setSoTimeout(5000);

                DatagramPacket packet1 = new DatagramPacket(buf1, buf1.length, address, UDP_PORT);
                DatagramPacket packet2 = new DatagramPacket(buf2, buf2.length);

                try{
                    socket.send(packet1);
                }catch(IOException e){
                    e.printStackTrace();
                }

                try{
                    while (true) {
                        socket.receive(packet2);
                        String realResponse = new String(packet2.getData(), 0, packet2.getLength());
                        if (realResponse.equals(SERVER_FOUND)) {
                            GameWindow.getInstance().sendAddressToFront(packet2.getAddress());
                        }
                    }
                }catch(SocketTimeoutException e){
                    socket.close();
                }
                catch(IOException e){
                    socket.close();
                    e.printStackTrace();
                }
            }catch(SocketException se){
                se.printStackTrace();
            }

        }catch(UnknownHostException e){
            e.printStackTrace();
        }
    }

    public void askForGameList(InetAddress serverAddress){
        try{
            if(tcpSocket != null && !serverAddress.getHostAddress().equals(tcpSocket.getInetAddress().getHostAddress())){
                tcpSocket.close();
                socketReader.close();
                socketPrinter.close();
            }

            Socket tcpSocket = new Socket(serverAddress, 9001);

            socketReader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            socketPrinter = new PrintStream(tcpSocket.getOutputStream());

            tcpSocket.setSoTimeout(5000);
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