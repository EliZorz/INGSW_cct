package application;

public class Singleton {
    private static Singleton istanza;
    private static boolean rmiSocket;
    private Singleton(boolean t){
        rmiSocket = t;
    }

    public static Singleton rmiSocketChoice(boolean rmiSocket){
        if(rmiSocket == true){  //nel caso di rmi instanzia un RemoteObject rmi
            istanza = new Singleton(true);
        }
        else{
            istanza = new Singleton(false);

        }
        return istanza;
    }
}
