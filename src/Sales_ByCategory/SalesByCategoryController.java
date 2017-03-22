/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sales_ByCategory;

import UsableMethods.Database;
import UsableMethods.dbConnection;
import UsableMethods.table;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

/**
 * FXML Controller class
 *
 * @author User
 */
public class SalesByCategoryController implements Initializable {

    @FXML
    private TreeTableView<table> treeTblView;

    @FXML
    private TreeTableColumn<table, String> Particulars;

    @FXML
    private DatePicker Date_to;

    @FXML
    private TreeTableColumn<table, String> Amount;

    @FXML
    private TreeTableColumn<table, String> Quantity;

    @FXML
    private DatePicker Date_from;

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    ObservableList<TreeItem<table>> tree = FXCollections.observableArrayList();
    ObservableList<TreeItem<table>> tree1 = FXCollections.observableArrayList();
    ObservableList<TreeItem<table>> tree2 = FXCollections.observableArrayList();
    dbConnection dbc = new dbConnection();

    TreeItem<table> totalSales = new TreeItem();

    public void useDatabase() {
        Database dbDate = new Database();
        Date_to.setEditable(false);
        Date_from.setEditable(false);
        Date_to.setValue(dbDate.NOW_LOCAL_DATEcurrentDate());
        Date_from.setValue(dbDate.NOW_LOCAL_DATEfirstDateOfMonth());
    }

    public void showTreeTableView() {
        try {

            Particulars.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<table, String> param)
                    -> new ReadOnlyStringWrapper(param.getValue().getValue().s1.get())
            );
            Quantity.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<table, String> param)
                    -> new ReadOnlyStringWrapper(param.getValue().getValue().s2.get())
            );
            Amount.setCellValueFactory(
                    (TreeTableColumn.CellDataFeatures<table, String> param)
                    -> new ReadOnlyStringWrapper(param.getValue().getValue().s3.get())
            );
            treeTblView.setRoot(totalSales);
            treeTblView.getRoot().getChildren().clear();
            String from_date = String.valueOf(Date_from.getValue());
            String to_date = String.valueOf(Date_to.getValue());

            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_category`";
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            Double quantity = 0.0;
            Double amount = 0.0;
            table tbl = new table();
            while (rs.next()) {
                int i = 1;
                String catName = rs.getString("cat_name");
                String catId = rs.getString("cat_id");
                String sql1 = "SELECT * FROM `tbl_subcategory` WHERE `cat_id`= '" + catId + "'";
                PreparedStatement pst1 = (PreparedStatement) con.prepareStatement(sql1);
                ResultSet rs1 = pst1.executeQuery(sql1);
                Double quantity1 = 0.0;
                Double amount1 = 0.0;
                table tbl1 = new table();
                while (rs1.next()) {

                    String subCatName = rs1.getString("subcategory_name");
                    String subCatId = rs1.getString("subcat_id");
                    Double quantity2 = 0.0;
                    Double amount2 = 0.0;
                    String sql2 = "SELECT * FROM `tbl_items_mp` WHERE `subcat_id` = '" + subCatId + "'";
                    PreparedStatement pst2 = (PreparedStatement) con.prepareStatement(sql2);
                    ResultSet rs2 = pst2.executeQuery(sql2);
                    table tbl2 = new table();
                    while (rs2.next()) {
                        String item_name = rs2.getString("item_name");
                        Double quantity3 = 0.0;
                        Double amount3 = 0.0;
                        table tbl3 = new table();
                        String sql4 = "SELECT * FROM `tbl_sales` WHERE `date` >= '" + from_date + "' And `date` <= '" + to_date + "'";
                        PreparedStatement pst4 = (PreparedStatement) con.prepareStatement(sql4);
                        ResultSet rs4 = pst4.executeQuery(sql4);
                        while (rs4.next()) {
                            String bill_id = rs4.getString("bill_id");
                            String sql3 = "SELECT * FROM `tbl_sales_details` WHERE `item_name` = '" + item_name + "' AND `bill_id` = '" + bill_id + "'";
                            PreparedStatement pst3 = (PreparedStatement) con.prepareStatement(sql3);
                            ResultSet rs3 = pst3.executeQuery(sql3);
                            while (rs3.next()) {
                                quantity3 = quantity3 + rs3.getDouble("quantity");
                                amount3 = amount3 + rs3.getDouble("amount");
                            }
                        }
                        String quantityString3 = String.valueOf(quantity3);
                        String amountString3 = String.valueOf(amount3);

                        tbl3.s1.set(item_name);
                        tbl3.s2.set(quantityString3);
                        tbl3.s3.set(amountString3);
                        TreeItem<table> items = new TreeItem();
                        items.setValue(tbl3);
                        tree2.add(items);

                        //items1.getChildren().add(items);
                        quantity2 += quantity3;
                        amount2 += amount3;
                    }
                    String quantityString2 = String.valueOf(quantity2);
                    String amountString2 = String.valueOf(amount2);
                    tbl2.s1.set(subCatName);
                    tbl2.s2.set(quantityString2);
                    tbl2.s3.set(amountString2);
                    TreeItem<table> subCategory = new TreeItem();
                    subCategory.setValue(tbl2);
                    subCategory.getChildren().addAll(tree2);
                    tree2.clear();
                    tree1.add(subCategory);

                    quantity1 += quantity2;
                    amount1 += amount2;
                }
                String quantityString1 = String.valueOf(quantity1);
                String amountString1 = String.valueOf(amount1);
                tbl1.s1.set(catName);
                tbl1.s2.set(quantityString1);
                tbl1.s3.set(amountString1);
                quantity += quantity1;
                amount += amount1;
                TreeItem<table> category = new TreeItem();
                category.setValue(tbl1);
                category.getChildren().addAll(tree1);
                tree1.clear();
                tree.addAll(category);

            }
            String quantityString = String.valueOf(quantity);
            String amountString = String.valueOf(amount);
            tbl.s1.set("total sales");
            tbl.s2.set(quantityString);
            tbl.s3.set(amountString);
            totalSales.setValue(tbl);

            totalSales.getChildren().addAll(tree);
            tree.clear();

        } catch (Exception e) {
            System.out.println("on showTreeTableView");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        useDatabase();
        showTreeTableView();
    }

}
