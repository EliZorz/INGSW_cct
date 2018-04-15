package application;

import application.Interfaces.ServicesManager;
import application.Interfaces.UserRemote;
import application.rmi.client.RmiManager;
import application.socket.client.SocketManager;

public class Singleton {

    private static Singleton instance = null;

    //private constructor, prevents other class from instantiating
    private Singleton(){}

    //static instance method
    public static Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    public UserRemote methodRmi(){
        ServicesManager chrmi;
        chrmi = new RmiManager();

        UserRemote u = null;
        try {
            u = chrmi.getUserService();
            System.out.println("lookup done");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }

    public UserRemote methodSocket(){
        ServicesManager chsock;
        chsock = new SocketManager();

        UserRemote u = null;
        try {
            u = chsock.getUserService();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }
}
