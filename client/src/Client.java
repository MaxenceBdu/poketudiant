import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.io.IOException;

public class Client{

    public void askForServers() throws UnknownHostException, SocketException, IOException{
        int udpPort = 9000;
        boolean found = false;
        String search = "looking for poketudiant servers";
        String wantedResponse = "i'm a poketudiant server";

        byte[] buf1 = search.getBytes();
        byte[] buf2 = new byte[50];
        
        InetAddress address = InetAddress.getByName("255.255.255.255");

        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        DatagramPacket packet1 = new DatagramPacket(buf1, buf1.length, address, udpPort); 

        DatagramPacket packet2 = new DatagramPacket(buf2, buf2.length);

        socket.send(packet1);
        while(!found){
            socket.receive(packet2);
            String realResponse = new String(packet2.getData(), 0, packet2.getLength());    
            String ip = packet2.getAddress().getHostAddress();
            
            System.out.println("Received : " + realResponse + " from " + ip);
            if(realResponse.equals(wantedResponse)){
                found = true;
                System.out.println("Host trouvé");
            }
        }                
        socket.close();
    }
}