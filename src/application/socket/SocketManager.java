package application.socket;

import application.Interfaces.ServicesManager;
import application.Interfaces.UserRemote;

import java.io.IOException;
import java.net.Socket;

public class SocketManager implements ServicesManager{
    Socket s = null;

    public SocketManager(){
        try{
            s = new Socket("local", 1099);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public UserRemote getUserService() throws Exception {
        return null;
    }

    //dovrebbe essere chiamata come la user manager da services manager come nel main controller simile a rmi chiamando la socket manager

    }


