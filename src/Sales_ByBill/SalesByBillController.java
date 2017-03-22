/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sales_ByBill;

import UsableMethods.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author User
 */
public class SalesByBillController implements Initializable {

    //Defining the 1st table for Sales
    @FXML
    public TableView<table> table_Sales;
    @FXML
    public TableColumn<table, Integer> sn;
    @FXML
    public TableColumn<table, String> date;
    @FXML
    public TableColumn<table, Integer> bill_id;
    @FXML
    public TableColumn<table, Double> total_amount;

    //Defining the 2nd table for Sales_details
    public TableView<table> table_Sales_details;
    @FXML
    public TableColumn<table, Integer> sn1;
    @FXML
    public TableColumn<table, String> particulars;
    @FXML
    public TableColumn<table, Integer> quantity;
    @FXML
    public TableColumn<table, Double> rate;
    @FXML
    public TableColumn<table, Double> amount;

    //defining the labels
    @FXML
    private DatePicker Date_From;

    @FXML
    private DatePicker Date_To;

    @FXML
    private Label lable_BillNo;

    @FXML
    private Label label_TotalSalesAmount;

    //db connection
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    dbConnection dbc = new dbConnection();

    final ObservableList<table> data = FXCollections.observableArrayList();
    final ObservableList<table> data2 = FXCollections.observableArrayList();
    public static Integer rowvalue = 0;

    
    public void getSelected() {
        try {
            sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            bill_id.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
            date.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            total_amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            
            table_Sales.setItems(data);

            table_Sales.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                    rowvalue = (data.indexOf(newValue) + 1);
                    System.out.println("ON RowValue is : " + rowvalue);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showUpperTable() {
        try {
            Double total_salesAmount =0.0;
            String date_from = String.valueOf(Date_From.getValue());
            String date_to = String.valueOf(Date_To.getValue());
            sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            bill_id.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
            date.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            total_amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            table_Sales.getItems().clear();
            int temp_sn=1;
            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_sales` WHERE `date` >= '" + date_from + "' And `date` <= '" + date_to + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while (rs.next()){
                
                table tbl1 = new table();
                
                tbl1.i1.set(temp_sn++);
                tbl1.i2.set(rs.getInt("bill_id"));
                tbl1.s1.set(rs.getString("date"));
                tbl1.d1.set(rs.getDouble("grand_total_amount"));
                total_salesAmount = total_salesAmount + rs.getDouble("grand_total_amount");
                data.add(tbl1);
                
                
            }
            label_TotalSalesAmount.setText(String.valueOf(total_salesAmount));
            table_Sales.setItems(data);
            
            table_Sales_details.getItems().clear();
            lable_BillNo.setText("");
            
            System.out.println("upper table");
                    } catch (Exception e) {
            System.out.println("showUpperTable method of salesbybill");
            e.printStackTrace();
        }
    }
    
      //for current date value and datepicker_to
    public static final LocalDate NOW_LOCAL_DATE_TO() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    //for date value with current month and year but day 1 which is use by datepicker_from
    public static final LocalDate NOW_LOCAL_DATE_FROM() {
        String date = new SimpleDateFormat("yyyy-MM-01").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }
    public void initialDate(){
        Date_From.setValue(NOW_LOCAL_DATE_FROM());
        Date_To.setValue(NOW_LOCAL_DATE_TO());
        Date_From.setEditable(false);
        Date_To.setEditable(false);
    }
    
    public void use_upperTable_for_lowerTable() {
        try {

            getSelected();
            Integer id = null;
            Integer new_row = rowvalue - 1;
            if(new_row>-1){
                table tbl = new table();
               tbl =  data.get(new_row);
          
            id = tbl.i2.get();
            showLowerTable(id);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void showLowerTable(int id){
        try {
            lable_BillNo.setText(String.valueOf(id));
            
            sn1.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            particulars.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            quantity.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
            rate.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
            table_Sales_details.getItems().clear();
            int sn_temp = 1;
            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_sales_details` WHERE `bill_id`='"+id+"'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while(rs.next()){
                table tbl2 = new table();
                tbl2.i1.set(sn_temp++);
                tbl2.s1.set(rs.getString("item_name"));
                tbl2.i2.set(rs.getInt("quantity"));
                tbl2.d1.set(rs.getDouble("price"));
                tbl2.d2.set(rs.getDouble("amount"));
                
                data2.add(tbl2);
            }
            table_Sales_details.setItems(data2);
        } catch (Exception e) {
            System.out.println("showLowerTable method of salesbybill_controller");
            e.printStackTrace();
        }
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialDate();
        showUpperTable();
        getSelected();
        

    }

}
