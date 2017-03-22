/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PurchaseEntry;

import UsableMethods.dbConnection;
import NumberTextField.NumberTextField;
import UsableMethods.Database;
import UsableMethods.Message;
import UsableMethods.table;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author Soman
 */
public class PurchaseEntryController implements Initializable {

    /*ComboBoxes for choosing one option from many option*/
    @FXML
    public ComboBox combobox_DistributerType;

    @FXML
    public ComboBox combobox_Particular;

    /*Labels */
    @FXML
    public Label label_totalAmount;

    @FXML
    public Label label_Credit;

    /*JFXTextField*/
    @FXML
    public JFXTextField textField_PurchaseTotal;

    @FXML
    public JFXTextField textField_Did;

    @FXML
    public JFXTextField textField_DistributerName;

    @FXML
    public JFXTextField textField_InvoiceId;

    @FXML
    public JFXTextField textField_ItemCode;

    @FXML
    public JFXTextField textField_ItemName;

    /*Numeric JFXTextField*/
    @FXML
    public NumberTextField textField_SubTotal;

    @FXML
    public NumberTextField textField_Discount;

    @FXML
    public NumberTextField textField_VAT;

    @FXML
    public NumberTextField textField_Cash;

    @FXML
    public NumberTextField textField_credit;

    @FXML
    public NumberTextField textField_Quantity;

    @FXML
    public NumberTextField textField_CostPrice;

    /*buttons*/
    @FXML
    public JFXButton button_Enter;

    @FXML
    public JFXButton button_Delete;

    @FXML
    public JFXButton button_Save;

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

    /*observable string list for combo box*/
    ObservableList<String> distributerTypeList = FXCollections
            .observableArrayList("Old", "New");

    ObservableList<String> particularList = FXCollections
            .observableArrayList("Cash", "Credit");

    /*obsrvable list for table*/
    ObservableList<table> data = FXCollections.observableArrayList();
    ObservableList<table> data2 = FXCollections.observableArrayList();

    Message mess = new Message();

    public static Integer rowvalue = 0;

    Connection con;
    PreparedStatement pst;
    dbConnection dbc = new dbConnection();

    String distributerType;
    Integer static_sn = 1;

    public static boolean checkDid = true;
    public static boolean checkDname = true;


    /*for typing  Distributer*/
    public void whileTypingData_DistributerId() {
        if (distributerType.equals("Old")) {
            checkDB_Did(true);
        } else {
            checkDB_Did(false);
        }
    }

    public String captialLetter(KeyEvent event, String string) {

        if (!(string.isEmpty())) {
            if (string.charAt(0) >= 'a' && string.charAt(0) <= 'z') {
                char c = string.charAt(0);
                c = (char) (c - 32);
                String s = string.substring(1);
                string = c + s;
                return string;

            }
        }

        return string;
    }

    public void whileTypingData_ItemName(KeyEvent event) {
        /*/if ((event.getCode().isLetterKey()) && (textField_ItemName.getText().length() == 1)) {
            textField_ItemName.setText(captialLetter(event, textField_ItemName.getText()));
            textField_ItemName.forward();
        }*/

        try {
            String checker = null;
            String itemcode = "";
            String itemname = "";
            Double itemrate = 0.0;
            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_items_mp`";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while (rs.next()) {
                itemcode = rs.getString("item_code");
                itemname = rs.getString("item_name");
                itemrate = rs.getDouble("rate");
                if (itemname.compareToIgnoreCase(textField_ItemName.getText()) == 0) {
                    checker = "item name found";
                    break;
                }
            }
            if (checker != null) {
                textField_ItemCode.setText(itemcode);
                textField_ItemName.setText(itemname);
                textField_ItemName.end();
                //textField_CostPrice.setText(String.valueOf(itemrate));
            } else {
                textField_ItemCode.clear();
                //textField_CostPrice.clear();
            }
            amountSetter();

        } catch (Exception e) {
            System.out.println("in whileTypingData_ItemName method of PurchaseController.java");
            e.printStackTrace();
        }

    }

    public void whileTypingData_DistributerName(KeyEvent event) {
        /*if ((event.getCode().isLetterKey()) && textField_DistributerName.getText().length() == 1) {
            textField_DistributerName.setText(captialLetter(event, textField_DistributerName.getText()));
            textField_DistributerName.forward();
        }*/
        if (distributerType.equals("Old")) {
            checkDB_Dname(true);
        } else {
            checkDB_Dname(false);
        }
    }

    public void EnterData(ActionEvent event) {
        Double subTotal = 0.0;
        column_sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
        column_itemcode.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
        column_itemname.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
        column_quantity.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
        column_costprice.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
        column_amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));

        table tbl = new table();
        if (!(textField_ItemCode.getText().isEmpty())) {
            if (!(textField_ItemName.getText().isEmpty())) {
                if (!(textField_Quantity.getText().isEmpty()) && Double.parseDouble(textField_Quantity.getText()) != 0.0) {
                    if (!(textField_CostPrice.getText().isEmpty()) && Double.parseDouble(textField_CostPrice.getText()) != 0.0) {
                        if (!(label_totalAmount.getText().isEmpty()) && Double.parseDouble(label_totalAmount.getText()) != 0.0) {
                            tbl.i1.set(static_sn);
                            tbl.s1.set(textField_ItemCode.getText());
                            tbl.s2.set(textField_ItemName.getText());
                            tbl.i2.set(Integer.parseInt(textField_Quantity.getText()));
                            tbl.d1.set(Double.parseDouble(textField_CostPrice.getText()));
                            tbl.d2.set(Double.parseDouble(label_totalAmount.getText()));
                            Double totalAmount = Double.parseDouble(label_totalAmount.getText());
                            data.add(tbl);
                            updateSubtotal(Double.parseDouble(label_totalAmount.getText()));
                            textField_clear();
        
                        }
                        else{
                            mess.alertMessage(Alert.AlertType.ERROR, "AMOUNT ERROR", "No Amount");
                        }
                        }else{
                        mess.alertMessage(Alert.AlertType.ERROR, "COST PRICE ERROR", "No Cost Price");
                    }
                    
                }else{
                    mess.alertMessage(Alert.AlertType.ERROR, "QUANTITY ERROR", "No Quantity");
                }
            }else{
                mess.alertMessage(Alert.AlertType.ERROR, "ITEM NAME ERROR", "No Item Name");
            }
        }else{
            mess.alertMessage(Alert.AlertType.ERROR, "ITEM CODE ERROR", "No Item Code");
        }

        table_itemDetails.setItems(data);
        

        static_sn++;
    }

    public void clickedOnPurchaseTotal() {
        textField_PurchaseTotal.selectAll();
    }

    public void updateSubtotal(Double value) {
        Double subTotal = 0.0;
        if (!(textField_SubTotal.getText().isEmpty())) {
            subTotal = Double.parseDouble(textField_SubTotal.getText());

        }
        subTotal = subTotal + value;
        textField_SubTotal.setText(String.valueOf(subTotal));
        textField_PurchaseTotal.setText(String.valueOf(subTotal));
        cashAndPurchaseTotal();
    }

    public void calculateSubTotal() {
        if (!(textField_SubTotal.getText().isEmpty())) {
            Double subTotal = Double.parseDouble(textField_SubTotal.getText());
            Double discount = 0.0;
            Double vat = 0.0;
            if (!(textField_Discount.getText().isEmpty())) {
                discount = Double.parseDouble(textField_Discount.getText());
            }
            if (!(textField_VAT.getText().isEmpty())) {
                vat = Double.parseDouble(textField_VAT.getText());
            }
            Double purchaseTotal = subTotal - discount + vat;
            textField_PurchaseTotal.setText(String.valueOf(purchaseTotal));

        } else {
            textField_PurchaseTotal.clear();
        }
        cashAndPurchaseTotal();
    }

    public void cashAndPurchaseTotal() {
        if (combobox_Particular.getValue().equals("Cash")) {
            textField_Cash.setText(textField_PurchaseTotal.getText());
        }
    }

    public void textField_clear() {
        textField_ItemCode.clear();
        textField_ItemName.clear();
        textField_Quantity.clear();
        textField_CostPrice.clear();
        label_totalAmount.setText("total amount");
    }

    public void getSelected() {
        try {
            column_sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            column_itemcode.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            column_itemname.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            column_quantity.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
            column_costprice.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            column_amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));

            table_itemDetails.setItems(data);

            table_itemDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                    rowvalue = (data.indexOf(newValue) + 1);
                    //System.out.println("ON RowValue is : " + rowvalue);
                }

            });
        } catch (Exception e) {
            System.out.println("on getSelected method of PurchaseController.java");
            e.printStackTrace();
        }
    }

    public void DeleteData(ActionEvent event) {
        getSelected();
        if (rowvalue > 0) {
            remove();

            // clear_flag = false;
        } else {

            mess.alertMessage(Alert.AlertType.ERROR, "ERROR", "Nothing is selected");
        }
    }

    public void remove() {

        boolean ans = mess.alertOptionMessage("DELETE", "Are you sure ??");
        if (ans) {
            int row = rowvalue - 1;
            table tbl = data.get(row);
            Double amount = tbl.getD2();
//            Double subTotal = Double.parseDouble(textField_SubTotal.getText());
            updateSubtotal(-amount);
            data.remove(row);

            refreshTable();
            if (row != static_sn - 2) {
                rearrangeSn(row);

            } else {
                static_sn = static_sn - 1;
            }
        }
    }

    public void rearrangeSn(int index) {
        column_sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
        column_itemcode.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
        column_itemname.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
        column_quantity.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
        column_costprice.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
        column_amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));

        while (index < static_sn - 2) {

            table tbl = data.get(index);

            tbl.i1.set(index + 1);
            //data.remove(index);
            data.set(index, tbl);
            index = index + 1;
        }
        static_sn = index + 1;
    }

    public void refreshTable() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                table_itemDetails.requestFocus();
                table_itemDetails.getSelectionModel().select(null);
                table_itemDetails.getFocusModel().focus(null);
            }
        });
    }

    /*for while typing and inserting*/
    public void checkDB_Did(boolean var) {
        try {
            String did = "";
            String dname = "";
            String checker = null;
            con = dbc.connection();
            String text = textField_Did.getText();
            String sql = "SELECT * FROM `tbl_distributer`";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while (rs.next()) {
                did = rs.getString("did");
                dname = rs.getString("dname");
                if (did.equals(text)) {
                    checker = "found did";
                    break;
                }
            }
            if (checker != null && (var == true)) {
                textField_DistributerName.setText(dname);
            }
            if (checker == null && var != false) {
                textField_DistributerName.setText("");
            }
            if (checker != null && (var == false)) {
                checkDid = false;
            } else {
                checkDid = true;
            }

        } catch (Exception e) {
            System.out.println("in checkDB_Did method of Purchase.java");
            e.printStackTrace();
        }
    }

    public void getTotalPurchase() {
        String subTotal = textField_SubTotal.getText();
        String discount = textField_Discount.getText();
        String vat = textField_VAT.getText();
        if (!(subTotal.isEmpty()) && !(discount.isEmpty()) && !(vat.isEmpty())) {
            Double totalPurchase = Double.parseDouble(subTotal) - (Double.parseDouble(subTotal) * Double.parseDouble(discount) / 100);
            totalPurchase = (totalPurchase) + ((totalPurchase) * Double.parseDouble(vat) / 100);
            textField_PurchaseTotal.setText(String.valueOf(totalPurchase));

        } else {
            textField_PurchaseTotal.setText("");
        }
    }

    public void saveData(ActionEvent event) {
        try {
            if ((checkDid == true) && (checkDname == true)) {
                if (!(textField_Did.getText().isEmpty())) {
                    if (!(textField_DistributerName.getText().isEmpty())) {
                        if (!(textField_InvoiceId.getText().isEmpty())) {
                            if (!(textField_PurchaseTotal.getText().isEmpty())) {
                                if (!(textField_Cash.getText().isEmpty())) {
                                    if (!(data.isEmpty())) {
                                        String sql = "SELECT * FROM `tbl_purchase`";
                                        con = dbc.connection();
                                        boolean checking_id = true;
                                        pst = (PreparedStatement) con.prepareStatement(sql);
                                        ResultSet rs = pst.executeQuery(sql);
                                        while (rs.next()) {
                                            if (rs.getString("invoice_id").equals(textField_InvoiceId.getText())) {
                                                checking_id = false;
                                                break;
                                            }
                                        }
                                        if (checking_id) {
                                            Double discount = 0.0;
                                            Double vat = 0.0;
                                            Integer id = Integer.parseInt(textField_InvoiceId.getText());
                                            Database gettingDate = new Database();
                                            String date = String.valueOf(gettingDate.NOW_LOCAL_DATEcurrentDate());
                                            String did = textField_Did.getText();
                                            String dname = textField_DistributerName.getText();
                                            String particular = String.valueOf(combobox_Particular.getValue());
                                            Double subtotal = Double.parseDouble(textField_SubTotal.getText());
                                            if (!(textField_Discount.getText().isEmpty())) {
                                                discount = Double.parseDouble(textField_Discount.getText());
                                            }
                                            Double purchaseTotal = Double.parseDouble(textField_PurchaseTotal.getText());
                                            if (!(textField_VAT.getText().isEmpty())) {
                                                vat = Double.parseDouble(textField_VAT.getText());
                                            }
                                            Double cash = Double.parseDouble(textField_Cash.getText());
                                            Double credit = purchaseTotal - cash;

                                            InsertData insert = new InsertData();
                                            if (!(distributerType.equals("Old"))) {
                                                insert.InsertDataInDistributerTable(did, dname, credit);
                                            } else {
                                                insert.UpdateDataInDistributerTable(did, credit);
                                            }
                                            insert.InsertDataIntoPurchase(id, date, did, dname, particular, subtotal, discount, vat, cash, credit);

                                            table tbl = new table();
                                            int i = 0;
                                            while (i < static_sn - 2) {
                                                tbl = data.get(i);
                                                String itemcode = tbl.s1.getValue();
                                                String itemname = tbl.s2.getValue();
                                                Integer quantity = (tbl.i2.getValue());
                                                Double costprice = tbl.d1.getValue();
                                                Double amount = tbl.d2.getValue();
                                                System.out.println(itemcode + " " + itemname + "" + quantity + " " + costprice + " " + amount);

                                                insert.InsertDataIntoPurchaseDetails(id, itemcode, itemname, quantity, costprice, amount);
                                                i++;

                                            }
                                            static_sn = 1;
                                            clearUpperdata();

                                            data.clear();
                                            table_itemDetails.getItems().clear();
                                        } else {
                                            mess.alertMessage(Alert.AlertType.ERROR, "DUBLICATE DATA", "Dublicate Invoice_id");
                                        }

                                    } else {
                                        mess.alertMessage(Alert.AlertType.ERROR, "NO DATA", "Table is empty");
                                    }
                                } else {
                                    mess.alertMessage(Alert.AlertType.ERROR, "NO DATA", "Cash field empty");
                                }
                            } else {
                                mess.alertMessage(Alert.AlertType.ERROR, "NO DATA", "Total Purchase field empty");
                            }
                        } else {
                            mess.alertMessage(Alert.AlertType.ERROR, "NO DATA", "Invoice Id field empty");
                        }
                    } else {
                        mess.alertMessage(Alert.AlertType.ERROR, "NO DATA", "Distributer Name field empty");
                    }
                } else {
                    mess.alertMessage(Alert.AlertType.ERROR, "NO DATA", "Distributer Id Field empty");
                }
            } else {
                mess.alertMessage(Alert.AlertType.ERROR, "DUBLICATE DATA", "Already in the DB cannot create Distributer");
            }
        } catch (Exception e) {
            System.out.println("on savedata method of purchase controller");
            e.printStackTrace();
        }
    }

    public void clearUpperdata() {
        combobox_DistributerType.setValue("Old");
        combobox_Particular.setValue("Credit");
        textField_Did.clear();
        textField_InvoiceId.clear();
        textField_DistributerName.clear();
        textField_SubTotal.clear();
        textField_Discount.clear();
        textField_VAT.clear();
        textField_PurchaseTotal.clear();
        textField_Cash.clear();
        textField_credit.clear();
    }

    public void writingInCredit() {
        String credit = textField_credit.getText();
        if (combobox_Particular.getValue().equals("Credit") && !(textField_PurchaseTotal.getText().equalsIgnoreCase("total after VAT and Discount")) && !(credit.isEmpty())) {
            Double cash = Double.parseDouble(textField_PurchaseTotal.getText()) - Double.parseDouble(credit);
            textField_Cash.setText(String.valueOf(cash));
        } else {
            textField_Cash.clear();
        }
    }

    public void writingInCash() {
        String cash = textField_Cash.getText();
        if (combobox_Particular.getValue().equals("Credit") && !(textField_PurchaseTotal.getText().equalsIgnoreCase("total after VAT and Discount")) && !(cash.isEmpty())) {
            Double credit = Double.parseDouble(textField_PurchaseTotal.getText()) - Double.parseDouble(cash);
            textField_credit.setText(String.valueOf(credit));
        } else {
            textField_credit.clear();
        }
    }

    /*getting amount value*/
    public void amountSetter() {
        String quantity = textField_Quantity.getText();
        String rate = textField_CostPrice.getText();
        if (!(quantity.isEmpty()) && !(rate.isEmpty())) {
            Double amount = Double.parseDouble(quantity) * Double.parseDouble(rate);
            label_totalAmount.setText(String.valueOf(amount));
        } else {
            label_totalAmount.setText("total amount");
        }
    }

    public void whileTypingData_ItemCode() {
        try {
            String checker = null;
            String itemcode = "";
            String itemname = "";
            Double itemrate = 0.0;
            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_items_mp`";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while (rs.next()) {
                itemcode = rs.getString("item_code");
                itemname = rs.getString("item_name");
                itemrate = rs.getDouble("rate");
                if (itemcode.equals(textField_ItemCode.getText())) {
                    checker = "item code found";
                    break;
                }
            }
            if (checker != null) {
                textField_ItemName.setText(itemname);
                //textField_CostPrice.setText(String.valueOf(itemrate));
            } else {
                textField_ItemName.clear();
                //textField_CostPrice.clear();
            }
            amountSetter();

        } catch (Exception e) {
            System.out.println("in whileTypingData_Itemcode method of PurchaseController.java");
            e.printStackTrace();
        }

    }

    public void checkDB_Dname(boolean var) {
        try {
            String did = "";
            String dname = "";
            String checker = null;
            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_distributer`";
            pst = (PreparedStatement) con.prepareCall(sql);
            ResultSet rs = pst.executeQuery(sql);
            while (rs.next()) {
                did = rs.getString("did");
                dname = rs.getString("dname");

                if (dname.compareToIgnoreCase(textField_DistributerName.getText()) == 0) {
                    checker = "found dname";
                    break;
                }
            }
            if (checker != null && var == true && !(did.equals(textField_Did.getText()))) {
                textField_Did.setText(did);
                textField_DistributerName.setText(dname);
                textField_DistributerName.end();
            }

            if (checker == null && var != false) {
                textField_Did.clear();

            }
            if (checker != null && (var == false)) {
                checkDname = false;
            } else {
                checkDname = true;
            }
        } catch (Exception e) {
            System.out.println("in checkDB_Dname method of purchase.java");
            e.printStackTrace();
        }

    }

    public void distributerTypeAction(ActionEvent event) {

        distributerType = String.valueOf(combobox_DistributerType.getValue());
        System.out.println(distributerType);
    }

    /*For initilizating combobox*/
    public void initializeComboBoxAndSubTotalTextField() {
        /*for  distributer type*/
        combobox_DistributerType.setValue("Old");
        combobox_DistributerType.setItems(distributerTypeList);

        distributerType = String.valueOf(combobox_DistributerType.getValue());

        /*for particular*/
        combobox_Particular.setValue("Credit");
        combobox_Particular.setItems(particularList);

    }

    public void particularComboBoxFunction(ActionEvent event) {
        if (combobox_Particular.getValue().equals("Cash")) {
            label_Credit.setVisible(false);
            textField_credit.setVisible(false);
            textField_Cash.setEditable(false);
            if (!(textField_PurchaseTotal.getText().equalsIgnoreCase("total after VAT and Discount"))) {
                textField_Cash.setText(textField_PurchaseTotal.getText());
            }
        } else {
            textField_credit.setVisible(true);
            label_Credit.setVisible(true);
            textField_Cash.setEditable(true);
            textField_Cash.clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getSelected();
        initializeComboBoxAndSubTotalTextField();
    }

}
