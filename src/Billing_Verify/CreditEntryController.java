/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Billing_Verify;

import UsableMethods.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rubesh
 */
public class CreditEntryController implements Initializable {

    @FXML
    private JFXComboBox<String> comboBox_CustomerType;

    @FXML
    private JFXTextField textField_Name;

    @FXML
    private JFXTextField textField_Id;

    @FXML
    private Label label_TotalAmount;

    @FXML
    private JFXTextField textField_ReceivedAmount;

    @FXML
    private Label label_RemainingAmount;

    @FXML
    private JFXButton btn_Save;

    @FXML
    private JFXButton btn_Cancel;

    Message m = new Message();
    BillingVerifyFXMLController bvc = new BillingVerifyFXMLController();
    private double totalAmount = bvc.grandTotal;

    dbConnection dbc = new dbConnection();
    Connection conn = null;
//    PreparedStatement pst;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //POPULATE COMBOBOX
        comboBox_CustomerType.getItems().addAll("Old", "New");
        comboBox_CustomerType.getSelectionModel().clearAndSelect(0);
        //SET TOTAL AMOUNT FROM THE BILLING-VERIFY
        label_TotalAmount.setText(String.valueOf(totalAmount));

        System.out.println("Label = " + label_RemainingAmount.getText().isEmpty());
    }

    @FXML
    public void getID() {
        if (comboBox_CustomerType.getSelectionModel().getSelectedItem().equals("Old")) {
            try {

                String checker = null;
                conn = dbc.connection();

                String name = textField_Name.getText();
                String sql = "select * from `tbl_credit_sales` where `cname`='" + name + "'";

                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery(sql);
                while (rs.next()) {
                    checker = name;
                    textField_Id.setText(rs.getString("cid"));
                    textField_Name.setText(rs.getString("cname"));
                }
                if (!(checker.equals(name))) {
                    textField_Id.setText("");
                }

            } catch (Exception e) {
                textField_Id.setText("");
//                System.out.println("getID ");
//                e.printStackTrace();
            }
        }

    }

    @FXML
    public void getName() {
        if (comboBox_CustomerType.getSelectionModel().getSelectedItem().equals("Old")) {
            try {

                String checker = null;
                conn = dbc.connection();

                String id = textField_Id.getText();
                String sql = "select * from `tbl_credit_sales` where `cid`='" + id + "'";
                PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery(sql);
                while (rs.next()) {
                    checker = id;
                    textField_Id.setText(rs.getString("cid"));
                    textField_Name.setText(rs.getString("cname"));
                }
                if (!(checker.equals(id))) {
                    textField_Name.setText("");
                }

            } catch (Exception e) {
                textField_Name.setText("");
//                System.out.println("getName ");
//                e.printStackTrace();
            }
        }
    }

    @FXML
    private void calculation() {
        try {
            double receivedAmount = Double.valueOf(textField_ReceivedAmount.getText());
            double remainingAmount = totalAmount - receivedAmount;
            label_RemainingAmount.setText(String.valueOf(remainingAmount));
        } catch (Exception e) {
            label_RemainingAmount.setText("");
//            System.out.println("calculation");
        }
    }

    @FXML
    private void saveButton() {

        if (!(textField_Id.getText().isEmpty()) & !(textField_Name.getText().isEmpty()) & !(label_RemainingAmount.getText().isEmpty())) {

            String cType = comboBox_CustomerType.getSelectionModel().getSelectedItem();
            String cId = textField_Id.getText();
            String cName = textField_Name.getText();
            double receivedAmount = Double.valueOf(textField_ReceivedAmount.getText());
            double remainingAmount = Double.valueOf(label_RemainingAmount.getText());


            if (comboBox_CustomerType.getSelectionModel().getSelectedItem().equals("Old")) {

                //INSERT INTO TBL_CREDIT_SALES_SUBDETAILS
                try {
                    conn = dbc.connection();
                    String sql = "insert into tbl_credit_sales_subdetails values(?,?,?,?,?,?,?)";
                    PreparedStatement pst = conn.prepareStatement(sql);

                    pst.setString(1, null);
                    pst.setString(2, cId);
                    pst.setInt(3, bvc.billid);
                    pst.setString(4, m.getDate());
                    pst.setDouble(5, totalAmount);
                    pst.setDouble(6, receivedAmount);
                    pst.setDouble(7, remainingAmount);

                    pst.executeUpdate();

                    //INSERT INTO TBL_SALES AND TBL_SALES_DETAILS
                    try {
                        bvc.saveIntoDB();

                    } catch (Exception e) {

                        System.out.println("SaveButton-Old-TryCatch2");
                        e.printStackTrace();
                    }

                } catch (Exception e) {

                    System.out.println("SaveButton-Old-TryCatch1");
                    e.printStackTrace();
                }

            } else if (comboBox_CustomerType.getSelectionModel().getSelectedItem().equals("New")) {

                //INSERT INTO TBL_CREDIT_SALES
                try {

                    conn = dbc.connection();
                    String sql1 = "insert into tbl_credit_sales values(?,?,?)";
                    PreparedStatement pst = conn.prepareStatement(sql1);

                    pst.setString(1, cId);
                    pst.setString(2, cName);
                    pst.setDouble(3, 0.00);

                    pst.executeUpdate();

                    //INSERT INTO TBL_CREDIT_SALES_SUBDETAILS
                    try {

                        String sql2 = "insert into tbl_credit_sales_subdetails values(?,?,?,?,?,?)";
                        pst = conn.prepareStatement(sql2);

                        pst.setString(1, cId);
                        pst.setInt(2, bvc.billid);
                        pst.setString(3, m.getDate());
                        pst.setDouble(4, totalAmount);
                        pst.setDouble(5, receivedAmount);
                        pst.setDouble(6, remainingAmount);

                        pst.executeUpdate();

                        //INSERT INTO TBL_SALES AND TBL_SALES_DETAILS
                        try {
                            bvc.saveIntoDB();

                        } catch (Exception e) {

                            System.out.println("SaveButton-New-TryCatch3");
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        System.out.println("SaveButton-New-TryCatch2");
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    System.out.println("SaveButton-New-TryCatch1");
                    e.printStackTrace();
                }
            }

            //CLEAR LABELS AND TEXTFIELDS
            comboBox_CustomerType.getSelectionModel().clearSelection();
            textField_Id.clear();
            textField_Name.clear();
            label_TotalAmount.setText("");
            textField_ReceivedAmount.clear();
            label_RemainingAmount.setText("");
            btn_Save.setDisable(true);

        } else {
            m.alertMessage(AlertType.ERROR, "Fields are empty.", "Please fill the fields.");
        }

        
    }

    @FXML
    private void cancelButton() {
        if (!(btn_Save.isDisabled())) {
            textField_Id.clear();
            textField_Name.clear();
            textField_ReceivedAmount.clear();
            label_RemainingAmount.setText("");
        } else {
            Stage stage = (Stage) btn_Cancel.getScene().getWindow();
            stage.close();
        }
    }

}
