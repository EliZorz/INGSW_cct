package application.rmi.client;

import application.Interfaces.UserRemote;
import application.contr.GuiNew;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 * Created by ELISA on 15/03/2018.
 */
public class Client {

    public void funzLog(boolean res) { //sono nel ramo RMI

            try {
                Registry registry = LocateRegistry.getRegistry();
                UserRemote pippo = (UserRemote) registry.lookup("UserRemote");
                //String reply = pippo.sendMessage("Client hello");

                System.out.print("RMI registry bindings: ");
                String[] e = registry.list();
                for (int i = 0; i < e.length; i++) {
                    System.out.println(e[i]);

                    if (!res) {

                        System.out.println("No user like that in your database");
                    } else {

                        System.out.println("I found your user!");

                        try {
                            new GuiNew().openFxml("/application/gui/MenuIniziale.fxml");

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }

                }
            } catch (RemoteException re) {
                re.printStackTrace();
            } catch (NotBoundException nbe) {
                nbe.printStackTrace();
            }

    }
}