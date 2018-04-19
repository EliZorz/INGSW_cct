package application.Interfaces;

import application.details.*;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Array;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface UserRemote extends Remote {

    boolean funzLog(String usr, String pwd) throws RemoteException; //questa funzione controllerà se username e password sono corretti

    ArrayList<ChildDbDetails> loadData() throws RemoteException;

    boolean addData(String name, String surname, String cf, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException;

    boolean deleteChild(String cf) throws RemoteException;

    boolean updateChild(String name, String surname, String oldcf, String cf, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException;

    ArrayList<IngredientsDbDetails> loadIngr() throws RemoteException;

    ArrayList<ContactsDbDetails> loadDataContacts() throws RemoteException;

    boolean addContact(String surname, String name, String cf, String mail, String tel, LocalDate birthday, String bornWhere, String address, String cap, String province, boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException;

    ArrayList<StaffDbDetails> loadDataStaff() throws RemoteException;

    boolean addDataStaff(String name, String surname, String cf, String mail, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException;

    boolean deleteStaff(String cf) throws RemoteException;

    ArrayList<DishesDbDetails> loadMenu() throws RemoteException;

    boolean addMenu(String num, String entree, String mainCourse, String dessert, String sideDish, String drink, LocalDate date)throws RemoteException;
}
