package application.socket;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient  {

    public static void main(String[] args) throws UnknownHostException {
        Socket socket = null;
        InetAddress addr;
        if (args.length == 0) addr = InetAddress.getByName(null);
        else
            addr = InetAddress.getByName(args[0]);
        try {
            socket = new Socket(addr, 1055);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
