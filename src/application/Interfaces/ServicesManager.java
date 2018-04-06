package application.Interfaces;

import java.rmi.Remote;

/**
 * Created by ELISA on 27/03/2018.
 */
public interface ServicesManager extends Remote{
    UserRemote getUserService() throws Exception;
}
