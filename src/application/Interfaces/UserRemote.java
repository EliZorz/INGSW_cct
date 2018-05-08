package application.Interfaces;

import application.details.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public interface UserRemote extends Remote {
    boolean funzLog(String usr, String pwd) throws RemoteException; //questa funzione controllerà se username e password sono corretti


    ArrayList<ChildDbDetails> loadData() throws RemoteException;
    boolean addData(String name, String surname, String cf, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy,
                    String nameContact, String surnameContact, String cfContact, String mailContact, String telContact, LocalDate birthdayContact, String bornWhereContact, String addressContact, String capContact, String provinceContact,
                    boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException;
    boolean deleteChild(String cf) throws RemoteException;
    boolean updateChild(String name, String surname, String oldcf, String cf, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException;

    ArrayList<IngredientsDbDetails> loadIngr() throws RemoteException;


    ArrayList<ContactsDbDetails> loadDataContacts(String cfChild) throws RemoteException;
    boolean addContact(ArrayList<String> selectedChild, String surname, String name, String cf, String mail, String tel, LocalDate birthday, String bornWhere, String address, String cap, String province, boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException;
    boolean deleteContact (String oldcfContact) throws RemoteException;
    boolean updateContact(String name, String surname, String oldcf, String cf, String mail, String tel, LocalDate bornOn, String bornWhere, String address, String cap, String province, int isDoc, int isGuardian, int isContact) throws RemoteException;


    ArrayList<StaffDbDetails> loadDataStaff() throws RemoteException;
    boolean addDataStaff(String name, String surname, String cf, String mail, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException;
    boolean deleteStaff(String cf) throws RemoteException;
    boolean updateStaff(String name, String surname, String oldcf, String cf, String mail, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException;


    ArrayList<SupplierDbDetails> loadDataSuppliers() throws RemoteException;
    boolean addDataSupplier(String name, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException;
    boolean updateSupplier(String name, String oldPiva, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException;
    boolean deleteSupplier(String piva) throws RemoteException;


    ArrayList<DishesDbDetails> loadMenu() throws RemoteException;
    boolean addMenu(String num, String entree, String mainCourse, String dessert, String sideDish, String drink, LocalDate date)throws RemoteException;


    ArrayList<TripTableDbDetails> loadDataTrip() throws RemoteException;
    boolean deleteTrip(String dep, LocalDateTime dateDep, LocalDateTime dateCom, String alloggio, LocalDateTime dateArr, String arr) throws RemoteException;
    ArrayList<ChildTripDbDetails> loadChildTrip() throws RemoteException;
    ArrayList<StaffTripDbDetails> loadStaffTrip() throws RemoteException;
    int[] addTrip (ArrayList<String> selectedChild, ArrayList<String> selectedStaff,
                     String timeDep, String timeArr, String timeCom,
                     String departureFrom, String ArrivalTo, String staying) throws RemoteException;
    ArrayList<ChildSelectedTripDbDetails> loadTripSelectedChildren (String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException;
    ArrayList<StaffSelectedTripDbDetails> loadTripSelectedStaff (String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException;
    ArrayList<CodRifChildDbDetails> findNotAvailableStaff (ArrayList<String> selectedStaffCf, String selectedTripDep, String selectedTripCom) throws RemoteException;
    ArrayList<CodRifChildDbDetails> findNotAvailableChild (ArrayList<String> selectedChildCf, String selectedTripDep, String selectedTripCom) throws RemoteException;
    int[] howManyActualParticipants(ArrayList<String> selectedChildCf, ArrayList<String> selectedStaffCf) throws RemoteException;
    HashMap<String, ArrayList<String>> associateBusToParticipants(ArrayList<String> selectedChildCfArrayList, int totChildren, ArrayList<String> selectedStaffCfArrayList, int totStaff,
                                                                  String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException;

}
