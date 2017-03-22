/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CreditPurchase;

import NumberTextField.NumberTextField;
import UsableMethods.Database;
import UsableMethods.Message;
import UsableMethods.dbConnection;
import UsableMethods.sqlForCashinhand;
import UsableMethods.table;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Soman
 */
public class CreditPurchaseController implements Initializable {

    /*datepickers*/
    @FXML
    public DatePicker date_From;

    @FXML
    public DatePicker date_To;

    /*textField and NumericTextField*/
    @FXML
    public JFXTextField textField_Did;

    @FXML
    public JFXTextField textField_Dname;

    @FXML
    public NumberTextField textField_PreDue;

    @FXML
    public NumberTextField textField_PaymentAmount;

    @FXML
    public NumberTextField textField_RemainingAmount;

    /*Buttons*/
    @FXML
    public JFXButton button_Save;

    @FXML
    public JFXButton button_Cancel;

    /*table for uppertable*/
    @FXML
    public TableView<table> table_Distributer;

    @FXML
    public TableColumn<table, Integer> column_detailSn;

    @FXML
    public TableColumn<table, String> column_detailDid;
    @FXML
    public TableColumn<table, String> column_detailDname;

    @FXML
    public TableColumn<table, Double> column_detailRemainingAmount;

    /*lower table*/
    @FXML
    public TableView<table> table_purchaseDetails;

    @FXML
    public TableColumn<table, Integer> column_purchaseSn;

    @FXML
    public TableColumn<table, String> column_purchaseDname;

    @FXML
    public TableColumn<table, Integer> column_purchaseInvoice;

    @FXML
    public TableColumn<table, String> column_purchaseDate;

    @FXML
    public TableColumn<table, Double> column_purchasePayment;

    @FXML
    public TableColumn<table, Double> column_purchaseTotal;

    @FXML
    public TableColumn<table, String> column_purchaseParticular;

    @FXML
    public TableColumn<table, Double> column_purchaseCredit;

    Connection con = null;
    PreparedStatement pst = null;
    dbConnection dbc = new dbConnection();

    //array list data for upper table
    final ObservableList<table> data = FXCollections.observableArrayList();

    //array list data for lower table
    final ObservableList<table> data2 = FXCollections.observableArrayList();

    Message msg = new Message();

    public int rowvalue = 0;

    public void showDistributerTable() {
        try {
            column_detailSn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            column_detailDid.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            column_detailDname.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            column_detailRemainingAmount.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            table_Distributer.getItems().clear();

            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_distributer`";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            int temp_sn = 1;
            while (rs.next()) {
                table tbl = new table();
                tbl.i1.set(temp_sn++);
                tbl.s1.set(rs.getString("did"));
                tbl.s2.set(rs.getString("dname"));
                tbl.d1.set(rs.getDouble("remaining_amount"));
                data.add(tbl);
            }
            table_Distributer.setItems(data);
        } catch (Exception e) {
            System.out.println("in showDistributerTable CreditPurachaseController.java");
            e.printStackTrace();
        }

    }

    public void initialDate() {
        Database db = new Database();
        date_From.setValue(db.NOW_LOCAL_DATEfirstDateOfMonth());
        date_To.setValue(db.NOW_LOCAL_DATEcurrentDate());
    }

    public void getSelected() {
        try {
            column_detailSn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            column_detailDid.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            column_detailDname.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            column_detailRemainingAmount.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            table_Distributer.setItems(data);

            table_Distributer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                    rowvalue = (data.indexOf(newValue) + 1);
                    //System.out.println("ON RowValue is : " + rowvalue);
                }
            });
        } catch (Exception e) {
            System.out.println("on getSelected method of payrollController.java");
            e.printStackTrace();
        }
    }

    public void clickOnDistributertable() {
        getSelected();
        showPurchaseTable();
    }

    public void showPurchaseTable() {
        try {
            Integer row = rowvalue - 1;
            String distributerName = null;
            table tbl = new table();
            if (row > -1) {
                tbl = data.get(row);
                distributerName = tbl.s2.get();
            }

            column_purchaseSn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            column_purchaseDname.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            column_purchaseInvoice.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
            column_purchaseDate.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            column_purchaseParticular.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            column_purchaseTotal.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            column_purchasePayment.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
            column_purchaseCredit.setCellValueFactory(new PropertyValueFactory<table, Double>("d3"));
            table_purchaseDetails.getItems().clear();

            if (distributerName != null) {
                con = dbc.connection();
                String from_date = String.valueOf(date_From.getValue());
                String to_date = String.valueOf(date_To.getValue());

                textField_Did.setText(tbl.s1.get());
                textField_Dname.setText(tbl.s2.get());
                textField_PreDue.setText(String.valueOf(tbl.d1.get()));
                String sql = "SELECT * FROM `tbl_purchase` WHERE `dname` = '" + distributerName + "' AND `date` >= '" + from_date + "' And `date` <= '" + to_date + "' ORDER BY `date` DESC";
                pst = (PreparedStatement) con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery(sql);
                int temp_sn = 1;
                while (rs.next()) {
                    table tbl1 = new table();
                    tbl1.i1.set(temp_sn++);
                    tbl1.s1.set(rs.getString("dname"));
                    tbl1.i2.set(rs.getInt("invoice_id"));
                    tbl1.s2.set(rs.getString("date"));
                    tbl1.s3.set(rs.getString("particular"));
                    tbl1.d1.set(rs.getDouble("purchase_total"));
                    tbl1.d2.set(rs.getDouble("payment"));
                    tbl1.d3.set(rs.getDouble("credit"));

                    data2.add(tbl1);
                }
                table_purchaseDetails.setItems(data2);
            }
        } catch (Exception e) {
            System.out.println("in showPurchaseTable creditPurchaseController.java");
            e.printStackTrace();
        }
    }

    public void whileWrittingonDid() {
        try {
            con = dbc.connection();
            String did = textField_Did.getText();
            String sql = "SELECT * FROM `tbl_distributer` WHERE `did` = '" + did + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            boolean checker = true;
            while (rs.next()) {
                checker = false;
                break;
            }
            if (checker == true) {
                textField_Dname.clear();
                textField_PreDue.clear();
            } else {
                textField_Dname.setText(rs.getString("dname"));
                textField_Dname.end();
                textField_PreDue.setText(rs.getString("remaining_amount"));
                textField_PreDue.end();

            }
        } catch (Exception e) {
            System.out.println("in whileWrittingonDid method of creditPurchaseController.java");
            e.printStackTrace();
        }
    }

    public void onClickCancel() {
        textField_Did.clear();
        textField_Dname.clear();
        textField_PreDue.clear();
        textField_PaymentAmount.clear();
        textField_RemainingAmount.clear();
    }

    public void onClickSave() {
        try {
            con = dbc.connection();
            if (!(textField_Did.getText().isEmpty())) {
                if (!(textField_Dname.getText().isEmpty())) {
                    if (!(textField_PreDue.getText().isEmpty()) && Double.parseDouble(textField_PreDue.getText()) != 0.0) {
                        if (!(textField_PaymentAmount.getText().isEmpty()) && Double.parseDouble(textField_PaymentAmount.getText()) != 0.0) {
                            if (!(textField_RemainingAmount.getText().isEmpty())) {
                                String did = textField_Did.getText();
                                String dname = textField_Dname.getText();
                                Double vat = 0.0;
                                Integer id = 0;
                                String particular = "Cash";
                                Double discount = 0.0;
                                Double purchase_total = 0.0;
                                String payment = textField_PaymentAmount.getText();
                                String credit = textField_RemainingAmount.getText();
                                Database db = new Database();
                                String date = String.valueOf(db.NOW_LOCAL_DATEcurrentDate());
                                Double remainingAmount = Double.parseDouble(textField_RemainingAmount.getText());
                                String sql = "UPDATE `tbl_distributer` SET `remaining_amount` = '" + remainingAmount + "' WHERE `did` = '" + did + "'";
                                pst = (PreparedStatement) con.prepareStatement(sql);
                                pst.executeUpdate();
                                int invoice = 0;
                                sql = "SELECT * FROM `tbl_purchase` ORDER BY invoice_id DESC LIMIT 1";
                                pst = (PreparedStatement) con.prepareStatement(sql);
                                ResultSet rs = pst.executeQuery(sql);
                                while(rs.next()){
                                    invoice = rs.getInt("invoice_id");
                                }
                                invoice++;
                                sql = "INSERT INTO `tbl_purchase`(`invoice_id`, `date`, `did`, `dname`, `particular`, `purchase_total`, `discount`, `vat`, `payment`, `credit`) VALUES ('" + invoice + "', '" +date + "', '" + did + "', '" + dname + "', '" + particular + "', '" + purchase_total + "', '" + discount + "', '" + vat + "', '" + payment + "', '" + credit + "')";
                                pst = (PreparedStatement) con.prepareStatement(sql);
                                pst.executeUpdate();
                                sqlForCashinhand cash = new sqlForCashinhand();
                                cash.insertInCashInhand(date, "invoice id: "+id, Double.parseDouble(payment), null);
                                onClickCancel();
                                showDistributerTable();
                                column_purchaseSn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
                                column_purchaseDname.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
                                column_purchaseInvoice.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
                                column_purchaseDate.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
                                column_purchaseParticular.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
                                column_purchaseTotal.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
                                column_purchasePayment.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
                                column_purchaseCredit.setCellValueFactory(new PropertyValueFactory<table, Double>("d3"));
                                table_purchaseDetails.getItems().clear();

                                String from_date = String.valueOf(date_From.getValue());
                                String to_date = String.valueOf(date_To.getValue());

                                sql = "SELECT * FROM `tbl_purchase` WHERE `dname` = '" + dname + "' AND `date` >= '" + from_date + "' And `date` <= '" + to_date + "' ORDER BY `date` DESC";
                                pst = (PreparedStatement) con.prepareStatement(sql);
                                rs = pst.executeQuery(sql);
                                int temp_sn = 1;
                                while (rs.next()) {
                                    table tbl1 = new table();
                                    tbl1.i1.set(temp_sn++);
                                    tbl1.s1.set(rs.getString("dname"));
                                    tbl1.i2.set(rs.getInt("invoice_id"));
                                    tbl1.s2.set(rs.getString("date"));
                                    tbl1.s3.set(rs.getString("particular"));
                                    tbl1.d1.set(rs.getDouble("purchase_total"));
                                    tbl1.d2.set(rs.getDouble("payment"));
                                    tbl1.d3.set(rs.getDouble("credit"));

                                    data2.add(tbl1);
                                }
                                table_purchaseDetails.setItems(data2);
                            }else{
                            msg.alertMessage(Alert.AlertType.ERROR, "REMAINING AMOUNT ERROR", "Field is Empty");
                            }
                        }else{
                            msg.alertMessage(Alert.AlertType.ERROR, "PAYMENT AMOUNT ERROR", "No Payment Amount");
                        }
                    }else{
                        msg.alertMessage(Alert.AlertType.ERROR, "PREVIOUS DUE ERROR", "No Previous Due Amount");
                    }
                }else{
                    msg.alertMessage(Alert.AlertType.ERROR, "DISTRIBUTER NAME ERROR", "No Distributer Name");
                }
            }else{
                msg.alertMessage(Alert.AlertType.ERROR, "DISTRIBUTER ID ERROR", "No Distributer Id");
            }

        } catch (Exception e) {
            System.out.println("in onClickSave CreditPurchaseController.java");
            e.printStackTrace();
        }
    }

    public void whileWritting() {
        Double preDue = 0.0;
        Double paymentAmount = 0.0;
        if (!(textField_PreDue.getText().isEmpty())) {
            preDue = Double.parseDouble(textField_PreDue.getText());
        }
        if (!(textField_PaymentAmount.getText().isEmpty())) {
            paymentAmount = Double.parseDouble(textField_PaymentAmount.getText());
        }
        Double amount = preDue - paymentAmount;
        textField_RemainingAmount.setText(String.valueOf(amount));
    }

    public void whileWrittingonDname() {
        try {
            con = dbc.connection();
            String dname = textField_Dname.getText();
            String sql = "SELECT * FROM `tbl_purchase` WHERE `dname` = '" + dname + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            boolean checker = true;
            while (rs.next()) {
                checker = false;
                break;
            }
            if (checker == true) {
                textField_Did.clear();
                textField_PreDue.clear();
            } else {
                textField_Did.setText(rs.getString("did"));
                textField_Did.end();
                textField_PreDue.setText(rs.getString("remaining_amount"));
                textField_PreDue.end();
            }
        } catch (Exception e) {
            System.out.println("in whileWrittingonDname method of creditPurchaseController.java");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getSelected();
        showDistributerTable();

        initialDate();
    }

}
