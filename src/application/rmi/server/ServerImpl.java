package application.rmi.server;

import application.Interfaces.UserRemote;
import application.contr.Database;
import application.details.*;
import application.details.ChildDbDetails;
import application.details.ChildGuiDetails;
import com.mysql.jdbc.Connection;
import javafx.collections.ObservableList;

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

    public ArrayList<IngredientsDbDetails> loadIngr() throws RemoteException {
        PreparedStatement st = null;

        ResultSet result = null;

        ArrayList<IngredientsDbDetails> ingrArrayList = new ArrayList<>(1);

        String queryLoad = "SELECT project.ingredient " +
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


    public boolean addData(String name, String surname, String cf, LocalDate birthday, String bornWhere, String residence, String address, String cap, String province, ArrayList<String> selectedAllergy) throws RemoteException {
        PreparedStatement st = null;

        String queryAdd = "INSERT INTO project.interni(Cognome, Nome, CF, DataNascita, CittaNascita, Residenza, Indirizzo, CAP, Provincia, Allergia)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?)";
        String queryLastCodRif = "SELECT MAX(CodRif) FROM bambino";  //select last CodRif inserted
        String queryAddCf = "INSERT INTO bambino(CodRif, Interni_CF) VALUES (?,?)";

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
                String newCod = "c1";

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
                    String newCod = "c" + (Integer.parseInt(currentLast.substring(1, currentLast.length()))+1);
                    st = this.connHere().prepareStatement(queryAddCf);
                    st.setString(1, newCod);
                    st.setString(2, cf);
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


        ResultSet result = null;



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


    public String sendMessage(String clientMessage) {
        return "Client Message".equals(clientMessage) ? "Server Message" : null;
    }


    public Connection connHere (){

        Database receivedCon = new Database();
        Connection connectionOK = receivedCon.databaseCon();
        if(connectionOK != null)
            System.out.println("Connection successful");
        else
            System.out.println("Connection failed");

        return connectionOK;

    }

}