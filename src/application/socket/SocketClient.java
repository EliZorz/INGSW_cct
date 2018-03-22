package application.socket;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class SocketClient  {

    public static void main(String[] args) throws IOException{
        InetAddress addr;
        if (args.length == 0) addr = InetAddress.getByName(null);
        else
            addr = InetAddress.getByName(args[0]);
        Socket socket = null;
        try{
            socket = new Socket(addr, SocketServer.port);
            System.out.println("Client start");
        }catch (IOException e){
            System.out.println("Errore di connessione del client");
            e.printStackTrace();
        }
    }

}
