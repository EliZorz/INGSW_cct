package application.socket;

import application.MenuIniziale;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
   public static final int port = 1050;

   public static void main (String[] args)throws IOException{
       ServerSocket serverSocket = new ServerSocket(port);
       System.out.println("start");
       Socket clientSocket = null;
       try{
           clientSocket = serverSocket.accept();
           System.out.println("accepted");

       }catch (IOException e){
           System.out.println("Error");
       }
   }



}
