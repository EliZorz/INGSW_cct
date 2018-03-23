package application.socket;

import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
    private ServerSocket server;
    public static void main(String args[])throws Exception {
        new ServerSocketThread();
    }
    public SocketServer ()throws Exception{
        server = new ServerSocket(1055);
        this.start();
    }

    public void run(){
        while (true){
            try{
                System.out.println("in attesa di connessione");
                Socket client = server.accept();
                System.out.println("connessione accettata");
                ClientSocketThread c = new ClientSocketThread(client);
            }catch(Exception e){
                System.out.println("errore");
            }
        }
    }



}
