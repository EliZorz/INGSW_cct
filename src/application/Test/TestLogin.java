package application.Test;


import application.rmi.server.ServerImpl;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TestLogin {

     private ServerImpl si;

     private TestLogin() throws RemoteException {
         si = new ServerImpl();
     }

     @Test
     void testNoCredentials() throws RemoteException{
         assertFalse(si.funzLog(null, null ));
     }

     @Test
     void testNoPassword() throws RemoteException{
         assertFalse(si.funzLog("ludo", null));
     }

     @Test
     void testNoParameters() throws RemoteException{
         assertFalse(si.funzLog("",""));
     }

     @Test
    void testCredentialsLogin() throws RemoteException{
         assertTrue(si.funzLog("ludo", "ludo123"));
     }








}
