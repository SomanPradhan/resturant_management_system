/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PurchaseDetails;

import NumberTextField.NumberTextField;
import UsableMethods.dbConnection;
import UsableMethods.table;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Soman
 */
public class PurchaseDetailsController implements Initializable {

    /*table view */
    @FXML
    public TableView<table> table_itemDetails;

    @FXML
    public TableColumn<table, Integer> column_sn;

    @FXML
    public TableColumn<table, String> column_itemcode;

    @FXML
    public TableColumn<table, String> column_itemname;

    @FXML
    public TableColumn<table, Integer> column_quantity;

    @FXML
    public TableColumn<table, Double> column_costprice;

    @FXML
    public TableColumn<table, Double> column_amount;
    
    
    //textField 
    @FXML
    public JFXTextField textField_Invoice;
    
    @FXML
    public JFXTextField textField_DistributerName;
    
    @FXML
    public JFXTextField textField_Particular;
    
    @FXML 
    public JFXTextField textField_Date;
    
    @FXML 
    public JFXTextField textField_Did;
    
    @FXML
    public NumberTextField textField_SubTotal;
    
    @FXML
    public NumberTextField textField_Discount;
    
    @FXML
    public NumberTextField textField_VAT;
    
    @FXML
    public NumberTextField textField_PurchaseTotal;
    
    @FXML
    public NumberTextField textField_Cash;
    
    @FXML
    public NumberTextField textField_Credit;
    
    
    
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    dbConnection dbc = new dbConnection();
          
    public ObservableList<table> data = FXCollections.observableArrayList();
    
    public void showData(Integer invoice){
        try {
            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_purchase` WHERE `invoice_id` = '"+invoice+"'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while(rs.next()){
                textField_Invoice.setText(rs.getString("invoice_id"));
                textField_DistributerName.setText(rs.getString("dname"));
                textField_Particular.setText(rs.getString("particular"));
                textField_Date.setText(rs.getString("date"));
                textField_Did.setText(rs.getString("did"));
                textField_SubTotal.setText(rs.getString("purchase_total"));
                textField_Discount.setText(rs.getString("discount"));
                textField_VAT.setText(rs.getString("vat"));
                textField_Cash.setText(rs.getString("payment"));
                textField_Credit.setText(rs.getString("credit"));
                Double PurchaseTotal = rs.getDouble("payment")+rs.getDouble("credit");
                textField_PurchaseTotal.setText(String.valueOf(PurchaseTotal));
                
                textField_Invoice.setEditable(false);
                textField_DistributerName.setEditable(false);
                textField_Particular.setEditable(false);
                textField_Date.setEditable(false);
                textField_Did.setEditable(false);
                textField_SubTotal.setEditable(false);
                textField_Discount.setEditable(false);
                textField_VAT.setEditable(false);
                textField_Cash.setEditable(false);
                textField_Credit.setEditable(false);
                textField_PurchaseTotal.setEditable(false);
                
                textField_Invoice.setFocusTraversable(false);
                textField_DistributerName.setFocusTraversable(false);
                textField_Particular.setFocusTraversable(false);
                textField_Date.setFocusTraversable(false);
                textField_Did.setFocusTraversable(false);
                textField_SubTotal.setFocusTraversable(false);
                textField_Discount.setFocusTraversable(false);
                textField_VAT.setFocusTraversable(false);
                textField_Cash.setFocusTraversable(false);
                textField_Credit.setFocusTraversable(false);
                textField_PurchaseTotal.setFocusTraversable(false);
                
                table_itemDetails.setFocusTraversable(false);
            }
            
            sql = "SELECT * FROM `tbl_purchase_details` WHERE `invoice_id` = '"+invoice+"'";
            pst = (PreparedStatement) con.prepareStatement(sql);
        column_sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
        column_itemcode.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
        column_itemname.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
        column_quantity.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
        column_costprice.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
        column_amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
        
        table_itemDetails.getItems().clear();
        int temp_sn = 1;

        table tbl = new table();
        rs = pst.executeQuery(sql);
        while(rs.next()){
            tbl.i1.set(temp_sn);
            tbl.s1.set(rs.getString("item_code"));
            tbl.s2.set(rs.getString("item_name"));
            tbl.i2.set(rs.getInt("quantity"));
            tbl.d1.set(rs.getDouble("cost_price"));
            tbl.d2.set(rs.getDouble("amount"));
            
            data.add(tbl);
        }
        
            table_itemDetails.setItems(data);
        } catch (Exception e) {
            System.out.println("in showData of PurchaseReportController");
            e.printStackTrace();
        }
        
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
