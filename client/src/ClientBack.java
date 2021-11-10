import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ClientBack{
    final int UDP_PORT = 9000;
    final int TCP_PORT = 9001;
    final String LOOKING_FOR_SERVERS = "looking for poketudiant servers";
    final String SERVER_FOUND = "i'm a poketudiant server";

    final String ASK_GAME_LIST = "require game list";

    private final GameWindow gameWindow;

    public ClientBack(GameWindow gameWindow){
        this.gameWindow = gameWindow;
        this.gameWindow.setClientBack(this);

        this.askForServers();
    }

    protected void askForServers() {
        int udpPort = 9000;

        byte[] buf1 = LOOKING_FOR_SERVERS.getBytes();
        byte[] buf2 = new byte[50];
        try {
            InetAddress address = InetAddress.getByName("255.255.255.255");

            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);

            DatagramPacket packet1 = new DatagramPacket(buf1, buf1.length, address, UDP_PORT);

            DatagramPacket packet2 = new DatagramPacket(buf2, buf2.length);

            socket.setSoTimeout(10000);
            socket.send(packet1);
            try{
                while (true) {
                    socket.receive(packet2);
                    String realResponse = new String(packet2.getData(), 0, packet2.getLength());
                    if (realResponse.equals(SERVER_FOUND)) {
                        this.gameWindow.sendAddressToFront(packet2.getAddress());
                    }
                }
            }catch (SocketTimeoutException e){
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public void askForGameList(InetAddress serverAddress){
        byte[] buf1 = ASK_GAME_LIST.getBytes();
        byte[] buf2 = new byte[256];
        try{
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket send = new DatagramPacket(buf1, buf1.length, serverAddress, TCP_PORT);
            DatagramPacket receive = new DatagramPacket(buf2, buf2.length);

            socket.send(send);
            socket.setSoTimeout(5000);

            try{
                socket.receive(receive);
            }catch(SocketTimeoutException e){
                socket.close();
                GameWindow.getInstance().sendGameListToFront("Server took too much time");
            }

            socket.close();
            GameWindow.getInstance().sendGameListToFront(new String(receive.getData()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}