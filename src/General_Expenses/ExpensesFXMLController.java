/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package General_Expenses;

import NumberTextField.NumberTextField;
import UsableMethods.Database;
import UsableMethods.Message;
import UsableMethods.dbConnection;
import UsableMethods.sqlForCashinhand;
import UsableMethods.table;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author KonohaVillage
 */
public class ExpensesFXMLController implements Initializable {

    boolean var = false;

    @FXML
    private TableColumn<table, Integer> column_Sn;

    @FXML
    private TableColumn<table, Double> column_TotalAmount;

    @FXML
    private NumberTextField textField_TotalAmount;

    @FXML
    private TableColumn<table, Double> column_Quantity;

    @FXML
    private TableColumn<table, String> column_Date;
    @FXML
    private TableView<table> table_Expenses;

    @FXML
    private TableColumn<table, Double> column_Amount;

    @FXML
    private TableColumn<table, String> column_ExpensesType;

    @FXML
    private TableColumn<table, String> column_Particular;

    @FXML
    public ComboBox combo_ExpensesType;
    
    @FXML
    private ComboBox comboBox_View;

    @FXML
    private Label label_Quantity;

    @FXML
    private TextField textField_Particular;

    @FXML
    private Label label_Particular;

    @FXML
    private Label label_Amount;

    @FXML
    private Label label_TotalAmount;

    @FXML
    private NumberTextField textField_Quantity;

    @FXML
    private Button button_Delete;

    @FXML
    private NumberTextField textField_Amount;

    @FXML
    private Button button_Save;

    @FXML
    private Label label_ExpensesType;

    @FXML
    private Button button_Edit;

    @FXML
    private DatePicker date_to;

    @FXML
    private DatePicker date_from;
    
       @FXML
    private Button button_clear;
    

    
    
    @FXML
    private TextField textField_TotalMill;
    
    
    @FXML
    private TextField textField_TotalAdmin;

    public ObservableList<String> ExpensesList = FXCollections
            .observableArrayList("Administrative Expenses", "Miscelloneous Expenses");
    
    public Integer rowvalue3 = 0;
    public static int rowvalue = 0;
    public static int listvalue = 0;
    Connection con = null;
    ResultSet rs=null;
    PreparedStatement pst = null;
    dbConnection dbc = new dbConnection();
    Database db = new Database();
    String staticId = null;
    private final IntegerProperty tableIndex = new SimpleIntegerProperty();
    Message msg = new Message();
    final ObservableList<table> data = FXCollections.observableArrayList();
    final ObservableList<String> viewList = FXCollections
            .observableArrayList("All","Administrative Expenses","Miscelloneous Expenses");
    
    String datee, type,part, amt,quant;

    @FXML
    void save_Expenses(ActionEvent event) {
        
        try {
            
            java.util.Date currentDate = new java.util.Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String date = dateFormat.format(currentDate);
            

            String particular = textField_Particular.getText();
            String expenses_type = (String) combo_ExpensesType.getValue();
            String amount = textField_Amount.getText();

            String quantity = textField_Quantity.getText();

            String total_amount = label_TotalAmount.getText();
            if (var == true) {
                updateData(date, expenses_type, particular, amount, quantity, total_amount);
                var = false;
                textField_Amount.setText("");
                textField_Quantity.setText("");
                textField_Particular.setText("");
                label_TotalAmount.setText("");
            } else {
                if ((!particular.isEmpty()) && !(textField_Amount.getText().isEmpty()) && !(textField_Quantity.getText().isEmpty())) {

                    if(Double.parseDouble(label_TotalAmount.getText())>0.0){
                    insertData(date, expenses_type, particular, amount, quantity, total_amount);
                    textField_Amount.setText("");
                    textField_Quantity.setText("");
                    textField_Particular.setText("");
                    label_TotalAmount.setText("");
                    msg.alertMessage(Alert.AlertType.NONE, "hurray", "data saved successfully");
                    }
                    else{
                        msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Amount is O cannot Insert");
                    }
                }
                 else {
                    msg.alertMessage(Alert.AlertType.NONE, "ërror", "the required field are empty");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        show_Data();

    }

    public void totalCalculate() {
        String calc_amount = textField_Amount.getText();
        String calc_quantity = textField_Quantity.getText();
        if (!(calc_quantity.isEmpty()) && !(calc_amount.isEmpty())) {
            Double temp_total = Double.parseDouble(calc_amount) * Double.parseDouble(calc_quantity);
            label_TotalAmount.setText(String.valueOf(temp_total));
        } else {
            label_TotalAmount.setText("0");
        }

    }

    @FXML
    void edit_Data(ActionEvent event) {
        onclickIntable();
       

    }

    @FXML
    void delete_Data(ActionEvent event) {
        getSelected();
        remove();
        show_Data();
    }
    
    @FXML
    void clear_field(ActionEvent event){
        defaultValue();
    }

    public void remove() {
       String[] options = {"Yes", "No"};

        int ans = JOptionPane.showOptionDialog(null, "Sure to Delete??", "Delete Confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (ans == 0) {
           rowvalue--;
           try {
                 table table = data.get(rowvalue);
                 String date = table.s1.get();
                 Double amount = table.d1.get();
                 Double quantity = table.d2.get();
                 String particular = table.s3.get();
                 String exptype = table.s2.get();
                     System.out.println(particular);
                 
                 con = dbc.connection();
                 String sql = "DELETE FROM `tbl_misc` WHERE `date` ='"+date+"' AND `amount`='"+amount+"' AND `quantity`= '"+quantity+"' AND `expenses_type` = '"+exptype+"' LIMIT 1";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
                 
                 sqlForCashinhand cash = new sqlForCashinhand();
                 cash.deleteInCashInhand(date,"Expense on: " +particular, amount, null);

                 
             } catch (Exception e) {
                 
                 e.printStackTrace();
             }
         
    }

        }
        


    public void comboExpenses() {
        combo_ExpensesType.setItems(ExpensesList);
        combo_ExpensesType.setValue("Administrative Expenses");
    }
    
    public void updateData(String date, String expenses_type, String particular, String amount, String quantity, String total_amount) {
        try {
            con = dbc.connection();
            
           String sql = "DELETE FROM tbl_misc WHERE expenses_type = '"+type+"' AND particular = '" +part+"' AND date='" +datee+"' AND amount='" +amt+"'AND quantity='" +quant+"'";
             pst = (PreparedStatement) con.prepareStatement(sql);
              int j = pst.executeUpdate();
                               sqlForCashinhand cash = new sqlForCashinhand();
                 cash.deleteInCashInhand(date,"Expense on: " +particular, Double.parseDouble(amt), null);
              pst = (PreparedStatement) con.prepareStatement("insert into tbl_misc values(?,?,?,?,?,?)");
            
            pst.setString(1, date);
            pst.setString(2, expenses_type);
            pst.setString(3, particular);
            pst.setString(4, amount);
            pst.setString(5, quantity);
            pst.setString(6, total_amount);

            int i = pst.executeUpdate();
            /*cash in hand insertion*/
            cash.insertInCashInhand(date,"Expense on: " +particular, Double.parseDouble(amount), null);
            if (i > 0) {
                msg.alertMessage(Alert.AlertType.NONE, "Saved", "Data saved successfully.");

            } else {
                msg.alertMessage(Alert.AlertType.NONE, "Error", "Data can not be saved.");
            }
             

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

 
 
 
//table view function 
    public void show_Data() {
        try {
           String viewBy = String.valueOf(comboBox_View.getValue());
            String from_date = String.valueOf(date_from.getValue());
            String to_date = String.valueOf(date_to.getValue());
            String sql = "SELECT * FROM `tbl_misc` WHERE `expenses_type` = '"+viewBy+"' AND `date` >= '" + from_date + "' And `date` <= '" + to_date + "'";
            if(viewBy=="All"){
             sql="SELECT * FROM `tbl_misc` WHERE `date` >= '" + from_date + "' And `date` <= '" + to_date + "'";
            }
            
            
            System.out.println("PreparedStatement created Successfully");
            con = dbc.connection();
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            int temp_sn = 1;

            column_Sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            column_Date.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            column_ExpensesType.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            column_Particular.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            column_Amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            column_Quantity.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
            column_TotalAmount.setCellValueFactory(new PropertyValueFactory<table, Double>("d3"));

            table_Expenses.getItems().clear();

            while (rs.next()) {
                table as = new table();

                as.i1.set(temp_sn++);
                as.s1.set(rs.getString("date"));
                as.s2.set(rs.getString("expenses_type"));
                as.s3.set(rs.getString("particular"));
                as.d1.set(rs.getDouble("amount"));
                as.d2.set(rs.getDouble("quantity"));
                as.d3.set(rs.getDouble("total_amount"));

                data.add(as);
            }
            Double temp_tamount=0.0;
            String sql2="SELECT `total_amount` FROM `tbl_misc` WHERE `expenses_type` = 'Administrative Expenses'";
            
            pst = (PreparedStatement) con.prepareStatement(sql2);
            rs = pst.executeQuery(sql2);
             while(rs.next()){
            Double ttl_amt=rs.getDouble("total_amount");
             temp_tamount= temp_tamount + ttl_amt;
           
             }
             textField_TotalAdmin.setText(String.valueOf(temp_tamount));
             
            temp_tamount=0.0;
            
            sql2="SELECT `total_amount` FROM `tbl_misc` WHERE `expenses_type` = 'Miscelloneous Expenses'";
            
            pst = (PreparedStatement) con.prepareStatement(sql2);
            rs = pst.executeQuery(sql2);
             while(rs.next()){
            Double ttl_amt=rs.getDouble("total_amount");
             temp_tamount= temp_tamount + ttl_amt;
           
             }
             textField_TotalMill.setText(String.valueOf(temp_tamount));
            table_Expenses.setItems(data);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
      public void comboBoxFunctionList(){
          combo_ExpensesType.setItems(ExpensesList);
          combo_ExpensesType.setValue("Administrative Expenses");
           comboBox_View.setItems(viewList);
           comboBox_View.setValue("All");
           
           
      }

    public void initialDate() {

        date_from.setValue(db.NOW_LOCAL_DATEfirstDateOfMonth());
        date_to.setValue(db.NOW_LOCAL_DATEcurrentDate());
    }

    public void getSelected() {
        try {
            column_Sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            column_Date.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            column_ExpensesType.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            column_Particular.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            column_Amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            column_Quantity.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
            column_TotalAmount.setCellValueFactory(new PropertyValueFactory<table, Double>("d3"));

            table_Expenses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {

                    rowvalue = (data.indexOf(newValue) + 1);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertData(String date, String expenses_type, String particular, String amount, String quantity, String total_amount) {
        try {

            String temp_id = null;
            con = dbc.connection();
            String sql = "SELECT * FROM tbl_misc";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while (rs.next()) {
                temp_id = rs.getString("date");
                if (temp_id == (date)) {
                    break;
                }
            }
            if (temp_id != (date)) {
                pst = (PreparedStatement) con.prepareStatement("insert into tbl_misc values(?,?,?,?,?,?)");

                pst.setString(1, date);
                pst.setString(2, expenses_type);
                pst.setString(3, particular);
                pst.setString(4, amount);
                pst.setString(5, quantity);
                pst.setString(6, total_amount);
                int i = pst.executeUpdate();

                sqlForCashinhand cash = new sqlForCashinhand();
                cash.insertInCashInhand(date,"Expense on: " +particular, Double.parseDouble(total_amount), null);
                
                
                if (i <= 0) {
                    msg.alertMessage(Alert.AlertType.NONE, "ERROR", "Cannot save data");
                }

            } else {
                msg.alertMessage(Alert.AlertType.NONE, "ERROR", "Dublicate id re-enter the values");
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void onclickIntable() {
        getSelected();
        var = true;

        System.out.println(var);

        try {
            if (rowvalue != -1) {
                int row = rowvalue - 1;
                table tbl = data.get(row);
                
                combo_ExpensesType.setValue(tbl.s2.get());
                type = tbl.s2.get();
                datee = tbl.s1.get();
                textField_Particular.setText(tbl.s3.get());
                part = tbl.s3.get();textField_Amount.setText(String.valueOf(tbl.d1.get()));
                amt = String.valueOf(tbl.d1.get());
                textField_Quantity.setText(String.valueOf(tbl.d2.get()));
                quant = String.valueOf(tbl.d2.get());
                label_TotalAmount.setText(String.valueOf(tbl.d3.get()));
            }
        } catch (Exception e) {

        }
    }
    
    public void defaultValue(){
           combo_ExpensesType.setValue("Administrative Expenses");
           
           textField_Amount.clear();
           textField_Particular.clear();
           textField_Quantity.clear();           
           label_TotalAmount.setText("0");
           var = false;
       }
    
//    public void viewby(){
//       
//        try {
//             String viewBy= String.valueOf(comboBox_View.getValue());
//             
//             String sql5 = "select * from `tbl_misc` where `expenses_type` = '"+viewBy+"'";
//            if(viewBy=="All"){
//             sql5="SELECT * FROM `tbl_misc` ";
//            }
//             Integer temp_sn= 1;
//
//            con = dbc.connection();
//            pst = (PreparedStatement) con.prepareStatement(sql5);
//            ResultSet rs = pst.executeQuery(sql5);
//
//            column_Sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
//            column_Date.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
//            column_ExpensesType.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
//            column_Particular.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
//            column_Amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
//            column_Quantity.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
//            column_TotalAmount.setCellValueFactory(new PropertyValueFactory<table, Double>("d3"));
//
//            table_Expenses.getItems().clear();
//
//            while (rs.next()) {
//                table as = new table();
//
//                as.i1.set(temp_sn++);
//                as.s1.set(rs.getString("date"));
//                as.s2.set(rs.getString("expenses_type"));
//                as.s3.set(rs.getString("particular"));
//                as.d1.set(rs.getDouble("amount"));
//                as.d2.set(rs.getDouble("quantity"));
//                as.d3.set(rs.getDouble("total_amount"));
//
//                data.add(as);
//            }
//            
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initialDate();
        comboBoxFunctionList();
        getSelected();
        comboExpenses();
        totalCalculate();
        show_Data();

    }
}
