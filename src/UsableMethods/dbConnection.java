
package UsableMethods;

//STEP 1. Import required packages

import java.sql.Connection;
import java.sql.DriverManager;
import javafx.scene.control.Alert;


public class dbConnection {
    
    Message m = new Message();
    
     // JDBC driver name and database URL
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost/resturant_management_system";
    
    //  Database credentials
    private final String USER = "root";
    private final String PASSWORD = "";
    public static Connection conn = null;
    
    public Connection connection(){
        
        
//        Statement stmt = null;
//        PreparedStatement pst = null;
        
        
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            
            //STEP 3: Open a connection
//           System.out.println("Connecting to a selected database...");
           conn =  DriverManager.getConnection(DB_URL, USER, PASSWORD);
//           System.out.println("Connected database successfully...");

            
            
        }catch(Exception e){
            m.alertMessage(Alert.AlertType.ERROR, "Connection failed..", "Please check the connection.");
            
        }
        
        return conn;
    }
    
}
