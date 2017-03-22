/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package credit;

//import Index.IndexController;
import UsableMethods.*;
import UsableMethods.Database;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author User
 */
public class CreditFXMLController implements Initializable {
    
    //DEFINING 1ST TABLE
    @FXML
    public TableView<table> table_Credit;
    @FXML
    public TableColumn<table, Integer> sn1;
    @FXML
    public TableColumn<table, String> id1;
    @FXML
    public TableColumn<table, String> name;
    @FXML
    public TableColumn<table, Double> total_remaining_amount;

   //DEFINING 2ND TABLE
    @FXML
    public TableView<table> table_Credit_details;

    @FXML
    public TableColumn<table, Integer> sn2;
    @FXML
    public TableColumn<table, String> id2;
    @FXML
    public TableColumn<table, String> bill_id;
    @FXML
    public TableColumn<table, String> date;
    @FXML
    public TableColumn<table, Double> total_amount;
    @FXML
    public TableColumn<table, Double> received_amount;
    @FXML
    public TableColumn<table, Double> remaining_amount;

    //Buttons and Label defined
    @FXML
    private Label label_Cid;
    @FXML
    private Label label_Name;
    @FXML
    private Label label_CreditAmount;
    @FXML
    private JFXTextField textField_ReceivedAmount;
    @FXML
    private Label label_RemainingAmount;

    @FXML
    private DatePicker datePicker_To;
    @FXML
    private DatePicker datePicker_From;
    @FXML
    private JFXButton btn_Save;
    @FXML
    private JFXButton btn_Cancel;
    @FXML
    private JFXButton btn_Delete;

    dbConnection dbc = new dbConnection();
    Message m = new Message();
    Connection conn = null;
    Database db = new Database();

//    private IndexController indexController;
    public static int rowValueTbl1 = 0;
    public static int rowValueTbl2 = 0;
    public static String cid = null;

    final ObservableList<table> data1 = FXCollections.observableArrayList();
    final ObservableList<table> data2 = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialDates();
        updateTopTable();
        getSelectedRowForTbl1();
        getSelectedRowForTbl2();
              
    }
    
    
//    public void injectMainController(IndexController indexController){
//        this.indexController = indexController;
//    }

    /*
    * UPDATE TOP TABLE *
     */
    private void updateTopTable() {

        try {
            //Removes all data in table to update again
            data1.removeAll(data1);

            Integer temp_sn = 1;
            conn = dbc.connection();

            String sql = "select * from `tbl_credit_sales`  ";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);

            sn1.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            id1.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            name.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            total_remaining_amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));

            while (rs.next()) {
                table t2 = new table();

                t2.i1.set((temp_sn++));
                t2.s1.set(rs.getString("cid"));
                t2.s2.set(rs.getString("cname"));
                t2.d1.set(Double.valueOf(rs.getString("total_rem_amount")));

                data1.add(t2);
//                System.out.println(data1);
            }
            table_Credit.setItems(data1);

        } catch (Exception e) {
            System.out.println("UpdateTopTable ");
            e.printStackTrace();
        }
    }

    public void getSelectedRowForTbl1() {
        try {
            sn1.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            id1.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            name.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            total_remaining_amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));

            table_Credit.setItems(data1);

            table_Credit.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                    rowValueTbl1 = (data1.indexOf(newValue) + 1);
//                    System.out.println("ON RowValueUp is : " + rowValueTbl1);

                    //UPDATE CID NAME LABELS
                    updateLabelsOnRowClick();

                    //UPDATE LOWER TABLE WITH CID
                    updateLowerTable();
                }

            });
        } catch (Exception e) {
            System.out.println("getSelectedRowForTbl1");
            e.printStackTrace();
        }
    }

    //UPDATE LABELS NAME, CID, CREDIT_AMOUNT
    public void updateLabelsOnRowClick() {
        try {
            if (rowValueTbl1 > 0) {

                //DATABSE ROW STARTS FROM 0
                int new_row = rowValueTbl1 - 1;

                conn = dbc.connection();
                String sql = "SELECT * FROM `tbl_credit_sales`  LIMIT " + new_row + ",1"; //LIMIT from, rows;

                PreparedStatement pst1 = conn.prepareStatement(sql);
                ResultSet rs = pst1.executeQuery(sql);

                while (rs.next()) {
                    cid = rs.getString("cid");
                    label_Cid.setText(cid);
                    label_Name.setText(rs.getString("cname"));
                    label_CreditAmount.setText(rs.getString("total_rem_amount"));
                }
            }

        } catch (Exception e) {
            System.out.println("updateLabelsOnRowClick");
            e.printStackTrace();
        }

    }

    /*
    * UPDATE LOWER TABLE *
     */
    @FXML
    private void updateLowerTable() {
        try {
            //CLEARS LOWER-TABLE
            data2.removeAll(data2);

            Integer temp_sn = 1;
            LocalDate from_date = datePicker_From.getValue();
            LocalDate to_date = datePicker_To.getValue();

            conn = dbc.connection();

            String sql = "select * from `tbl_credit_sales_subdetails` WHERE `cid`= '" + cid + "' AND `date` >= '" + from_date + "' And `date` <= '" + to_date + "' ORDER BY `date` DESC";
//            String sql = "select * from `tbl_credit_sales_subdetails` WHERE `cid`= '" + cid + "' ORDER BY `date` DESC";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);

            sn2.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            id2.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            bill_id.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            date.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            total_amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            received_amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
            remaining_amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d3"));

            while (rs.next()) {
                table t = new table();

                t.i1.set((temp_sn++));
                t.s1.set(rs.getString("cid"));

                if (rs.getString("bill_id") == null) {
                    t.s2.set("-");
                } else {
                    t.s2.set(rs.getString("bill_id"));
                }

                t.s3.set(rs.getString("date"));
                t.d1.set(rs.getDouble("total_amount"));
                t.d2.set(rs.getDouble("received_amount"));
                t.d3.set(rs.getDouble("remaining_amount"));

                data2.add(t);
            }
            table_Credit_details.setItems(data2);

        } catch (Exception e) {
            System.out.println("UpdateLowerTable ");
            e.printStackTrace();
        }

    }

    @FXML
    public void calculation() {
        try {
            double receivedAmount = Double.valueOf(textField_ReceivedAmount.getText());
            double creditAmount = Double.valueOf(label_CreditAmount.getText());
            double remainingAmount = creditAmount - receivedAmount;
            label_RemainingAmount.setText(String.valueOf(remainingAmount));
        } catch (Exception e) {
            label_RemainingAmount.setText("");
//            System.out.println("calculation");
        }
    }

    public void initialDates() {
        datePicker_From.setValue(db.NOW_LOCAL_DATEfirstDateOfMonth());
        datePicker_To.setValue(db.NOW_LOCAL_DATE_TO());
    }

    
    @FXML
    public void save() {
        
        String tblName = "tbl_credit_sales_subdetails";
        String cid = label_Cid.getText();
        Integer bill_id = null;
        Double creditamount = Double.valueOf(label_CreditAmount.getText());
        Double receivedAmount = 0.0;
        if(!textField_ReceivedAmount.getText().isEmpty())        
        receivedAmount = Double.valueOf(textField_ReceivedAmount.getText());
        Double remainingAmount = Double.valueOf(label_RemainingAmount.getText());

        ArrayList values = new ArrayList();
        values.add(null);
        values.add(cid);
        values.add(bill_id);
        values.add(db.getCurrentDate());
        values.add(creditamount);
        values.add(receivedAmount);
        values.add(remainingAmount);

//        System.out.println(values);

        int i = db.insertData(tblName, values);
        System.out.println(i);
        int rowval = rowValueTbl1;
        
        //CLEAR TEXTFIELD AND LABEL
        textField_ReceivedAmount.clear();
        label_RemainingAmount.setText("");
        
        //REFRESH TABLES
        updateTopTable();
        reSelected(rowval);
        updateLowerTable();
        
        
        
//        label_Cid.setText("");
//        label_CreditAmount.setText("");
//        label_Name.setText("");
        
    }
    
    
    /*
    * For reselecting the data in the top-table after its get refreshed *
     */
    public void reSelected(int rowval) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int row = rowval - 1;
                table_Credit.requestFocus();
                table_Credit.getSelectionModel().select(row);
                table_Credit.getFocusModel().focus(row);
            }
        });
    }

    public void getSelectedRowForTbl2() {
        try {

            table_Credit_details.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                    rowValueTbl2 = (data2.indexOf(newValue) + 1);
//                    System.out.println("ON RowValueDown is : " + rowValueTbl2);
                }

            });
        } catch (Exception e) {
            System.out.println("getSelectedRowForTbl2");
            e.printStackTrace();
        }
    }

    @FXML
    private void delete() {

        String[] options = {"Yes", "No"};
        Boolean ans = m.alertOptionMessage("Delete confirm!", "Are you sure want to delete?");
        if (ans == true) {
            rowValueTbl2--;
            String temp_id = null;
            if (rowValueTbl2 > -1) {
                try {

                    String from_date = String.valueOf(datePicker_From.getValue());
                    String to_date = String.valueOf(datePicker_To.getValue());

                    String sql = "SELECT * FROM `tbl_credit_sales_subdetails` WHERE `cid`=" + cid + " AND `date`>= '" + from_date + "' AND `date`<= '" + to_date + "' LIMIT " + rowValueTbl2 + ",1";

                    int temp = 0;

                    conn = dbc.connection();
                    PreparedStatement pst = conn.prepareStatement(sql);
                    ResultSet rs = pst.executeQuery(sql);
                    while (rs.next()) {
                        temp = Integer.parseInt(rs.getString("sn"));
                        temp_id = rs.getString("cid");
                    }

                    String sql2 = "DELETE FROM `tbl_credit_sales_subdetails` WHERE `sn`=" + temp;
//                    PreparedStatement ps = conn.prepareStatement(sql2);
                    int i = db.deleteData(sql2);
                    int rowval = rowValueTbl1;
                    
                    //REFRESH TABLES
                    updateTopTable();
                    reSelected(rowval);
                    updateLowerTable();

                } catch (Exception e) {
                    System.out.println("delete");
                    e.printStackTrace();
                }
            }
        }

    }
    
    @FXML
    private void cancel(){
        
        //CLEAR TEXTFIELD AND LABEL
        textField_ReceivedAmount.clear();
        label_RemainingAmount.setText("");
    }
}
