package application.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//quello corretto
public class SocketThread extends Thread {
    String line=null;
    private BufferedReader in = null;  //uso questo e non Scanner perché è synchronized
    PrintWriter out =null;
    Socket s=null;

    public SocketThread(Socket s){
        this.s=s;
    }

    public void run() {
        try{
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out =new PrintWriter(s.getOutputStream());

        }catch(IOException e){
            System.out.println("IO error in server thread");
        }

        try {
            line= in.readLine();
            while(line.compareTo("QUIT")!=0){

                out.println(line); //stampa quello che ho scritto
                out.flush();
                System.out.println("Response to Client  :  "+line);
                line= in.readLine();
            }
        } catch (IOException e) {

            line=this.getName();  //salva in line il nome del thread che sta eseguendo
            System.out.println("IO Error/ Client "+line+" terminated abruptly");
        }
        catch(NullPointerException e){
            line=this.getName();
            System.out.println("Client "+line+" Closed");
        }

        finally{
            try{
                System.out.println("Connection Closing..");
                if (in !=null){
                    in.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if(out !=null){
                    out.close();
                    System.out.println("Socket Out Closed");
                }
                if (s!=null){
                    s.close();  //fa terminare la accept di quel solo client
                    //System.out.println(s.isClosed()); //serve per controllare che la socket sia chiusa
                    System.out.println("Socket Closed");
                }

            }
            catch(IOException ie){
                System.out.println("Socket Close Error");
            }
        }
    }
}
