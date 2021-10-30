import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.io.IOException;



public class Main{

    public static void main(String[] args) throws UnknownHostException, SocketException, IOException{
        boolean found = false;
        byte[] buf1, buf2 = new byte[50];
        String search = "looking for poketudiant servers";
        String wantedResponse = "i'm a poketudiant server";
        InetAddress address = InetAddress.getByName("255.255.255.255");

        buf1 = search.getBytes();

        DatagramSocket socket = new DatagramSocket(9000);
        socket.setBroadcast(true);

        DatagramPacket packet1 = new DatagramPacket(buf1, buf1.length, address, 9000); 

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