package application.contr;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by ELISA on 22/03/2018.
 */
public class Database {

    private static final String usrname = "root";

    private static final String pw = "123456"; //"Monali2009!"

    //JDBC driver name and DB URL
    private static final String url = "jdbc:mysql://localhost:3306/Project";

    private static final String db = "com.mysql.jdbc.Driver";


    public Connection databaseCon (){

        Connection con = null;

        try{
            //register JDBC driver
            Class.forName(db);
            //establish connection
            con = (Connection) DriverManager.getConnection(url, usrname, pw);



            if (con != null) {
                System.out.println("Connection successful");
            } else {
                System.out.println("Connection failed");
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //return
        return con;

    }
}
