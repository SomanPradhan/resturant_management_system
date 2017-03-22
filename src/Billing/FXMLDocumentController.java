package Billing;

import UsableMethods.*;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXTextField;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import java.util.ArrayList;
import static java.lang.System.out;
import static java.lang.System.err;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rubesh
 */
public class FXMLDocumentController implements Initializable {

    //CREATE LISTVIEW OF TABLE NO
    @FXML
    public ListView<String> listView;

    @FXML
    private Label label_TableNo;

    //CREATE TABLE FOOD_ORDER
    @FXML
    public TableView<table> table_FoodOrder;

    @FXML
    public TableColumn<table, Double> sn;
    @FXML
    public TableColumn<table, String> foodCode;
    @FXML
    public TableColumn<table, String> foodName;
    @FXML
    public TableColumn<table, Double> quantity;
    @FXML
    public TableColumn<table, Double> price;
    @FXML
    public TableColumn<table, Double> amount;

    //DEFINE BUTTONS AND TEXTFIELDS
    @FXML
    JFXButton btn_Enter;
    @FXML
    JFXButton btn_Delete;
    @FXML
    JFXButton btn_Verify;
    @FXML
    JFXButton btn_Print;
    @FXML
    JFXButton btn_Finish;
    @FXML
    JFXTextField textField_FoodCode;
    @FXML
    JFXTextField textField_FoodName;
    @FXML
    JFXTextField textField_Quantity;
    @FXML
    JFXTextField textField_Total;
    @FXML
    Label label_Date;
    @FXML
    Label label_Time;
    @FXML
    JFXButton btn_Credit;
    @FXML
    JFXButton btn_Switch;

    //DECLARE CLASS OBJECT
    Message m = new Message();
    Database db = new Database();
    dbConnection dbc = new dbConnection();
    
    Connection conn = null;
    

    //DEFINE VARIABLE
    public static Integer listvalue = 0;
    public static Integer rowvalue = 0;

    private IntegerProperty tableIndex = new SimpleIntegerProperty();
    private IntegerProperty listIndex = new SimpleIntegerProperty();

    //CREATE TABLE DATA
    public final ObservableList<table> data = FXCollections.observableArrayList();

    /**
     * ********************************************
     * METHOD FOR ENTER INTO TABLE *
     * *********************************************
     */
    public void enterIntoTable(ActionEvent event) {
        if (textField_FoodCode.getText().isEmpty() || textField_Quantity.getText().isEmpty()) {

            m.alertMessage(AlertType.WARNING, "One of the fields is empty", "Please fill the empty fields");
        } else {

            //UPDATE TABLE
            String foodcode = textField_FoodCode.getText();
            double quantity = Double.valueOf(textField_Quantity.getText());

            Double sn = listvalue + getLastSN(listvalue);
            Integer tokenid = listvalue;

            //GETS ITEM NAME AND RATE
            try {
                Collection c = getItemsMP(textField_FoodCode.getText());
//                Collection c = bdb.getItemsMP(foodcode);
                if (!c.isEmpty()) {
                    Iterator iter = c.iterator();
                    Object itemcode = iter.next();
                    Object itemname = iter.next();
                    Object rate = iter.next();
                    System.out.println("itemcode = " + itemcode);
                    System.out.println("itemname = " + itemname);
                    System.out.println("rate = " + rate);


                    String fcode = String.valueOf(itemcode);
                    String fname = String.valueOf(itemname);
                    Double fprice = Double.valueOf(String.valueOf(rate));
                    Double famount = quantity * fprice;
                    
                    
                    ArrayList values = new ArrayList();
                    values.add(sn);
                    values.add(fcode);
                    values.add(fname);
                    values.add(quantity);
                    values.add(fprice);
                    values.add(famount);
                    values.add(tokenid);
                    
                    int i = db.insertData("tbl_sales_temp", values);
                    System.out.println(i);
                    
                }
            } catch (Exception e) {
                System.out.println("EnterIntoTable");
                e.printStackTrace();
                m.alertMessage(AlertType.ERROR, "Code doesnot match","Please re-check the Item Code.");
            }

            //CLEAR TEXTFIELD
            textField_Quantity.setText("");
            textField_Quantity.deselect();

            textField_FoodCode.setText("");
            textField_FoodCode.deselect();

            //UPDATE TABLEVIEW
            updateFrontEndTable(listvalue);

            //UPDATE TOTAL
            textField_Total.setText(String.valueOf(updateTotal(listvalue)));

        }
    }
    
    public void whiletypingCode(){
        
        try {
            String sql = "SELECT * FROM `tbl_items_mp`";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while(rs.next()){
                String code = rs.getString("item_code");
                String name = rs.getString("item_name");
                if(textField_FoodCode.getText().trim().equalsIgnoreCase(code.trim())){
                    textField_FoodCode.setText(code.trim());
                    textField_FoodName.setText(name.trim());
                    textField_FoodCode.end();
                }
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void whiletypingName(){
        
        try {
            String sql = "SELECT * FROM `tbl_items_mp`";
            PreparedStatement pst = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while(rs.next()){
                String code = rs.getString("item_code");
                String name = rs.getString("item_name");
                if(textField_FoodName.getText().trim().equalsIgnoreCase(name.trim())){
                    textField_FoodCode.setText(code.trim());
                    textField_FoodName.setText(name.trim());
                    textField_FoodName.end();
                }
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    

    /**
     * ********************************************
     * METHOD FOR DELETE FROM TABLE *
     * *********************************************
     */
    public void deleteFromTable(ActionEvent event) {
        deleteFunction();
    }

    public void deleteFunction() {

        Boolean ans = m.alertOptionMessage("Delete confirm!", "Are you sure want to delete?");
        if (ans == true) {
            rowvalue--;
            if (rowvalue > -1) {

                //DELETE DATA FROM DATABASE
                Double sn = getSelectedSN(listvalue, rowvalue);
                
                String sql = "delete from `tbl_sales_temp` where `sn` =" + sn;
                int i = db.deleteData(sql);
                System.out.println(i);

                //UPDATE TABLEVIEW
                updateFrontEndTable(listvalue);

                //UPDATE TOTAL
                textField_Total.setText(String.valueOf(updateTotal(listvalue)));
            }

            table_FoodOrder.getSelectionModel().clearSelection();
        }
    }

    @FXML
    public void keyPressed(KeyEvent ke) {
        System.out.println(ke);
        FXMLDocumentController fdc = new FXMLDocumentController();
        int code = ke.getKeyCode();
        System.out.println(code);
        if (code == KeyEvent.VK_DELETE && listvalue != null) {
            deleteFunction();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //CREATE LISTVIEW OF TABLES
        ObservableList<String> items = FXCollections.observableArrayList("Table 1",
                "Table 2", "Table 3", "Table 4", "Table 5", "Table 6", "Table 7", "table 8", "table 9", "table 10");

        listView.setItems(items);

        //LISTCELL SELECTION ACTION
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
               
                listIndex.set(items.indexOf(newValue));
//                System.out.println("ON listIndex is : " + items.indexOf(newValue));
//                System.out.println("Selected item: " + newValue);

                listvalue = (items.indexOf(newValue) + 1);
//                System.out.println("The new listValue is " + listvalue);

                //UPDATE TABLEVIEW
                updateFrontEndTable(listvalue);
                //UPDATE TOTAL
                textField_Total.setText(String.valueOf(updateTotal(listvalue)));
            }
        });

        sn.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
        foodCode.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
        foodName.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
        quantity.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
        price.setCellValueFactory(new PropertyValueFactory<table, Double>("d3"));
        amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d4"));

        table_FoodOrder.setItems(data);

        table_FoodOrder.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                tableIndex.set(data.indexOf(newValue));
                rowvalue = (data.indexOf(newValue) + 1);
//                System.out.println("ON RowValue is : " + rowvalue);
            }

        });
        
        System.out.println("Next Bill No is : " +getNextBillId());

        //UPDATE TIME AND DATE
        currentDateTime();

        //DISABLE BUTTONS AND TEXTFIELD 
        disableFunction();
        
    }

    /**
     * ********************************************
     * GETS THE DYNAMIC TIME AND DATE *
     * ********************************************
     */
    public void currentDateTime() {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                int i = 0;
                while (true) {
                    final int finalI = i;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd ");
                            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                            //GET CURRENT TIME WITH DATE()
                            Date date = new Date();
                            label_Date.setText(dateFormat.format(date));
                            label_Time.setText(timeFormat.format(date));

//                            //GET CURRENT TIME WITH CALENDER()
//                            Calendar time = Calendar.getInstance();
//                            label_Date.setText(dateFormat.format(time.getTime()));
//                            label_Time.setText(timeFormat.format(time.getTime()));
                        }
                    });
                    i++;
                    Thread.sleep(1000);
                }
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    /**
     * ********************************************
     * POPULATING THE TABLEVIEW WITH DATABASE *
     * *********************************************
     */
    public boolean updateFrontEndTable(Integer listvalue) {
        System.out.println("listvalue = "+listvalue);
        label_TableNo.setText(listvalue.toString());

        //EMPTY THE TABLE FIRST
        data.removeAll(data);
//        assert table_FoodOrder != null : "fx:id=\"table_FoodOrder\" was not injected: check your FXML file 'FXMLDocument.fxml'.";


        String sql = "select * from tbl_sales_temp where `token_id` =" + listvalue;
        dbConnection dbc = new dbConnection();

        try {
            Connection conn = dbc.connection();
            Statement s = conn.prepareStatement(sql);
            ResultSet rs = s.executeQuery(sql);
//            System.out.println("Hello Nepal  "+String.valueOf(rs));

            //LOOP THROUGH ALL VALUES
            while (rs.next()) {
                table tbl = new table();

                tbl.d1.set(Double.valueOf(rs.getString("sn")));
                tbl.s1.set(rs.getString("item_code"));
                tbl.s2.set(rs.getString("item_name"));
                tbl.d2.set(Double.valueOf(rs.getString("quantity")));
                tbl.d3.set(Double.valueOf(rs.getString("price")));
                tbl.d4.set(Double.valueOf(rs.getString("amount")));
                tbl.i1.set(Integer.valueOf(rs.getString("token_id")));
                data.add(tbl);
            }
            table_FoodOrder.setItems(data);

            return true;

        } catch (Exception e) {
            System.out.println("FrontEndTable");
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * RETURNS THE TOTAL-AMOUNT VALUE *
      */
    public double updateTotal(Integer listvalue) {
        double total_amount = 0.00;
        String sql = "select `amount` from tbl_sales_temp where `token_id` =" + listvalue;
        dbConnection dbc = new dbConnection();
        try {
            Connection conn = dbc.connection();
            Statement s = conn.prepareStatement(sql);
            ResultSet rs = s.executeQuery(sql);

            //LOOP THROUGH ALL VALUES
            while (rs.next()) {
                double amount = rs.getDouble("amount");
                total_amount += amount;
            }
            return total_amount;
        } catch (Exception e) {
            System.out.println("updateTotal");
            e.printStackTrace();
        }
        return total_amount;
    }
    
    /**
     * RETURNS LAST SN VALUE FROM TBL_SALES_TEMP *
      */
    public Double getLastSN(Integer listvalue) {
        Double rowCount = 0.01;
        double row = 0.00;

        try {
            conn = dbc.connection();
            String sql = "select `sn` from tbl_sales_temp where `token_id` ='" + listvalue + "' order by `sn` desc LIMIT 1";

            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                row = rs.getDouble("sn");
            }
            if (row != 0.00f) {
                row = row - listvalue;
            }
            row = row + 0.01f;

            rowCount = row;
            row = 0.00f;
            listvalue = 0;
            return rowCount;

        } catch (Exception e) {
            System.out.println("getLastSN");
            e.printStackTrace();
        }
        return rowCount;
    }

    /**
     * RETURNS NEXT BILL NO *
      */
    public Integer getNextBillId() {
        Integer billno = 1;
        try {
            conn = dbc.connection();
            String sql = "select `bill_id` from tbl_sales order by `bill_id` desc LIMIT 1";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                billno = rs.getInt("bill_id");
                billno++;
            }
            return billno;
        } catch (Exception e) {
            System.out.println("getNextBillId");
            e.printStackTrace();
        }

        return billno;
    }

    /**
     * RETURNS SELECTED SN *
      */
    public Double getSelectedSN(Integer listvalue, Integer rowvalue) {
        Double id = 0.00;

        try {
            conn = dbc.connection();
            String sql = "select * from tbl_sales_temp where `token_id` ='" + listvalue + "' LIMIT 1 OFFSET " + rowvalue;
//            String sql = "select * from tbl_sales_temp where `token_id` = 10 LIMIT 1 OFFSET 3 ";

            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                id = rs.getDouble("sn");
                return id;
            }

        } catch (Exception e) {
            System.out.println("getSelectedSN");
            e.printStackTrace();
        }

        return id;
    }

 
    /**
     * RETURNS ITEMS_MP AND RATE OF PASSED ITEM_CODE *
      */
    public ArrayList<String> getItemsMP(String itemcode) {
        String icode = null;
        String itemname = null;
        String rate = null;
        ArrayList<String> set1 = new ArrayList<String>();
//        Set<String> set1 = new HashSet<String>();
        try {
            conn = dbc.connection();
            String sql = "select * from tbl_items_mp where `item_code`= '" + itemcode.trim() + "' ";
//            String sql = "select * from tbl_mp where `item_code` =  " + itemcode ;
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                icode = rs.getString("item_code");
                itemname = rs.getString("item_name");
                rate = rs.getString("rate");
            }
            set1.add(icode);
            set1.add(itemname);
            set1.add(rate);
            System.out.println("ArrayList contains: " + set1);

            return set1;

        } catch (Exception e) {
            System.out.println("playWithMP");
            e.printStackTrace();
        }

        return set1;

    }

    /**
     * **************************************************
     * VERIFYING METHOD (CONTAINS DISCOUNT & VAT) *
     * ***************************************************
     */
    public void verifyMethod(ActionEvent event) throws Exception {

        try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Billing_Verify/BillingVerify.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            final Stage stage = new Stage();
            stage.setTitle("Verify and Print");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();

        } catch (Exception e) {
            System.out.println("verifymethod");
            e.printStackTrace();
            m.alertMessage(AlertType.ERROR, "Cannot open Verify Window","Please Re-try Again.");
        }
    }
  
    @FXML
    public void openCredit(){
        
        try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/credit/Credit.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            final Stage stage = new Stage();
            stage.setTitle("Credit Sales ");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();

        } catch (Exception e) {
            System.out.println("OpenCredit");
            e.printStackTrace();
            m.alertMessage(AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    @FXML
    private void switchMethod(){
        
        try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Login/Login.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            final Stage stage = new Stage();
            stage.setTitle("User Login");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.sizeToScene();
            btn_Switch.setDisable(true);
            stage.showAndWait();
            btn_Switch.setDisable(false);

        } catch (Exception e) {
            System.out.println("SwitchMethod");
            e.printStackTrace();
            m.alertMessage(AlertType.ERROR, "Cannot open User Login Window","Please Re-try Again.");
        }
    }

    //DISABLES BUTTONS AND TEXTFIELD IF EMPTY OR NOT SELECTED
    private void disableFunction() {

        if (listvalue < 1) {
            btn_Enter.setDisable(true);
            btn_Delete.setDisable(true);
            btn_Verify.setDisable(true);
            textField_FoodCode.setDisable(true);
            textField_FoodName.setDisable(true);
            textField_Quantity.setDisable(true);

            //WAITS FOR TABLE LIST TO BE CLICKED
            Task task = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    int i = 0;
                    while (true) {
                        final int finalI = i;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                if (listvalue >= 1) {
                                    btn_Enter.setDisable(false);
                                    textField_FoodCode.setDisable(false);
                                    textField_FoodName.setDisable(false);
                                    textField_Quantity.setDisable(false);

                                    if (table_FoodOrder.getItems().isEmpty()) {
                                        btn_Verify.setDisable(true);
                                        btn_Delete.setDisable(true);
                                    } else {
                                        btn_Verify.setDisable(false);
                                        btn_Delete.setDisable(false);
                                    }
                                }
                            }
                        });
                        i++;
                        Thread.sleep(1000);
                    }

                }

            };
            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();

        }
    }

    /*
    public void aaa() {
        Task task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                int i = 0;
                while (true) {
                    final int finalI = i;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(finalI);
                        }
                    });
                    i++;
                    Thread.sleep(1000);
                }

            }

        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
     */
}
