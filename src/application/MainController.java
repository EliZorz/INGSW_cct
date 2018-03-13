package application;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class MainController {

	@FXML
	private TextField txtUsername;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private  Label lblStatus;

	private static final String usrname = "root";

	private static final String pw = "Monali2009!";

	//JDBC driver name and DB URL
	private static final String url = "jdbc:mysql://localhost:3306/Login";
	private static final String db = "com.mysql.jdbc.Driver";


	public void handleLogin() throws SQLException {

		Connection con = null;
		PreparedStatement st = null;
		ResultSet result = null;
		
		
		String usr = txtUsername.getText().toString();
		String pwd = txtPassword.getText().toString();
		String query = "SELECT * FROM UserIn WHERE Username = ? AND Password = ? " ;
		
		
		try {
			//register JDBC driver
			Class.forName(db);
			
			//establish connection
			con = (Connection) DriverManager.getConnection(url, usrname, pw);
			if(con != null){
				System.out.println("Connection successful");
			} else {
				System.out.println("Connection failed");
			}

			//execute query
			st = con.prepareStatement(query);
			st.setString(1, usr);
			st.setString(2, pwd);
			
			result = st.executeQuery();
			
			//extract data from dataSet
			if( !result.next() ) {
				lblStatus.setText("Login failed");
				System.out.println("No user like that in your database");
			} else {
				result.beforeFirst();
				while(result.next()) {
					String usrFound = result.getString("Username");
					System.out.println("USER: " + usrFound);
					String pwdFound = result.getString("Password");
					System.out.println("PASSWORD: " + pwdFound);
				}

				lblStatus.setText("Login SUCCESSFUL.");
				System.out.println("I found your user!");

				try{
					FXMLLoader fxmlLoader =  new FXMLLoader(getClass().getResource("/application/MenuIniziale.fxml"));
					//Pane mainPane = (Pane)FXMLLoader.load(getClass().getResource("/application/MenuIniziale.fxml"));
					Parent p= (Parent) fxmlLoader.load();
					Stage stage = new Stage();
					stage.setScene(new Scene(p));

					//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					//Scene scene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());


					//Scene scene = new Scene(mainPane);
					//primaryStage.setScene(scene);
					stage.setTitle("Menu Iniziale");
					//primaryStage.sizeToScene();
					stage.show();
				} catch (Exception ex){
					ex.printStackTrace();
				}
				}

			
		} catch (Exception se) {
			se.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException sqe) {
					sqe.printStackTrace();
				}
			}
			if(result != null){
		        try{
		            result.close();
		        }catch(Exception e){
		            e.printStackTrace();
		        }
		    }
			
			
		}

	
	
		    
			/*
			 try {
			
		    	if(result.next()) {
		    		System.out.println("Usr: " + result.getString("Username") + "Pwd: " + result.getString("Password"));
		    		
		    	} else {
		    		lblStatus.setText("Access denied");
		    	}
		    	
		    }
		    catch(Exception ex) {
		        
		        System.out.println("Cannot get resultSet row count: " + ex);
		        
		    }
			*/
			
			/*
			//questa funziona ma ritorna com.mysql.jdbc.JDBC42ResultSet@1c9ed0ea
			System.out.println(result.toString());
			
			
			//questa non funziona
			while(result.next()) {
	
				System.out.println("Usr: " + result.getString("Username") + "Pwd: " + result.getString("Password"));
				
			}
			*/
			
			/*
			String query = "SELECT Username, Password FROM UserIn WHERE Username = '" + txtUsername.getText() + "'AND Password = '" + txtPassword.getText() + "'";
			PreparedStatement st;
			Connection con= DriverManager.getConnection(url, usrname, pw);
			
			if(con != null){
				System.out.println("Connection successful");
			} else {
				System.out.println("Connection failed");
			}

			st = con.prepareStatement(query);
			ResultSet result = st.executeQuery(query);
			
			int count = 0;	
			ArrayList<String> name= new ArrayList<>();
			
			if(!result.next()) {
				System.out.println("No result found");
			} else {
				do {
					System.out.println(result.getString("Username"));
					name.add(result.getString("Username"));
					System.out.println(result.getString("Password"));
					name.add(result.getString("Password"));
					count ++;
				}	while(result.next());

			}

			if(count>1) {
				lblStatus.setText("There are too many identical Users");
			} else if (count == 0) {
				lblStatus.setText("No user found. Try again.");
			} else {
				lblStatus.setText("Login SUCCESSFUL.");

			}
			*/
	}
	

}

	/*public void btnAction() {
		@FXML
		btnLogin.setOnAction(new EventHandler<ActionEvent>() {
			@Override 
			public void handleLogin(ActionEvent e) {
				String query = "SELECT Username, Password FROM UserIn WHERE Username = '" + txtUsername + "'AND Password = '" + txtPassword + "'";
				Statement st;
				Connection con= DriverManager.getConnection(url, usrname, pw);

				st = con.prepareStatement(query);
				ResultSet result = st.executeQuery(query);
				int count = 0;

				ArrayList<String> array= new ArrayList<>();

				while(result.next()) {
					System.out.println(result.getString("Username"));
					array.add(result.getString("Username"));
					System.out.println(result.getString("Password"));
					array.add(result.getString("Password"));
					count ++;
				}

				boolean grantAccess = false;
				//verifico che in db non esistano pi� di un usr+psw identici n� nessun usr+psw corrispondenti all'inserimento
				if(count>1) {
					lblStatus.setText("There are too many identical Users");
				} else if (count == 0) {
					lblStatus.setText("No user found. Try again.");
				} else {
					lblStatus.setText("Login SUCCESSFUL.");
					if ( ) {
						accessGranted = true;
					} else {
						accessGranted = false;
					}

				}
			}
		});
			
			if (grantAccess) {
				lblStatus.setText("CONFIRMED. Access granted.");
				
				//connette a Server via socket
				
				
				
				
				
			} else {
				lblStatus.setText("Access denied.");
			}
				//una volta connesso, verifico dati login inseriti, se gi� presenti in database consento accesso a quell'usr+psw	
			

				/*
					if (txtUsername.getText().equals(result.getString("Username")) && txtPassword.getText().equals(result.getString("Password"))) {
						lblStatus.setText("Login successful.");
					} else {
						lblStatus.setText("Login failed.");
					}
				 */
