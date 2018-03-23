package application.socket;


import java.net.Socket;

public class ClientSocketThread extends Thread {
    public Socket client = null;


    public ClientSocketThread(Socket client)
    {
        this.client = client;
        this.start();
    }

    @Override
    public void run() {
        try
        {


            System.out.println("Sto servendo il client");

            // eventuali elaborazioni e scambi di informazioni con il client

            // ...

            // chiusura dei buffer e del socket

            client.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

