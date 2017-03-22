/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import UsableMethods.Database;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Rubesh
 */
public class Login extends Application {
    
    @Override
    public void start(final Stage stage) throws Exception {
        Runtime.getRuntime().exec("C:\\xampp\\mysql\\bin\\mysqld.exe", null, new File("C:\\xampp\\mysql\\bin\\"));
        
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

        Scene scene = new Scene(root);
        stage.getIcons().add(new Image("/Images/logo_cup.png"));

        stage.setTitle("User Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
        
    }
    
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
        Runtime.getRuntime().exec("C:\\xampp\\mysql\\bin\\mysqld.exe", null, new File("C:\\xampp\\mysql\\bin\\"));}
        catch(Exception e){
            e.printStackTrace();
        }
        launch(args);
    }
    
}
