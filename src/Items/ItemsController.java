/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

import NumberTextField.NumberTextField;
import UsableMethods.Message;
import UsableMethods.dbConnection;
import UsableMethods.table;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Soman
 */
public class ItemsController implements Initializable {

    /*comboBoxes*/
    @FXML
    public ComboBox comboBox_inputType;

    @FXML
    public ComboBox comboBox_itemType;

    @FXML
    public ComboBox comboBox_catId;

    @FXML
    public ComboBox comboBox_catName;

    @FXML
    public ComboBox comboBox_subCatId;

    @FXML
    public ComboBox comboBox_subCatName;

    /*buttons*/
    @FXML
    public JFXButton button_Delete;

    @FXML
    public JFXButton button_Edit;

    @FXML
    public JFXButton button_Save;

    @FXML
    public JFXButton button_Cancel;

    /*textFields*/
    @FXML
    public JFXTextField textField_itemName;

    @FXML
    public JFXTextField textField_catId;

    @FXML
    public JFXTextField textField_catName;

    @FXML
    public JFXTextField textField_subCatName;

    @FXML
    public NumberTextField textField_Rate;

    @FXML
    public NumberTextField textField_costPrice;

    @FXML
    public JFXTextField textField_subCatId;

    @FXML
    public JFXTextField textField_itemCode;

    /*table view*/
    @FXML
    public TableView<table> table_ItemDetails;

    @FXML
    public TableColumn<table, String> column_catId;

    @FXML
    public TableColumn<table, String> column_catName;

    @FXML
    public TableColumn<table, String> column_subCatName;

    @FXML
    public TableColumn<table, Double> column_costPrice;

    @FXML
    public TableColumn<table, Integer> column_Sn;

    @FXML
    public TableColumn<table, String> column_subCatId;

    @FXML
    public TableColumn<table, Double> column_rate;

    @FXML
    public TableColumn<table, String> column_itemName;

    @FXML
    public TableColumn<table, String> column_itemCode;

    @FXML
    public TableColumn<table, Integer> column_subCatNumber;

    @FXML
    public TableColumn<table, Integer> column_itemNumber;

    public ObservableList<String> inputTypeList = FXCollections.observableArrayList("All", "Category", "Cat and SubCat");

    public ObservableList<String> itemTypeList = FXCollections.observableArrayList("Sales", "Inventory");

    public ObservableList<table> data = FXCollections.observableArrayList();
    public ObservableList<String> catIdList = FXCollections.observableArrayList();
    public ObservableList<String> catNameList = FXCollections.observableArrayList();

    public ObservableList<String> subCatIdList = FXCollections.observableArrayList();
    public ObservableList<String> subCatNameList = FXCollections.observableArrayList();

    public Integer rowvalue = 0;
    public boolean checker = true;
    public String global_catId = null;
    public String global_subcatId = null;
    public String global_itemId = null;

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    dbConnection dbc = new dbConnection();
    Message msg = new Message();

    public void initialComboBox() {
        comboBox_inputType.setValue("All");
        comboBox_inputType.setItems(inputTypeList);

        comboBox_itemType.setValue("Sales");
        comboBox_itemType.setItems(itemTypeList);
        inputTypefunCombo();
    }

    public void inputTypefunCombo() {
        if (String.valueOf(comboBox_inputType.getValue()).equals("All")) {
            textField_catName.setVisible(false);
            textField_catId.setVisible(false);
            textField_subCatId.setVisible(false);
            textField_subCatName.setVisible(false);
            comboBox_subCatId.setVisible(true);
            comboBox_subCatName.setVisible(true);
            comboBox_catName.setVisible(true);
            comboBox_catId.setVisible(true);
            textField_itemName.setDisable(false);
            textField_itemCode.setDisable(false);
            textField_costPrice.setDisable(false);
            textField_Rate.setDisable(false);
            textField_itemName.clear();
            textField_itemCode.clear();
            textField_costPrice.clear();
            textField_Rate.clear();
            textField_subCatId.clear();
            textField_subCatName.clear();
            textField_catId.clear();
            textField_catName.clear();
            column_subCatId.setVisible(true);
            column_subCatName.setVisible(true);
            column_itemCode.setVisible(true);
            column_itemName.setVisible(true);
            column_costPrice.setVisible(true);
            column_rate.setVisible(true);
            column_subCatNumber.setVisible(false);
            column_itemNumber.setVisible(false);
        } else if (String.valueOf(comboBox_inputType.getValue()).equals("Cat and SubCat")) {
            textField_catName.setVisible(false);
            textField_catId.setVisible(false);
            textField_subCatId.setVisible(true);
            textField_subCatName.setVisible(true);
            comboBox_subCatId.setVisible(false);
            comboBox_subCatName.setVisible(false);
            comboBox_catName.setVisible(true);
            comboBox_catId.setVisible(true);
            textField_itemName.setDisable(true);
            textField_itemCode.setDisable(true);
            textField_costPrice.setDisable(true);
            textField_Rate.setDisable(true);
            textField_itemName.clear();
            textField_itemCode.clear();
            textField_costPrice.clear();
            textField_subCatId.setDisable(false);
            textField_subCatName.setDisable(false);
            textField_Rate.clear();
            textField_subCatId.clear();
            textField_subCatName.clear();
            textField_catId.clear();
            textField_catName.clear();
            column_subCatId.setVisible(true);
            column_subCatName.setVisible(true);
            column_itemCode.setVisible(false);
            column_itemName.setVisible(false);
            column_costPrice.setVisible(false);
            column_rate.setVisible(false);
            column_subCatNumber.setVisible(false);
            column_itemNumber.setVisible(true);
        } else {
            textField_catName.setVisible(true);
            textField_catId.setVisible(true);
            textField_subCatId.setVisible(true);
            textField_subCatName.setVisible(true);
            comboBox_subCatId.setVisible(false);
            comboBox_subCatName.setVisible(false);
            comboBox_catName.setVisible(false);
            comboBox_catId.setVisible(false);
            textField_subCatId.setDisable(true);
            textField_subCatName.setDisable(true);
            textField_itemName.setDisable(true);
            textField_itemCode.setDisable(true);
            textField_costPrice.setDisable(true);
            textField_Rate.setDisable(true);
            textField_itemName.clear();
            textField_itemCode.clear();
            textField_costPrice.clear();
            textField_Rate.clear();
            textField_subCatId.clear();
            textField_subCatName.clear();
            textField_catId.clear();
            textField_catName.clear();

            column_subCatId.setVisible(false);
            column_subCatName.setVisible(false);
            column_itemCode.setVisible(false);
            column_itemName.setVisible(false);
            column_costPrice.setVisible(false);
            column_rate.setVisible(false);
            column_subCatNumber.setVisible(true);
            column_itemNumber.setVisible(false);
        }
        initialComboBoxSql();
        showTableFunction();
    }

    public void initialComboBoxSql() {
        try {
            comboBox_catId.getItems().clear();
            comboBox_catName.getItems().clear();
            con = dbc.connection();
            String type = String.valueOf(comboBox_itemType.getValue());
            String sql = "SELECT * FROM `tbl_category` WHERE `type` = '" + type + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while (rs.next()) {
                String temp_id = rs.getString("cat_id");
                String temp_name = rs.getString("cat_name");
                catIdList.add(temp_id);
                catNameList.add(temp_name);
            }
            comboBox_catName.setItems(catNameList);
            comboBox_catId.setItems(catIdList);

        } catch (Exception e) {
            System.out.println("in the initialComboBoxSql method of ItemsController.java");
            e.printStackTrace();
        }

    }

    public void onClikcCatIdCombo() {
        try {
            con = dbc.connection();
            String id = String.valueOf(comboBox_catId.getValue());
            String sql = "SELECT * FROM `tbl_category` WHERE `cat_id` = '" + id + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while (rs.next()) {

                String temp_name = rs.getString("cat_name");
                comboBox_catName.setValue(temp_name);
            }
            onClickOnCatComboBox();

        } catch (Exception e) {
            System.out.println("in the onClickCatIdCombo method of ItemsController.java");
            e.printStackTrace();
        }
    }

    public void onClickEdit() {
        try {
            getSelected();
            checker = false;
            Integer row = rowvalue - 1;
            if (row > -1) {
                table tbl = data.get(row);
                global_catId = tbl.s1.get();
                global_subcatId = tbl.s3.get();
                global_itemId = tbl.s5.get();
                String sql = "SELECT * FROM `tbl_category` WHERE `cat_id` = '" + global_catId + "'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while (rs.next()) {
                    comboBox_itemType.setValue(rs.getString("type"));
                }

                if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("All")) {
                    comboBox_catId.setValue(tbl.s1.get());
                    comboBox_catName.setValue(tbl.s2.get());
                    comboBox_subCatId.setValue(tbl.s3.get());
                    comboBox_subCatName.setValue(tbl.s3.get());
                    textField_itemCode.setText(tbl.s5.get());
                    textField_itemName.setText(tbl.s6.get());
                    textField_costPrice.setText(String.valueOf(tbl.d1.get()));
                    textField_Rate.setText(String.valueOf(tbl.d2.get()));
                    comboBox_subCatId.setDisable(true);
                    comboBox_subCatName.setDisable(true);
                    comboBox_catId.setDisable(true);
                    comboBox_catName.setDisable(true);
                    comboBox_itemType.setDisable(true);
                } else if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("Category")) {
                    textField_catId.setText(tbl.s1.get());
                    textField_catName.setText(tbl.s2.get());
                    textField_subCatId.clear();
                    textField_subCatName.clear();
                    textField_itemCode.clear();
                    textField_itemName.clear();
                    textField_costPrice.clear();
                    textField_Rate.clear();
                    comboBox_itemType.setDisable(false);

                } else {
                    comboBox_catId.setValue(tbl.s1.get());
                    comboBox_catName.setValue(tbl.s2.get());
                    textField_subCatId.setText(tbl.s3.get());
                    textField_subCatName.setText(tbl.s4.get());
                    textField_itemCode.clear();
                    textField_itemName.clear();
                    textField_costPrice.clear();
                    textField_Rate.clear();
                    comboBox_catId.setDisable(true);
                    comboBox_catName.setDisable(true);
                    comboBox_itemType.setDisable(true);
                }
                button_Delete.setDisable(true);
                button_Edit.setDisable(true);
                comboBox_inputType.setDisable(true);

            }
        } catch (Exception e) {
            System.out.println("in clickEdit of ItemsController.java");
        }
    }

    public void showTableFunction() {
        try {

            column_Sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            column_catId.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            column_catName.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            column_subCatId.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            column_subCatName.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
            column_itemCode.setCellValueFactory(new PropertyValueFactory<table, String>("s5"));
            column_itemName.setCellValueFactory(new PropertyValueFactory<table, String>("s6"));
            column_costPrice.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            column_rate.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
            column_subCatNumber.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
            column_itemNumber.setCellValueFactory(new PropertyValueFactory<table, Integer>("i3"));
            table_ItemDetails.getItems().clear();

            con = dbc.connection();
            String itemType = String.valueOf(comboBox_itemType.getValue());

            String sql = "SELECT * FROM `tbl_category` WHERE `type` = '" + itemType + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            Integer temp_sn = 1;

            if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("All")) {
                while (rs.next()) {
                    String cat_id = rs.getString("cat_id");
                    String cat_name = rs.getString("cat_name");
                    Integer subCatNo = rs.getInt("subcat_num");
                    String sql1 = "SELECT * FROM `tbl_subcategory` WHERE `cat_id` = '" + cat_id + "'";
                    PreparedStatement pst1 = (PreparedStatement) con.prepareStatement(sql1);
                    ResultSet rs1 = pst1.executeQuery(sql1);
                    while (rs1.next()) {
                        String subCat_id = rs1.getString("subcat_id");
                        String subCat_name = rs1.getString("subcategory_name");
                        Integer itemNo = rs1.getInt("items_num");
                        String sql2 = "SELECT * FROM `tbl_items_mp` WHERE `subcat_id` = '" + subCat_id + "'";
                        PreparedStatement pst2 = (PreparedStatement) con.prepareStatement(sql2);
                        ResultSet rs2 = pst2.executeQuery(sql2);
                        while (rs2.next()) {
                            table tbl = new table();
                            tbl.i1.set(temp_sn++);
                            tbl.s1.set(cat_id);
                            tbl.s2.set(cat_name);
                            tbl.s3.set(subCat_id);
                            tbl.s4.set(subCat_name);
                            tbl.s5.set(rs2.getString("item_code"));
                            tbl.s6.set(rs2.getString("item_name"));
                            tbl.d1.set(rs2.getDouble("cost_price"));
                            tbl.d2.set(rs2.getDouble("rate"));
                            tbl.i2.set(subCatNo);
                            tbl.i3.set(itemNo);
                            data.add(tbl);

                        }

                    }
                }
            }
            if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("Cat and SubCat")) {
                while (rs.next()) {
                    String cat_id = rs.getString("cat_id");
                    String cat_name = rs.getString("cat_name");
                    Integer subCatNo = rs.getInt("subcat_num");
                    String sql1 = "SELECT * FROM `tbl_subcategory` WHERE `cat_id` = '" + cat_id + "'";
                    PreparedStatement pst1 = (PreparedStatement) con.prepareStatement(sql1);
                    ResultSet rs1 = pst1.executeQuery(sql1);
                    while (rs1.next()) {
                        String subCat_id = rs1.getString("subcat_id");
                        String subCat_name = rs1.getString("subcategory_name");
                        Integer itemNo = rs1.getInt("items_num");

                        table tbl = new table();
                        tbl.i1.set(temp_sn++);
                        tbl.s1.set(cat_id);
                        tbl.s2.set(cat_name);
                        tbl.s3.set(subCat_id);
                        tbl.s4.set(subCat_name);
                        tbl.i3.set(itemNo);
                        data.add(tbl);

                    }
                }
            }
            if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("Category")) {
                while (rs.next()) {
                    String cat_id = rs.getString("cat_id");
                    String cat_name = rs.getString("cat_name");
                    Integer subCatNo = rs.getInt("subcat_num");
                    table tbl = new table();
                    tbl.i1.set(temp_sn++);
                    tbl.s1.set(cat_id);
                    tbl.s2.set(cat_name);
                    tbl.i2.set(subCatNo);
                    data.add(tbl);
                }
            }

            table_ItemDetails.setItems(data);
        } catch (Exception e) {
            System.out.println("in showTableFunction method fo ItemController.java");
            e.printStackTrace();
        }
    }

    public void onClikcCatNameCombo() {
        try {
            con = dbc.connection();
            String name = String.valueOf(comboBox_catName.getValue());
            String sql = "SELECT * FROM `tbl_category` WHERE `cat_name` = '" + name + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while (rs.next()) {

                String temp_id = rs.getString("cat_id");
                comboBox_catId.setValue(temp_id);
            }
            onClickOnCatComboBox();

        } catch (Exception e) {
            System.out.println("in the onClickCatNameCombo method of ItemsController.java");
            e.printStackTrace();
        }
    }

    public void onClikcSubCatIdCombo() {
        try {
            con = dbc.connection();
            String id = String.valueOf(comboBox_subCatId.getValue());
            String sql = "SELECT * FROM `tbl_subcategory` WHERE `subcat_id` = '" + id + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while (rs.next()) {

                String temp_name = rs.getString("subcategory_name");
                comboBox_subCatName.setValue(temp_name);
            }
        } catch (Exception e) {
            System.out.println("in the onClickSubCatIdCombo method of ItemsController.java");
            e.printStackTrace();
        }
    }

    public void onClikcSubCatNameCombo() {
        try {
            con = dbc.connection();
            String name = String.valueOf(comboBox_subCatName.getValue());
            String sql = "SELECT * FROM `tbl_subcategory` WHERE `subcategory_name` = '" + name + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while (rs.next()) {

                String temp_id = rs.getString("subcat_id");
                comboBox_subCatId.setValue(temp_id);
            }
        } catch (Exception e) {
            System.out.println("in the onClickSubCatNameCombo method of ItemsController.java");
            e.printStackTrace();
        }
    }

    public void onClickOnCatComboBox() {
        try {
            comboBox_subCatId.getItems().clear();
            comboBox_subCatName.getItems().clear();
            con = dbc.connection();
            String id = String.valueOf(comboBox_catId.getValue());
            String sql = "SELECT * FROM `tbl_subcategory` WHERE `cat_id` ='" + id + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while (rs.next()) {
                String temp_id = rs.getString("subcat_id");
                String temp_name = rs.getString("subcategory_name");
                subCatIdList.add(temp_id);
                subCatNameList.add(temp_name);
            }
            comboBox_subCatName.setItems(subCatNameList);
            comboBox_subCatId.setItems(subCatIdList);

        } catch (Exception e) {
            System.out.println("in the onClickOnCatComboBox method of ItemsController.java");
            e.printStackTrace();
        }
    }

    public void onClickDelete() {
        try {
            Integer row = rowvalue - 1;
            if (row > -1) {
                table tbl = data.get(row);
                String itemCode = tbl.s5.get();
                String catId = tbl.s1.get();
                String subCatId = tbl.s3.get();
                boolean ans = msg.alertOptionMessage("DELETE", "Are you sure?");
                if (ans) {
                    con = dbc.connection();
                    String sql = "SELECT * FROM `tbl_category` WHERE `cat_id` = '" + catId + "'";
                    pst = (PreparedStatement) con.prepareStatement(sql);
                    Integer subCatNo = 0;
                    rs = pst.executeQuery(sql);
                    while (rs.next()) {
                        subCatNo = rs.getInt("subcat_num");
                    }
                    sql = "SELECT * FROM `tbl_subcategory` WHERE `subcat_id` = '" + subCatId + "'";
                    Integer itemNo = 0;
                    rs = pst.executeQuery(sql);
                    while (rs.next()) {
                        itemNo = rs.getInt("items_num");
                    }
                    if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("Category") && (subCatNo == 0)) {
                        sql = "DELETE FROM `tbl_category` WHERE `cat_id` ='" + catId + "'";
                        pst = (PreparedStatement) con.prepareStatement(sql);

                        pst.executeUpdate(sql);

                    } else if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("Cat and SubCat") && ((itemNo == 0) || (itemNo == null))) {
                        subCatNo--;
                        sql = "UPDATE `tbl_category` SET `subcat_num` = '" + subCatNo + "' WHERE `cat_id` = '" + catId + "'";
                        pst = (PreparedStatement) con.prepareStatement(sql);
                        pst.executeUpdate(sql);
                        sql = "DELETE FROM `tbl_subcategory` WHERE `subcat_id` = '" + subCatId + "'";
                        pst = (PreparedStatement) con.prepareStatement(sql);

                        pst.executeUpdate(sql);
                    } else if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("All")) {
                        itemNo--;
                        sql = "UPDATE `tbl_subcategory` SET `items_num` = '" + itemNo + "' WHERE `subcat_id` = '" + subCatId + "'";
                        pst = (PreparedStatement) con.prepareStatement(sql);
                        pst.executeUpdate(sql);
                        sql = "DELETE FROM `tbl_items_mp` WHERE `item_code` = '" + itemCode + "'";
                        pst = (PreparedStatement) con.prepareStatement(sql);

                        pst.executeUpdate(sql);
                    } else {
                        msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Cannot Delete");

                    }
                    initialComboBoxSql();

                    showTableFunction();
                }
            } else {
                msg.alertMessage(Alert.AlertType.ERROR, "NO ITEMS SELECTED", "Please select item to delete");
            }
            checker = true;
        } catch (Exception e) {
            System.out.println("in onClickDelete method of ItemsController.java");
            e.printStackTrace();
        }
    }

    public void onClickSave() {
        try {
            con = dbc.connection();
            boolean checking = true;
            if (checker) {
                if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("All")) {
                    String catId = String.valueOf(comboBox_catId.getValue());
                    String catName = String.valueOf(comboBox_catName.getValue());
                    String subCatName = String.valueOf(comboBox_subCatName.getValue());
                    String subCatId = String.valueOf(comboBox_subCatId.getValue());
                    String itemCode = textField_itemCode.getText();
                    String itemName = textField_itemName.getText();
                    if (catId != "null" && !(catId.isEmpty())) {
                        if (catName != "null" && !(catName.isEmpty())) {
                            if (subCatId != "null" && !(subCatId.isEmpty())) {
                                if (subCatName != "null" && !(subCatName.isEmpty())) {
                                    if (itemCode != "null" && !(itemCode.isEmpty())) {
                                        if (itemName != "null" && !itemName.isEmpty()) {
                                            if (textField_costPrice != null && !(textField_costPrice.getText().isEmpty())) {
                                                if (textField_Rate != null && !(textField_Rate.getText().isEmpty())) {
                                                    Double costPrice = Double.parseDouble(textField_costPrice.getText());
                                                    Double rate = Double.parseDouble(textField_Rate.getText());

                                                    String name = "";
                                                    String code = "";
                                                    String sql2 = "SELECT * FROM `tbl_items_mp`";
                                                    pst = (PreparedStatement) con.prepareStatement(sql2);
                                                    rs = pst.executeQuery(sql2);
                                                    while (rs.next()) {
                                                        name = rs.getString("item_name");
                                                        code = rs.getString("item_code");
                                                        if (name.trim().equalsIgnoreCase(itemName.trim()) || code.trim().equalsIgnoreCase(itemCode.trim())) {
                                                            checking = false;
                                                        }
                                                    }

                                                    if (checking == true) {
                                                        String sql = "INSERT INTO `tbl_items_mp`(`subcat_id`, `item_code`, `item_name`, `cost_price`, `rate`) VALUES ('" + subCatId + "', '" + itemCode + "', '" + itemName + "', '" + costPrice + "', '" + rate + "')";
                                                        pst = (PreparedStatement) con.prepareStatement(sql);
                                                        pst.executeUpdate(sql);
                                                        sql = "SELECT * FROM `tbl_subcategory` WHERE `subcat_id`= '" + subCatId + "'";
                                                        pst = (PreparedStatement) con.prepareStatement(sql);
                                                        rs = pst.executeQuery(sql);
                                                        Integer temp_no = 0;
                                                        while (rs.next()) {
                                                            temp_no = rs.getInt("items_num");
                                                        }
                                                        temp_no++;
                                                        sql = "UPDATE `tbl_subcategory` SET `items_num` = '" + temp_no + "' WHERE `subcat_id`= '" + subCatId + "'";
                                                        pst = (PreparedStatement) con.prepareStatement(sql);
                                                        pst.executeUpdate(sql);
                                                        showTableFunction();
                                                        onClickCancel();
                                                    } else {
                                                        msg.alertMessage(Alert.AlertType.ERROR, "Dublicate Entry", "Item name or Item Code is Dublicate");
                                                    }
                                                } else {
                                                    msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Rate field is empty");
                                                }
                                            } else {
                                                msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Cost Price field is empty");
                                            }
                                        } else {
                                            msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Item Name field is empty");
                                        }
                                    } else {
                                        msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Item code field is empty");
                                    }
                                } else {
                                    msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Subcat Name  is empty");
                                }
                            } else {
                                msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Subcat Id  is empty");
                            }
                        } else {
                            msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Cat Name  is empty");
                        }
                    } else {
                        msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Cat Id  is empty");
                    }
                }
                if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("Cat and SubCat")) {
                    String subCatId = textField_subCatId.getText();
                    String subCatName = textField_subCatName.getText();
                    String catId = String.valueOf(comboBox_catId.getValue());
                    String catName = String.valueOf(comboBox_catName.getValue());
                    if (catId != "null" && !catId.isEmpty()) {
                        if (catName != "null" && !catName.isEmpty()) {
                            if (subCatName != null && !subCatName.isEmpty()) {
                                if (subCatId != null && !subCatId.isEmpty()) {

                                    String name = "";
                                    String code = "";
                                    String sql2 = "SELECT * FROM `tbl_subcategory`";
                                    pst = (PreparedStatement) con.prepareStatement(sql2);
                                    rs = pst.executeQuery(sql2);
                                    while (rs.next()) {
                                        name = rs.getString("subcategory_name");
                                        code = rs.getString("subcat_id");
                                        if (name.trim().equalsIgnoreCase(subCatName.trim()) || code.trim().equalsIgnoreCase(subCatId.trim())) {
                                            checking = false;
                                        }
                                    }
                                    if (checking == true) {
                                        String sql = "INSERT INTO `tbl_subcategory`(`cat_id`, `subcat_id`, `subcategory_name`, `items_num`) VALUES ('" + catId + "', '" + subCatId + "', '" + subCatName + "', '0' )";
                                        pst = (PreparedStatement) con.prepareStatement(sql);
                                        pst.executeUpdate(sql);
                                        sql = "SELECT * FROM `tbl_category` WHERE `cat_id` = '" + catId + "'";
                                        pst = (PreparedStatement) con.prepareStatement(sql);
                                        rs = pst.executeQuery(sql);
                                        Integer temp_no = 0;
                                        while (rs.next()) {
                                            temp_no = rs.getInt("subcat_num");
                                        }
                                        temp_no++;
                                        sql = "UPDATE `tbl_category` SET `subcat_num` = '" + temp_no + "' WHERE `cat_id` = '" + catId + "'";
                                        pst = (PreparedStatement) con.prepareStatement(sql);
                                        pst.executeUpdate(sql);
                                        initialComboBoxSql();
                                        showTableFunction();
                                        onClickCancel();
                                    } else {
                                        msg.alertMessage(Alert.AlertType.ERROR, "Dublicate Entry", "Subcategory name or Subcategory Id is already entered");
                                    }
                                } else {
                                    msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Subcat Id  is empty");
                                }
                            } else {
                                msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Subcat Name  is empty");
                            }
                        } else {
                            msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Cat Name  is empty");
                        }
                    } else {
                        msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Cat Id  is empty");
                    }

                }
                if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("Category")) {
                    String catId = textField_catId.getText();
                    String catName = textField_catName.getText();
                    if (catId != null && !(catId.isEmpty())) {

                        if (catName != null && !(catName.isEmpty())) {

                            String name = "";
                            String code = "";
                            String sql2 = "SELECT * FROM `tbl_category`";
                            pst = (PreparedStatement) con.prepareStatement(sql2);
                            rs = pst.executeQuery(sql2);
                            while (rs.next()) {
                                name = rs.getString("cat_name");
                                code = rs.getString("cat_id");
                                if (name.trim().equalsIgnoreCase(catName.trim()) || code.trim().equalsIgnoreCase(catId.trim())) {
                                    checking = false;
                                }
                            }
                            if (checking == true) {
                                String type = String.valueOf(comboBox_itemType.getValue());
                                String sql = "INSERT INTO `tbl_category`(`type`, `cat_id`, `cat_name`, `subcat_num`) VALUES ('" + type + "', '" + catId + "', '" + catName + "', '0')";
                                pst = (PreparedStatement) con.prepareStatement(sql);
                                pst.executeUpdate(sql);
                                comboBox_catId.getItems().clear();
                                comboBox_catName.getItems().clear();
                                showTableFunction();
                                onClickCancel();
                            } else {
                                msg.alertMessage(Alert.AlertType.ERROR, "Dublicate Entry", "Category Name or Category Id is already Entered");
                            }

                        } else {
                            msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Cat Name  is empty");
                        }
                    } else {
                        msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Cat Id  is empty");
                    }
                }

            } else {
                if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("All")) {
                    String itemCode = textField_itemCode.getText();
                    String itemName = textField_itemName.getText();
                    String costPrice = textField_costPrice.getText();
                    String rate = textField_Rate.getText();
                    if (!itemCode.isEmpty() && itemCode != null) {
                        if (!itemName.isEmpty() && itemName != null) {
                            if (!costPrice.isEmpty() && costPrice != null) {
                                if (!rate.isEmpty() && rate != null) {
                                    String sql = "UPDATE `tbl_items_mp` SET `item_code` = '" + itemCode + "',`item_name` = '" + itemName + "',`cost_price` = '" + costPrice + "',`rate` = '" + rate + "' WHERE `item_code` = '" + global_itemId + "'";
                                    pst = (PreparedStatement) con.prepareStatement(sql);
                                    pst.executeUpdate(sql);
                                    showTableFunction();
                                    onClickCancel();
                                } else {
                                    msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Rate field is empty");
                                }
                            } else {
                                msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Cost Price field is empty");
                            }
                        } else {
                            msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Item Name field is empty");
                        }
                    } else {
                        msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Item code field is empty");
                    }
                }
                if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("Cat and SubCat")) {
                    String subCatId = textField_subCatId.getText();
                    String subCatName = textField_subCatName.getText();
                    if (!subCatId.isEmpty() && subCatId != null) {
                        if (!subCatName.isEmpty() && subCatName != null) {

                            String sql = "UPDATE `tbl_subcategory` SET `subcat_id` = '" + subCatId + "',`subcategory_name` = '" + subCatName + " WHERE `subcat_id`= '" + global_subcatId + "'";
                            pst = (PreparedStatement) con.prepareStatement(sql);
                            pst.executeUpdate(sql);
                            sql = "UPDATE `tbl_items_mp` SET `subcat_id` = '" + subCatId + "' WHERE `subcat_id`= '" + global_subcatId + "'";
                            pst = (PreparedStatement) con.prepareStatement(sql);
                            pst.executeUpdate(sql);
                            showTableFunction();
                            onClickCancel();
                        } else {
                            msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Subcat Name  is empty");
                        }
                    } else {
                        msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Subcat Id  is empty");
                    }
                }
                if (String.valueOf(comboBox_inputType.getValue()).equalsIgnoreCase("Category")) {
                    String catId = textField_catId.getText();
                    String catName = textField_catName.getText();
                    if (!catId.isEmpty() && catId != null) {
                        if (!catName.isEmpty() && catName != null) {
                            String itemType = String.valueOf(comboBox_itemType.getValue());

                            String sql = "UPDATE `tbl_category` SET `type` = '" + itemType + "', `cat_id` = '" + catId + "',`cat_name` = '" + catName + "' WHERE `cat_id` = '" + global_catId + "'";
                            pst = (PreparedStatement) con.prepareStatement(sql);
                            pst.executeUpdate(sql);
                            sql = "UPDATE `tbl_subcategory` SET `cat_id` = '" + catId + "' WHERE `cat_id` = '" + global_catId + "'";
                            pst = (PreparedStatement) con.prepareStatement(sql);
                            pst.executeUpdate(sql);
                            initialComboBoxSql();
                            showTableFunction();
                            onClickCancel();
                        } else {
                            msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Cat Name  is empty");
                        }
                    } else {
                        msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Cat Id  is empty");
                    }
                }

            }

            checker = true;
        } catch (Exception e) {
            msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Dublicate Entry");
            System.out.println("in onClickSave method of ItemsController.java");
            e.printStackTrace();
        }
    }

    public void onClickItemType() {
        if (checker) {
            showTableFunction();

        }
        initialComboBoxSql();
    }

    public void getSelected() {
        try {
            column_Sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            column_catId.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            column_catName.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            column_subCatId.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            column_subCatName.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
            column_itemCode.setCellValueFactory(new PropertyValueFactory<table, String>("s5"));
            column_itemName.setCellValueFactory(new PropertyValueFactory<table, String>("s6"));
            column_costPrice.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            column_rate.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
            table_ItemDetails.setItems(data);

            table_ItemDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                    rowvalue = (data.indexOf(newValue) + 1);
                    //System.out.println("ON RowValue is : " + rowvalue);
                }

            });
        } catch (Exception e) {
            System.out.println("on getSelected method of ItemsController.java");
            e.printStackTrace();
        }
    }

    public void onClickCancel() {
        textField_catId.clear();
        textField_catName.clear();
        textField_subCatId.clear();
        textField_subCatName.clear();
        textField_itemName.clear();
        textField_itemCode.clear();
        textField_costPrice.clear();
        textField_Rate.clear();

        comboBox_catName.getItems().clear();
        comboBox_catId.getItems().clear();
        comboBox_subCatName.getItems().clear();
        comboBox_subCatId.getItems().clear();
        initialComboBoxSql();
        checker = true;
        button_Delete.setDisable(false);
        button_Edit.setDisable(false);
        comboBox_subCatId.setDisable(false);
        comboBox_subCatName.setDisable(false);
        comboBox_catId.setDisable(false);
        comboBox_catName.setDisable(false);
        comboBox_itemType.setDisable(false);
        comboBox_inputType.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getSelected();
        initialComboBox();
        initialComboBoxSql();
        showTableFunction();
    }

}
