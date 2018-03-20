package application;

public class Singleton {
    private static Singleton istanza;
    private static boolean rmiSocket;
    private Singleton(boolean t){
        rmiSocket = t;
    }

    public static Singleton rmiSocketChoice(boolean rmiSocket){
        if(rmiSocket == true){  //nel caso di RMI instanzia un RemoteObject RMI
            istanza = new Singleton(true);
        }
        else{
            istanza = new Singleton(false);

        }
        return istanza;
    }
}
