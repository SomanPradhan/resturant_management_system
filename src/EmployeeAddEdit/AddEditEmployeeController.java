
package EmployeeAddEdit;

import UsableMethods.Database;
import UsableMethods.Message;
import UsableMethods.dbConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import UsableMethods.table;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class AddEditEmployeeController implements Initializable {
     //@FXML
    //private DatePicker date_pick;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private JFXTextField textField_address;

    @FXML
    private Label label_post;

    @FXML
    private JFXTextField textField_contact;

    @FXML
    private Label label_address;

    @FXML
    private JFXButton button_save;

    @FXML
    private JFXTextField textField_id;

    @FXML
    private JFXTextField textField_name;

    @FXML
    private Label label_contact;

    @FXML
    private JFXTextField textField_joined_date;

    @FXML
    private Label label_joined_date;

    @FXML
    private JFXTextField textField_salary;

    @FXML
    private Label label_salary;

    @FXML
    private JFXTextField textField_post;

    @FXML
    private Label label_name;

    @FXML
    private Label label_id;

    @FXML
    private JFXButton button_cancel;
    
    Message msg=new Message();
    Alert alert = new Alert(AlertType.WARNING);
     Connection con = null;
    PreparedStatement pst = null;
    PreparedStatement ps=null;
    dbConnection dbc = new dbConnection();
     
     table value = new table();
     String staticId;
     
     @FXML
    void pickDate(ActionEvent event) {
        LocalDate date=datePicker.getValue();
        textField_joined_date.setText(date.toString());
    }
     
     public void setValue(String eid,String ename,String post,String address, String contact,String join_date,String monthly_salary){
        System.out.println("before error id "+eid);
         textField_id.setText(eid);
         textField_name.setText(ename);
         textField_post.setText(post);
         textField_address.setText(address);
         textField_contact.setText(contact);
         textField_joined_date.setText(join_date);
         textField_salary.setText(monthly_salary);
         
         System.out.println("before error sid=id"+staticId);
         staticId=eid;
         System.out.println("after error sid=id "+staticId);
         System.out.println("after error id "+eid);
     }
     
    @FXML
    void save_data(ActionEvent event) {
        try{
            
            
            String eid=textField_id.getText();
            String ename=textField_name.getText();
            String post=textField_post.getText();
            String address=textField_address.getText();
            String contact=textField_contact.getText();
            String join_date=textField_joined_date.getText();
            String monthly_salary=textField_salary.getText();
            
            String tempo_id =null;
            String temp_id = staticId;
            if(temp_id.isEmpty()){
                if(!eid.isEmpty())
                    if(!ename.isEmpty())
                        if(!post.isEmpty())
                        if(!address.isEmpty())
                            if(!contact.isEmpty())
                                if(!join_date.isEmpty())
                                    if(!monthly_salary.isEmpty()){
                        
            insertData( eid, ename, post, address, contact,join_date,monthly_salary);
            Database daba = new Database();
            ArrayList value = new ArrayList();
            value.add(eid);
            value.add(0);
            daba.insertData("tbl_employee_expenses", value);
            Stage stage1 = (Stage) button_save.getScene().getWindow();
                stage1.close();
            }
            else
                                        
                                    { alert.setTitle("Warning Dialog");
                                    alert.setHeaderText("Look, a Warning Dialog");
                                       alert.setContentText("salary field is empty");

                                        alert.showAndWait(); }
            else
                                    { alert.setTitle("Warning Dialog");
                                    alert.setHeaderText("Look, a Warning Dialog");
                                       alert.setContentText("joined_date field is empty");

                                        alert.showAndWait(); }
            else
                                { alert.setTitle("Warning Dialog");
                                    //alert.setHeaderText("Look, a Warning Dialog");
                                       alert.setContentText("contact field is empty");

                                        alert.showAndWait(); }
            else
                            { alert.setTitle("Warning Dialog");
                                    //alert.setHeaderText("Look, a Warning Dialog");
                                       alert.setContentText("address field is empty");

                                        alert.showAndWait(); }
            else
                            { alert.setTitle("Warning Dialog");
                                    //alert.setHeaderText("Look, a Warning Dialog");
                                       alert.setContentText("post field is empty");

                                        alert.showAndWait(); }
            else
                        { alert.setTitle("Warning Dialog");
                                    //alert.setHeaderText("Look, a Warning Dialog");
                                       alert.setContentText("name field is empty");

                                        alert.showAndWait(); }
            else
                    { alert.setTitle("Warning Dialog");
                                    //alert.setHeaderText("Look, a Warning Dialog");
                                       alert.setContentText("id field is empty");

                                        alert.showAndWait(); }
                            }
            else{   if(!eid.isEmpty())
                    if(!ename.isEmpty())
                        if(!post.isEmpty())
                            if(!address.isEmpty())
                                if(!contact.isEmpty())
                                    if(!join_date.isEmpty())
                        if(!monthly_salary.isEmpty()){
                            
                updateData(eid, ename, post, address, contact, join_date, monthly_salary, temp_id);
                String sql = "UPDATE `tbl_employee_expenses` SET `eid`='"+eid+"' WHERE `eid`='"+temp_id+"'";            
            
            Stage stage1 = (Stage) button_save.getScene().getWindow();
                stage1.close();
            }
            else
                                        
                                    { alert.setTitle("ERROR");
                                       alert.setContentText("salary field is empty");
                                        alert.showAndWait(); }
            else
                                    { alert.setTitle("ERROR");
                                       alert.setContentText("joined_date field is empty");
                                        alert.showAndWait(); }
            else
                                { alert.setTitle("ERROR");
                                       alert.setContentText("contact field is empty");
                                        alert.showAndWait(); }
            else
                            { alert.setTitle("ERROR");
                                       alert.setContentText("address field is empty");

                                        alert.showAndWait(); }
            else
                            { alert.setTitle("ERROR");
                                       alert.setContentText("post field is empty");

                                        alert.showAndWait(); }
            else
                        { alert.setTitle("ERROR");
                                    alert.setHeaderText("Look, a Warning Dialog");
                                        alert.showAndWait(); }
            else
                    { alert.setTitle("ERROR");
                                       alert.setContentText("id field is empty");

                                        alert.showAndWait(); 
                    }
                    }
        }catch(Exception e){
               e.printStackTrace();
        }
    }


    @FXML
    void cancel_field(ActionEvent event) {
        Stage stage1 = (Stage) button_cancel.getScene().getWindow();
                stage1.close();
         
    }
    
    public void insertData(String eid,String ename,String post,String address,String contact,String join_date,String monthly_salary){
        try{
            String temp_id=null;
           con = dbc.connection();
           String sql ="SELECT * FROM tbl_employee_details";
             ps = (PreparedStatement) con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery(sql);
             while(rs.next()){
                 temp_id = rs.getString("eid");
                 System.out.println("temp_id value in database is "+temp_id);
                 if(temp_id.equals(eid))
                     break;
             }
             if( temp_id==null || !temp_id.equals(eid)){
             pst = (PreparedStatement) con.prepareStatement("insert into tbl_employee_details values(?,?,?,?,?,?,?)");
            System.out.println("PreparedStatement created Successfully");
           
           pst.setString(1, eid);
           pst.setString(2, ename);
           pst.setString(3, post);
           pst.setString(4, address);
           pst.setString(5, contact);
           pst.setString(6, join_date);
           pst.setString(7, monthly_salary);
            System.out.println("Executing Update to database");
            int i = pst.executeUpdate();
            System.out.println("Database Updated Successfully");
            if (i >1) {
                msg.alertMessage(AlertType.NONE, "Error", "DATA CAN NOT BE SAVED");                
            } else {
                msg.alertMessage(AlertType.NONE, "Saved", "DATA SAVED.");

            }
        
             }
             else
                 msg.alertMessage(AlertType.NONE, "Error", "Dublicate id can not saved data");
                 
            

        }           
            catch(Exception e){
            e.printStackTrace();
        }
    }
   public void updateData(String eid,String ename,String post,String address,String contact,String join_date,String monthly_salary,String staticId){
        try{
           con = dbc.connection();
           String sql ="UPDATE `tbl_employee_details` SET `eid`='"+eid+"',`ename`='"+ename+"',`post`='"+post+"',`address`='"+address+"',`contact`='"+contact+"',`join_date`='"+join_date+"',`monthly_salary`= '"+monthly_salary+"' WHERE `eid` = '"+staticId+"'";
             pst = (PreparedStatement) con.prepareStatement(sql);
             
//             if((temp_id.equalsIgnoreCase(staticId))){
//           System.out.println("database staticId ="+staticId);
//           sql = "DELETE FROM tbl_employee_details WHERE eid = '" +staticId+"'";
//            ps = (PreparedStatement) con.prepareStatement(sql);
//
//            int j = ps.executeUpdate();
//            pst = (PreparedStatement) con.prepareStatement("insert into tbl_employee_details values(?,?,?,?,?,?,?)");
//            System.out.println("PreparedStatement created Successfully");
//            
//            pst.setString(1, eid);
//            pst.setString(2, ename);
//            pst.setString(3, post);
//            pst.setString(4, address);
//            pst.setString(5, contact);
//            pst.setString(6, join_date);
//            pst.setString(7, monthly_salary);
//            
//            
            int i = pst.executeUpdate();
            System.out.println("Database Updated Successfully");

            if (i >0) {
                msg.alertMessage(AlertType.NONE, "Saved", "Data saved successfully.");
                
            } else {
                msg.alertMessage(AlertType.NONE, "Error", "Data can not be saved.");
            }
//             }
            
            
            
            
}        
        catch(Exception e){
                 msg.alertMessage(AlertType.ERROR, "ERROR", "Dublicate id");
        }
   }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
