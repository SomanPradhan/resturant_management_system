/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccountSetting;

import AccountSetting_AddEdit.UserDatabase;
import AccountSetting_AddEdit.FXMLUserAddEditController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import UsableMethods.dbConnection;
import UsableMethods.Message;
import UsableMethods.table;
import com.jfoenix.controls.JFXButton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * FXML Controller class
 *
 * @author Soman
 */
public class FXMLAccountingController implements Initializable {
     @FXML
    public TableColumn<table, String> Username_Column;

    @FXML
    public JFXButton btn_Add;

    @FXML
    public TableColumn<table, String> DateModified_Column;

    @FXML
    public TableColumn<table, Integer> SN_Column;

    @FXML
    public TableColumn<table, String> Id_Column;

    @FXML
    public TableColumn<table, String> Password_Column;

    @FXML
    public TableColumn<table, String> Privileges_Column;

    @FXML
    public JFXButton btn_Remove;

    @FXML
    public TableView<table> table_AccountSetting;

    @FXML
    public JFXButton btn_Edit;
    
    Message mess = new Message();
    public static int rowvalue=0;
    public static int listvalue=0;
    Connection con = null;
    PreparedStatement pst = null;
    dbConnection dbc = new dbConnection();
    UserDatabase db = new UserDatabase();

    final ObservableList<table> data = FXCollections.observableArrayList();
    public void runAdd(ActionEvent event){
        runAddEdit("","","","");
       
    }
       public void runAddEdit(String id,String username,String password,String privileges){
           try {
            if(listvalue==0){
            listvalue=listvalue+1;
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AccountSetting_AddEdit/FXMLUserAddEdit.fxml"));
            
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Add or Edit");
            stage.setResizable(false);
           
            FXMLUserAddEditController showController = loader.getController();
            showController.setValue(id,username,password,privileges);
            
            stage.showAndWait();
            // stage.onCloseRequestProperty();
            if(showController.variBoo()==true)
            {run();
            listvalue=0;
          System.out.println("working");
        
            }
            }
            else
                mess.alertMessage(Alert.AlertType.ERROR, "ERROR", "Already Add/Edit running");
                  } catch (Exception ex) {
                      System.out.println("se");
            ex.printStackTrace();
        }
       }
    public void runRemove(ActionEvent event){
        getSelected();
        if(rowvalue>0)
        remove();
        else
            mess.alertMessage(Alert.AlertType.ERROR, "ERROR", "No Item selected");
        }
    public void remove(){
       boolean ans = mess.alertOptionMessage("DELETE", "Are you sure ??");
        if (ans) {
        rowvalue--;
        if(rowvalue>-1){  
            System.out.println("rowvalue is="+rowvalue);
        UserDatabase valuepass = new UserDatabase();
        valuepass.DeleteRow(valuepass.SelectedSn(rowvalue));
        run();
       }
        }
    }
    public void getSelected(){
            try{
             Id_Column.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
             Username_Column.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
             Password_Column.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
             Privileges_Column.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
             DateModified_Column.setCellValueFactory(new PropertyValueFactory<table, String>("s5"));
             table_AccountSetting.setItems(data);
             
        table_AccountSetting.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
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
    
    public void runEdit(ActionEvent event){
     getSelected();
       if(rowvalue>0)
        edit();
        else
           mess.alertMessage(Alert.AlertType.ERROR, "ERROR", "No Item selected");
        
    }
    public void edit(){
        if(rowvalue>-1){
        rowvalue--;
        String id=null,username = null,password = null,privileges = null;
        UserDatabase valuepass = new UserDatabase();
        id=valuepass.SelectedSn(rowvalue);
        try{
           con = dbc.connection();
           String sql = "SELECT * FROM `tbl_user` WHERE id='"+id+"'";
            ResultSet rs = con.createStatement().executeQuery(sql);
              while (rs.next()) {

                username = rs.getString("username");
                System.out.println("THE SELECTED  username is " + username);
                password=rs.getString("password");
                System.out.println("THE SELECTED password is "+password);
                privileges=rs.getString("privileges");
                System.out.println("THE SELECTED privileges is"+privileges);
              }
              runAddEdit(id, username, password,privileges);
            run(); 
        }catch(Exception e){
            
        }
        
       }
    }
    public void run(){
      try { 
          
             String sql = "select * from tbl_user";
             
             System.out.println("PreparedStatement created Successfully");
             
             //assert table_AccountSetting != null : "fx:id=\"table_AccountSetting\" was not injected: check your FXML file 'FXMLDocument.fxml'.";
             con=dbc.connection();
             pst = (PreparedStatement) con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery(sql);
              int temp_sn=1;
             SN_Column.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
             Id_Column.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
             Username_Column.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
             Password_Column.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
             Privileges_Column.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
             DateModified_Column.setCellValueFactory(new PropertyValueFactory<table, String>("s5"));
             
             
            table_AccountSetting.getItems().clear();
          
             
             while (rs.next()){
                 table as = new table();
                 
                 as.i1.set(temp_sn++);
                 
                 as.s1.set(rs.getString("id"));
                 as.s2.set(rs.getString("username"));
                 as.s3.set(rs.getString("password"));
                 as.s4.set(rs.getString("privileges"));
                
                 as.s5.set(String.valueOf(rs.getDate("date_modified")));
                 data.add(as);
             }
             table_AccountSetting.setItems(data);
             
         } catch (SQLException ex) {
            ex.printStackTrace();
         }
    
    }        
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
      run();
    getSelected();
}
         }