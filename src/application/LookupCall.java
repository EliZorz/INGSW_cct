package application;

import application.Interfaces.ServicesManager;
import application.Interfaces.UserRemote;
import application.rmi.client.RmiManager;
import application.socket.client.SocketManager;

public class LookupCall {
    private static LookupCall instance = null;
    private ServicesManager servicesMan;
    private UserRemote u = null;


    //private constructor, prevents other class from instantiating
    private LookupCall(){}


    public static LookupCall getInstance(){
        if(instance == null){
            return new LookupCall();
        }
        else {
            return instance;
        }
    }

    public UserRemote methodRmi(){
        servicesMan = new RmiManager();
        try {
            u = servicesMan.getUserService();
            System.out.println("lookup done");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }

    public UserRemote methodSocket(){
        servicesMan= new SocketManager();
        try {
            u = servicesMan.getUserService();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }

}