/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CreditPurchase;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Soman
 */
public class CreditPurchase extends Application{

    @Override
    public void start(Stage stage) {
        //System.out.println("Start started");
        try{
            
            //System.out.println("Start function runs");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreditPurchase.fxml"));
            Parent root =(Parent) loader.load();
            //System.out.println("Start function runs after loading");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Credit Purchase");
            stage.show();
    }catch(Exception e){
        System.out.println("on start method of CreditPurchase.java");
        e.printStackTrace();
    }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //System.out.println("main function run");
        launch(args);
        
    }
}
