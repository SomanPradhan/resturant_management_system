/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PurchaseReport;

import NumberTextField.NumberTextField;
import PurchaseDetails.PurchaseDetailsController;
import UsableMethods.Database;
import UsableMethods.dbConnection;
import UsableMethods.table;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Soman
 */
public class PurchaseReportController implements Initializable {
    
    //comboBox 
    @FXML
    public ComboBox comboBox_ArrangeBy;

    @FXML
    public ComboBox comboBox_ParticularType;

    //NumberTextField 
    @FXML
    public NumberTextField textField_TotalVat;

    @FXML
    public NumberTextField textField_TotalCash;

    @FXML
    public NumberTextField textField_TotalCredit;

    @FXML
    public NumberTextField textField_TotalPurchase;
    
    @FXML
    public NumberTextField textField_TotalDiscount;

    //DatePicker
    @FXML
    public DatePicker Date_From;

    @FXML
    public DatePicker Date_To;

    //Table Column
    
    @FXML
    public TableView<table> table_Purchase;
    
    @FXML
    public TableColumn<table, String> column_Date;

    @FXML
    public TableColumn<table, Double> column_Credit; 
    
    @FXML
    public TableColumn<table, Double> column_Payment;
    
    @FXML
    public TableColumn<table, Integer> column_Invoice;

    @FXML
    public TableColumn<table, String> column_Did;

    @FXML
    public TableColumn<table, Double> column_PurchaseTotal;

    @FXML
    public TableColumn<table, Double> column_Discount;

    @FXML
    public TableColumn<table, String> column_Dname;
    
    @FXML
    public TableColumn<table, Integer> column_sn;

    @FXML
    public TableColumn<table, String> column_Particular;

    @FXML
    public TableColumn<table, Double> column_Vat;

    
    public ObservableList<table> data = FXCollections.observableArrayList();
    public ObservableList<String> arrangeByList = FXCollections
            .observableArrayList("Date","Did");
    public ObservableList<String> ParticularTypeList = FXCollections
            .observableArrayList("All","Cash","Credit");
    
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    dbConnection dbc = new dbConnection();
    
    public Integer index = 0;
    //public Integer public_Invoice = 0;
    public int invoice=0;
    public boolean checker = true;
    
     public void initialDatepickerCombobox(){
        Database db = new Database();
        Date_From.setValue(db.NOW_LOCAL_DATEfirstDateOfMonth());
        Date_To.setValue(db.NOW_LOCAL_DATEcurrentDate());
        
        comboBox_ArrangeBy.setItems(arrangeByList);
        comboBox_ArrangeBy.setValue("Date");
        comboBox_ParticularType.setItems(ParticularTypeList);
        comboBox_ParticularType.setValue("All");
        
        textField_TotalPurchase.setEditable(false);
        textField_TotalDiscount.setEditable(false);
        textField_TotalVat.setEditable(false);
        textField_TotalCash.setEditable(false);
        textField_TotalCredit.setEditable(false);
    }
    
    
    public void showTable(){
        try {
            
            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_purchase` WHERE ";
            String from_date = String.valueOf(Date_From.getValue());
            String to_date = String.valueOf(Date_To.getValue());
            String particularType = String.valueOf(comboBox_ParticularType.getValue());
            if(!(particularType.equalsIgnoreCase("All"))){
                sql = sql+"`particular` = '"+particularType+"' AND ";
            }
            sql = sql +"`date` >= '" + from_date + "' And `date` <= '" + to_date + "' ";
            String arrangeBy = String.valueOf(comboBox_ArrangeBy.getValue());
            if(arrangeBy.equalsIgnoreCase("Date")){
                sql = sql +"ORDER BY `date` DESC";
            }
            else{
                sql = sql+ "ORDER BY `did` ASC";
            }
            column_sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            column_Invoice.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
            column_Date.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            column_Did.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            column_Dname.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            column_Particular.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
            column_PurchaseTotal.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            column_Discount.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
            column_Vat.setCellValueFactory(new PropertyValueFactory<table, Double>("d3"));
            column_Payment.setCellValueFactory(new PropertyValueFactory<table, Double>("d4"));
            column_Credit.setCellValueFactory(new PropertyValueFactory<table, Double>("d5"));
            
            table_Purchase.getItems().clear();
            
            Double purchaseTotal = 0.0;
            Double discount = 0.0;
            Double vat = 0.0;
            Double payment = 0.0;
            Double credit = 0.0;
            
            int temp_sn = 1;
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while(rs.next()){
                table tbl = new table();
                
                tbl.i1.set(temp_sn++);
                tbl.i2.set(rs.getInt("invoice_id"));
                tbl.s1.set(rs.getString("date"));
                tbl.s2.set(rs.getString("did"));
                tbl.s3.set(rs.getString("dname"));
                tbl.s4.set(rs.getString("particular"));
                tbl.d1.set(rs.getDouble("purchase_total"));
                tbl.d2.set(rs.getDouble("discount"));
                tbl.d3.set(rs.getDouble("vat"));
                tbl.d4.set(rs.getDouble("payment"));
                tbl.d5.set(rs.getDouble("credit"));
                
                purchaseTotal += rs.getDouble("purchase_total");
                discount += rs.getDouble("discount");
                vat += rs.getDouble("vat");
                payment += rs.getDouble("payment");
                credit += rs.getDouble("credit");
                
                data.add(tbl);
            }
            
            textField_TotalPurchase.setText(String.valueOf(purchaseTotal));
            textField_TotalDiscount.setText(String.valueOf(discount));
            textField_TotalVat.setText(String.valueOf(vat));
            textField_TotalCash.setText(String.valueOf(payment));
            textField_TotalCredit.setText(String.valueOf(credit));
            table_Purchase.setItems(data);
            
        } catch (Exception e) {
            System.out.println("on showtable method of purchaseReportController.java");
            e.printStackTrace();
        }
        
    }
    
      public void showingPurchaseDetails(){
        try {
            getSelected();
            index--;
            Integer public_Invoice=null;
            if(index > -1){
             table tbl = data.get(index);
                    
                public_Invoice = tbl.i2.get();
            }
            if(public_Invoice!=null ){
            if(public_Invoice!=0 && checker == true){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PurchaseDetails/PurchaseDetails.fxml"));
            invoice = public_Invoice;
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            checker = false;
            stage.setScene(scene);
            stage.setResizable(false);
            PurchaseDetailsController showController = loader.getController();
            showController.showData(public_Invoice);
            refreshTable();
            stage.showAndWait();
            checker = true;
            public_Invoice=0;
            }
            }
        } catch (Exception e) {
            System.out.println("in showingPurchaseDetails method PurchaseReportController.java");
            e.printStackTrace();
        }
    }
    
     public void refreshTable() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                table_Purchase.requestFocus();
                table_Purchase.getSelectionModel().select(null);
                table_Purchase.getFocusModel().focus(null);
            }
        });
    }
    
     /* for showing the value of selected row*/
    public void getSelected() {
        try {
            column_sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            column_Invoice.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
            column_Date.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            column_Did.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            column_Dname.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            column_Particular.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
            column_PurchaseTotal.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            column_Discount.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
            column_Vat.setCellValueFactory(new PropertyValueFactory<table, Double>("d3"));
            column_Payment.setCellValueFactory(new PropertyValueFactory<table, Double>("d4"));
            column_Credit.setCellValueFactory(new PropertyValueFactory<table, Double>("d5"));
            
            table_Purchase.setItems(data);

            table_Purchase.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                   index = (data.indexOf(newValue)+1);
                }

            });
        } catch (Exception e) {
            System.out.println("on getSelected method of PurchaseReportController.java");
            e.printStackTrace();
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getSelected();
        initialDatepickerCombobox(); 
        showTable();
        
    }    
    
}
