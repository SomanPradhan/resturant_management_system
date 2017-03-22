/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccountSetting_AddEdit;
import UsableMethods.table;
import UsableMethods.dbConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Soman
 */
public class FXMLUserAddEditController implements Initializable {
       @FXML
    public JFXRadioButton radioBtn_admin;

    @FXML
    public JFXTextField textField_password;

    @FXML
    public JFXTextField textField_username;
    
    @FXML
    public JFXTextField textField_id; 
 
    @FXML
    public JFXButton btn_cancel;

    @FXML
    public ToggleGroup privilieges;

    @FXML
    public JFXRadioButton radioBtn_user;

    @FXML
    public JFXButton btn_save;
     
    @FXML
    public Label textId_error;
    
    @FXML
    public Label textUsername_error;
    
    @FXML 
    public Label textPassword_error;
    @FXML 
    public Label textPrivileges_error;
    
     public static boolean passboo=true;
     dbConnection dbc = new dbConnection();
     UserDatabase db = new UserDatabase();
     table value = new table();
     String staticId;
     Connection con = null;
     ResultSet rs = null;
     PreparedStatement pst= null;
     public void setValue(String id,String username,String password,String privileges){
         textId_error.setVisible(false);
         textUsername_error.setVisible(false);
         textPassword_error.setVisible(false);
         textPrivileges_error.setVisible(false);
         System.out.println("before error id "+id);
         textField_id.setText(id);
         textField_username.setText(username);
         textField_password.setText(password);
         String privileges1 = privileges;
         if(privileges1.equals("Admin")){
             privilieges.selectToggle(radioBtn_admin);
         }
         else{
             privilieges.selectToggle(radioBtn_user);
         }
         System.out.println("before error sid"+staticId);
         staticId=id;
         System.out.println("after error sid= "+staticId);
         System.out.println("after error id "+id);
     }
    public void saveFun(ActionEvent event) {
           try{
           
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date_modified = dateFormat.format(currentDate);
            System.out.println(date_modified);
            String privileges;
            String id=textField_id.getText();
            String username=textField_username.getText();
            String password=textField_password.getText();
            if(radioBtn_admin.isSelected()){
                privileges="Admin";
            }
            else{
                privileges="User";
            }
           
            String temp_id = staticId;
            if(temp_id.isEmpty()){
                    if(!id.isEmpty()||!username.isEmpty()||!password.isEmpty())
                    if(!id.isEmpty()||!username.isEmpty())
                        if(!id.isEmpty()||!password.isEmpty())
                            if(!username.isEmpty()||!password.isEmpty())
                if(!id.isEmpty())
                    if(!username.isEmpty())
                        if(!password.isEmpty()){
                 String tempo_id="";
                 con = dbc.connection();
                 String sql ="SELECT * FROM `tbl_user`";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery(sql);
                 while(rs.next()){
                 tempo_id = rs.getString("id");
                 System.out.println("temp_id value in database is "+tempo_id);
                 if(tempo_id.equals(id))
                     break;
                  }
                 if(!tempo_id.equals(id)){
                        db.insertData( id, username, password, privileges, date_modified);
                        passboo = true;
                        Stage stage1 = (Stage) btn_save.getScene().getWindow();
                stage1.close();
                 }
                 else{
                     textId_error.setText("* dublicate id");
                     labelBoolean(true,false,false);
                                      }
                        }else{
                            labelBoolean(false,false,true);    
                            
                        }
                    else{
                            labelBoolean(false,true,false);
                    }
                else{
                            textId_error.setText("* id is requried");
                                labelBoolean(true,false,false);
                }
                            else{
                                labelBoolean(false,true,true);
                            }
                else{
                                textId_error.setText("* id is requried");
                                labelBoolean(true,false,true);
                              
                            }
                else{
                                textId_error.setText("* id is requried");
                                labelBoolean(true,true,false);
                            }
                else{
                                textId_error.setText("* id is requried");
                                labelBoolean(true,true,true);
                            }
                }
            
            else{   if(!id.isEmpty()||!username.isEmpty()||!password.isEmpty())
                    if(!id.isEmpty()||!username.isEmpty())
                        if(!id.isEmpty()||!password.isEmpty())
                            if(!username.isEmpty()||!password.isEmpty())
                if(!id.isEmpty())
                    if(!username.isEmpty())
                        if(!password.isEmpty()){
                            int count = db.returnCount();
           String privilegesState=db.returnPrivileges(staticId);
           System.out.println("database staticId ="+staticId);
           if(count>1 || !privilegesState.equals("Admin") || privileges.equals("Admin")){
                        db.updateData( id, username, password, privileges, date_modified,temp_id);
                        passboo=true;
                        Stage stage1 = (Stage) btn_save.getScene().getWindow();
                stage1.close();
           }  
           else{
               textPrivileges_error.setVisible(true);
                             
           }           
           }else{
                   labelBoolean(false,false,true);    
                            
                        }
                    else{
                                labelBoolean(false,true,false);
                    }
                else{
                            textId_error.setText("* id is requried");    
                            labelBoolean(true,false,false);
                }
                            else{
                                labelBoolean(false,true,true);
                            }
                else{
                                textId_error.setText("* id is requried");
                                labelBoolean(true,false,true);
                            }
                else{
                                textId_error.setText("* id is requried");
                                labelBoolean(true,true,false);
                            }
                else{
                                textId_error.setText("* id is requried");
                                labelBoolean(true,true,true);
                            }
            }
            
            
          
           }catch(Exception e){
               System.out.println("did't work");
               e.printStackTrace();
           }
    }
    public void cancelFun(ActionEvent event){
        passboo=true;
        Stage stage1 = (Stage) btn_cancel.getScene().getWindow();
                stage1.close();
    }
        

        @Override
    public void initialize(URL location, ResourceBundle resources) {
      
    }
   public void labelBoolean(boolean first, boolean second, boolean third){
       textId_error.setVisible(first);
       textUsername_error.setVisible(second);
       textPassword_error.setVisible(third);
   }
    
   public boolean variBoo(){
       return passboo;
   }
}

    


