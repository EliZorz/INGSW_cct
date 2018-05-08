package application.rmi.server;

import application.Interfaces.UserRemote;
import application.contr.Database;
import application.details.*;
import application.details.ChildDbDetails;
import com.mysql.jdbc.Connection;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by ELISA on 21/03/2018.
 */
public class ServerImpl extends UnicastRemoteObject implements UserRemote {  //socket e rmi usano entrambe questa implementazione


    public ServerImpl() throws RemoteException {
        super();
    }


//LOGIN----------------------------------------------------------------------------------
    @Override
    public boolean funzLog (String usr, String pwd){

        PreparedStatement st = null;

        ResultSet result = null;

        String queryLog = "SELECT * FROM project.UserIn WHERE Username = ? AND Password = ? ";//"SELECT * FROM sys.login WHERE Username = ? AND Password = ? " ;

        boolean res = false;

        try{

            st = this.connHere().prepareStatement(queryLog);
            st.setString(1, usr);
            st.setString(2, pwd);

            result = st.executeQuery();


        } catch (SQLException e) {
            System.out.println("Error during search in DB");
            e.printStackTrace();
        }


        try{
            if( !result.next() ) {
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


//CHILDREN---------------------------------------------------------------------------------------
    @Override
    public ArrayList<ChildDbDetails> loadData() throws RemoteException {

        PreparedStatement st = null;

        ResultSet result = null;

        ArrayList<ChildDbDetails> childDbArrayList = new ArrayList<>(9);

        String queryLoad = "SELECT Cognome, Nome, CF, DataNascita, CittaNascita, Residenza, Indirizzo, CAP, Provincia" +
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
        String queryLastCodRif = "SELECT MAX(CodRif) FROM bambino";  //select last CodRif inserted
        String queryAddCf = "INSERT INTO bambino(CodRif, Interni_CF) VALUES (?,?)";
        String queryAddFirstContact = "INSERT INTO adulto(Cognome, Nome, CF, Mail, Tel, DataNascita, CittaNascita, Indirizzo, CAP, Provincia, Pediatra, Tutore, Contatto, Bambino_CodRif)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String newCod = null;
        ArrayList<CodRifChildDbDetails> codRifArrayList = new ArrayList<>(1);

        ResultSet result = null;

        //divide items from arraylist selectedAllergy into string to put into database
        StringBuilder allAllergies = new StringBuilder();
        if(! selectedAllergy.isEmpty()){
            for(String s : selectedAllergy){
                allAllergies.append(selectedAllergy.toString()+ ", ");
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

        try {
            /*  assign code number to new child :
            **    1) query to select bambino.CodRif from interni inner join bambino on CF
            **    2) save CodRif in list
            **    3) pick latest element list and ++
            **    4) save new number in list, associate it with new child's CF
            */
            st = this.connHere().prepareStatement(queryLastCodRif);
            result = st.executeQuery(queryLastCodRif);

        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            if( !result.next() ) {
                System.out.println("No CodRif in DB");
                //then new child's CodRif is 1
                newCod = "c1";

                //add to DB
                st = this.connHere().prepareStatement(queryAddCf);
                st.setString(1, newCod);
                st.setString(2, cf);
                st.executeUpdate();

            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");

                try {
                    while (result.next()) {
                        CodRifChildDbDetails lastCod = new CodRifChildDbDetails(result.getString(1));
                        codRifArrayList.add(lastCod);
                    }

                    String currentLast = codRifArrayList.get(0).getCodRif();
                    newCod = "c" + (Integer.parseInt(currentLast.substring(1, currentLast.length()))+1);
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

        return true;
    }


    @Override
    public boolean deleteChild(String oldcf) throws RemoteException{
        PreparedStatement st = null;

        String queryDelete = "DELETE FROM interni WHERE CF = '" + oldcf + "';";

        //NOTA: CANCELLANDO CODRIF, NON VANNO RIFORMATTATI I CODRIF SUCCESSIVI (come al Poli le matricole non sono modificate una volta che altri si laureano)

        try{
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

        //divide items from arraylist selectedAllergy into string to put into database
        StringBuilder allAllergies = new StringBuilder();
        if(! selectedAllergy.isEmpty()){
            for(String s : selectedAllergy){
                allAllergies.append(selectedAllergy.toString()+ ", ");
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

//ALLERGIES----------------------------------------------------------------------------
    @Override
    public ArrayList<IngredientsDbDetails> loadIngr() throws RemoteException {
        PreparedStatement st = null;

        ResultSet result = null;

        ArrayList<IngredientsDbDetails> ingrArrayList = new ArrayList<>(1);

        String queryLoad = "SELECT ingredient " +
                "FROM ingredients INNER JOIN fornitore " +
                "ON ingredients.Fornitore_PIVA = fornitore.PIVA";

        try{
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try{
            if( !result.next() ) {
                System.out.println("No ingredient in DB");

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

        try{
            st = this.connHere().prepareStatement(queryLoadContacts);
            result = st.executeQuery(queryLoadContacts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if( !result.next() ) {
                System.out.println("No contact in DB");
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
    public boolean addContact (ArrayList<String> selectedChild, String surname, String name, String cf, String mail, String tel, LocalDate birthday, String bornWhere, String address, String cap, String province, boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
        PreparedStatement st = null;

        String querySearchCodRif = "SELECT CodRif" +
                                    " FROM bambino" +
                                    " WHERE bambino.Interni_CF = '" + selectedChild.get(2) + "';";
        ResultSet result;
        String prova = null;

        String queryAddContact = "INSERT INTO adulto(Cognome, Nome, CF, Mail, Tel, DataNascita, CittaNascita, Indirizzo, CAP, Provincia, Pediatra, Tutore, Contatto, Bambino_CodRif)" +
                                    " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            //search CodRif of the selected child, to add it to db in Adulto
            st = this.connHere().prepareStatement(querySearchCodRif);
            result = st.executeQuery(querySearchCodRif);

            if( !result.next() ) {
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
    public boolean deleteContact(String oldcfContact) throws RemoteException {
        PreparedStatement st = null;

        String queryDelete = "DELETE FROM adulto WHERE CF = '" + oldcfContact + "';";
        try{
            st = this.connHere().prepareStatement(queryDelete);
            st.executeUpdate(queryDelete);
            System.out.println("Deleted from adulto.");

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
    public boolean updateContact(String name, String surname, String oldcf, String cf, String mail, String tel, LocalDate bornOn, String bornWhere, String address, String cap, String province, int isDoc, int isGuardian, int isContact) throws RemoteException{
        PreparedStatement st = null;

        //NOTA: Bambino_CodRif NON VIENE MODIFICATO IN UPDATE!
        String queryEdit = "UPDATE adulto SET Cognome ='" + surname + "', Nome ='" + name + "', CF ='" + cf + "', " +
                "Mail = '" + mail + "', Tel = '" + tel + "', DataNascita ='" + Date.valueOf(bornOn) + "', CittaNascita='" + bornWhere + "', " +
                "Indirizzo='" + address + "', CAP='" + cap + "', Provincia='" + province + "', Pediatra='" + isDoc + "', " +
                "Tutore = '" + isGuardian + "', Contatto = '" + isContact + "' " +
                "WHERE CF = '" + oldcf + "';";

        try {
            st = this.connHere().prepareStatement(queryEdit);
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


//STAFF---------------------------------------------------------------------------------------
    @Override
    public ArrayList<StaffDbDetails> loadDataStaff() throws RemoteException {

        PreparedStatement st = null;

        ResultSet result = null;

        ArrayList<StaffDbDetails> staffDbArrayList = new ArrayList<>(10);

        String queryLoad = "SELECT Cognome, Nome, CF, Mail, DataNascita, CittaNascita, Residenza, Indirizzo, CAP, Provincia" +
                " FROM interni INNER JOIN personaleint" +
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
        String queryLastCodRif = "SELECT MAX(CodID) FROM personaleint";  //select last CodRif inserted
        String queryAddCf = "INSERT INTO personaleint(Mail, CodID, Interni_CF) VALUES (?,?,?)";

        ArrayList<CodRifChildDbDetails> codRifArrayList = new ArrayList<>(1);

        ResultSet result = null;

        //divide items from arraylist selectedAllergy into string to put into database
        StringBuilder allAllergies = new StringBuilder();
        if(! selectedAllergy.isEmpty()){
            for(String s : selectedAllergy){
                allAllergies.append(selectedAllergy.toString()+ ", ");
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

        try {
            //  assign code number to new staff member
            st = this.connHere().prepareStatement(queryLastCodRif);
            result = st.executeQuery(queryLastCodRif);

        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            if( !result.next() ) {
                System.out.println("No CodID in DB");
                //then new CodID is 1
                String newCod = "s1";

                //add to DB
                st = this.connHere().prepareStatement(queryAddCf);
                st.setString(1, mail);
                st.setString(2, newCod);
                st.setString(3, cf);
                st.executeUpdate();

            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");

                try {
                    while (result.next()) {
                        CodRifChildDbDetails lastCod = new CodRifChildDbDetails(result.getString(1));
                        codRifArrayList.add(lastCod);
                    }

                    String currentLast = codRifArrayList.get(0).getCodRif();
                    String newCod = "s" + (Integer.parseInt(currentLast.substring(1, currentLast.length()))+1);
                    st = this.connHere().prepareStatement(queryAddCf);
                    st.setString(1, mail);
                    st.setString(2, newCod);
                    st.setString(3, cf);
                    st.executeUpdate();

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

        return true;
    }



    @Override
    public boolean deleteStaff(String cf) throws RemoteException{
        PreparedStatement st = null;

        String queryDelete = "DELETE FROM interni WHERE CF = '" + cf + "';";
        String queryDeleteCodID = "DELETE FROM personaleint WHERE Interni_CF = '" + cf + "';";

        //NOTA: CANCELLANDO CODRIF, NON VANNO RIFORMATTATI I CODRIF SUCCESSIVI (come al Poli le matricole non sono modificate una volta che altri si laureano)

        try{
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

        //divide items from arraylist selectedAllergy into string to put into database
        StringBuilder allAllergies = new StringBuilder();
        if (!selectedAllergy.isEmpty()) {
            for (String s : selectedAllergy) {
                allAllergies.append(selectedAllergy.toString() + ", ");
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
                        //get string from db, put into list of ChildGuiData, ready to put it into GUI
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
    public boolean deleteSupplier(String piva) throws RemoteException{
        PreparedStatement st = null;
        String queryDelete = "DELETE FROM fornitore WHERE PIVA = '" + piva + "';";

        try{
            st = this.connHere().prepareStatement(queryDelete);
            st.executeUpdate(queryDelete);
            System.out.println("Deleted from fornitore.");
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


//MENU BASE---------------------------------------------------------------------------------------

        @Override
    public ArrayList<DishesDbDetails> loadMenu() throws RemoteException {
        PreparedStatement st;
        ResultSet result = null;
        ArrayList<DishesDbDetails> dishes = new ArrayList<>(4);

        String queryLoad1 = "SELECT * FROM project.menu_base";

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

            } else {
                result.beforeFirst();
                System.out.println("Processing ResultSet");
                try {
                    while (result.next()) {
                        DishesDbDetails prova  = null;
                        prova = new DishesDbDetails(result.getString(1),result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getString(5),result.getString(6),result.getString(7));


                        //get string from db, put into list of ChildGuiData, ready to put it into GUI
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


        try {
            //add data new child into db
            st = this.connHere().prepareStatement(queryAdd);
            st.setString(1, num);
            st.setString(2, entree);
            st.setString(3, mainCourse);
            st.setString(4, dessert);
            st.setString(5, sideDish);
            st.setString(6, drink);
            st.setDate(7, Date.valueOf(date));
            st.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


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
    public boolean deleteTrip(String dep, LocalDateTime dateDep, LocalDateTime dateCom, String staying, LocalDateTime dateArr, String arr) throws RemoteException {
        PreparedStatement st = null;

        DateTimeFormatter dtfdep = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timestamp timestampDep = Timestamp.valueOf(dateDep.format(dtfdep));

        DateTimeFormatter dtfarr = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timestamp timestampArr = Timestamp.valueOf(dateArr.format(dtfarr));

        DateTimeFormatter dtfcom = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timestamp timestampCom = Timestamp.valueOf(dateCom.format(dtfcom));

        System.out.println("You selected " + dep + "  " + arr + "  " + dateDep +"  "+ dateArr +"  "+ dateCom +"  "+ staying);

        String queryDelete = "DELETE FROM gita " +
                "WHERE Partenza ='"+ dep +"' AND DataOraPar ='"+ timestampDep +"' AND DataOraRit ='"+ timestampCom +"' AND Alloggio ='"+ staying +"' AND DataOraArr ='"+ timestampArr +"' AND Destinazione ='"+ arr + "';";

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

        return staffNotAvailableArrayList;
    }

    @Override
    public ArrayList<CodRifChildDbDetails> findNotAvailableChild(ArrayList<String> selectedChildCf, String selectedTripDep, String selectedTripCom) throws RemoteException{
        PreparedStatement st;
        ResultSet resultChild = null;
        ArrayList<CodRifChildDbDetails> childNotAvailableArrayList = new ArrayList<>();
        String[] selectedChildArray = selectedChildCf.toArray(new String[selectedChildCf.size()]);

        /* OVERLAP QUERY
        SELECT G2.NumGita FROM gita AS G1
        INNER JOIN gita AS G2 ON (G1.NumGita = G2.NumGita
        AND G2.DataOraPar <= G1.DataOraRit <= G2.DataOraRit
        OR G2.DataOraPar <= G1.DataOraPar <= G2.DataOraRit)
        WHERE G1.NumGita <> G2.NumGita
        AND G1.DataOraPar = '...' AND G1.DataOraRit = '...'
        */
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
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        busFromFirstLoop:
        for(int i=0; i < busAvailableArrayList.size(); i++){  //until the last bus in AL
            String capienzaPerBusString = busAvailableArrayList.get(i).getCapacity();  //get capacity of the bus
            int capienzaPerBus = Integer.parseInt(capienzaPerBusString);        //convert capacity to int
            String plateString = busAvailableArrayList.get(i).getPlate();

            if(capienzaPerBus >= totParticipants){      //if capacity >= totPart -> save that bus, we're done.
                //everyParticipant goes on that bus -> save in hashmap <plate,everyoneAL<String>>
                busToPartecipantHashMap.put(plateString, everyParticipantArrayList);

                //make bus mine
                String queryMakeBusMine = "INSERT INTO gita_has_bus (gita_NumGita, bus_Targa) VALUES(?,?)";
                try {
                    st = this.connHere().prepareStatement(queryMakeBusMine);
                    st.setString(1,numGita);
                    st.setString(2,plateString);
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

                break busFromFirstLoop;

            } //else proseguo in for
        }
        //se arrivo a fine busAvailableAL senza trovare un bus, quindi se hashmap.isempty() -> salvo bus da fine AL tornando indietro, finchè
        // o trovo che, arrivato a inizio AL, ho ancora dei partecipanti non assegnati -> bus non sufficienti -> ERR
        // o trovo bus per tutte le persone (continuo ricerca finchè totPart - capienza > 0 )
        if(busToPartecipantHashMap.isEmpty()){
            ArrayList<String> participantsOnBus = new ArrayList<>();

            busFromLastLoop:
            for(int i = busAvailableArrayList.size(); i >= 0; i--){  //finchè non ho ripercorso tutti i bus dall'ultimo al primo (cioè da quello con capacità maggiore, DESC)
                String capienzaPerBusString = busAvailableArrayList.get(i).getCapacity();  //get capacity of the bus
                int capienzaPerBus = Integer.parseInt(capienzaPerBusString);        //convert capacity to int
                String plateString = busAvailableArrayList.get(i).getPlate();

                int remainingParticipants = totParticipants - capienzaPerBus;

                while (remainingParticipants > 0) { //finchè restano partecipanti da assegnare ai bus
                    if(remainingParticipants > 0 && i==0) {  //se ho ancora partecipanti da assegnare ma non ho più bus assegnabili
                        return null;    //null == ERRORE : NON HO ABBASTANZA BUS ASSEGNABILI

                    } else {

                        //salvo in AL chi sta su quel bus (fino alla capacità max)
                        for (int pos = 0; pos <= everyParticipantArrayList.size(); pos++) {
                            for (int k = everyParticipantArrayList.size(); k >= capienzaPerBus; k--) {
                                //aggiungo quella persona a partOnBusAL da inizio AL
                                participantsOnBus.add(pos, everyParticipantArrayList.get(k));
                                //elimino quella persona da everyPartAL
                                everyParticipantArrayList.remove(everyParticipantArrayList.get(k));
                            }
                        }
                        busToPartecipantHashMap.put(plateString, participantsOnBus);

                        // make bus mine
                        String queryMakeBusMine = "INSERT INTO gita_has_bus (gita_NumGita, bus_Targa) VALUES(?,?)";
                        try {
                            st = this.connHere().prepareStatement(queryMakeBusMine);
                            st.setString(1,numGita);
                            st.setString(2,plateString);
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
                    }
                }

            }
        }

        return busToPartecipantHashMap;
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