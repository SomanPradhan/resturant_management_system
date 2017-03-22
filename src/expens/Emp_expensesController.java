

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expens;

import UsableMethods.*;
import NumberTextField.NumberTextField;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author User
 */
public class Emp_expensesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    //defining first table
    @FXML
    public TableView<table> table_Expenses;

    @FXML
    public TableColumn<table, Integer> sne;
    @FXML
    public TableColumn<table, String> eid;
    @FXML
    public TableColumn<table, String> ename;
    @FXML
    public TableColumn<table, String> post;
    @FXML
    public TableColumn<table, Double> total_amount;

    //defining second table
    @FXML
    public TableView<table> table_expenses_detail;

    @FXML
    public TableColumn<table, Integer> sn;
    @FXML
    public TableColumn<table, String> itemcode;
    @FXML
    public TableColumn<table, String> date;
    @FXML
    public TableColumn<table, String> itemname;
    @FXML
    public TableColumn<table, String> itemrate;
    @FXML
    public TableColumn<table, Double> quantity;
    @FXML
    public TableColumn<table, Double> amount;

    //Buttons and Label defined
    @FXML
    private JFXButton button_delete;
    @FXML
    private Label label_Itemrate;
    @FXML
    private JFXButton label_Cancel;
    @FXML
    private Label label_Name;
    @FXML
    private JFXTextField textField_Itemcode;
    @FXML
    private JFXButton button_Submit;
    @FXML
    private Label label_Itemname;
    @FXML
    private Label label_Amount;
    @FXML
    private NumberTextField textField_Quantity;
    @FXML
    private Label label_Eid;
    @FXML
    private Label label_DiscountPrice;
    @FXML
    private NumberTextField numericTextField_Discount;
    @FXML
    private DatePicker date_From;
    @FXML
    private DatePicker date_To;

    //db connection
    public static String eid_static;
    public static boolean clear_flag;
    public static boolean userData_flag = false;
    public static int rowvalue = 0;
    public static int listvalue = 0;
    public static int rowvalue_tbl2 = 0;
    public static int sn_static;
    Connection con = null;
    PreparedStatement pst = null;
    PreparedStatement ps = null;
    PreparedStatement pstt = null;
    ResultSet rs = null;
    dbConnection dbc = new dbConnection();

    final ObservableList<table> data = FXCollections.observableArrayList();
    final ObservableList<table> data2 = FXCollections.observableArrayList();
    
    Message msg=new Message();

    @FXML
    //for entering the data "Submit Button"
    public void enter_data(ActionEvent event) {

        try {

            //for current date from the system
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String insertedDate = dateFormat.format(currentDate);

            boolean flag = false;
            String em_id, im_code;
            con = dbc.connection();
            String eid, ename, itemcode, itemname;
            Double itemrate, amount, quantity;
            eid = label_Eid.getText();
            ename = label_Name.getText();
            itemcode = textField_Itemcode.getText();
            itemname = label_Itemname.getText();
            itemrate = Double.parseDouble(label_DiscountPrice.getText());
            amount = Double.parseDouble(label_Amount.getText());
            quantity = Double.parseDouble(textField_Quantity.getText());
            
//checking for Eid and Itemcode
            String sql1 = "SELECT `eid` FROM `tbl_employee_details`";
            String sql2 = "SELECT `item_code` FROM `tbl_items_mp`";
            ps = (PreparedStatement) con.prepareStatement(sql1);
            pstt = (PreparedStatement) con.prepareStatement(sql2);
            ResultSet rs1 = ps.executeQuery(sql1);
         
            while (rs1.next()) {
                ResultSet rs2 = pstt.executeQuery(sql2);
                while (rs2.next()) {
                    em_id = rs1.getString("eid");
                    im_code = rs2.getString("item_code");
                    if (em_id.equals(eid) && im_code.equals(itemcode)) {
                        flag = true;
                    }
                }
            }
           //inserting into table_employee_expenses_details 
            if (flag == true) {
               
                int sn = sn_static;
                String sql="INSERT INTO `tbl_employee_expenses_details`(`eid`, `date`, `item_code`, `item_name`, `item_rate`, `quantity`, `amount`) VALUES ('"+eid+"', '"+insertedDate+"', '"+itemcode+"', '"+itemname+"', '"+itemrate+"', '"+quantity+"', '"+amount+"')";
                //for inserting into the database
                if (amount != 0.0) {
                    pst = (PreparedStatement) con.prepareStatement(sql);

                    int i = pst.executeUpdate();

                    cancel();
                } else {
                    //JOptionPane.showMessageDialog(null, "Amount is 0", "Error", JOptionPane.ERROR_MESSAGE);
                    msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Amount is 0");
                }
            } else {
                //JOptionPane.showMessageDialog(null, "Eid or itemcode didn't matched", "Error", JOptionPane.ERROR_MESSAGE);
                msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Eid or itemcode didn't matched");
            }
            int rowval = rowvalue;
            clear_flag = true;
            use_data(); 
            show_tbl1_data();
            reSelected(rowval);
            show_tbl2_data(eid);
            //use_tbl1_for_tbl2();
        } catch (Exception ex) {
            ex.printStackTrace();
            //JOptionPane.showMessageDialog(null, "Eid or itemcode didn't matched or no quantity", "Error", JOptionPane.ERROR_MESSAGE);
            msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Eid or itemcode didn't matched or no quantity");
        }
    }
    public void datepickerfunction(){
        int row = rowvalue -1;
        if(row==-1){
            show_tbl1_data();
        }
        else{
            use_tbl1_for_tbl2();
        }
    }
    
    //for the discount amount
    public void discount(){
       try{
        Double item = Double.parseDouble(label_Itemrate.getText());
        Double dis;
        if(!(numericTextField_Discount.getText().isEmpty()))
        dis = Double.parseDouble(numericTextField_Discount.getText());
        else
            dis =0.0;
         Double discountAmount = item - ((dis/100)*item);
         label_DiscountPrice.setText(String.valueOf(discountAmount));
         amount_food();
    }catch(Exception e){
        
    }
    }
    @FXML
    //for clearing the data "Cancel button"
    public void cancel_data(ActionEvent event) {
        clear_flag = false;
        cancel();

    }
    
    //for clearing the data in label
    public void cancel() {
        if (clear_flag == false) {
            label_Eid.setText("Emp_id");
            label_Name.setText("Name of emp");
        }
        textField_Itemcode.setText(null);
        textField_Itemcode.setPromptText("Code of item");
        label_Itemname.setText("name of item");
        label_Itemrate.setText("0.0");
        textField_Quantity.setText("0.0");

        label_Amount.setText("0.0");
    }
    
    //on mouseclick for "Quantity"
    public void quantityClear() {
        textField_Quantity.setText(null);
        label_Amount.setText("0.0");
    }

    //displaying data from tbl_expenses_details in table2 from date_from to date_to
    public void show_tbl2_data(String id) {
        try {
            LocalDate from_date = date_From.getValue();
//            System.out.println("Date value from" + from_date);
            LocalDate to_date = date_To.getValue();
            Integer temp_sn = 1;
            String sql = null;

            String to_dateString = String.valueOf(to_date);
//            System.out.println("string valuen date string value " + to_dateString);
            sql = "SELECT * FROM `tbl_employee_expenses_details` WHERE `eid`=" + id + " And `date` <= '" + to_date + "' And `date` >= '" + from_date + "'";

            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            itemcode.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            itemname.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            date.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            itemrate.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
            quantity.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));

            table_expenses_detail.getItems().clear();

            while (rs.next()) {
                table as = new table();

                as.i1.setValue(temp_sn++);
                as.s1.set(rs.getString("item_code"));
                as.s2.set(rs.getString("item_name"));
                as.s4.set(rs.getString("item_rate"));
                as.s3.set(rs.getString("date"));
                as.d1.set(rs.getDouble("quantity"));
                as.d2.set(rs.getDouble("amount"));

                data2.add(as);
            }
            table_expenses_detail.setItems(data2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //if data is entry on tbl_employee_details and not in tbl_employee_expense then it is used to automatically add
    public void lost_data() {
        try {
            String id = null;
            String sql3 = null;
            String sql = "select * from tbl_employee_details";
            String sql2 = "SELECT * FROM `tbl_employee_expenses`";
            //System.out.println("PreparedStatement created Successfully");
            con = dbc.connection();

            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);

            while (rs.next()) {
                boolean state = false;
                ps = (PreparedStatement) con.prepareStatement(sql2);
                ResultSet rs2 = ps.executeQuery(sql2);
                id = rs.getString("eid");
                while (rs2.next()) {
                    if (rs.getString("eid").equals(rs2.getString("eid"))) {
                        state = true;
                    }
                }
                if (state != true) {
                    //inserting if there is no any data
                    sql3 = "INSERT INTO `tbl_employee_expenses` (`eid`, `total_amount`) VALUES ('" + id + "', '0.0');";
                    pst = (PreparedStatement) con.prepareStatement(sql3);
                    //System.out.println("PreparedStatement created Successfully44");
                    //System.out.println("update left");
                    int rs8 = pst.executeUpdate();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //display data from tbl_expenses in table1
    public void show_tbl1_data() {
        try {
            lost_data();
            Integer temp_sn1 = 1;
            String sql = "select * from tbl_employee_details";
            String sql2 = "SELECT * FROM `tbl_employee_details` AS `ed`,`tbl_employee_expenses`AS`ee` WHERE ed.Eid = ee.Eid";
            //System.out.println("PreparedStatement created Successfully");

            con = dbc.connection();
            ps = (PreparedStatement) con.prepareStatement(sql2);
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            ResultSet rs2 = ps.executeQuery(sql2);
            sne.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            eid.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            ename.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            post.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            total_amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));

            table_Expenses.getItems().clear();
            
            String id;
            LocalDate from_date = date_From.getValue();
            //System.out.println("Date value from" + from_date);
            LocalDate to_date = date_To.getValue();
            while (rs.next() && rs2.next()) {
                table as = new table();

                if (rs.getString("eid").equals(rs2.getString("eid"))) {
                    //System.out.println("id " + rs.getString("eid") + " another id " + rs2.getString("eid"));

                    as.i1.set(temp_sn1++);
                    as.s1.set(rs.getString("eid"));
                    as.s2.set(rs.getString("ename"));
                    as.s3.set(rs.getString("post"));
                    
                    /* */
                     Double value = 0.00;
                id = rs.getString("eid");
                //System.out.println("value of id=" + id);
                String sql3 = "SELECT * FROM `tbl_employee_expenses_details` WHERE `eid`= '" + id + "' AND `date` >= '" + from_date + "' And `date` <= '" + to_date + "'";
                pstt = (PreparedStatement) con.prepareStatement(sql2);
                ResultSet rst = pstt.executeQuery(sql3);
                //System.out.println("run query 2" + sql3);
                while (rst.next()) {
                    //System.out.println("run execute query before " + value);
                    value += rst.getDouble("amount");
                    //System.out.println("run execute query after " + value);

                }
                    as.d1.set(value);
                    data.add(as);

                }
            }

            table_Expenses.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    //using table on eid value to displaying the table 2 values whose eid value is equal to eid
    public void use_tbl1_for_tbl2() {
        try {

            getSelected();
            String id = null;
            Integer new_row = rowvalue - 1;
            String sql = "SELECT * FROM tbl_employee_details LIMIT " + new_row + ",1";
            con = dbc.connection();
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            //System.out.println("run id");
            while (rs.next()) {
                id = rs.getString("eid");
                label_Eid.setText(rs.getString("eid"));
                label_Name.setText(rs.getString("ename"));
            }
            use_data();
            show_tbl2_data(id);
            eid_static = id;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
//display according to date in tableview
    public void use_data() {
        try {

            String sql2 = null;
            String id = null;
            String sql = "SELECT * FROM `tbl_employee_expenses`";
            con = dbc.connection();
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
           
            LocalDate from_date = NOW_LOCAL_DATE_FROM();
            //System.out.println("this datepicker datefrom =" + from_date);

            LocalDate to_date = NOW_LOCAL_DATE_TO();
            //System.out.println("this datepicker datefrom =" + to_date);
            while (rs.next()) {
                Double value = 0.00;
                id = rs.getString("eid");
                //System.out.println("value of id=" + id);
                sql2 = "SELECT * FROM `tbl_employee_expenses_details` WHERE `eid`= '" + id + "' AND `date` >= '" + from_date + "' And `date` <= '" + to_date + "'";
                ps = (PreparedStatement) con.prepareStatement(sql2);
                ResultSet rst = ps.executeQuery(sql2);
                //System.out.println("run query 2" + sql2);
                while (rst.next()) {
                    //System.out.println("run execute query before " + value);
                    value += rst.getDouble("amount");
                    //System.out.println("run execute query after " + value);

                }
                update_data(id, value);

            }
            int rowval = rowvalue;

            //use_data();
            show_tbl1_data();
            reSelected(rowval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //displaying the initial value of the date pickers
    public void initial(){
        date_To.setValue(NOW_LOCAL_DATE_TO());
        date_From.setValue(NOW_LOCAL_DATE_FROM());
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

    
    //it is used to update the value in database whenever a new data is added or deleted 
    public void update_data(String id, Double value) {

        try {
            //System.out.println("run this program");
            con = dbc.connection();
            String sql = "UPDATE `tbl_employee_expenses` SET `total_amount` = '" + value + "' WHERE `tbl_employee_expenses`.`eid` = " + id;
            ps = (PreparedStatement) con.prepareStatement(sql);
            int i = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //deleting the data from the table 2
    public void delete_from_tbl2(ActionEvent event) {
        getSelected_for_tbl2();
        if (rowvalue_tbl2 > 0) {
            remove();

            clear_flag = false;
        } else {
            //JOptionPane.showMessageDialog(null, "No item selected", "Error", JOptionPane.ERROR_MESSAGE);
            msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "No item Selected");
        }

    }

    //used by the function delete_from_tbl2 for deleting data
    public void remove() {
        String[] options = {"Yes", "No"};
        //int ans = JOptionPane.showOptionDialog(null, "Sure to Delete??", "Delete Confrim", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        boolean ans= msg.alertOptionMessage("Delete", "Are you sure ??");
        if (ans) {
            rowvalue_tbl2--;
            String temp_id = null;
            if (rowvalue_tbl2 > -1) {
                try {
                    //System.out.println("rowvalue dsaa " + rowvalue_tbl2);
                    String from_date =  String.valueOf(date_From.getValue());
                    String to_date =  String.valueOf(date_To.getValue());
                    //System.out.println("rowvalue dsaa " + rowvalue_tbl2);
                    String sql = "SELECT * FROM `tbl_employee_expenses_details` WHERE `eid`=" + eid_static + " AND `date`>= '"+from_date+"' AND `date`<= '"+to_date+"' LIMIT " + rowvalue_tbl2 + ",1";

                    int temp = 0;

                    con = dbc.connection();
                    pst = (PreparedStatement) con.prepareStatement(sql);
                    ResultSet rs = pst.executeQuery(sql);
                    while (rs.next()) {
                        temp = Integer.parseInt(rs.getString("sn"));
                        temp_id = rs.getString("eid");
                    }

                    String sql2 = "DELETE FROM `tbl_employee_expenses_details` WHERE `sn`=" + temp;
                    ps = (PreparedStatement) con.prepareStatement(sql2);
                    int i = ps.executeUpdate(sql2);
                    use_data();
                 
                    show_tbl2_data(temp_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    
    // used for selecting the table2 data which can be use for deleting the data
    public void getSelected_for_tbl2() {
        try {
            sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            itemcode.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            itemname.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            date.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            itemrate.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
            quantity.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));

            table_expenses_detail.setItems(data2);

            table_expenses_detail.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                    rowvalue_tbl2 = (data2.indexOf(newValue) + 1);
                    //System.out.println("ON RowValue for tbl 2 is : " + rowvalue_tbl2);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //for reselecting the data in the table1 after its get refreshed
    public void reSelected(int rowval) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int row = rowval - 1;
                table_Expenses.requestFocus();
                table_Expenses.getSelectionModel().select(row);
                table_Expenses.getFocusModel().focus(row);
            }
        });
    }

    //for selecting the data which is clicked by the user
    public void getSelected() {
        try {
            sne.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            eid.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            ename.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            post.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));

            table_Expenses.setItems(data);

            table_Expenses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                    rowvalue = (data.indexOf(newValue) + 1);
                    //System.out.println("ON RowValue is : " + rowvalue);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {        
        initial();
        use_data();
        show_tbl1_data();
        getSelected();
        getSelected_for_tbl2();        
    }

//get the quantity and display the amount
    public void amount_food() {
        try {
            String rate, quantity;
            String amount;
            rate = (label_DiscountPrice.getText());

            quantity = (textField_Quantity.getText());
            if (quantity.isEmpty()) {
                label_Amount.setText("0.0");
            }
            amount = String.valueOf(Double.parseDouble(rate) * Double.parseDouble(quantity));
            label_Amount.setText(amount);

        } catch (Exception e) {

        }
    }
    
//checking the itemcode and getting its name and rate
    public void id_food() {
        try {
            String varItem = null;
            con = dbc.connection();
            String itemcode = textField_Itemcode.getText();
            String sql = "SELECT * FROM `tbl_items_mp` WHERE `item_code`='" + itemcode + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while (rs.next()) {
                varItem = itemcode;
                label_Itemname.setText(rs.getString("item_name"));
                label_Itemrate.setText(rs.getString("rate"));
            }
            if (varItem!=itemcode) {
                label_Itemname.setText("name of item");
                label_Itemrate.setText("0.0");
                label_Amount.setText("0.0");
            }

            amount_food();
            discount();
        } catch (Exception e) {
            label_Itemname.setText("name of item");
            label_Itemrate.setText("0.0");
            label_Amount.setText("0.0");
            discount();
        }
    }

}
