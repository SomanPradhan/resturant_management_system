/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reports;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Rubesh
 */

public class TestController implements Initializable {
@FXML
    JFXTextField textField_Username;

    @FXML
    JFXTextField textField_Password;
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    public void getText(){
        String username = textField_Username.getText();
    }
    
    public void SignIn(ActionEvent event){
        
    }
    
}
