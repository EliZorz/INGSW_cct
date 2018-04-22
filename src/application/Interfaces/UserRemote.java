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

    ArrayList<String> loadIngr(LocalDate day) throws RemoteException;

    boolean funzLog(String usr, String pwd) throws RemoteException; //questa funzione controllerà se username e password sono corretti

    ArrayList<ChildDbDetails> loadData() throws RemoteException;

    boolean addData(String name, String surname, String cf, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException;


    boolean updateChild(String name, String surname, String oldcf, String cf, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException;

    ArrayList<IngredientsDbDetails> loadIngr() throws RemoteException;

    boolean updateMenu(String num, String entree, String main, String dessert, String side, String drink, LocalDate day, ArrayList<String> selectedIngredients, LocalDate oldDate) throws RemoteException;

    boolean deleteStaff(String cf) throws RemoteException;

    ArrayList<DishesDbDetails> loadMenu() throws RemoteException;

    ArrayList<ContactsDbDetails> loadDataContacts() throws RemoteException;

    boolean addContact(String surname, String name, String cf, String mail, String tel, LocalDate birthday, String bornWhere, String address, String cap, String province, boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException;

    boolean deleteChild(String cf) throws RemoteException;

    ArrayList<StaffDbDetails> loadDataStaff() throws RemoteException;

    boolean addDataStaff(String name, String surname, String cf, String mail, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException;

    boolean addMenu(String num, String entree, String mainCourse, String dessert, String sideDish, String drink, LocalDate date,ArrayList<String> selectedIngredients)throws RemoteException;

    boolean controllDate(LocalDate d) throws RemoteException;

    boolean deleteMenu(LocalDate d) throws RemoteException;


}
