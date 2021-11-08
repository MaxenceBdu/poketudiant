import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ClientBack{
    private final ClientFront clientFront;
    private InetAddress serverAddress;

    public ClientBack(ClientFront clientFront){
        this.clientFront = clientFront;
        this.clientFront.setClientBack(this);

        this.askForServers();
    }

    protected void askForServers() {
        final String LOOKING_FOR_SERVERS = "looking for poketudiant servers";
        final String SERVER_FOUND = "i'm a poketudiant server";

        int udpPort = 9000;
        boolean found = false;

        byte[] buf1 = LOOKING_FOR_SERVERS.getBytes();
        byte[] buf2 = new byte[50];
        try {
            InetAddress address = InetAddress.getByName("255.255.255.255");

            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);

            DatagramPacket packet1 = new DatagramPacket(buf1, buf1.length, address, udpPort);

            DatagramPacket packet2 = new DatagramPacket(buf2, buf2.length);

            socket.setSoTimeout(10000);
            socket.send(packet1);
            List<InetAddress> addresses = new ArrayList<>();
            try{
                while (true) {
                    socket.receive(packet2);
                    String realResponse = new String(packet2.getData(), 0, packet2.getLength());
                    String ip = packet2.getAddress().getHostAddress();

                    //System.out.println("Received : " + realResponse + " from " + ip);
                    if (realResponse.equals(SERVER_FOUND)) {
                        addresses.add(InetAddress.getByName(ip));
                        //System.out.println("Host trouvé : " + ip);
                    }
                }
            }catch (SocketTimeoutException e){
                socket.close();
                this.clientFront.displayServers(addresses);
                for (InetAddress addr:addresses) {
                    System.out.println(addr.getHostAddress());
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }


        //this.clientFront.displayServers();
    }
}