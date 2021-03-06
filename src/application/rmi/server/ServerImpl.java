package application.rmi.server;

import application.Interfaces.UserRemote;
import application.contr.Database;
import application.details.*;
import application.details.ChildDbDetails;
import com.mysql.jdbc.Connection;
import javafx.beans.property.StringProperty;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Created by ELISA on 21/03/2018.
 */
public class ServerImpl extends UnicastRemoteObject implements UserRemote {  //socket e rmi usano entrambe questa implementazione


    public ServerImpl() throws RemoteException {
        super();
    }


    //LOGIN----------------------------------------------------------------------------------
    @Override
    public boolean funzLog(String usr, String pwd) {

        PreparedStatement st = null;
        ResultSet result = null;
        String queryLog = "SELECT * FROM project.UserIn WHERE Username = ? AND Password = ? ";
        boolean res = false;

        try {
            st = this.connHere().prepareStatement(queryLog);
            st.setString(1, usr);
            st.setString(2, pwd);
            result = st.executeQuery();

        } catch (SQLException e) {
            System.out.println("Error during search in DB");
            e.printStackTrace();
        }

        try {
            if (!result.next()) {
                System.out.println("No user like that in your database");
                res = false;
            } else {
                result.beforeFirst();
                while (result.next()) {
                    String usrFound = result.getString("Username");
                    System.out.println("USER: " + usrFound);
                    String pwdFound = result.getString("Password");
                    System.out.println("PASSWORD: " + pwdFound);
                }
                res = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return res;

    }


    @Override
    public boolean logout(){
        return true;
    }


    //CHILDREN---------------------------------------------------------------------------------------
    @Override
    public ArrayList<ChildDbDetails> loadData() throws RemoteException {

        PreparedStatement st = null;
        ResultSet result = null;
        ArrayList<ChildDbDetails> childDbArrayList = new ArrayList<>(9);

        String queryLoad = "SELECT Cognome, Nome, CF, DataNascita, CittaNascita, Residenza, Indirizzo, CAP, Provincia" +
                " FROM project.interni INNER JOIN project.bambino" +
                " ON interni.CF = bambino.Interni_CF";

        try {
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            if (!result.next()) {
                System.out.println("No child in DB");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        ChildDbDetails prova = new ChildDbDetails(result.getString(1),
                                result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),
                                result.getString(6),
                                result.getString(7),
                                result.getString(8),
                                result.getString(9));
                        //get string from db, put into list of ChildGuiData, ready to put it into GUI
                        childDbArrayList.add(prova);

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //ritorna lista di bambini
        return childDbArrayList;


    }


    @Override
    public boolean addData(String name, String surname, String cf, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy,
                           String nameContact, String surnameContact, String cfContact, String mailContact, String telContact, LocalDate birthdayContact, String bornWhereContact, String addressContact, String capContact, String provinceContact,
                           boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
        PreparedStatement st = null;

        String queryAdd = "INSERT INTO interni(Cognome, Nome, CF, DataNascita, CittaNascita, Residenza, Indirizzo, CAP, Provincia, Allergie)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?)";
        String queryLastCodRif = "SELECT COALESCE(MAX(CodRif), 0) FROM bambino";  //select last CodRif inserted
        String queryAddCf = "INSERT INTO bambino(CodRif, Interni_CF) VALUES (?,?)";
        String queryAddFirstContact = "INSERT INTO adulto(Cognome, Nome, CF, Mail, Tel, DataNascita, CittaNascita, Indirizzo, CAP, Provincia, Pediatra, Tutore, Contatto, Bambino_CodRif)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


        String newCod;
        ArrayList<CodRifChildDbDetails> codRifArrayList = new ArrayList<>(1);

        ResultSet result = null;

        //divide items from arraylist selectedAllergy into string to put into database
        StringBuilder allAllergies = new StringBuilder();
        if (!selectedAllergy.isEmpty()) {
            for (String s : selectedAllergy) {
                allAllergies.append(s).append(", ");
            }
            System.out.println(allAllergies.toString());
        } else {
            allAllergies.append("none");
        }


        try {
            //add data new child into db
            st = this.connHere().prepareStatement(queryAdd);
            st.setString(1, surname);
            st.setString(2, name);
            st.setString(3, cf);
            st.setDate(4, java.sql.Date.valueOf(birthday));
            st.setString(5, bornWhere);
            st.setString(6, residence);
            st.setString(7, address);
            st.setString(8, cap);
            st.setString(9, province);
            st.setString(10, allAllergies.toString());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            /*  assign code number to new child :
            **    1) query to select bambino.CodRif from interni inner join bambino on CF
            **    2) save CodRif in list
            **    3) pick latest element list and ++
            **    4) save new number in list, associate it with new child's CF
            */
            st = this.connHere().prepareStatement(queryLastCodRif);
            result = st.executeQuery(queryLastCodRif);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!result.next()) {
                System.out.println("Error");
                return false;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet to create new Bambino");
                try {
                    while (result.next()) {
                        CodRifChildDbDetails lastCod = new CodRifChildDbDetails(result.getString(1));
                        codRifArrayList.add(lastCod);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (codRifArrayList.get(0).getCodRif().equals("0")) {
                    System.out.println("First Child in DB");
                    //then new child's CodRif is 1
                    newCod = "b1";

                    //add to DB
                    st = this.connHere().prepareStatement(queryAddCf);
                    st.setString(1, newCod);
                    st.setString(2, cf);
                    st.executeUpdate();

                    //add contact associated
                    st = this.connHere().prepareStatement(queryAddFirstContact);
                    st.setString(1, surnameContact);
                    st.setString(2, nameContact);
                    st.setString(3, cfContact);
                    st.setString(4, mailContact);
                    st.setString(5, telContact);
                    st.setDate(6, java.sql.Date.valueOf(birthdayContact));
                    st.setString(7, bornWhereContact);
                    st.setString(8, addressContact);
                    st.setString(9, capContact);
                    st.setString(10, provinceContact);
                    st.setBoolean(11, isDoc);
                    st.setBoolean(12, isGuardian);
                    st.setBoolean(13, isContact);
                    st.setString(14, newCod);
                    st.executeUpdate();


                } else {
                    String currentLast = codRifArrayList.get(0).getCodRif();
                    newCod = "b" + (Integer.parseInt(currentLast.substring(1, currentLast.length())) + 1);
                    System.out.println("new CodRif");
                    st = this.connHere().prepareStatement(queryAddCf);
                    st.setString(1, newCod);
                    st.setString(2, cf);
                    st.executeUpdate();

                    st = this.connHere().prepareStatement(queryAddFirstContact);
                    st.setString(1, surnameContact);
                    st.setString(2, nameContact);
                    st.setString(3, cfContact);
                    st.setString(4, mailContact);
                    st.setString(5, telContact);
                    st.setDate(6, java.sql.Date.valueOf(birthdayContact));
                    st.setString(7, bornWhereContact);
                    st.setString(8, addressContact);
                    st.setString(9, capContact);
                    st.setString(10, provinceContact);
                    st.setBoolean(11, isDoc);
                    st.setBoolean(12, isGuardian);
                    st.setBoolean(13, isContact);
                    st.setString(14, newCod);
                    st.executeUpdate();

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }


    @Override
    public boolean deleteChild(String oldcf) throws RemoteException {
        PreparedStatement st = null;

        String queryDelete = "DELETE FROM interni WHERE CF = '" + oldcf + "';";

        //NOTA: CANCELLANDO CODRIF, NON VANNO RIFORMATTATI I CODRIF SUCCESSIVI (come al Poli le matricole non sono modificate una volta che altri si laureano)

        if(oldcf == null)
            return false;
        try {
            st = this.connHere().prepareStatement(queryDelete);
            st.executeUpdate(queryDelete);
            System.out.println("Deleted from interni.");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public boolean updateChild(String name, String surname, String oldcf, String cf, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException {

        PreparedStatement st = null;

        //first load allergies in database
        //then divide items from arraylist selectedAllergy into string
        // finally put allAllergies into database
        ResultSet resultPrev = null;
        ArrayList<String> prevAllergies = new ArrayList<>();
        String queryLoadPreviousAllergies = "SELECT Allergie FROM project.interni WHERE CF ='"+oldcf+"';";
        if(name == null || surname == null || oldcf == null || cf == null || bornOn == null || bornWhere == null || residence == null || address == null || cap == null || province == null )
            return false;
        try {
            st = this.connHere().prepareStatement(queryLoadPreviousAllergies);
            resultPrev = st.executeQuery(queryLoadPreviousAllergies);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if(!resultPrev.next())
                System.out.println("Error, there's not even 'none' written in database.");
            else {
                resultPrev.beforeFirst();
                System.out.println("Processing ResultSet");
                while(resultPrev.next()){
                    String prevAllergyString = resultPrev.getString(1);
                    List<String> prevAllergy = new ArrayList<>(Arrays.asList(prevAllergyString.split(", ")));
                    if(!prevAllergy.equals("none")){
                        System.out.println(prevAllergy);
                        System.out.println(prevAllergy + " already in db for this child");
                    }
                    //se prevAllergy == "none" allora lascio ArrayList vuoto
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        StringBuilder allAllergies = new StringBuilder();

        //create string to put into db
        if (!selectedAllergy.isEmpty()) {

            for(String s : selectedAllergy){
                if(!prevAllergies.contains(s)){
                    prevAllergies.add(s);
                    System.out.println(s + " not in database yet.");
                }
                else
                    System.out.println(s + " already into database...");
            }

            //create string from arrayList
            for (String s : prevAllergies) {
                allAllergies.append(s).append(", ");
            }
            System.out.println(allAllergies.toString());
        } else {
            allAllergies.append("none");
        }


        String queryEdit = "UPDATE interni SET Cognome ='" + surname + "', Nome ='" + name + "', CF ='" + cf + "', " +
                "DataNascita ='" + Date.valueOf(bornOn) + "', CittaNascita='" + bornWhere + "', Residenza='" + residence + "', " +
                "Indirizzo='" + address + "', CAP='" + cap + "', Provincia='" + province + "', Allergie='" + allAllergies.toString() + "'" +
                "WHERE CF = '" + oldcf + "';";

        try {
            //COD RIF NON CAMBIA, IL CF IN bambino, CHE è FOREIGN KEY, è IMPOSTATO ON UPDATE CASCADE!
            //edit data new child into db
            st = this.connHere().prepareStatement(queryEdit);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    //ALLERGIES----------------------------------------------------------------------------
    @Override
    public ArrayList<IngredientsDbDetails> loadIngr() throws RemoteException {
        PreparedStatement st = null;

        ResultSet result = null;

        ArrayList<IngredientsDbDetails> ingrArrayList = new ArrayList<>(1);

        String queryLoad = "SELECT ingredient " +
                "FROM ingredients INNER JOIN fornitore " +
                "ON ingredients.Fornitore_PIVA = fornitore.PIVA";

        try {
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);
            System.out.println("Allessandro");

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            if (!result.next()) {
                System.out.println("No ingredient in DB");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        IngredientsDbDetails prova = new IngredientsDbDetails(result.getString(1));
                        //get string from db, put into list of ChildGuiData, ready to put it into GUI
                        ingrArrayList.add(prova);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return ingrArrayList;

    }

    //CONTACTS------------------------------------------------------------------------------------------
    @Override
    public ArrayList<ContactsDbDetails> loadDataContacts(String cfChild) throws RemoteException {

        //LOAD SURNAME, IS_DOC, IS_GUARDIAN, IS_CONTACT
        PreparedStatement st = null;

        ResultSet result = null;

        ArrayList<ContactsDbDetails> contactsDbArrayList = new ArrayList<>(13);

        String queryLoadContacts = "SELECT Cognome, Nome, CF, Mail, Tel, DataNascita, CittaNascita, Indirizzo, CAP, Provincia, Pediatra, Tutore, Contatto" +
                " FROM adulto INNER JOIN bambino" +
                " WHERE adulto.Bambino_CodRif = bambino.CodRif AND bambino.Interni_CF = '" + cfChild + "';";

        try {
            st = this.connHere().prepareStatement(queryLoadContacts);
            result = st.executeQuery(queryLoadContacts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (!result.next()) {
                System.out.println("No contact in DB");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        ContactsDbDetails prova = new ContactsDbDetails(result.getString(1),
                                result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),
                                result.getString(6),
                                result.getString(7),
                                result.getString(8),
                                result.getString(9),
                                result.getString(10),
                                result.getString(11),
                                result.getString(12),
                                result.getString(13));
                        contactsDbArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return contactsDbArrayList;
    }


    @Override
    public boolean addContact(String selectedChild, String surname, String name, String cf, String mail, String tel, LocalDate birthday, String bornWhere, String address, String cap, String province, boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
        PreparedStatement st = null;

        String querySearchCodRif = "SELECT CodRif" +
                " FROM bambino" +
                " WHERE bambino.Interni_CF = '" + selectedChild + "';";
        ResultSet result;
        String prova = null;

        String queryAddContact = "INSERT INTO adulto(Cognome, Nome, CF, Mail, Tel, DataNascita, CittaNascita, Indirizzo, CAP, Provincia, Pediatra, Tutore, Contatto, Bambino_CodRif)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if(selectedChild == null)
            return false;
        if(!isContact && !isDoc && !isGuardian)
            return false;
        try {
            //search CodRif of the selected child, to add it to db in Adulto
            st = this.connHere().prepareStatement(querySearchCodRif);
            result = st.executeQuery(querySearchCodRif);

            if (!result.next()) {
                System.out.println("No contact in DB");
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        prova = result.getString(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            //add data new child into db
            st = this.connHere().prepareStatement(queryAddContact);
            st.setString(1, surname);
            st.setString(2, name);
            st.setString(3, cf);
            st.setString(4, mail);
            st.setString(5, tel);
            st.setDate(6, Date.valueOf(birthday));
            st.setString(7, bornWhere);
            st.setString(8, address);
            st.setString(9, cap);
            st.setString(10, province);
            st.setBoolean(11, isDoc);
            st.setBoolean(12, isGuardian);
            st.setBoolean(13, isContact);
            st.setString(14, prova);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;

    }


    @Override
    public boolean deleteContact(String oldcfContact, String cfChild) throws RemoteException {
        PreparedStatement st = null;
        ResultSet result = null;
        String codRifChildString = null;
        String queryFindCodRif = "SELECT Bambino_CodRif FROM adulto INNER JOIN bambino WHERE CF = '"+oldcfContact+"' AND Interni_CF  = '"+ cfChild+"';";
        if(oldcfContact == null || cfChild == null)
            return false;
        try{
            st = this.connHere().prepareStatement(queryFindCodRif);
            result = st.executeQuery(queryFindCodRif);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            if(!result.next()){
                System.out.println("No such child in db");
                return false;
            } else {
                result.beforeFirst();
                while(result.next()) {
                    System.out.println("Processing ResultSet");
                    try {
                        codRifChildString = result.getString(1);
                        System.out.println(codRifChildString);

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    String queryDelete = "DELETE FROM project.adulto WHERE CF = '" + oldcfContact + "' AND Bambino_CodRif = '" + codRifChildString +"';";
                    if(oldcfContact == null || cfChild == null)
                        return false;
                    try {
                        st = this.connHere().prepareStatement(queryDelete);
                        st.executeUpdate(queryDelete);
                        System.out.println("Deleted from adulto.");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    @Override
    public boolean updateContact(String name, String surname, String oldcfContact, String cf, String cfChild, String mail, String tel, LocalDate bornOn, String bornWhere, String address, String cap, String province, boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
        PreparedStatement st = null;
        ResultSet result = null;
        String codRifChildString = null;
        String queryFindCodRif = "SELECT Bambino_CodRif FROM adulto INNER JOIN bambino WHERE CF = '"+oldcfContact+"' AND Interni_CF  = '"+ cfChild+"';";
        if(oldcfContact == null || cf == null || cfChild == null)
            return false;
        try{
            st = this.connHere().prepareStatement(queryFindCodRif);
            result = st.executeQuery(queryFindCodRif);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            if(!result.next()){
                System.out.println("No such child in db");
                return false;
            } else {
                result.beforeFirst();
                while(result.next()) {
                    System.out.println("Processing ResultSet");
                    try {
                        codRifChildString = result.getString(1);
                        System.out.println(codRifChildString);

                    } catch (Exception e){
                        e.printStackTrace();
                    }


                    int isDocBool = 0;
                    int isGuardianBool = 0;
                    int isContactBool = 0;
                    if(isDoc) isDocBool = 1;
                    if(isContact) isContactBool = 1;
                    if(isGuardian) isGuardianBool = 1;

                    String queryEdit = "UPDATE project.adulto SET Cognome='"+surname+"', Nome='"+name+"', CF='"+cf+"', Mail='"+mail+"', Tel='"+tel+"'," +
                            " DataNascita='"+bornOn+"', CittaNascita='"+bornWhere+"', Indirizzo='"+address+"', CAP='"+cap+"', Provincia='"+province+"'," +
                            " Pediatra='"+isDocBool+"', Tutore='"+isGuardianBool+"', Contatto='"+isContactBool+"'" +
                            " WHERE CF = '" + oldcfContact + "' AND Bambino_CodRif = '"+codRifChildString+"';";

                    System.out.println(queryEdit);

                    st = this.connHere().prepareStatement(queryEdit);
                    st.executeUpdate();

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    @Override
    public boolean controllCF(String CF) throws RemoteException{
        ResultSet result = null;
        PreparedStatement st = null;
        String queryControll = "SELECT * FROM interni WHERE CF = '"+ CF+"'";
        Boolean controll = false;

        try{
            st = this.connHere().prepareStatement(queryControll);
            result = st.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            if(!result.next()){
                System.out.println("No CF like this in DB");
                result.close();
                controll = true;
            }
            else
                controll = false;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return controll;
    }




    //STAFF---------------------------------------------------------------------------------------
    @Override
    public ArrayList<StaffDbDetails> loadDataStaff() throws RemoteException {

        PreparedStatement st = null;

        ResultSet result = null;

        ArrayList<StaffDbDetails> staffDbArrayList = new ArrayList<>(10);

        String queryLoad = "SELECT Cognome, Nome, CF, Mail, DataNascita, CittaNascita, Residenza, Indirizzo, CAP, Provincia" +
                " FROM interni INNER JOIN personaleint" +
                " ON interni.CF = personaleint.Interni_CF";

        try {
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            if (!result.next()) {
                System.out.println("No staff in DB");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        StaffDbDetails prova = new StaffDbDetails(result.getString(1),
                                result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),
                                result.getString(6),
                                result.getString(7),
                                result.getString(8),
                                result.getString(9),
                                result.getString(10));
                        //get string from db, put into list of ChildGuiData, ready to put it into GUI
                        staffDbArrayList.add(prova);

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return staffDbArrayList;


    }

    @Override
    public boolean addDataStaff(String name, String surname, String cf, String mail, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException {
        PreparedStatement st = null;

        String queryAdd = "INSERT INTO interni(Cognome, Nome, CF, DataNascita, CittaNascita, Residenza, Indirizzo, CAP, Provincia, Allergie)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?)";
        String queryLastCodRif = "SELECT COALESCE(MAX(CodID), 0) FROM personaleint";  //select last CodRif inserted
        String queryAddCf = "INSERT INTO personaleint(Mail, CodID, Interni_CF) VALUES (?,?,?)";

        ArrayList<CodRifChildDbDetails> codRifArrayList = new ArrayList<>(1);

        ResultSet result = null;

        String newCod;

        //divide items from arraylist selectedAllergy into string to put into database
        StringBuilder allAllergies = new StringBuilder();
        if (!selectedAllergy.isEmpty()) {
            for (String s : selectedAllergy) {
                allAllergies.append(s).append(", ");
            }
            System.out.println(allAllergies.toString());
        } else {
            allAllergies.append("none");
        }


        try {
            st = this.connHere().prepareStatement(queryAdd);
            st.setString(1, surname);
            st.setString(2, name);
            st.setString(3, cf);
            st.setDate(4, java.sql.Date.valueOf(birthday));
            st.setString(5, bornWhere);
            st.setString(6, residence);
            st.setString(7, address);
            st.setString(8, cap);
            st.setString(9, province);
            st.setString(10, allAllergies.toString());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            //  assign code number to new staff member
            st = this.connHere().prepareStatement(queryLastCodRif);
            result = st.executeQuery(queryLastCodRif);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!result.next()) {
                System.out.println("Error");
                return false;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet to create new Staff member");
                try {
                    while (result.next()) {
                        CodRifChildDbDetails lastCod = new CodRifChildDbDetails(result.getString(1));
                        codRifArrayList.add(lastCod);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (codRifArrayList.get(0).getCodRif().equals("0")) {
                    System.out.println("First Staff member in DB");
                    //then new staff's CodRif is 1
                    newCod = "s1";

                    //add to DB
                    st = this.connHere().prepareStatement(queryAddCf);
                    st.setString(1, mail);
                    st.setString(2, newCod);
                    st.setString(3, cf);
                    st.executeUpdate();


                } else {
                    String currentLast = codRifArrayList.get(0).getCodRif();
                    newCod = "s" + (Integer.parseInt(currentLast.substring(1, currentLast.length())) + 1);
                    System.out.println("new CodRif");
                    st = this.connHere().prepareStatement(queryAddCf);
                    st.setString(1, mail);
                    st.setString(2, newCod);
                    st.setString(3, cf);
                    st.executeUpdate();

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }


    @Override
    public boolean deleteStaff(String cf) throws RemoteException {
        PreparedStatement st = null;

        String queryDelete = "DELETE FROM interni WHERE CF = '" + cf + "';";
        String queryDeleteCodID = "DELETE FROM personaleint WHERE Interni_CF = '" + cf + "';";

        //NOTA: CANCELLANDO CODRIF, NON VANNO RIFORMATTATI I CODRIF SUCCESSIVI (come al Poli le matricole non sono modificate una volta che altri si laureano)
        if(cf == null)
            return false;
        try {
            st = this.connHere().prepareStatement(queryDeleteCodID);
            st.executeUpdate(queryDeleteCodID);
            System.out.println("Deleted CodRif.");

            st = this.connHere().prepareStatement(queryDelete);
            st.executeUpdate(queryDelete);
            System.out.println("Deleted from interni.");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public boolean updateStaff(String name, String surname, String oldcf, String cf, String mail, LocalDate bornOn, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException {
        PreparedStatement st = null;

        ResultSet resultPrev = null;
        ArrayList<String> prevAllergies = new ArrayList<>();
        String queryLoadPreviousAllergies = "SELECT Allergie FROM project.interni WHERE CF ='"+oldcf+"';";
        try {
            st = this.connHere().prepareStatement(queryLoadPreviousAllergies);
            resultPrev = st.executeQuery(queryLoadPreviousAllergies);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if(!resultPrev.next())
                System.out.println("Error, there's not even 'none' written in database.");
            else {
                resultPrev.beforeFirst();
                System.out.println("Processing ResultSet");
                while(resultPrev.next()){
                    String prevAllergyString = resultPrev.getString(1);
                    List<String> prevAllergy = new ArrayList<>(Arrays.asList(prevAllergyString.split(", ")));
                    if(!prevAllergy.equals("none")){
                        System.out.println(prevAllergy);
                        System.out.println(prevAllergy + " already in db for this staff member");
                    }
                    //se prevAllergy == "none" allora lascio ArrayList vuoto
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        StringBuilder allAllergies = new StringBuilder();

        //create string to put into db
        if (!selectedAllergy.isEmpty()) {

            for(String s : selectedAllergy){
                if(!prevAllergies.contains(s)){
                    prevAllergies.add(s);
                    System.out.println(s + " not in database yet.");
                }
                else
                    System.out.println(s + " already into database...");
            }

            //create string from arrayList
            for (String s : prevAllergies) {
                allAllergies.append(s).append(", ");
            }
            System.out.println(allAllergies.toString());
        } else {
            allAllergies.append("none");
        }



        String queryEdit = "UPDATE interni SET Cognome ='" + surname + "', Nome ='" + name + "', CF ='" + cf + "', " +
                "DataNascita ='" + Date.valueOf(bornOn) + "', CittaNascita ='" + bornWhere + "', Residenza ='" + residence + "', " +
                "Indirizzo ='" + address + "', CAP ='" + cap + "', Provincia ='" + province + "', Allergie ='" + allAllergies.toString() + "'" +
                "WHERE CF = '" + oldcf + "';";

        String queryEditMail = "UPDATE personaleint SET Mail ='" + mail + "'" +
                "WHERE Interni_CF = '" + oldcf + "';";

        try {
            //CODID NON CAMBIA, IL CF IN personaleint, CHE è FOREIGN KEY, è IMPOSTATO ON UPDATE CASCADE!
            st = this.connHere().prepareStatement(queryEdit);
            st.executeUpdate();
            st = this.connHere().prepareStatement(queryEditMail);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }


    //SUPPLIER---------------------------------------------------------------------------------------
    @Override
    public ArrayList<SupplierDbDetails> loadDataSuppliers() throws RemoteException {
        PreparedStatement st = null;
        ResultSet result = null;
        ArrayList<SupplierDbDetails> supplierDbArrayList = new ArrayList<>(7);

        String queryLoad = "SELECT * FROM fornitore";

        try{
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !result.next() ) {
                System.out.println("No supplier in DB");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        SupplierDbDetails prova = new SupplierDbDetails(result.getString(1),
                                result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),
                                result.getString(6),
                                result.getString(7));
                        supplierDbArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return supplierDbArrayList;
    }

    @Override
    public boolean addDataSupplier(String name, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException {
        PreparedStatement st = null;
        String queryAdd = "INSERT INTO fornitore(NomeAzienda, PIVA, Mail, Tel, Indirizzo, CAP, Provincia)" +
                " VALUES (?,?,?,?,?,?,?)";
        if(name == null || piva == null || mail == null || tel == null || address == null || cap == null || province == null)
            return false;
        try {
            st = this.connHere().prepareStatement(queryAdd);
            st.setString(1, name);
            st.setString(2, piva);
            st.setString(3, mail);
            st.setString(4, tel);
            st.setString(5, address);
            st.setString(6, cap);
            st.setString(7, province);
            st.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public boolean updateSupplier(String name, String oldPiva, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException{
        PreparedStatement st = null;

        String queryEdit = "UPDATE fornitore SET PIVA ='" + piva + "', NomeAzienda ='" + name + "', Mail ='" + mail + "', " +
                "Tel ='" + tel + "', Indirizzo ='" + address + "', CAP ='" + cap + "', Provincia ='" + province + "'" +
                "WHERE PIVA = '" + oldPiva + "';";
        if(name == null || oldPiva == null || piva == null || mail == null || tel == null || address == null || cap == null || province == null)
            return false;
        try {
            st = this.connHere().prepareStatement(queryEdit);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean deleteSupplier(String piva, ArrayList<IngredientsDbDetails> ingrNO) throws RemoteException{
        PreparedStatement st = null;
        PreparedStatement stSpecialMenu = null;
        String query = "DELETE FROM project.fornitore WHERE PIVA ='"+piva+"'";
        String querySearchMenu;
        ArrayList<SpecialMenuDbDetails> special = new ArrayList<>();
        ResultSet res = null;
        if(piva == null)
            return false;
        if(ingrNO != null) {
            for (IngredientsDbDetails x : ingrNO) {
                querySearchMenu = "SELECT menu_special_date, menu_special_CF, menu_special_allergie FROM project.menu_special_has_dish_ingredients WHERE dish_ingredients_ingredients_ingredient ='" + x.getIngr() + "'";
                try {
                    stSpecialMenu = this.connHere().prepareStatement(querySearchMenu);
                    res = stSpecialMenu.executeQuery(querySearchMenu);
                    res.beforeFirst();
                    while (res.next()) {
                        SpecialMenuDbDetails sp = new SpecialMenuDbDetails(res.getString(1), null, null, null, null, null, res.getString(2), res.getString(3));
                        special.add(sp);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        String queryDelete;
        try{
            st = this.connHere().prepareStatement(query);
            st.executeUpdate(query);
            for(SpecialMenuDbDetails x : special) {
                queryDelete= "DELETE FROM menu_special WHERE date = '"+x.getDate()+"' and interni_CF ='"+x.getFC()+"' and interni_Allergie ='"+x.getAllergies()+"'";
                stSpecialMenu = this.connHere().prepareStatement(queryDelete);
                stSpecialMenu.executeUpdate(queryDelete);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<CodRifChildDbDetails> loadDataIngr(String selectedSupplier) throws RemoteException{
        PreparedStatement st = null;
        ResultSet result = null;
        ArrayList<CodRifChildDbDetails> ingrArrayList = new ArrayList<>(1);

        String queryLoad = "SELECT * FROM ingredients " +
                "WHERE Fornitore_PIVA = '" + selectedSupplier + "';";

        try{
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !result.next() ) {
                System.out.println("No ingredient in DB");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        CodRifChildDbDetails prova = new CodRifChildDbDetails(result.getString(1));
                        ingrArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return ingrArrayList;
    }

    @Override
    public boolean addIngrToDb(String ingredient, String selectedSupplier) throws RemoteException {
        PreparedStatement st = null;
        ResultSet resultDuplicate =null;
        String queryFindDuplicateIngredient = "SELECT ingredient " +
                "FROM ingredients " +
                "WHERE ingredient = '"+ ingredient +"';";
        String queryAdd = "INSERT INTO ingredients(ingredient, Fornitore_PIVA)" +
                " VALUES (?,?)";
        if(ingredient == null || selectedSupplier == null)
            return false;
        try {
            st = this.connHere().prepareStatement(queryFindDuplicateIngredient);
            resultDuplicate = st.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
        }
        try{
            if( !resultDuplicate.next() ) {
                System.out.println("No duplicate ingredient in DB. Proceed...");
                try{
                    st = this.connHere().prepareStatement(queryAdd);
                    st.setString(1, ingredient);
                    st.setString(2, selectedSupplier);
                    st.executeUpdate();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultDuplicate != null)
                    resultDuplicate.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return true;

    }

    public ArrayList<DishesDbDetails> loadMenuWithThisSupplier(String selectedSupplier) throws RemoteException {
        PreparedStatement st = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        ResultSet res = null;
        ArrayList<DishesDbDetails> menu = new ArrayList<>();
        ArrayList<PlatesDbDetails> plates = new ArrayList<>();
        String queryNomePiatto = "SELECT Nome_piatto, ingredients_ingredient FROM project.dish_ingredients JOIN project.ingredients ON ingredients_ingredient = ingredient where fornitore_PIVA = '" + selectedSupplier + "'";
        String query;
        try {
            st = this.connHere().prepareStatement(queryNomePiatto);
            result = st.executeQuery(queryNomePiatto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (!result.next()) {
                System.out.println("no ingredients");
                return null;
            } else {
                result.beforeFirst();
                try {
                    while (result.next()) {
                        PlatesDbDetails plate = new PlatesDbDetails(result.getString(1), result.getString(2));
                        plates.add(plate);
                    }
                    for (PlatesDbDetails y : plates) {
                        query = "SELECT * from project.menu_base where date IN (SELECT menu_base_date FROM project.menu_base_has_dish_ingredients where dish_ingredients_Nome_piatto ='" +y.getNomePiatto()+"')";
                        statement = this.connHere().prepareStatement(query);
                        res = statement.executeQuery(query);
                        if(res.next()) {
                            res.beforeFirst();
                            while(res.next())
                                menu.add(new DishesDbDetails(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6), res.getString(7)));

                        }
                        if(menu.isEmpty())
                            System.out.println("EMPTY MENU");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null && res != null) {
                    result.close();
                    res.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

       return menu;
    }

    @Override
    public ArrayList<IngredientsDbDetails> loadNoIngr(String selectedSupplier) throws RemoteException {
        String queryNomePiatto = "SELECT ingredient FROM project.ingredients  where fornitore_PIVA = '" + selectedSupplier + "'";
        PreparedStatement st;
        ResultSet result = null;
        ArrayList<IngredientsDbDetails> ingrNo = new ArrayList<>();
        if(selectedSupplier == null)
            return null;
        try {
            st = this.connHere().prepareStatement(queryNomePiatto);
            result = st.executeQuery(queryNomePiatto);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (!result.next()) {
                System.out.println("no ingredients");
                return null;
            } else {
                result.beforeFirst();
                while (result.next()) {
                    IngredientsDbDetails ingr = new IngredientsDbDetails(result.getString(1));
                    ingrNo.add(ingr);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingrNo;
    }

    @Override
    public boolean controllPiva(String piva) throws RemoteException{
        ResultSet result = null;
        PreparedStatement st = null;
        String queryControll = "SELECT * FROM fornitore WHERE PIVA = '"+ piva+"'";
        String queryControllNoleggio = "SELECT * FROM noleggio WHERE PIVA = '"+ piva+"'";
        Boolean controll = false;
        if(piva == null)
            return true;
        try{
            st = this.connHere().prepareStatement(queryControll);
            result = st.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            if(!result.next()){
                try{
                    st = this.connHere().prepareStatement(queryControllNoleggio);
                    result = null;
                    result = st.executeQuery();
                }catch(SQLException e){
                    e.printStackTrace();
                }
                if(!result.next()) {
                    System.out.println("No piva like this in DB");
                    result.close();
                    controll = true;
                }
            }
            else
                controll = false;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return controll;

    }

//COACH OPERATORS ---------------------------------------------------------------------------------------------

    @Override
    public ArrayList<SupplierDbDetails> loadDataCoachOperator() throws RemoteException {
        PreparedStatement st = null;
        ResultSet result = null;
        ArrayList<SupplierDbDetails> supplierDbArrayList = new ArrayList<>(7);

        String queryLoad = "SELECT * FROM noleggio";

        try {
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (!result.next()) {
                System.out.println("No coach operator in DB");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        SupplierDbDetails prova = new SupplierDbDetails(result.getString(1),
                                result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),
                                result.getString(6),
                                result.getString(7));
                        supplierDbArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return supplierDbArrayList;
    }

    @Override
    public boolean addDataCoachOperator(String name, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException {
        PreparedStatement st = null;
        String queryAdd = "INSERT INTO noleggio(NomeAzienda, PIVA, Mail, Tel, Indirizzo, CAP, Provincia)" +
                " VALUES (?,?,?,?,?,?,?)";
        if(name == null || piva == null || mail == null || tel == null || address == null || cap == null || province == null)
            return false;
        try {
            st = this.connHere().prepareStatement(queryAdd);
            st.setString(1, name);
            st.setString(2, piva);
            st.setString(3, mail);
            st.setString(4, tel);
            st.setString(5, address);
            st.setString(6, cap);
            st.setString(7, province);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public boolean updateCoachOperator(String name, String oldPiva, String piva, String mail, String tel, String address, String cap, String province) throws RemoteException {
        PreparedStatement st = null;

        String queryEdit = "UPDATE noleggio SET PIVA ='" + piva + "', NomeAzienda ='" + name + "', Mail ='" + mail + "', " +
                "Tel ='" + tel + "', Indirizzo ='" + address + "', CAP ='" + cap + "', Provincia ='" + province + "'" +
                "WHERE PIVA = '" + oldPiva + "';";
        if(name == null ||oldPiva == null|| piva == null || mail == null || tel == null || address == null || cap == null || province == null)
            return false;

        try {
            st = this.connHere().prepareStatement(queryEdit);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean deleteCoachOperatorBus(String plate) throws RemoteException {
        /*
        1. elimino targa selezionata
        2. controllo se per il noleggio associato al bus che sto eliminando esistono altri bus
        3. se ResultSet della SELECT .next() != null, elimino solo quella targa dal db bus
        4. else elimina l'interno fornitore da db noleggio
         */
        PreparedStatement st = null;
        String queryDeleteBus = "DELETE FROM bus WHERE Targa = '" + plate + "';";
        if(plate == null)
            return false;
        try {
            st = this.connHere().prepareStatement(queryDeleteBus);
            st.executeUpdate(queryDeleteBus);
            System.out.println("Deleted from bus.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }


    @Override
    public boolean zeroActualParticipants(String plate) throws RemoteException {
        System.out.println("ZERO ACTUAL PARTICIPANTS IS NOW RUNNING");
        PreparedStatement st = null;
        String queryFindGitaWithBus = "SELECT gita_NumGita FROM gita_has_bus WHERE bus_Targa = '" + plate + "';";
        ResultSet resultNumGita = null;
        ArrayList<CodRifChildDbDetails> numGitaArrayList = new ArrayList<>();
        ArrayList<String> numGitaArrayListString = new ArrayList<>();

        String queryFindPeopleToTrip = "SELECT interni_CF FROM interni_has_gita WHERE gita_NumGita = ? ";
        ResultSet resultParticipants = null;
        ArrayList<CodRifChildDbDetails> participantsArrayList = new ArrayList<>();
        ArrayList<String> participantsArrayListString = new ArrayList<>();

        String querySetZero = "UPDATE interni_has_gita" +
                " SET Partecipante_effettivo = ? " +
                " WHERE interni_CF = ? " +
                " AND gita_NumGita = ? ";

        try {
            st = this.connHere().prepareStatement(queryFindGitaWithBus);
            resultNumGita = st.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (!resultNumGita.next()) {
                System.out.println("No trip in db.");
                return false;
            } else {
                System.out.println("Found trip '" + resultNumGita.toString() + "'");
                resultNumGita.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultNumGita.next()) {
                        CodRifChildDbDetails prova = new CodRifChildDbDetails(resultNumGita.getString(1));
                        numGitaArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                for (CodRifChildDbDetails object : numGitaArrayList) {
                    numGitaArrayListString.add(Objects.toString(object.getCodRif(), null));
                }

                for (String num : numGitaArrayListString) {
                    st = this.connHere().prepareStatement(queryFindPeopleToTrip);
                    st.setString(1, num);
                    resultParticipants = st.executeQuery();
                    if (!resultParticipants.next()) {
                        System.out.println("No participant.");
                        return false;
                    } else {
                        resultParticipants.beforeFirst();
                        System.out.println("Processing ResultSet");
                        try {
                            while (resultParticipants.next()) {
                                CodRifChildDbDetails prova = new CodRifChildDbDetails(resultParticipants.getString(1));
                                participantsArrayList.add(prova);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        for (CodRifChildDbDetails object : participantsArrayList) {
                            participantsArrayListString.add(Objects.toString(object.getCodRif(), null));
                        }
                        for (String person : participantsArrayListString) {
                            st = this.connHere().prepareStatement(querySetZero);
                            st.setInt(1, 0);
                            st.setString(2, person);
                            st.setString(3, num);
                            st.executeUpdate();
                            System.out.println("******** Edited effettivo *******");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (resultParticipants != null)
                    resultParticipants.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return true;
    }


    @Override
    public boolean deleteIsHere(String plate) throws RemoteException {
        //cancello persone collegate alle gite connesse al bus da interni_is_here
        PreparedStatement st = null;
        String queryFindGitaWithBus = "SELECT gita_NumGita FROM gita_has_bus WHERE bus_Targa = '" + plate + "';";
        ResultSet resultNumGita = null;
        ArrayList<CodRifChildDbDetails> numGitaArrayList = new ArrayList<>();
        ArrayList<String> numGitaArrayListString = new ArrayList<>();

        try {
            st = this.connHere().prepareStatement(queryFindGitaWithBus);
            resultNumGita = st.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (!resultNumGita.next()) {
                System.out.println("No trip connected to bus. Proceed and delete bus.");
                return false;
            } else {
                resultNumGita.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultNumGita.next()) {
                        CodRifChildDbDetails prova = new CodRifChildDbDetails(resultNumGita.getString(1));
                        numGitaArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                for (CodRifChildDbDetails object : numGitaArrayList) {
                    numGitaArrayListString.add(Objects.toString(object.getCodRif(), null));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String queryFindParticipantToTrip = "SELECT interni_CF FROM interni_is_here WHERE gita_NumGita = ?";
        ResultSet resultParticipant = null;
        ArrayList<CodRifChildDbDetails> participantArrayList = new ArrayList<>();
        ArrayList<String> participantArrayListString = new ArrayList<>();

        String querySetZero = "DELETE FROM interni_is_here WHERE gita_NumGita = ? AND interni_CF = ? ";

        for (String num : numGitaArrayListString) {
            try {
                st = this.connHere().prepareStatement(queryFindParticipantToTrip);
                st.setString(1, num);
                resultParticipant = st.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (!resultParticipant.next()) {
                    System.out.println("No participant in db.");
                } else {
                    resultParticipant.beforeFirst();
                    System.out.println("Processing ResultSet");
                    try {
                        while (resultParticipant.next()) {
                            CodRifChildDbDetails prova = new CodRifChildDbDetails(resultParticipant.getString(1));
                            participantArrayList.add(prova);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    for (CodRifChildDbDetails object : participantArrayList) {
                        participantArrayListString.add(Objects.toString(object.getCodRif(), null));
                    }

                    for(String part : participantArrayListString){
                        st = this.connHere().prepareStatement(querySetZero);
                        st.setString(1, num);
                        st.setString(2,part);
                        st.executeUpdate();
                        System.out.println("Deleted from interni_is_here");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (resultParticipant != null)
                        resultParticipant.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (st != null)
                        st.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    @Override
    public void deleteFromGitaHasBus(String plate) throws RemoteException{
        PreparedStatement st =null;
        String queryDelete = "DELETE FROM gita_has_bus WHERE bus_Targa = '" + plate + "';";

        try{
            st = this.connHere().prepareStatement(queryDelete);
            st.executeUpdate(queryDelete);
            System.out.println("Deleted from gita_has_bus.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean deleteCoachOperator(String piva) throws RemoteException{
        PreparedStatement st =null;
        String queryDelete = "DELETE FROM noleggio WHERE PIVA = '" + piva + "';";

        if(piva == null)
            return false;
        try{
            st = this.connHere().prepareStatement(queryDelete);
            st.executeUpdate(queryDelete);
            System.out.println("Deleted from noleggio.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    @Override
    public ArrayList<BusPlateCapacityDbDetails> loadDataBus(String selectedSupplier) throws RemoteException{
        PreparedStatement st = null;
        ResultSet result = null;
        ArrayList<BusPlateCapacityDbDetails> supplierDbArrayList = new ArrayList<>(2);

        String queryLoad = "SELECT * FROM bus " +
                "WHERE Noleggio_PIVA = '" + selectedSupplier + "';";

        try{
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !result.next() ) {
                System.out.println("No bus in DB");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        BusPlateCapacityDbDetails prova = new BusPlateCapacityDbDetails(result.getString(1),
                                result.getString(2));
                        supplierDbArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return supplierDbArrayList;
    }

    @Override
    public boolean addBusToDb(String plate, int capacity, String selectedSupplier) throws RemoteException {
        PreparedStatement st = null;
        ResultSet resultDuplicate =null;
        String queryFindDuplicateKey = "SELECT Targa " +
                "FROM bus " +
                "WHERE Targa = '"+ plate +"' AND Noleggio_PIVA = '"+ selectedSupplier +"';";
        String queryAdd = "INSERT INTO bus(Targa, capienza, Noleggio_PIVA)" +
                " VALUES (?,?,?)";
        if(plate == null || capacity == 0 || selectedSupplier == null)
            return false;
        try {
            st = this.connHere().prepareStatement(queryFindDuplicateKey);
            resultDuplicate = st.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
        }
        try{
            if( !resultDuplicate.next() ) {
                System.out.println("No duplicate bus in DB. Proceed...");
                try{
                    st = this.connHere().prepareStatement(queryAdd);
                    st.setString(1, plate);
                    st.setInt(2, capacity);
                    st.setString(3, selectedSupplier);
                    st.executeUpdate();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultDuplicate != null)
                    resultDuplicate.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return true;

    }

    @Override
    public boolean controllBus(String plate) throws RemoteException{
        ResultSet result = null;
        PreparedStatement st = null;
        String queryControll = "SELECT * FROM bus WHERE Targa = '"+ plate+"'";
        Boolean controll = false;

        try{
            st = this.connHere().prepareStatement(queryControll);
            result = st.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            if(!result.next()){
                System.out.println("No BUS like this in DB");
                result.close();
                controll = true;
            }
            else
                controll = false;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return controll;

    }

//MENU ---------------------------------------------------------------------------------------

    @Override
    public DishesDbDetails loadThisMenu(LocalDate date) throws RemoteException {
        PreparedStatement st;
        ResultSet result = null;
        DishesDbDetails dishes  = null;
        String queryLoad1 = "SELECT * FROM project.menu_base WHERE date ='"+date+"'";

        try{
            st = this.connHere().prepareStatement(queryLoad1);
            result = st.executeQuery(queryLoad1);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try{
            System.out.println("ok");
            if( !result.next() ) {
                System.out.println("No menu in Db");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        dishes = new DishesDbDetails(result.getString(1),result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),result.getString(6),result.getString(7));


                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return dishes;
    }

    @Override
    public ArrayList<IngredientsDbDetails> searchIngredients(String dish) throws RemoteException {
        PreparedStatement st;
        String querySearch = "SELECT ingredients_ingredient FROM project.dish_ingredients WHERE Nome_piatto='"+dish+"';";
        ResultSet result = null;
        ArrayList<IngredientsDbDetails> ingredientsForThisDish = new ArrayList<>();
        try{
            st = this.connHere().prepareStatement(querySearch);
            result = st.executeQuery(querySearch);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            if(!result.next()){
                System.out.println("Error no ingredients for this dish");
                return null;
            }else{
                result.beforeFirst();
                try{
                    while(result.next()){
                        IngredientsDbDetails example = new IngredientsDbDetails(result.getString(1));
                        ingredientsForThisDish.add(example);
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(result != null){
                    result.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return ingredientsForThisDish;

    }

    @Override
    public ArrayList<IngredientsDbDetails> loadIngr(LocalDate d) throws RemoteException{
        PreparedStatement st = null;
        String queryLoad = "SELECT dish_ingredients_ingredients FROM project.menu_base_has_dish_ingredients WHERE menu_base_date ='" + d+"'";
        ResultSet result = null;
        ArrayList<IngredientsDbDetails> resIngr = new ArrayList<>();
        try{
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if(!result.next()){
                System.out.println("Error");
                return null;
            }else{
                result.beforeFirst();
                try{
                    while(result.next()){
                        IngredientsDbDetails str = new IngredientsDbDetails(result.getString(1));
                        resIngr.add(str);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return resIngr;
    }

    @Override
    public boolean updateMenu(String num, String entree, String main, String dessert, String side, String drink, LocalDate day,  LocalDate oldDate) throws RemoteException{

        PreparedStatement st = null;
        PreparedStatement stIngr = null;
        PreparedStatement stAdd = null;
        ArrayList<IngredientsDbDetails> ingredients = new ArrayList<>();

        String queryUpdate = "UPDATE project.menu_base SET NumPiatti='" + num +"', entrees ='"+ entree +"', main_courses ='"+ main+ "', dessert = '" + dessert+"', side_dish = '"+ side+"', drink = '"+ drink +"', date ='"+ day +"' WHERE date = '"+ oldDate+"'";

        String queryDelete = "DELETE FROM project.menu_base_has_dish_ingredients WHERE menu_base_date = '"+day+"'";

        String queryAdd = "INSERT INTO project.menu_base_has_dish_ingredients (menu_base_date, dish_ingredients_Nome_piatto, dish_ingredients_ingredients_ingredient)"+"VALUES (?,?,?)";

        try {

            st = this.connHere().prepareStatement(queryUpdate);
            st.executeUpdate();
            stIngr = this.connHere().prepareStatement(queryDelete);
            stIngr.executeUpdate();
            stAdd = this.connHere().prepareStatement(queryAdd);

            if(!entree.isEmpty()) {
                ingredients = searchIngredients(entree);
                for(IngredientsDbDetails x : ingredients){
                    stAdd.setDate(1,Date.valueOf(day));
                    stAdd.setString(2, entree);
                    stAdd.setString(3, x.getIngr());
                    stAdd.executeUpdate();
                }
            }
            if(!main.isEmpty()) {
                ingredients = searchIngredients(main);
                for(IngredientsDbDetails x : ingredients){
                    stAdd.setDate(1,Date.valueOf(day));
                    stAdd.setString(2, main);
                    stAdd.setString(3, x.getIngr());
                    stAdd.executeUpdate();
                }
            }

            if(!side.isEmpty()) {
                ingredients = searchIngredients(side);
                for(IngredientsDbDetails x : ingredients){
                    stAdd.setDate(1,Date.valueOf(day));
                    stAdd.setString(2, side);
                    stAdd.setString(3, x.getIngr());
                    stAdd.executeUpdate();
                }
            }

            if(!dessert.isEmpty()) {
                ingredients = searchIngredients(dessert);
                for(IngredientsDbDetails x : ingredients){
                    stAdd.setDate(1,Date.valueOf(day));
                    stAdd.setString(2, dessert);
                    stAdd.setString(3, x.getIngr());
                    stAdd.executeUpdate();
                }
            }

            if(!drink.isEmpty()) {
                ingredients = searchIngredients(drink);
                for(IngredientsDbDetails x : ingredients){
                    stAdd.setDate(1,Date.valueOf(day));
                    stAdd.setString(2, drink);
                    stAdd.setString(3, x.getIngr());
                    stAdd.executeUpdate();
                }
            }

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (st != null && stIngr != null && stAdd != null) {
                    st.close();
                    stIngr.close();
                    stAdd.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


//MENU BASE---------------------------------------------------------------------------------------

    @Override
    public ArrayList<DishesDbDetails> loadMenu() throws RemoteException {
        PreparedStatement st;
        ResultSet result = null;
        ArrayList<DishesDbDetails> dishes = new ArrayList<>(7);

        String queryLoad1 = "SELECT * FROM project.menu_base";

        try{
            st = this.connHere().prepareStatement(queryLoad1);
            result = st.executeQuery(queryLoad1);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try{
            System.out.println("Trying to load basic menu");
            if( !result.next() ) {
                System.out.println("No menu in Db");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        DishesDbDetails prova  = null;
                        prova = new DishesDbDetails(result.getString(1),
                                result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),
                                result.getString(6),
                                result.getString(7));

                        //String number, String entree, String mainCourse, String dessert, String sideDish,String drink,String day

                        dishes.add(prova);

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dishes;

    }

    @Override
    public boolean addMenu(String num, String entree, String mainCourse, String dessert, String sideDish, String drink, LocalDate date) throws RemoteException {
        PreparedStatement st = null;

        String queryAdd = "INSERT INTO project.menu_base(NumPiatti,entrees, main_courses,dessert, side_dish, drink, date)" +
                " VALUES (?,?,?,?,?,?,?)";

        String queryAddDish = "INSERT INTO project.menu_base_has_dish_ingredients (menu_base_date, dish_ingredients_Nome_piatto, dish_ingredients_ingredients_ingredient)"+"VALUES(?,?,?)";
        PreparedStatement stDish;
        ArrayList<IngredientsDbDetails> ingredients;

        try {
            //add data new child into db
            st = this.connHere().prepareStatement(queryAdd);
            stDish= this.connHere().prepareStatement(queryAddDish);
            st.setString(1, num);
            st.setString(2, entree);
            st.setString(3, mainCourse);
            st.setString(4, dessert);
            st.setString(5, sideDish);
            st.setString(6, drink);
            st.setDate(7, Date.valueOf(date));
            st.executeUpdate();

            if(!entree.isEmpty()) {
                ingredients = searchIngredients(entree);
                for(IngredientsDbDetails x : ingredients){
                    stDish.setDate(1,Date.valueOf(date));
                    stDish.setString(2, entree);
                    stDish.setString(3,x.getIngr());
                    stDish.executeUpdate();
                }
            }

            if(!mainCourse.isEmpty()) {
                ingredients = searchIngredients(mainCourse);
                for(IngredientsDbDetails x : ingredients){
                    stDish.setDate(1,Date.valueOf(date));
                    stDish.setString(2, mainCourse);
                    stDish.setString(3,x.getIngr());
                    stDish.executeUpdate();
                }
            }

            if(!sideDish.isEmpty()) {
                ingredients = searchIngredients(sideDish);
                for(IngredientsDbDetails x : ingredients){
                    stDish.setDate(1,Date.valueOf(date));
                    stDish.setString(2, sideDish);
                    stDish.setString(3,x.getIngr());
                    stDish.executeUpdate();
                }
            }

            if(!dessert.isEmpty()) {
                ingredients = searchIngredients(dessert);
                for(IngredientsDbDetails x : ingredients){
                    stDish.setDate(1,Date.valueOf(date));
                    stDish.setString(2, dessert);
                    stDish.setString(3,x.getIngr());
                    stDish.executeUpdate();
                }
            }

            if(!drink.isEmpty()) {
                ingredients = searchIngredients(drink);
                for(IngredientsDbDetails x : ingredients){
                    stDish.setDate(1,Date.valueOf(date));
                    stDish.setString(2, drink);
                    stDish.setString(3,x.getIngr());
                    stDish.executeUpdate();
                }
            }

        } catch (Exception e){
            e.printStackTrace();
            if(loadThisMenu(date) != null){
                deleteMenu(date);
                return false;
            }
        } finally {
            try {
                if (st != null ) {
                    st.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    @Override
    public boolean controllDate(LocalDate d) throws RemoteException {
        PreparedStatement st = null;
        String queryControll = "SELECT date FROM project.menu_base WHERE date = '" + d + "';";

        ResultSet result = null;

        try {

            st = this.connHere().prepareStatement(queryControll);
            result = st.executeQuery();


        } catch (SQLException e) {
            System.out.println("Error during search in DB");
            e.printStackTrace();
        }

        try {
            if (!result.next()) {
                System.out.println("No date like this in database");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteMenu(LocalDate d) throws RemoteException {
        PreparedStatement st = null;
        //PreparedStatement st1 = null;

        String queryDelete = "DELETE FROM project.menu_base WHERE date = '" + d + "';";

        // String queryDeleteIngredients = "DELETE FROM project.menu_base_has_dish_ingredients WHERE menu_base_date = '"+d+"'";

        try{
            st = this.connHere().prepareStatement(queryDelete);
            //  st.executeUpdate(queryDeleteIngredients);
            st.executeUpdate(queryDelete);
            System.out.println("Menu deleted.");


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (st != null){
                    st.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public boolean saveIngredients(String dish, ArrayList<String> selectedIngredients) throws RemoteException{
        PreparedStatement st = null;
        try {
            for (String x : selectedIngredients) {
                String queryAddIngredients = "INSERT INTO project.dish_ingredients(Nome_piatto, ingredients_ingredient)" + " VALUES(?,?)";
                st = this.connHere().prepareStatement(queryAddIngredients);
                st.setString(1, dish);
                st.setString(2, x);
                st.executeUpdate();
            }
            try{
                if(st != null) {
                    st.close();
                }
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }



    public ArrayList<SpecialDbDetails> loadInterniWithAllergies(LocalDate date) throws RemoteException {
        PreparedStatement st;
        PreparedStatement stIngr;
        ResultSet result = null;
        ResultSet resIngr = null;
        ArrayList<SpecialDbDetails> special = new ArrayList<>();
        ArrayList<IngredientsDbDetails> ingredients = new ArrayList<>();
        ArrayList<SpecialDbDetails> specialInterni = new ArrayList<>();
        String queryLoad = "SELECT CF,Allergie FROM project.interni WHERE Allergie  != 'none' and CF NOT IN (SELECT menu_special_CF FROM project.menu_special_has_dish_ingredients WHERE menu_special_date = '"+date+"')";
        String queryIngr = "SELECT dish_ingredients_ingredients_ingredient FROM project.menu_base_has_dish_ingredients WHERE menu_base_date =' "+date+"'";
        IngredientsDbDetails ingred;
        SpecialDbDetails spec;

        //prima queryLoad
        try{
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);
        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            if(!result.next()){
                System.out.println("No interni in db");
                return null;
            }else{
                result.beforeFirst();
                try{
                    while(result.next()){
                        spec = new SpecialDbDetails(result.getString(1),result.getString(2));
                        special.add(spec);
                    }

                    //poi queryIngr
                    try{
                        stIngr = this.connHere().prepareStatement(queryIngr);
                        resIngr = stIngr.executeQuery(queryIngr);
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                    try{
                        if(!resIngr.next()){
                            System.out.println("No ingredient in db");
                            return null;
                        }
                        else{
                            result.beforeFirst();
                            try{
                                while(resIngr.next()){
                                    ingred = new IngredientsDbDetails(resIngr.getString(1));
                                    ingredients.add(ingred);
                                }

                            } catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                    for(SpecialDbDetails x : special){
                        for(IngredientsDbDetails y : ingredients){
                            if(x.getAllergie().contains(y.getIngr()))
                                specialInterni.add(new SpecialDbDetails(x.getCF(), x.getAllergie()));
                        }
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return specialInterni;
    }


    @Override
    public ArrayList<SpecialMenuDbDetails> loadSpecialMenu() throws RemoteException{
        PreparedStatement st;
        ResultSet result = null;
        ArrayList<SpecialMenuDbDetails> dishes = new ArrayList<>(8);

        String queryLoad1 = "SELECT * FROM project.menu_special";

        try{
            st = this.connHere().prepareStatement(queryLoad1);
            result = st.executeQuery(queryLoad1);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try{
            System.out.println("ok");
            if( !result.next() ) {
                System.out.println("No menu in Db");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        SpecialMenuDbDetails prova = new SpecialMenuDbDetails(result.getString(6),
                                result.getString(1),
                                result.getString(4),
                                result.getString(3),
                                result.getString(5),
                                result.getString(2),
                                result.getString(7),
                                result.getString(8));

                        dishes.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }



    public boolean deleteSpecialMenu(LocalDate date, String FC, String allergies) throws RemoteException{
        PreparedStatement st = null;
        String queryDelete = "DELETE FROM project.menu_special WHERE date = '" + date + "'and interni_CF ='"+FC+"' and interni_Allergie = '"+ allergies+"'";
        if(date == null || FC == null || allergies == null)
            return false;
        try{
            st = this.connHere().prepareStatement(queryDelete);
            st.executeUpdate(queryDelete);
            System.out.println("Menu deleted.");

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (st != null){
                    st.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean addSpecialMenu(String entree, String main, String dessert, String side, String drink, LocalDate date,SpecialDbDetails special) throws RemoteException{
        String queryAdd = "INSERT INTO project.menu_special (entrees, main_courses, dessert, side_dish, drink, date, interni_CF, interni_Allergie) " +"VALUES (?,?,?,?,?,?,?,?)";
        String queryAddDish = "INSERT INTO project.menu_special_has_dish_ingredients (menu_special_date, dish_ingredients_Nome_piatto, dish_ingredients_ingredients_ingredient, menu_special_CF, menu_special_Allergie)"+"VALUES(?,?,?,?,?)";
        PreparedStatement st = null;
        PreparedStatement stDish = null;
        ArrayList<IngredientsDbDetails> ingredients;
        if(special == null || drink == null || dessert == null)
            return false;
        try {
            st = this.connHere().prepareStatement(queryAdd);
            st.setString(1, entree);
            st.setString(2, main);
            st.setString(3, dessert);
            st.setString(4, side);
            st.setString(5, drink);
            st.setDate(6, Date.valueOf(date));
            st.setString(7, special.getCF());
            st.setString(8, special.getAllergie());
            st.executeUpdate();

            stDish= this.connHere().prepareStatement(queryAddDish);

            if(!entree.isEmpty()) {
                ingredients = searchIngredients(entree);
                for(IngredientsDbDetails x : ingredients){
                    stDish.setDate(1, Date.valueOf(date));
                    stDish.setString(2, entree);
                    stDish.setString(3,x.getIngr());
                    stDish.setString(4, special.getCF());
                    stDish.setString(5, special.getAllergie());
                    stDish.executeUpdate();
                }
            }

            if(!main.isEmpty()) {
                ingredients = searchIngredients(main);
                for(IngredientsDbDetails x : ingredients){
                    stDish.setDate(1, Date.valueOf(date));
                    stDish.setString(2, main);
                    stDish.setString(3, x.getIngr());
                    stDish.setString(4, special.getCF());
                    stDish.setString(5, special.getAllergie());
                    stDish.executeUpdate();
                }
            }

            if(!side.isEmpty()) {
                ingredients = searchIngredients(side);
                for(IngredientsDbDetails x : ingredients){
                    stDish.setDate(1,Date.valueOf(date));
                    stDish.setString(2, side);
                    stDish.setString(3,x.getIngr());
                    stDish.setString(4, special.getCF());
                    stDish.setString(5, special.getAllergie());
                    stDish.executeUpdate();
                }
            }

            if(!dessert.isEmpty()) {
                ingredients = searchIngredients(dessert);
                for(IngredientsDbDetails x : ingredients){
                    stDish.setDate(1,Date.valueOf(date));
                    stDish.setString(2, dessert);
                    stDish.setString(3,x.getIngr());
                    stDish.setString(4, special.getCF());
                    stDish.setString(5, special.getAllergie());
                    stDish.executeUpdate();
                }
            }

            if(!drink.isEmpty()) {
                ingredients = searchIngredients(drink);
                for(IngredientsDbDetails x : ingredients){
                    stDish.setDate(1,Date.valueOf(date));
                    stDish.setString(2, drink);
                    stDish.setString(3,x.getIngr());
                    stDish.setString(4, special.getCF());
                    stDish.setString(5, special.getAllergie());
                    stDish.executeUpdate();
                }
            }

        }catch (SQLException e) {
            deleteSpecialMenu(date, special.getCF(), special.getAllergie());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (st != null && stDish != null){
                    st.close();
                    stDish.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public boolean updateSpecialMenu(String entree, String main, String dessert, String side, String drink, LocalDate date, SpecialDbDetails special) throws RemoteException{
        PreparedStatement st = null;
        PreparedStatement stIngr = null;
        PreparedStatement stAdd = null;
        ArrayList<IngredientsDbDetails> ingredients;

        String queryUpdate = "UPDATE project.menu_special SET entrees ='"+entree+"' , main_courses = '"+main+"' , dessert = '"+dessert+"' , side_dish ='"+side+"' , drink ='"+drink+"' where date='"+date+"' and interni_CF = '"+special.getCF()+"' and interni_Allergie = '"+special.getAllergie()+"'";
        String queryDelete = "DELETE FROM  project.menu_special_has_dish_ingredients WHERE menu_special_date = '"+date+"' and  menu_special_CF='"+special.getCF()+"'";
        String queryAdd = "INSERT INTO project.menu_special_has_dish_ingredients (menu_special_date, dish_ingredients_Nome_piatto, dish_ingredients_ingredients_ingredient, menu_special_CF, menu_special_allergie)"+"VALUES(?,?,?,?,?)";
        try{
            st = this.connHere().prepareStatement(queryUpdate);
            stIngr = this.connHere().prepareStatement(queryDelete);
            stAdd = this.connHere().prepareStatement(queryAdd);
            st.executeUpdate();
            stIngr.executeUpdate();
            if(!entree.isEmpty()) {
                ingredients = searchIngredients(entree);
                for(IngredientsDbDetails x : ingredients){
                    stAdd.setDate(1,Date.valueOf(date));
                    stAdd.setString(2, entree);
                    stAdd.setString(3,x.getIngr());
                    stAdd.setString(4, special.getCF());
                    stAdd.setString(5, special.getAllergie());
                    stAdd.executeUpdate();
                }
            }

            if(!main.isEmpty()) {
                ingredients = searchIngredients(main);
                for(IngredientsDbDetails x : ingredients){
                    stAdd.setDate(1,Date.valueOf(date));
                    stAdd.setString(2, main);
                    stAdd.setString(3,x.getIngr());
                    stAdd.setString(4, special.getCF());
                    stAdd.setString(5, special.getAllergie());
                    stAdd.executeUpdate();
                }
            }

            if(!side.isEmpty()) {
                ingredients = searchIngredients(side);
                for(IngredientsDbDetails x : ingredients){
                    stAdd.setDate(1,Date.valueOf(date));
                    stAdd.setString(2, side);
                    stAdd.setString(3,x.getIngr());
                    stAdd.setString(4, special.getCF());
                    stAdd.setString(5, special.getAllergie());
                    stAdd.executeUpdate();
                }
            }

            if(!dessert.isEmpty()) {
                ingredients = searchIngredients(dessert);
                for(IngredientsDbDetails x : ingredients){
                    stAdd.setDate(1,Date.valueOf(date));
                    stAdd.setString(2, dessert);
                    stAdd.setString(3,x.getIngr());
                    stAdd.setString(4, special.getCF());
                    stAdd.setString(5, special.getAllergie());
                    stAdd.executeUpdate();
                }
            }

            if(!drink.isEmpty()) {
                ingredients = searchIngredients(drink);
                for(IngredientsDbDetails x : ingredients){
                    stAdd.setDate(1,Date.valueOf(date));
                    stAdd.setString(2, drink);
                    stAdd.setString(3,x.getIngr());
                    stAdd.setString(4, special.getCF());
                    stAdd.setString(5, special.getAllergie());
                    stAdd.executeUpdate();
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            return false;
        }finally {
            try {
                if (st != null && stAdd != null && stIngr != null){
                    st.close();
                    stAdd.close();
                    stIngr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    //TRIP---------------------------------------------------------------------------------------------
    @Override
    public ArrayList<TripTableDbDetails> loadDataTrip() throws RemoteException {
        PreparedStatement st;
        ResultSet result = null;
        ArrayList<TripTableDbDetails> trips = new ArrayList<>(7);
        String queryLoadTrip = "SELECT DataOraPar, DataOraArr, DataOraRit, Alloggio, Partenza, Destinazione, NumGita FROM gita";

        try{
            st = this.connHere().prepareStatement(queryLoadTrip);
            result = st.executeQuery(queryLoadTrip);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            if( !result.next() ) {
                System.out.println("No trip in Db");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        TripTableDbDetails prova  = null;
                        prova = new TripTableDbDetails(result.getString(1),
                                result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),
                                result.getString(6));
                        //get string from db, put into list of ChildGuiData, ready to put it into GUI
                        trips.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trips;
    }


    @Override
    public boolean deleteTrip(String dep, String dateDep, String dateCom, String staying, String dateArr, String arr) throws RemoteException {
        PreparedStatement st = null;

        System.out.println("You selected " + dep + "  " + arr + "  " + dateDep +"  "+ dateArr +"  "+ dateCom +"  "+ staying);

        if(dep == null || dateDep == null || dateCom == null || dateArr == null || arr == null)
            return false;

        String queryDelete = "DELETE FROM gita " +
                "WHERE Partenza ='"+ dep +"' AND DataOraPar ='"+ dateDep +"' AND DataOraRit ='"+ dateCom +"' AND Alloggio ='"+ staying +"' AND DataOraArr ='"+ dateArr +"' AND Destinazione ='"+ arr + "';";

        try{
            st = this.connHere().prepareStatement(queryDelete);
            st.executeUpdate(queryDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public ArrayList<ChildTripDbDetails> loadChildTrip() throws RemoteException {
        //load all children (to add new trip)
        PreparedStatement st = null;
        ResultSet result = null;
        ArrayList<ChildTripDbDetails> childDbArrayList = new ArrayList<>();

        String queryLoad = "SELECT Cognome, Nome, CF" +
                " FROM project.interni INNER JOIN project.bambino" +
                " ON interni.CF = bambino.Interni_CF";

        try{
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !result.next() ) {
                System.out.println("No child in DB");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        ChildTripDbDetails prova = new ChildTripDbDetails(result.getString(1),
                                result.getString(2),
                                result.getString(3));
                        childDbArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return childDbArrayList;
    }


    @Override
    public ArrayList<StaffTripDbDetails> loadStaffTrip() throws RemoteException{
        //load all staff members (to add new trip)
        PreparedStatement st = null;
        ResultSet result = null;
        ArrayList<StaffTripDbDetails> staffDbArrayList = new ArrayList<>();

        String queryLoad = "SELECT Cognome, Nome, CF" +
                " FROM project.interni INNER JOIN project.personaleint" +
                " ON interni.CF = personaleint.Interni_CF";

        try{
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !result.next() ) {
                System.out.println("No staff in DB");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        StaffTripDbDetails prova = new StaffTripDbDetails(result.getString(1),
                                result.getString(2),
                                result.getString(3));
                        staffDbArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return staffDbArrayList;
    }


    @Override
    public int[] addTrip (ArrayList<String> selectedChild, ArrayList<String> selectedStaff,
                          String localDateDep, String localDateArr, String localDateCom,
                          String departureFrom, String arrivalTo, String staying) throws RemoteException {
        PreparedStatement st = null;

        String queryAddTrip = "INSERT INTO gita (Partenza, DataOraPar, DataOraRit, Alloggio, DataOraArr, Destinazione, NumGita)" +
                " VALUES (?,?,?,?,?,?,?)";
        String queryLastNumGita = "SELECT COALESCE(MAX(NumGita), 0) FROM gita"; //replaces null from empty NumGita with g1
        //to create new NumGita for the added trip simply ++

        //add into interni_has_gita (still not true if it is participant or not, leave default = false), as for selectedAllergy
        //execute query in for cycle, to divide items from arraylist selectedChildren/selectedStaff into string to put every item into database
        //divide items from arraylist selectedChild/selectedStaff into string to put into database
        String[] selectedChildArray = selectedChild.toArray(new String[selectedChild.size()]);
        String[] selectedStaffArray = selectedStaff.toArray(new String[selectedStaff.size()]);
        String queryAddSelectedParticipants = "INSERT INTO interni_has_gita (interni_CF, gita_NumGita, Partecipante_effettivo)" +
                " VALUES (?,?,?)";

        String newNumGita;
        ArrayList<NumGitaDbDetails> numGitaArrayList = new ArrayList<>(1);
        ResultSet result = null;
        int[] totParticipants = new int[2];
        totParticipants[0] = 0;
        totParticipants[1] = 0;


        try {
            st = this.connHere().prepareStatement(queryLastNumGita);
            result = st.executeQuery(queryLastNumGita);
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            if( !result.next() ) {
                System.out.println("Error");
                return null;
            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet to create new NumGita");
                try {
                    while (result.next()) {
                        NumGitaDbDetails lastCod = new NumGitaDbDetails(result.getString(1));
                        numGitaArrayList.add(lastCod);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (numGitaArrayList.get(0).getNumGita().equals("0")) {
                    System.out.println("First NumGita in DB");
                    //then new child's CodRif is 1
                    newNumGita = "g1";
                    //first add new NumGita to gita
                    st = this.connHere().prepareStatement(queryAddTrip);
                    st.setString(1, departureFrom);
                    st.setString(2, localDateDep);
                    st.setString(3, localDateCom);
                    st.setString(4, staying);
                    st.setString(5, localDateArr);
                    st.setString(6, arrivalTo);
                    st.setString(7, newNumGita);
                    st.executeUpdate();

                    //add interni_CF into interni_has_gita: for every selectedParticipant his CF, newNumGita, Partecipante_effettivo = 0
                    for (String selectedParticipantCf : selectedChildArray) {
                        st = this.connHere().prepareStatement(queryAddSelectedParticipants);
                        st.setString(1, selectedParticipantCf);
                        st.setString(2, newNumGita);
                        st.setInt(3,0);
                        st.executeUpdate();

                        totParticipants[0]++;
                        System.out.println(totParticipants[0]);
                    }
                    for (String selectedParticipantCf : selectedStaffArray) {
                        st = this.connHere().prepareStatement(queryAddSelectedParticipants);
                        st.setString(1, selectedParticipantCf);
                        st.setString(2, newNumGita);
                        st.setInt(3, 0);
                        st.executeUpdate();

                        totParticipants[1]++;
                        System.out.println(totParticipants[1]);
                    }
                } else {
                    try {
                        String currentLast = numGitaArrayList.get(0).getNumGita();
                        newNumGita = "g" + (Integer.parseInt(currentLast.substring(1, currentLast.length())) + 1);
                        System.out.println("new NumGita created");
                        //first add new NumGita to gita
                        st = this.connHere().prepareStatement(queryAddTrip);
                        st.setString(1, departureFrom);
                        st.setString(2, localDateDep);
                        st.setString(3, localDateCom);
                        st.setString(4, staying);
                        st.setString(5, localDateArr);
                        st.setString(6, arrivalTo);
                        st.setString(7, newNumGita);
                        st.executeUpdate();

                        //then add interni_CF into interni_has_gita
                        for (String selectedParticipantCf : selectedChildArray) {
                            st = this.connHere().prepareStatement(queryAddSelectedParticipants);
                            st.setString(1, selectedParticipantCf);
                            st.setString(2, newNumGita);
                            st.setString(3, "0");
                            st.executeUpdate();

                            totParticipants[0]++;
                            System.out.println(totParticipants[0]);
                        }
                        for (String selectedParticipantCf : selectedStaffArray) {
                            st = this.connHere().prepareStatement(queryAddSelectedParticipants);
                            st.setString(1, selectedParticipantCf);
                            st.setString(2, newNumGita);
                            st.setString(3, "0");
                            st.executeUpdate();

                            totParticipants[1]++;
                            System.out.println(totParticipants[1]);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return totParticipants;
    }


    @Override
    public ArrayList<ChildSelectedTripDbDetails> loadTripSelectedChildren (String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        //load children related to the selected trip
        PreparedStatement st = null;
        ResultSet resultNumGita = null;
        ResultSet resultChildren = null;

        System.out.println(selectedDep + ", "+ selectedDepFrom + ", " + selectedAccomodation + ", " + selectedArr + ", " + selectedArrTo + ", " + selectedCom);

        ArrayList<ChildSelectedTripDbDetails> childDbArrayList = new ArrayList<>(3);
        ArrayList<NumGitaDbDetails> numGitaFoundArrayList = new ArrayList<>(1);

        if(selectedArr == null || selectedCom == null || selectedDepFrom == null || selectedDep == null )
            return null;

        String queryFindNumGita = "SELECT NumGita" +
                " FROM gita" +
                " WHERE Partenza ='"+ selectedDepFrom + "' AND DataOraPar ='"+ selectedDep +"' AND DataOraRit ='"+ selectedCom +"' AND Alloggio ='"+ selectedAccomodation +"' AND DataOraArr ='"+ selectedArr +"' AND Destinazione ='"+ selectedArrTo + "';";

        try{
            st = this.connHere().prepareStatement(queryFindNumGita);
            resultNumGita = st.executeQuery(queryFindNumGita);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultNumGita.next() ) {
                System.out.println("No trip in DB");
                return null;
            } else {
                resultNumGita.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultNumGita.next()) {
                        NumGitaDbDetails numGitaFound = new NumGitaDbDetails(resultNumGita.getString(1));
                        numGitaFoundArrayList.add(numGitaFound);
                    }
                    System.out.println(numGitaFoundArrayList.get(0).getNumGita());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String numGita = numGitaFoundArrayList.get(0).getNumGita();

        String queryLoadChildrenParticipant = "SELECT Cognome, Nome, CF" +
                " FROM interni AS I INNER JOIN" +
                " bambino AS B ON (I.CF = B.Interni_CF) INNER JOIN" +
                " interni_has_gita AS IG ON (B.Interni_CF = IG.Interni_CF AND IG.gita_NumGita = '" + numGita + "');";

        try{
            st = this.connHere().prepareStatement(queryLoadChildrenParticipant);
            resultChildren = st.executeQuery(queryLoadChildrenParticipant);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultChildren.next() ) {
                System.out.println("No child in DB");
            } else {
                resultChildren.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultChildren.next()) {
                        ChildSelectedTripDbDetails prova = new ChildSelectedTripDbDetails(resultChildren.getString(1),
                                resultChildren.getString(2),
                                resultChildren.getString(3));
                        childDbArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultChildren != null)
                    resultChildren.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return childDbArrayList;
    }

    @Override
    public ArrayList<StaffSelectedTripDbDetails> loadTripSelectedStaff (String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        //load staff members related to the selected trip
        PreparedStatement st = null;
        ResultSet resultNumGita = null;
        ResultSet resultStaff = null;

        System.out.println(selectedDep + ", "+ selectedDepFrom + ", " + selectedAccomodation + ", " + selectedArr + ", " + selectedArrTo + ", " + selectedCom);

        ArrayList<StaffSelectedTripDbDetails> staffDbArrayList = new ArrayList<>(3);
        ArrayList<NumGitaDbDetails> numGitaFoundArrayList = new ArrayList<>(1);

        String queryFindNumGita = "SELECT NumGita" +
                " FROM gita" +
                " WHERE Partenza ='"+ selectedDepFrom + "' AND DataOraPar ='"+ selectedDep +"' AND DataOraRit ='"+ selectedCom +"' AND Alloggio ='"+ selectedAccomodation +"' AND DataOraArr ='"+ selectedArr +"' AND Destinazione ='"+ selectedArrTo + "';";

        try{
            st = this.connHere().prepareStatement(queryFindNumGita);
            resultNumGita = st.executeQuery(queryFindNumGita);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultNumGita.next() ) {
                System.out.println("No trip in DB");
                return null;
            } else {
                resultNumGita.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultNumGita.next()) {
                        NumGitaDbDetails numGitaFound = new NumGitaDbDetails(resultNumGita.getString(1));
                        numGitaFoundArrayList.add(numGitaFound);
                    }
                    System.out.println(numGitaFoundArrayList.get(0).getNumGita());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String numGita = numGitaFoundArrayList.get(0).getNumGita();

        String queryLoadStaffParticipant = "SELECT Cognome, Nome, CF" +
                " FROM interni AS I INNER JOIN" +
                " personaleint AS PI ON (I.CF = PI.Interni_CF) INNER JOIN" +
                " interni_has_gita AS IG ON (PI.Interni_CF = IG.Interni_CF AND IG.gita_NumGita = '" + numGita + "');";

        try{
            st = this.connHere().prepareStatement(queryLoadStaffParticipant);
            resultStaff = st.executeQuery(queryLoadStaffParticipant);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultStaff.next() ) {
                System.out.println("No staff in DB");
            } else {
                resultStaff.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultStaff.next()) {
                        StaffSelectedTripDbDetails prova = new StaffSelectedTripDbDetails(resultStaff.getString(1),
                                resultStaff.getString(2),
                                resultStaff.getString(3));
                        staffDbArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultStaff != null)
                    resultStaff.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return staffDbArrayList;
    }


    @Override
    public ArrayList<CodRifChildDbDetails> findNotAvailableStaff(ArrayList<String> selectedStaffCf, String selectedTripDep, String selectedTripCom) throws RemoteException{
        PreparedStatement st;
        ResultSet resultStaff = null;
        ArrayList<CodRifChildDbDetails> staffNotAvailableArrayList = new ArrayList<>();
        String[] selectedStaffArray = selectedStaffCf.toArray(new String[selectedStaffCf.size()]);


        //seleziono CF di chi ha Partecipante_effettivo = 1
        // NELLE GITE PER PERIODI SOVRAPPOSTI A QUELLO DATO
        // ed è stato selezionato per la gita corrente
        // -> se ! resultStaff.next() NON posso aggiungere quegli elementi

        String queryFindNotAvailableStaff = "SELECT DISTINCT IG.interni_CF " +
                "FROM interni_has_gita AS IG " +
                "WHERE IG.interni_CF = ? " +
                "AND IG.Partecipante_effettivo = '1' " +
                "AND IG.gita_NumGita IN " +
                "(SELECT DISTINCT G2.NumGita " +
                "FROM gita AS G1, gita AS G2 " +
                "WHERE ((G1.DataOraPar <= G2.DataOraPar AND G2.DataOraPar <= G1.DataOraRit) " +
                "OR (G2.DataOraPar <= G1.DataOraPar AND G1.DataOraPar <= G2.DataOraRit)) " +
                "AND G1.DataOraPar = '"+ selectedTripDep + "' AND G1.DataOraRit = '"+ selectedTripCom + "')";

        System.out.println("Found NOT AVAILABLE staff members? YES/NO");
        for(String staff : selectedStaffArray) {
            try {
                st = this.connHere().prepareStatement(queryFindNotAvailableStaff);
                st.setString(1,staff);
                resultStaff = st.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (!resultStaff.next()) {  //NON ho staff che partecipa a gite sovrapposte
                    System.out.println("NO");
                } else { //HO staff che partecipa a gite sovrapposte
                    System.out.println("YES");
                    resultStaff.beforeFirst();
                    try {
                        while (resultStaff.next()) {
                            CodRifChildDbDetails prova = new CodRifChildDbDetails(resultStaff.getString(1));
                            staffNotAvailableArrayList.add(prova);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(staffNotAvailableArrayList.isEmpty())
            return null;
        return staffNotAvailableArrayList;
    }

    @Override
    public ArrayList<CodRifChildDbDetails> findNotAvailableChild(ArrayList<String> selectedChildCf, String selectedTripDep, String selectedTripCom) throws RemoteException{
        PreparedStatement st;
        ResultSet resultChild = null;
        ArrayList<CodRifChildDbDetails> childNotAvailableArrayList = new ArrayList<>();
        String[] selectedChildArray = selectedChildCf.toArray(new String[selectedChildCf.size()]);

        String queryFindNotAvailableChild = "SELECT DISTINCT IG.interni_CF " +
                "FROM interni_has_gita AS IG " +
                "WHERE IG.interni_CF = ? " +
                "AND IG.Partecipante_effettivo = '1' " +
                "AND IG.gita_NumGita IN " +
                "(SELECT DISTINCT G2.NumGita " +
                "FROM gita AS G1, gita AS G2 " +
                "WHERE ((G1.DataOraPar <= G2.DataOraPar AND G2.DataOraPar <= G1.DataOraRit) " +
                "OR (G2.DataOraPar <= G1.DataOraPar AND G1.DataOraPar <= G2.DataOraRit)) " +
                "AND G1.DataOraPar = '"+ selectedTripDep + "' AND G1.DataOraRit = '"+ selectedTripCom + "')";

        System.out.println("Found NOT AVAILABLE children? YES/NO");
        for(String child : selectedChildArray) {
            try {
                st = this.connHere().prepareStatement(queryFindNotAvailableChild);
                st.setString(1,child);
                resultChild = st.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (!resultChild.next()) {  //NON ho staff che partecipa a gite sovrapposte
                    System.out.println("NO");
                } else { //HO staff che partecipa a gite sovrapposte
                    System.out.println("YES");
                    resultChild.beforeFirst();
                    try {
                        while (resultChild.next()) {
                            CodRifChildDbDetails prova = new CodRifChildDbDetails(resultChild.getString(1));
                            childNotAvailableArrayList.add(prova);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(childNotAvailableArrayList.isEmpty())
            return null;
        return childNotAvailableArrayList;
    }


    @Override
    public int[] howManyActualParticipants (ArrayList<String> selectedChildCf, ArrayList<String> selectedStaffCf) throws RemoteException {
        PreparedStatement st;
        int[] totParticipants = new int[2];
        totParticipants[0] = 0;
        totParticipants[1] = 0;
        String[] selectedChildArray = selectedChildCf.toArray(new String[selectedChildCf.size()]);
        String[] selectedStaffArray = selectedStaffCf.toArray(new String[selectedStaffCf.size()]);

        //se sono in questa funzione è perchè tra quelli selezionati non ho bambini che non possono partecipare -> tutti quelli selezionati per questa gita sono da salvare
        String queryMakeActualChild = "UPDATE interni_has_gita" +
                " SET Partecipante_effettivo = ? " +
                " WHERE interni_CF = ? ";
        String queryMakeActualStaff = "UPDATE interni_has_gita" +
                " SET Partecipante_effettivo = ? " +
                " WHERE interni_CF = ? ";

        for(String child : selectedChildArray){
            System.out.println(child);
            try{
                st = this.connHere().prepareStatement(queryMakeActualChild);
                st.setInt(1, 1);
                st.setObject(2, child);
                st.executeUpdate();

                totParticipants[0] ++;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for(String staff : selectedStaffArray) {
            System.out.println(staff);

            try{
                st = this.connHere().prepareStatement(queryMakeActualStaff);
                st.setInt(1,1);
                st.setObject(2, staff);
                st.executeUpdate();

                totParticipants[1] ++;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return totParticipants;
    }


    @Override
    public HashMap<String, ArrayList<String>> associateBusToParticipants(ArrayList<String> selectedChildCfArrayList, int totChildren, ArrayList<String> selectedStaffCfArrayList, int totStaff,
                                                                         String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation,
                                                                         String selectedArr, String selectedArrTo) throws RemoteException {
        PreparedStatement st = null;
        ResultSet resultNumGita = null;

        System.out.println(selectedDep + ", "+ selectedDepFrom + ", " + selectedAccomodation + ", " + selectedArr + ", " + selectedArrTo + ", " + selectedCom);

        ArrayList<NumGitaDbDetails> numGitaFoundArrayList = new ArrayList<>(1);

        String queryFindNumGita = "SELECT NumGita" +
                " FROM gita" +
                " WHERE Partenza ='"+ selectedDepFrom + "' AND DataOraPar ='"+ selectedDep +"' AND DataOraRit ='"+ selectedCom +"' AND Alloggio ='"+ selectedAccomodation +"' AND DataOraArr ='"+ selectedArr +"' AND Destinazione ='"+ selectedArrTo + "';";
        try{
            st = this.connHere().prepareStatement(queryFindNumGita);
            resultNumGita = st.executeQuery(queryFindNumGita);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultNumGita.next() ) {
                System.out.println("No trip in DB");
                return null;
            } else {
                resultNumGita.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultNumGita.next()) {
                        NumGitaDbDetails numGitaFound = new NumGitaDbDetails(resultNumGita.getString(1));
                        numGitaFoundArrayList.add(numGitaFound);
                    }
                    System.out.println(numGitaFoundArrayList.get(0).getNumGita());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String numGita = numGitaFoundArrayList.get(0).getNumGita();

        String queryFindAvailableBus = "SELECT B.Targa, B.capienza " +
                "FROM bus AS B " +
                "WHERE B.Targa NOT IN " +
                "(SELECT GB.bus_Targa " +
                "FROM gita_has_bus AS GB " +
                "WHERE GB.gita_NumGita IN " +
                "(SELECT DISTINCT G2.NumGita " +
                "FROM gita AS G1, gita AS G2 " +
                "WHERE ((G1.DataOraPar <= G2.DataOraPar AND G2.DataOraPar <= G1.DataOraRit) " +
                "OR (G2.DataOraPar <= G1.DataOraPar AND G1.DataOraPar <= G2.DataOraRit))" +
                "AND G1.DataOraPar = '"+ selectedDep +"' AND G1.DataOraRit = '"+ selectedCom +"'))" +
                "ORDER BY B.capienza ASC";

        HashMap<String, ArrayList<String>> busToPartecipantHashMap = new HashMap<>();
        ResultSet resultBus = null;
        ArrayList<BusPlateCapacityDbDetails> busAvailableArrayList = new ArrayList<>(2);
        int totParticipants = totChildren + totStaff;
        ArrayList<String> everyParticipantArrayList = new ArrayList<>();
        everyParticipantArrayList.addAll(selectedChildCfArrayList);
        everyParticipantArrayList.addAll(selectedStaffCfArrayList);

        for(String cf : everyParticipantArrayList){
            System.out.println(cf + " has to be assigned");
        }

        try {
            st = this.connHere().prepareStatement(queryFindAvailableBus);
            resultBus = st.executeQuery(queryFindAvailableBus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (!resultBus.next()) {
                System.out.println("No buses available for this period.");
                return null; // null == ERRORE : NON HO ABBASTANZA BUS ASSEGNABILI
            } else {
                System.out.println("Found buses available.");
                resultBus.beforeFirst();
                try {
                    while (resultBus.next()) {
                        BusPlateCapacityDbDetails prova = new BusPlateCapacityDbDetails(resultBus.getString(1),
                                resultBus.getString(2));
                        busAvailableArrayList.add(prova);
                        System.out.println("This bus is available: " + prova );
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String queryAddToInterniIsHere = "INSERT INTO interni_is_here (interni_CF, gita_NumGita, bus_Targa, is_here) VALUES (?,?,?,?)";


        for(int i=0; i < busAvailableArrayList.size(); i++){  //until the last bus in AL
            String capienzaPerBusString = busAvailableArrayList.get(i).getCapacity();  //get capacity of the bus
            int capienzaPerBus = Integer.parseInt(capienzaPerBusString);        //convert capacity to int
            String plateString = busAvailableArrayList.get(i).getPlate();

            if(capienzaPerBus >= totParticipants) {      //if capacity >= totPart -> save that bus, we're done.
                //everyParticipant goes on that bus -> save in hashmap <plate,everyoneAL<String>>
                busToPartecipantHashMap.put(plateString, everyParticipantArrayList);

                //make bus mine
                String queryMakeBusMine = "INSERT INTO gita_has_bus (gita_NumGita, bus_Targa) VALUES(?,?)";
                try {
                    st = this.connHere().prepareStatement(queryMakeBusMine);
                    st.setString(1, numGita);
                    st.setString(2, plateString);
                    st.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (st != null)
                            st.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //add participant and related bus for every participant in interni_is_here
                for (String who : everyParticipantArrayList) {
                    try {
                        st = this.connHere().prepareStatement(queryAddToInterniIsHere);
                        st.setString(1, who);
                        st.setString(2, numGita);
                        st.setString(3, plateString);
                        st.setInt(4, 0);
                        st.executeUpdate();
                        System.out.println("saved in interni_is_here");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            if (st != null)
                                st.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                //quando ho trovato il bus giusto, esco
                break;
            }

        }
        //se arrivo a fine busAvailableAL senza trovare un bus, quindi se hashmap.isempty() -> salvo bus da fine AL tornando indietro, finchè
        // o trovo che, arrivato a inizio AL, ho ancora dei partecipanti non assegnati -> bus non sufficienti -> ERR
        // o trovo bus per tutte le persone (continuo ricerca finchè totPart - capienza > 0 )

        if(busToPartecipantHashMap.isEmpty()){
            System.out.println("Found no bus with dimension >= than that needed. Searching...");
            ArrayList<String> participantsOnBus = new ArrayList<>();
            int capienzaTot = 0;
            int pos = 0;
            int k = everyParticipantArrayList.size() -1;
            //finchè non ho ripercorso tutti i bus dall'ultimo al primo (cioè da quello con capacità maggiore, DESC)
            for(int i = busAvailableArrayList.size() -1 ; i > 0; i--){
                //se ho ancora partecipanti allora continuo, assegnando persone su un nuovo bus
                if (totParticipants > 0) {
                    String capienzaPerBusString = busAvailableArrayList.get(i).getCapacity();  //get capacity of the bus
                    int capienzaPerBus = Integer.parseInt(capienzaPerBusString);        //convert capacity to int
                    String plateString = busAvailableArrayList.get(i).getPlate();

                    capienzaTot += capienzaPerBus;  //sommo alla capienza totale la capienza di questo bus
                    System.out.println(capienzaTot);

                    //aggiungo persona a bus finchè restano posti disponibili sul bus

                    // make bus mine
                    String queryMakeBusMine = "INSERT INTO gita_has_bus (gita_NumGita, bus_Targa) VALUES(?,?)";
                    try {
                        st = this.connHere().prepareStatement(queryMakeBusMine);
                        st.setString(1, numGita);
                        st.setString(2, plateString);
                        st.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (st != null)
                                st.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    for (int numSeat = capienzaPerBus; numSeat > 0; numSeat--) {
                        if (totParticipants > 0) {//se ho ancora partecipanti
                            //salvo in AL chi sta su quel bus (da ultimo in everyParticipantAL a primo)
                            participantsOnBus.add(pos, everyParticipantArrayList.get(k));
                            System.out.println(participantsOnBus.get(pos) + " added to participants on bus");

                            //aggiungo la persona interni_is_here, assegnandola al bus su cui dovrebbe trovarsi (per ogni persona assegnabile a quel bus)
                            try {
                                st = this.connHere().prepareStatement(queryAddToInterniIsHere);
                                st.setString(1, participantsOnBus.get(pos));
                                st.setString(2, numGita);
                                st.setString(3, plateString);
                                st.setInt(4, 0);
                                st.executeUpdate();
                                System.out.println("& is now in interni_is_here");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            //infine elimino quella persona da everyPartAL
                            Iterator<String> it = everyParticipantArrayList.iterator();
                            while (it.hasNext()) {
                                if (it.next().equals(everyParticipantArrayList.get(k))) {
                                    it.remove();
                                    break;
                                }
                            }
                            for(String cf : everyParticipantArrayList){
                                System.out.println(cf +" still here");
                            }

                            pos++;
                            k--;
                        }

                        totParticipants --;
                        busToPartecipantHashMap.put(plateString, participantsOnBus);
                    }
                }
            }
            if(totParticipants - capienzaTot > 0){ //se esco dal for e ho ancora partecipanti non assegnati a bus
                return null;
            }
        }

        return busToPartecipantHashMap;
    }


    @Override
    public ArrayList<ChildSelectedTripDbDetails> loadWhoTrip (String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        //load children & staff that are effective participants related to the selected trip
        PreparedStatement st = null;
        ResultSet resultNumGita = null;
        ResultSet resultParticipants = null;

        ArrayList<ChildSelectedTripDbDetails> participantsArrayList = new ArrayList<>(3);
        ArrayList<NumGitaDbDetails> numGitaFoundArrayList = new ArrayList<>(1);
        if(selectedDep == null || selectedDepFrom == null || selectedArr == null || selectedArrTo == null )
            return null;
        String queryFindNumGita = "SELECT NumGita" +
                " FROM gita" +
                " WHERE Partenza ='"+ selectedDepFrom + "' AND DataOraPar ='"+ selectedDep +"' AND DataOraRit ='"+ selectedCom +"' AND Alloggio ='"+ selectedAccomodation +"' AND DataOraArr ='"+ selectedArr +"' AND Destinazione ='"+ selectedArrTo + "';";

        try{
            st = this.connHere().prepareStatement(queryFindNumGita);
            resultNumGita = st.executeQuery(queryFindNumGita);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultNumGita.next() ) {
                System.out.println("No trip in DB");
                return null;
            } else {
                resultNumGita.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultNumGita.next()) {
                        NumGitaDbDetails numGitaFound = new NumGitaDbDetails(resultNumGita.getString(1));
                        numGitaFoundArrayList.add(numGitaFound);
                    }
                    System.out.println(numGitaFoundArrayList.get(0).getNumGita());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String numGita = numGitaFoundArrayList.get(0).getNumGita();

        String queryLoadParticipants = "SELECT Cognome, Nome, CF" +
                " FROM interni AS I INNER JOIN" +
                " interni_has_gita AS IG ON (I.CF = IG.Interni_CF AND IG.gita_NumGita = '" + numGita + "')" +
                " WHERE Partecipante_effettivo = '1'";

        try{
            st = this.connHere().prepareStatement(queryLoadParticipants);
            resultParticipants = st.executeQuery(queryLoadParticipants);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultParticipants.next() ) {
                System.out.println("No participant in DB");
            } else {
                resultParticipants.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultParticipants.next()) {
                        ChildSelectedTripDbDetails prova = new ChildSelectedTripDbDetails(resultParticipants.getString(1),
                                resultParticipants.getString(2),
                                resultParticipants.getString(3));
                        participantsArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultParticipants != null)
                    resultParticipants.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return participantsArrayList;
    }


    @Override
    public ArrayList<CodRifChildDbDetails> loadBusTrip (String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        //load buses related to the selected trip
        PreparedStatement st = null;
        ResultSet resultNumGita = null;
        ResultSet resultBus = null;

        ArrayList<CodRifChildDbDetails> busArrayList = new ArrayList<>(1);
        ArrayList<NumGitaDbDetails> numGitaFoundArrayList = new ArrayList<>(1);

        String queryFindNumGita = "SELECT NumGita" +
                " FROM gita" +
                " WHERE Partenza ='"+ selectedDepFrom + "' AND DataOraPar ='"+ selectedDep +"' AND DataOraRit ='"+ selectedCom +"' AND Alloggio ='"+ selectedAccomodation +"' AND DataOraArr ='"+ selectedArr +"' AND Destinazione ='"+ selectedArrTo + "';";

        try{
            st = this.connHere().prepareStatement(queryFindNumGita);
            resultNumGita = st.executeQuery(queryFindNumGita);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultNumGita.next() ) {
                System.out.println("No trip in DB");
                return null;
            } else {
                resultNumGita.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultNumGita.next()) {
                        NumGitaDbDetails numGitaFound = new NumGitaDbDetails(resultNumGita.getString(1));
                        numGitaFoundArrayList.add(numGitaFound);
                    }
                    System.out.println(numGitaFoundArrayList.get(0).getNumGita());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String numGita = numGitaFoundArrayList.get(0).getNumGita();

        String queryLoadBus = "SELECT bus_Targa " +
                "FROM gita_has_bus " +
                "WHERE gita_NumGita = '" + numGita +"';";

        try{
            st = this.connHere().prepareStatement(queryLoadBus);
            resultBus = st.executeQuery(queryLoadBus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultBus.next() ) {
                System.out.println("No bus in DB");
            } else {
                resultBus.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultBus.next()) {
                        CodRifChildDbDetails prova = new CodRifChildDbDetails(resultBus.getString(1));
                        busArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultBus != null)
                    resultBus.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return busArrayList;
    }


    @Override
    public ArrayList<SolutionDbDetails> loadSolution (String selectedTripDepFrom, String selectedTripDep, String selectedTripCom, String selectedTripAccomodation, String selectedTripArr, String selectedTripArrTo) throws RemoteException{
        //load children & staff that are effective participants related to the selected trip
        //AND bus connected to each of them
        PreparedStatement st = null;
        ResultSet resultNumGita = null;
        ResultSet resultParticipantsAndBus = null;

        ArrayList<SolutionDbDetails> participantsAndBusArrayList = new ArrayList<>(4);
        ArrayList<NumGitaDbDetails> numGitaFoundArrayList = new ArrayList<>(1);

        String queryFindNumGita = "SELECT NumGita" +
                " FROM gita" +
                " WHERE Partenza ='"+ selectedTripDepFrom + "' AND DataOraPar ='"+ selectedTripDep +"' AND DataOraRit ='"+ selectedTripCom +"' AND Alloggio ='"+ selectedTripAccomodation +"' AND DataOraArr ='"+ selectedTripArr +"' AND Destinazione ='"+ selectedTripArrTo + "';";

        try{
            st = this.connHere().prepareStatement(queryFindNumGita);
            resultNumGita = st.executeQuery(queryFindNumGita);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultNumGita.next() ) {
                System.out.println("No trip in DB");
                return null;
            } else {
                resultNumGita.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultNumGita.next()) {
                        NumGitaDbDetails numGitaFound = new NumGitaDbDetails(resultNumGita.getString(1));
                        numGitaFoundArrayList.add(numGitaFound);
                    }
                    System.out.println(numGitaFoundArrayList.get(0).getNumGita());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String numGita = numGitaFoundArrayList.get(0).getNumGita();

        String queryLoadParticipantsAndBus = "SELECT I.Nome, I.Cognome, I.CF, GB.bus_Targa " +
                " FROM interni AS I INNER JOIN" +
                " interni_is_here AS IH ON (I.CF = IH.interni_CF) INNER JOIN" +
                " gita_has_bus AS GB ON (IH.bus_Targa = GB.bus_Targa AND GB.gita_NumGita = '" + numGita + "')" +
                " WHERE IH.gita_NumGita = '" + numGita + "';";

        try{
            st = this.connHere().prepareStatement(queryLoadParticipantsAndBus);
            resultParticipantsAndBus = st.executeQuery(queryLoadParticipantsAndBus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultParticipantsAndBus.next() ) {
                System.out.println("No participant in DB");
            } else {
                resultParticipantsAndBus.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultParticipantsAndBus.next()) {
                        SolutionDbDetails prova = new SolutionDbDetails(resultParticipantsAndBus.getString(1),
                                resultParticipantsAndBus.getString(2),
                                resultParticipantsAndBus.getString(3),
                                resultParticipantsAndBus.getString(4));
                        participantsAndBusArrayList.add(prova);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultParticipantsAndBus != null)
                    resultParticipantsAndBus.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return participantsAndBusArrayList;

    }


    @Override
    public void makeIsHereFalse(String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException{
        PreparedStatement st = null;
        ResultSet resultNumGita = null;
        ArrayList<NumGitaDbDetails> numGitaFoundArrayList = new ArrayList<>();
        String queryFindNumGita = "SELECT NumGita" +
                " FROM gita" +
                " WHERE Partenza ='"+ selectedDepFrom + "' AND DataOraPar ='"+ selectedDep +"' AND DataOraRit ='"+ selectedCom +"' AND Alloggio ='"+ selectedAccomodation +"' AND DataOraArr ='"+ selectedArr +"' AND Destinazione ='"+ selectedArrTo + "';";

        try{
            st = this.connHere().prepareStatement(queryFindNumGita);
            resultNumGita = st.executeQuery(queryFindNumGita);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultNumGita.next() ) {
                System.out.println("No trip in DB");
            } else {
                resultNumGita.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultNumGita.next()) {
                        NumGitaDbDetails numGitaFound = new NumGitaDbDetails(resultNumGita.getString(1));
                        numGitaFoundArrayList.add(numGitaFound);
                    }
                    System.out.println(numGitaFoundArrayList.get(0).getNumGita());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String numGita = numGitaFoundArrayList.get(0).getNumGita();

        String queryEditIsHereMakeFalse = "UPDATE interni_is_here" +
                " SET is_here = '0'" +
                " WHERE gita_NumGita = '" + numGita +"';";
        try{
            st = this.connHere().prepareStatement(queryEditIsHereMakeFalse);
            st.executeUpdate();
            System.out.println("is_here for this trip set to 0");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public ArrayList<String> findParticipantOnWrongBus(ArrayList<String> selectedChildCfArrayList, String selectedBus, String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        PreparedStatement st = null;
        ResultSet resultNumGita = null;
        ArrayList<NumGitaDbDetails> numGitaFoundArrayList = new ArrayList<>();
        String queryFindNumGita = "SELECT NumGita" +
                " FROM gita" +
                " WHERE Partenza ='"+ selectedDepFrom + "' AND DataOraPar ='"+ selectedDep +"' AND DataOraRit ='"+ selectedCom +"' AND Alloggio ='"+ selectedAccomodation +"' AND DataOraArr ='"+ selectedArr +"' AND Destinazione ='"+ selectedArrTo + "';";

        try{
            st = this.connHere().prepareStatement(queryFindNumGita);
            resultNumGita = st.executeQuery(queryFindNumGita);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultNumGita.next() ) {
                System.out.println("No trip in DB");
                return null;
            } else {
                resultNumGita.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultNumGita.next()) {
                        NumGitaDbDetails numGitaFound = new NumGitaDbDetails(resultNumGita.getString(1));
                        numGitaFoundArrayList.add(numGitaFound);
                    }
                    System.out.println(numGitaFoundArrayList.get(0).getNumGita());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String numGita = numGitaFoundArrayList.get(0).getNumGita();

        ResultSet resultPeopleOnBus = null;
        ArrayList<CodRifChildDbDetails> peopleOnBusArrayList = new ArrayList<>();
        ArrayList<String> correctArrayList = new ArrayList<>();

        String queryFindPeopleOnThisBus = "SELECT interni_CF" +
                " FROM interni_is_here" +
                " WHERE bus_Targa = '"+ selectedBus +"'" +
                " AND gita_NumGita = '"+numGita +"';";

        try{
            st = this.connHere().prepareStatement(queryFindPeopleOnThisBus);
            resultPeopleOnBus = st.executeQuery(queryFindPeopleOnThisBus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultPeopleOnBus.next() ) {
                System.out.println("No people for this trip in DB");
                return null;
            } else {
                resultPeopleOnBus.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultPeopleOnBus.next()) {
                        CodRifChildDbDetails personCf = new CodRifChildDbDetails(resultPeopleOnBus.getString(1));
                        peopleOnBusArrayList.add(personCf);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(String child : selectedChildCfArrayList){
            for(CodRifChildDbDetails childOnBus : peopleOnBusArrayList){
                if(child.equals(childOnBus.getCodRif())){
                    correctArrayList.add(child);
                }
            }
        }

        //delete from who is here who should be on the bus to find who is on the wrong bus -> null or someone, I return -> Controller will highlight
        selectedChildCfArrayList.removeAll(correctArrayList);

        if(selectedChildCfArrayList.isEmpty()){
            System.out.println("Empty arrayList. Return null for findParticipantsOnWrongBus");
            return null;
        }

        return selectedChildCfArrayList;  //contains now just who shouldn't be here

    }


    @Override
    public ArrayList<String> findMissingParticipantsOnThisBus(ArrayList<String> participantOnWrongBusArrayList, ArrayList<String> selectedChildCfArrayList, String selectedBus, String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException {
        PreparedStatement st = null;
        ResultSet resultNumGita = null;
        ArrayList<NumGitaDbDetails> numGitaFoundArrayList = new ArrayList<>();
        String queryFindNumGita = "SELECT NumGita" +
                " FROM gita" +
                " WHERE Partenza ='"+ selectedDepFrom + "' AND DataOraPar ='"+ selectedDep +"' AND DataOraRit ='"+ selectedCom +"' AND Alloggio ='"+ selectedAccomodation +"' AND DataOraArr ='"+ selectedArr +"' AND Destinazione ='"+ selectedArrTo + "';";

        try{
            st = this.connHere().prepareStatement(queryFindNumGita);
            resultNumGita = st.executeQuery(queryFindNumGita);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultNumGita.next() ) {
                System.out.println("No trip in DB");
                return null;
            } else {
                resultNumGita.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultNumGita.next()) {
                        NumGitaDbDetails numGitaFound = new NumGitaDbDetails(resultNumGita.getString(1));
                        numGitaFoundArrayList.add(numGitaFound);
                    }
                    System.out.println(numGitaFoundArrayList.get(0).getNumGita());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String numGita = numGitaFoundArrayList.get(0).getNumGita();

        ResultSet resultPeopleOnBus = null;
        ArrayList<CodRifChildDbDetails> peopleOnBusArrayList = new ArrayList<>();

        String queryFindPeopleOnThisBus = "SELECT interni_CF" +
                " FROM interni_is_here" +
                " WHERE bus_Targa = '"+ selectedBus +"'" +
                " AND gita_NumGita = '"+numGita +"';";

        try{
            st = this.connHere().prepareStatement(queryFindPeopleOnThisBus);
            resultPeopleOnBus = st.executeQuery(queryFindPeopleOnThisBus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultPeopleOnBus.next() ) {
                System.out.println("No people for this trip in DB");
            } else {
                resultPeopleOnBus.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultPeopleOnBus.next()) {
                        CodRifChildDbDetails personCf = new CodRifChildDbDetails(resultPeopleOnBus.getString(1));
                        peopleOnBusArrayList.add(personCf);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(participantOnWrongBusArrayList != null) {
            selectedChildCfArrayList.removeAll(participantOnWrongBusArrayList);
            for (String notWrong : selectedChildCfArrayList) {
                System.out.println(notWrong + " is not wrong");
            }
        }
        ArrayList<String> notMissingArrayList = new ArrayList<>();
        ArrayList<String> peopleOnBusArrayListString = new ArrayList<>();
        for (CodRifChildDbDetails object : peopleOnBusArrayList) {
            peopleOnBusArrayListString.add(Objects.toString(object.getCodRif(), null));
        }

        for (String childOnBus : peopleOnBusArrayListString) {
            System.out.println("NEXT who should be on is " + childOnBus);
            for (String childNotWrong : selectedChildCfArrayList) {
                System.out.println("searching...");
                if (childNotWrong.equals(childOnBus)) {
                    notMissingArrayList.add(childNotWrong);
                    System.out.println("And here he/she is!");
                }
            }
        }

        //delete from who is here who is selected (not missing) to find who is on the wrong bus -> null or someone, I return -> Controller will highlight
        peopleOnBusArrayListString.removeAll(notMissingArrayList);
        for (String voila : peopleOnBusArrayListString) {
            System.out.println(voila + " IS NOT HERE! HELP!");
        }

        if(peopleOnBusArrayListString.isEmpty())
            return null;

        return peopleOnBusArrayListString;   //contains now just who is missing

    }


    @Override
    public void makeIsHereTrue (String selectedBus, String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException{
        PreparedStatement st = null;
        ResultSet resultNumGita = null;
        ArrayList<NumGitaDbDetails> numGitaFoundArrayList = new ArrayList<>();
        String queryFindNumGita = "SELECT NumGita" +
                " FROM gita" +
                " WHERE Partenza ='"+ selectedDepFrom + "' AND DataOraPar ='"+ selectedDep +"' AND DataOraRit ='"+ selectedCom +"' AND Alloggio ='"+ selectedAccomodation +"' AND DataOraArr ='"+ selectedArr +"' AND Destinazione ='"+ selectedArrTo + "';";

        try{
            st = this.connHere().prepareStatement(queryFindNumGita);
            resultNumGita = st.executeQuery(queryFindNumGita);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultNumGita.next() ) {
                System.out.println("No trip in DB");
            } else {
                resultNumGita.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultNumGita.next()) {
                        NumGitaDbDetails numGitaFound = new NumGitaDbDetails(resultNumGita.getString(1));
                        numGitaFoundArrayList.add(numGitaFound);
                    }
                    System.out.println(numGitaFoundArrayList.get(0).getNumGita());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String numGita = numGitaFoundArrayList.get(0).getNumGita();

        String queryEditIsHereMakeTrue = "UPDATE interni_is_here" +
                " SET is_here = '1'" +
                " WHERE gita_NumGita = '" + numGita +"'" +
                " AND bus_Targa = '" + selectedBus + "';";
        try{
            st = this.connHere().prepareStatement(queryEditIsHereMakeTrue);
            st.executeUpdate();
            System.out.println("is_here for this trip set to 0");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public ArrayList<ChildSelectedTripDbDetails> loadMissing (ArrayList<String> missingCf, String selectedBus, String selectedDepFrom, String selectedDep, String selectedCom, String selectedAccomodation, String selectedArr, String selectedArrTo) throws RemoteException{
        PreparedStatement st = null;
        ResultSet resultNumGita = null;
        ResultSet resultParticipants = null;

        ArrayList<ChildSelectedTripDbDetails> missingParticipantsArrayList = new ArrayList<>(3);
        ArrayList<NumGitaDbDetails> numGitaFoundArrayList = new ArrayList<>(1);

        String queryFindNumGita = "SELECT NumGita" +
                " FROM gita" +
                " WHERE Partenza ='"+ selectedDepFrom + "' AND DataOraPar ='"+ selectedDep +"' AND DataOraRit ='"+ selectedCom +"' AND Alloggio ='"+ selectedAccomodation +"' AND DataOraArr ='"+ selectedArr +"' AND Destinazione ='"+ selectedArrTo + "';";

        try{
            st = this.connHere().prepareStatement(queryFindNumGita);
            resultNumGita = st.executeQuery(queryFindNumGita);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !resultNumGita.next() ) {
                System.out.println("No trip in DB");
                return null;
            } else {
                resultNumGita.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (resultNumGita.next()) {
                        NumGitaDbDetails numGitaFound = new NumGitaDbDetails(resultNumGita.getString(1));
                        numGitaFoundArrayList.add(numGitaFound);
                    }
                    System.out.println(numGitaFoundArrayList.get(0).getNumGita());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultNumGita != null)
                    resultNumGita.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (st != null)
                    st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String numGita = numGitaFoundArrayList.get(0).getNumGita();



        String queryLoadParticipants = "SELECT I.Cognome, I.Nome, I.CF" +
                " FROM interni AS I INNER JOIN" +
                " interni_is_here AS IH ON (I.CF = IH.interni_CF AND IH.gita_NumGita = '" + numGita + "')" +
                " WHERE IH.interni_CF = ? ";

        for(String missing : missingCf) {
            try {
                st = this.connHere().prepareStatement(queryLoadParticipants);
                st.setString(1, missing);
                resultParticipants = st.executeQuery();
                System.out.println("No one saw " + resultParticipants);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (!resultParticipants.next()) {
                    System.out.println("No missing participant found");
                } else {
                    resultParticipants.beforeFirst();
                    System.out.println("Processing ResultSet");
                    try {
                        while (resultParticipants.next()) {
                            ChildSelectedTripDbDetails prova = new ChildSelectedTripDbDetails(resultParticipants.getString(1),
                                    resultParticipants.getString(2),
                                    resultParticipants.getString(3));
                            missingParticipantsArrayList.add(prova);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return missingParticipantsArrayList;

    }

//USEFUL EVERYWHERE------------------------------------------------------------------------------

    public String sendMessage(String clientMessage) {
        return "Client Message".equals(clientMessage) ? "Server Message" : null;
    }


    public Connection connHere(){

        Database receivedCon = new Database();
        Connection connectionOK = receivedCon.databaseCon();
        if(connectionOK != null)
            System.out.println("Connection successful");
        else
            System.out.println("Connection failed");

        return connectionOK;

    }

}