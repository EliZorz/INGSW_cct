package application.rmi.server;

import application.Interfaces.UserRemote;
import application.contr.Database;
import application.details.*;
import application.details.ChildDbDetails;
import application.details.ChildGuiDetails;
import com.mysql.jdbc.Connection;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by ELISA on 21/03/2018.
 */
public class ServerImpl extends UnicastRemoteObject implements UserRemote {  //socket e rmi usano entrambe questa implementazione


    public ServerImpl() throws RemoteException {
        super();
    }

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
    public ArrayList<IngredientsDbDetails> loadIngr() throws RemoteException {
        PreparedStatement st = null;

        ResultSet result = null;

        ArrayList<IngredientsDbDetails> ingrArrayList = new ArrayList<>(1);

      /*  String queryLoad = "SELECT project.ingredient " +
                "FROM ingredients INNER JOIN fornitore " +
                "ON ingredients.Fornitore_PIVA = fornitore.PIVA";*/
      String queryLoad = "SELECT ingredient FROM project.ingredients";

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
    public ArrayList<String> searchIngredients(String dish) throws RemoteException {
        PreparedStatement st = null;
        String querySearch = "SELECT ingredients_ingredient FROM project.menu_base_has_ingredients WHERE Nome_Piatto='"+dish+"'";
        ResultSet result = null;
        ArrayList<String> ingredientsForThisDish = new ArrayList<>();
        try{
            st = this.connHere().prepareStatement(querySearch);
            result = st.executeQuery(querySearch);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            if(!result.next()){
                System.out.println("Error");
            }else{
                result.beforeFirst();
                try{
                    while(result.next()){
                        String str = new String(result.getString(3));
                        System.out.println(str);
                        String[] ingredients = str.split("\\s");
                        for(String x : ingredients)
                            ingredientsForThisDish.add(x);
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
    public ArrayList<String> loadIngr(LocalDate d) throws RemoteException{
        PreparedStatement st = null;
        String queryLoad = "SELECT ingredients_ingredient FROM project.menu_base_has_ingredients WHERE date ='" + d+"'";
        ResultSet result = null;
        ArrayList<String> resIngr = new ArrayList<>();
        try{
            st = this.connHere().prepareStatement(queryLoad);
            result = st.executeQuery(queryLoad);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            if(!result.next()){
                System.out.println("Error");
            }else{
                result.beforeFirst();
                try{
                    while(result.next()){
                        String str = new String(result.getString(1));
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
    public boolean updateMenu(String num, String entree, String main, String dessert, String side, String drink, LocalDate day, ArrayList<String> selectedIngredients, LocalDate oldDate) throws RemoteException{

        PreparedStatement st = null;
        PreparedStatement stIngr = null;
        PreparedStatement stIngr2 = null;

        String queryUpdate = "UPDATE project.menu_base SET NumPiatti='" + num +"', entrees ='"+ entree +"', main_courses ='"+ main+ "', dessert = '" + dessert+"', side_dish = '"+ side+"', drink = '"+ drink +"', date ='"+ day +"' WHERE date = '"+ oldDate+"'";

        ResultSet result = null;
        ResultSet resultIngr = null;
        ResultSet resultIngr2 = null;

        try {
            //add data new child into db
            st = this.connHere().prepareStatement(queryUpdate);
            st.executeUpdate();

            ArrayList<String> ingrMenu = loadIngr(oldDate);
            for(String y : ingrMenu) {
                String queryDelete = "DELETE FROM project.menu_base_has_ingredients WHERE ingredients_ingredient = '"+y+"' and date = '" + oldDate + "'";
                System.out.println();
                stIngr = this.connHere().prepareStatement(queryDelete);
                stIngr.executeUpdate();
            }

            for(String x : selectedIngredients){
               // String queryAddIngr = "UPDATE project.menu_base_has_ingredients SET Ingredients_ingredient ='"+ x+"', date ='"+ day +"' WHERE date= '"+ oldDate+"'";

                String queryAddIngr = "INSERT INTO project.menu_base_has_ingredients(Ingredients_ingredient, date) VALUES (?,?)";
                stIngr2 = this.connHere().prepareStatement(queryAddIngr);
                stIngr2.setString(1,x);
                stIngr2.setDate(2,Date.valueOf(day));
                stIngr2.executeUpdate();
            }

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                if (st != null && stIngr != null && stIngr2 != null) {
                    st.close();
                    stIngr.close();
                    stIngr2.close();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    @Override
    public boolean addData(String name, String surname, String cf, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy,
                           String nameContact, String surnameContact, String cfContact, String mailContact, String telContact, LocalDate birthdayContact, String bornWhereContact, String addressContact, String capContact, String provinceContact,
                           boolean isDoc, boolean isGuardian, boolean isContact) throws RemoteException {
        PreparedStatement st = null;

        String queryAdd = "INSERT INTO interni(Cognome, Nome, CF, DataNascita, CittaNascita, Residenza, Indirizzo, CAP, Provincia, Allergia)" +
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
                "Indirizzo='" + address + "', CAP='" + cap + "', Provincia='" + province + "', Allergia='" + allAllergies.toString() + "'" +
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
        String queryLoadContacts = "SELECT Cognome, Nome, CF, Mail, Tel, DataNascita, CittaNascita, Indirizzo, CAP, Provincia, Pediatra, Tutore, Contatto" +
                            " FROM adulto INNER JOIN bambino" +
                            " WHERE adulto.Bambino_CodRif = bambino.CodRif AND bambino.Interni_CF = '" + cfChild + "';";
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

        String queryAdd = "INSERT INTO interni(Cognome, Nome, CF, DataNascita, CittaNascita, Residenza, Indirizzo, CAP, Provincia, Allergia)" +
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
                "Indirizzo ='" + address + "', CAP ='" + cap + "', Provincia ='" + province + "', Allergia ='" + allAllergies.toString() + "'" +
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
    public boolean addMenu(String num, String entree, String mainCourse, String dessert, String sideDish, String drink, LocalDate date,ArrayList<String> selectedIngredients) throws RemoteException {
        PreparedStatement st = null;
        PreparedStatement stIngr = null;



        String queryAdd = "INSERT INTO project.menu_base(NumPiatti,entrees, main_courses,dessert, side_dish, drink, date)" +
                " VALUES (?,?,?,?,?,?,?)";


        ResultSet result = null;
        ResultSet resultIngr = null;



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
            for(String x : selectedIngredients){
                String queryAddIngr = "INSERT INTO project.menu_base_has_ingredients(Ingredients_ingredient , date)" + "VALUES(?,?)";
                stIngr = this.connHere().prepareStatement(queryAddIngr);
                stIngr.setString(1,x);
                stIngr.setDate(2,Date.valueOf(date));
                stIngr.executeUpdate();
            }

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                if (st != null && stIngr != null) {
                    st.close();
                    stIngr.close();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }




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
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteMenu(LocalDate d) throws RemoteException {
        PreparedStatement st = null;

        String queryDelete = "DELETE FROM project.menu_base WHERE date = '" + d + "';";

        try{
            st = this.connHere().prepareStatement(queryDelete);
            st.executeUpdate(queryDelete);
            System.out.println("Menu deleted.");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null){
                    st.close();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    }

//TRIP---------------------------------------------------------------------------------------------
    @Override
    public ArrayList<TripTableDbDetails> loadDataTrip() throws RemoteException {
        PreparedStatement st;
        ResultSet result = null;
        ArrayList<TripTableDbDetails> trips = new ArrayList<>(7);
        String queryLoadTrip = "SELECT Luoghi, DataOraPar, DataOraRit, DataOraArr, Alloggio FROM gita";
        String queryCount = "SELECT COUNT(*) " +
                "FROM interni_has_gita " +
                "WHERE Presente_effettivo = 1";


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
                                result.getString(5));
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