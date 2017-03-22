/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wastage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author SujanDhakal
 */
public class WastageRepairFX extends Application {
    
    @Override
    public void start(Stage stage){
        try{
        Parent root = FXMLLoader.load(getClass().getResource("wastageRepair.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }catch(Exception e){
        System.out.println("in start method WastageRepairFX.java");
        e.printStackTrace();
    }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
