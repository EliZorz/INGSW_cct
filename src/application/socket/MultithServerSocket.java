package application.socket;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
 //NON SERVE PIù
/**
 * Created by ELISA on 20/03/2018.
 */
public class MultithServerSocket implements Runnable {

    Socket sock;
    public MultithServerSocket(Socket sock){
        this.sock = sock;
    }

    public void run(){
        try {
            PrintStream pstream = new PrintStream(sock.getOutputStream());

            for (int i = 100; i >= 0; i--){
                pstream.println(i + "connected.");
            }

            pstream.close();
            sock.close();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
