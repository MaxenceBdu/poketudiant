public abstract class Client{
    public static void start(){
        ClientBack cb = new ClientBack(new ClientFront());
    }
}