
package EmployeeDetails;

import EmployeeAddEdit.AddEditEmployeeController;
import UsableMethods.Message;
import UsableMethods.dbConnection;
import UsableMethods.table;
import com.jfoenix.controls.JFXButton;
import java.awt.EventQueue;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JOptionPane;


public class EmployeeController implements Initializable {
    @FXML
    private TableColumn<table, Integer> column_Sn;

    @FXML
    private TableColumn<table, String> column_Id;

    @FXML
    private TableColumn<table, String> column_Joined_Date;

    @FXML
    private TableColumn<table, String> column_Post;

    @FXML
    private JFXButton button_Add;

    @FXML
    private TableColumn<table, String> column_Name;

    @FXML
    private TableColumn<table, String> column_Contact;

    @FXML
    private TableColumn<table, String> column_Salary;

    @FXML
    private TableColumn<table, String> column_Address;

    @FXML
    private TableView<table> table_Employee;

    @FXML
    private JFXButton button_Delete;

    @FXML
    private JFXButton button_Edit;
    
    Alert alert = new Alert(AlertType.CONFIRMATION);
    
    Message msg = new Message();
    
    boolean checker = true;
   public static int rowvalue=0;
    public static int listvalue=0;
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs=null;
    dbConnection dbc = new dbConnection();
    private IntegerProperty tableIndex = new SimpleIntegerProperty();
    final ObservableList<table> data = FXCollections.observableArrayList();
    
    //delete employee function
   public void remove(){
alert.setTitle("Confirmation Dialog");
alert.setHeaderText("Look, a Confirmation Dialog");
alert.setContentText("Are you ok with this?");

Optional<ButtonType> result = alert.showAndWait();
if (result.get() == ButtonType.OK){
    if(rowvalue>-1){
        rowvalue--;
        forFunction valuepass = new forFunction();
        valuepass.DeleteRow(valuepass.SelectedSn(rowvalue));
        show_Data();
       }    
    // ... user chose OK
} else {
    // ... user chose CANCEL or closed the dialog
}

       
        

   }
   
   public boolean updateFrontEndTable(Integer listvalue) {
        //EMPTY THE TABLE FIRST
        data.removeAll(data);
        
        
        String sql = "select * from tbl_employee_details where `eid` =" + listvalue;
        dbConnection dbc = new dbConnection();
        int i= 1;

        try {
            Connection conn = dbc.connection();
            Statement s = conn.prepareStatement(sql);
            ResultSet rs = s.executeQuery(sql);
//            

            //LOOP THROUGH ALL VALUES
            while (rs.next()) {
                table fo = new table();

                fo.i1.set(i++);
                fo.s1.set(rs.getString("ename"));
                fo.s2.set(rs.getString("post"));
                fo.s3.set(String.valueOf(rs.getString("address")));
                fo.s4.set(String.valueOf(rs.getString("contact")));
                fo.s5.set(String.valueOf(rs.getString("join_date")));
                fo.s6.set(String.valueOf(rs.getString("montlhy_salary")));
                data.add(fo);
            }
            table_Employee.setItems(data);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
   
   //table view function 
   public void show_Data(){
      try { 
          
             String sql = "select * from tbl_employee_details";
             
             System.out.println("PreparedStatement created Successfully");
             
             con=dbc.connection();
             pst = (PreparedStatement) con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery(sql);
              int temp_sn=1;
             column_Sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
             column_Id.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
             column_Name.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
             column_Post.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
             column_Address.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
             column_Contact.setCellValueFactory(new PropertyValueFactory<table, String>("s5"));
             column_Joined_Date.setCellValueFactory(new PropertyValueFactory<table, String>("s6"));
             column_Salary.setCellValueFactory(new PropertyValueFactory<table, String>("s7"));
             
            table_Employee.getItems().clear();
          
             
             while (rs.next()){
                 table as = new table();
                 
                 as.i1.set(temp_sn++);
                 
                 as.s1.set(rs.getString("eid"));
                 as.s2.set(rs.getString("ename"));
                 as.s3.set(rs.getString("post"));
                 as.s4.set(rs.getString("address"));
                 as.s5.set(rs.getString("contact"));
                 as.s6.set(String.valueOf(rs.getDate("join_date")));
                 as.s7.set(rs.getString("monthly_salary"));
                 
                 data.add(as);
             }
             table_Employee.setItems(data);
             
         } catch (SQLException ex) {
            ex.printStackTrace();
         }
    
    }
   
   //edit through data base
   public void edit(){
       Integer row = rowvalue-1;
               if(row >-1){
        
        String eid=null, ename = null, post = null, address = null, contact=null,join_date=null, monthly_salary=null;
        forFunction valuepass = new forFunction();
        eid=valuepass.SelectedSn(row);
        try{
           con = dbc.connection();
           String sql = "SELECT * FROM tbl_employee_details WHERE eid='"+eid+"'";
            ResultSet rs = con.createStatement().executeQuery(sql);
              while (rs.next()) {

                ename = rs.getString("ename");
                post=rs.getString("post");
                address=rs.getString("address");
                contact=rs.getString("contact");
                join_date=rs.getString("join_date");
                monthly_salary=rs.getString("monthly_salary");
              }
              runAddEdit(eid, ename, post,address,contact,join_date, monthly_salary);
            show_Data(); 
        }catch(Exception e){
            
        }
        
       }
    }
   
   public void runAddEdit(String eid,String ename,String post,String address, String contact, String join_date, String monthly_salary){
           try {
           
               if(checker){
        
            System.out.println("this run");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EmployeeAddEdit/addEditEmployee.fxml"));
            
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Add or Edit");
            stage.setResizable(false);
            checker = false;
            AddEditEmployeeController showController = loader.getController();
            showController.setValue(eid,ename,post,address,contact,join_date,monthly_salary);
            stage.showAndWait();
            show_Data();
            checker = true;
          System.out.println("working");
               }
               else{
                   msg.alertMessage(AlertType.ERROR, "ERROR", "Already Running Add Edit");
               }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       }
   
   public void getSelected(){
            try{
             column_Sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
             column_Id.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
             column_Name.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
             column_Post.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
             column_Address.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
             column_Contact.setCellValueFactory(new PropertyValueFactory<table, String>("s5"));
             column_Joined_Date.setCellValueFactory(new PropertyValueFactory<table, String>("s6"));
             column_Salary.setCellValueFactory(new PropertyValueFactory<table, String>("s7"));
             
             table_Employee.setItems(data);
             
        table_Employee.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                
                rowvalue = (data.indexOf(newValue) + 1);
                System.out.println("ON RowValue is : " + rowvalue);
            }

        }); 
            }catch(Exception e){
                e.printStackTrace();
            }
        }
   

    @FXML
    void add_Employee(ActionEvent event)  {
        
runAddEdit("","","","","","","");

    }
    
    public class forFunction {
    Connection con = null;
    PreparedStatement pst = null;
    public String SelectedSn(Integer rowvalue){
         String id=null;
        try {
           
            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_employee_details` LIMIT "+rowvalue+",1";
            ResultSet rs = con.createStatement().executeQuery(sql);
              while (rs.next()) {

                id = rs.getString("eid");                
            }
            
        } catch (Exception ex) {
            ShowErrorMessage("No item Selected", "ERROR");
        }
        return id;
      }
    public void DeleteRow(String id){
         try {
            dbc.connection();

            String sql = "DELETE FROM `tbl_employee_details` WHERE eid = '" +id+"'";
            pst = (PreparedStatement) con.prepareStatement(sql);

            int i = pst.executeUpdate();
            sql ="DELETE FROM `tbl_employee_expenses` WHERE eid = '" +id+"'";
            pst = (PreparedStatement) con.prepareStatement(sql);

            i = pst.executeUpdate();
        } catch (Exception e) {
            

        }
    }

     public void ShowErrorMessage(String message, String title) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

    
    @FXML
    void delete_Employee(ActionEvent event) {
        getSelected();
        if(rowvalue>0){
        remove();
        
        }
        else
            JOptionPane.showMessageDialog(null, "No Item selected", "Error", JOptionPane.ERROR_MESSAGE);
        //remove();
        
    }
    
    
    

    @FXML
    void edit_Employee(ActionEvent event) {
         getSelected();
         if(rowvalue>0)
     edit();
         else
             JOptionPane.showMessageDialog(null, "No Item selected", "Error", JOptionPane.ERROR_MESSAGE);
        
    }
    

   @Override
    public void initialize(URL url, ResourceBundle rb) {
        show_Data();
    getSelected();
    
      
    }    
    
}
