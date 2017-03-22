/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Payroll;

//import DatabaseConnection.dbConnection;
import UsableMethods.*;
import NumberTextField.NumberTextField;
import UsableMethods.Database;
import UsableMethods.table;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Soman
 */
public class PayrollController implements Initializable {

    /*labels */
    @FXML
    public Label label_Eid;

    @FXML
    public Label label_Name;

    @FXML
    public Label label_Post;

    @FXML
    public Label label_Salary;

    @FXML
    public Label label_PreDueSalary;

    @FXML
    public Label label_RemainingAmount;

    /*buttons*/
    @FXML
    public JFXButton button_delete;

    @FXML
    public JFXButton button_Submit;

    @FXML
    public JFXButton button_Cancel;

    /*TextField*/
    @FXML
    public NumberTextField textField_PaymentAmount;

    /*DatePickers*/
    @FXML
    private DatePicker date_from;


    @FXML
    private DatePicker date_to;

    /*For Upper table*/
    @FXML
    public TableView<table> table_Up;

    @FXML
    private TableColumn<table, Integer> sn_up;

    @FXML
    private TableColumn<table, String> eid_up;

    @FXML
    private TableColumn<table, String> name_up;

    @FXML
    private TableColumn<table, String> post_up;

    @FXML
    private TableColumn<table, Double> monSal_up;

    @FXML
    private TableColumn<table, Double> remSal_up;

    @FXML
    private TableColumn<table, String> lastPayDate_up;

    /*for lower table*/
    @FXML
    public TableView<table> table_Down;

    @FXML
    private TableColumn<table, Integer> sn_ColumnTblDown;

    @FXML
    private TableColumn<table, String> eid_ColumnTblDown;

    @FXML
    private TableColumn<table, String> monthlySalary_ColumnTblDown;

    @FXML
    private TableColumn<table, String> empExp_ColumnTblDown;

    @FXML
    private TableColumn<table, String> paidAmo_ColumnTblDown;

    @FXML
    private TableColumn<table, String> date_ColumnTblDown;

    @FXML
    private TableColumn<table, Double> remSal_ColumnTblDown;

    Connection con;
    dbConnection dbc = new dbConnection();
    PreparedStatement pst;
    PreparedStatement pstt;
    PreparedStatement ps;
    

    public static int rowvalue = 0;
    public static int rowvalue2 = 0;
    public static String eid_static;
     
    Message mess = new Message();
    

    //array list data for upper table
    final ObservableList<table> data = FXCollections.observableArrayList();
    
    
    //array list data for lower table
    final ObservableList<table> data2 = FXCollections.observableArrayList();

    /*for inserting into the database */
    public void insertInto(ActionEvent event) {
        try {
            con = dbc.connection();
            boolean checker = false;

            int rowval = rowvalue;

            /*Storing values of the lables textfield and datepicker in variables*/
            String eid = label_Eid.getText();
            String name = label_Name.getText();//unused
            String post = label_Post.getText();//unused
            String monSalary = (label_Salary.getText()); //unused
            String preDueSal = (label_PreDueSalary.getText());//unused
            //System.out.println("outside if");
            Double paymentAmount = 0.0;
            Database db = new Database();
            LocalDate date = db.NOW_LOCAL_DATEcurrentDate();
            /*to check whether the textField is empty or not*/
            if(!(textField_PaymentAmount.getText().isEmpty())){
                //System.out.println("inside if");
                String paymentAmount_String = textField_PaymentAmount.getText();   
               // System.out.println("and after payment amount string");
                paymentAmount = Double.parseDouble(paymentAmount_String);
            }
            String paymentDate = String.valueOf(date);

            
            /*checking whether the upper table is clicked or not*/
            if (!(eid.equals("id of employee"))) {

                /*checking whether the textField value is 0(zero) or not*/
                if (paymentAmount != 0.0) {
                    Double remainingSalary = -paymentAmount;
                    /*selecting the date which is smaller then just smaller than the payment date*/
                    String sql6 = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + eid + "' AND `date` < '" + paymentDate + "' ORDER BY `date` DESC LIMIT 0,1";
                    pstt = (PreparedStatement) con.prepareStatement(sql6);
                    ResultSet rstt = pstt.executeQuery(sql6);
                    while (rstt.next()) {
                        remainingSalary = rstt.getDouble("remaining_salary");
                        remainingSalary = remainingSalary - paymentAmount;
                    }

                 //   System.out.println("update date " + paymentDate);

                    /*selecting the date and eid from which update or insert is done */

                    String sql = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + eid + "' AND `date` = '" + paymentDate + "' ORDER BY `date` DESC";
                    pst = (PreparedStatement) con.prepareStatement(sql);
                    ResultSet rs = pst.executeQuery(sql);
                    while (rs.next()) {

                        remainingSalary -= rs.getDouble("payment_amount");
                        checker = true;//checker used in checking whether update or inset is done 
                    }

                    /*if checker = false then insert is done else update is done*/
                    if (checker == false) {
                        /*creating object of Insert_data for inserting data*/
                        Insert_data db_insert = new Insert_data();

                        /*inserting into databse using db_insert object of Insert_data*/
                        db_insert.insert_for_click_submitButton(eid, paymentAmount, paymentDate, remainingSalary);
                    } else {
                        /*object creation of the update_data class for updating the requried data*/
                        Update_data db_update = new Update_data();
                        remainingSalary =   db_update.update_for_click_submitButton(eid, paymentAmount, paymentDate, remainingSalary);
                    }
                    
                    

                    /*creating object of the Update_data for updating the data after insertion or update of the data*/
                    Update_data db_update = new Update_data();
                      db_update.update_after_insertingDeleting(eid, paymentDate, remainingSalary);

                    label_PreDueSalary.setText(label_RemainingAmount.getText());
                    textField_PaymentAmount.setText("");
                    label_RemainingAmount.setText("remaining salary");

                    showUpperTable();
                    showLowerTable(eid);
                    reSelected(rowval);
                }
                
                else{
                    mess.alertMessage(Alert.AlertType.ERROR, "Payment Amount missing", "No value in payment Amount");
                    }

            }
            
            else{
                mess.alertMessage(Alert.AlertType.ERROR, "Didn't click on table", "Click on the UpperTable to get values");
                
            }
        } catch (Exception e) {
            System.out.println("on insertInto method of PayrollController.java ");
            e.printStackTrace();
        }
    }

    /*cancel reset all the field and tables view to default*/
    public void cancel_data(ActionEvent event) {
        showUpperTable();
        label_Eid.setText("id of employee");
        label_Name.setText("name of employee");
        label_Post.setText("post of employee");
        label_Salary.setText("salary of employee");
        label_PreDueSalary.setText("due amount salary");
        textField_PaymentAmount.setText("");
        Database for_date = new Database();
        label_RemainingAmount.setText("remaining salary");
        table_Down.getItems().clear();
    }

    
    /*for taking and checking the  date like for monthly salary , employee expense and current date*/
    

    /* for showing the value of selected row*/
    public void getSelected() {
        try {
            sn_up.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            eid_up.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            name_up.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            post_up.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            monSal_up.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            remSal_up.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
            lastPayDate_up.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));

            table_Up.setItems(data);

            table_Up.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

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


    /*For showing upper table */
    public void showUpperTable() {
        try {
            Integer sn_temp = 1;
            String sql = "SELECT * FROM `tbl_employee_details`";
            con = dbc.connection();
            pst = (PreparedStatement) con.prepareStatement(sql);

            sn_up.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            eid_up.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            name_up.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            post_up.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            monSal_up.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            remSal_up.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
            lastPayDate_up.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));

            table_Up.getItems().clear();

            ResultSet rs = pst.executeQuery(sql);
            while (rs.next()) {

                table as = new table();
                String lastPayment = "no date";
                boolean lastPaymentDate_flag = true;
                boolean remainingDate_flag = true;
                Double remainingSalary = 0.0;
                String eid = rs.getString("eid");
                //System.out.println("eid = " + eid);
                String sql2 = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + eid + "' ORDER BY `date` DESC";
                pstt = (PreparedStatement) con.prepareStatement(sql2);
                ResultSet rst = pstt.executeQuery(sql2);
                while (rst.next()) {
                    //System.out.println("in rst.next() of showupppertable");
                    String pa = rst.getString("payment_amount");
                    if ((pa != null) && (lastPaymentDate_flag == true)) {
                        //System.out.println("in if of pa and lastPayment of showupppertable");
                        lastPayment = rst.getString("date");
                        lastPaymentDate_flag = false;
                    }
                    if (remainingDate_flag == true) {
                       // System.out.println("in if of remainingDate of showupppertable");
                        remainingSalary = rst.getDouble("remaining_salary");
                        remainingDate_flag = false;
                    }
                    if (remainingDate_flag == true && lastPaymentDate_flag == true) {
                        break;
                    }

                }
                as.i1.set(sn_temp++);
                as.s1.set(rs.getString("eid"));
                as.s2.set(rs.getString("ename"));
                as.s3.set(rs.getString("post"));
                as.d1.set(rs.getDouble("monthly_salary"));
                as.d2.set(remainingSalary);
                as.s4.set(lastPayment);

                data.add(as);
            }
            table_Up.setItems(data);
        } catch (Exception e) {
            System.out.println("on showUpperTable methods of the PayController.java");
            e.printStackTrace();
        }
    }

    /*for showing values on the label by selecting on the uppper table*/
    public void use_upTable_for_selection() {
        try {
            getSelected();

            //String id = null;
            int new_row = rowvalue - 1;

            if(new_row>-1){
            String sql = "SELECT * FROM `tbl_employee_details` LIMIT " + new_row + ",1";

            con = dbc.connection();
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while (rs.next()) {
                //String lastPayment = "no date";
                //boolean lastPaymentDate_flag = true;
                // boolean remainingDate_flag = true;
                Double remainingSalary = 0.0;
                String eid = rs.getString("eid");
                //System.out.println("eid = " + eid);
                //System.out.println("on use table");
                String sql2 = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + eid + "' ORDER BY `date` DESC LIMIT 0,1";
                pstt = (PreparedStatement) con.prepareStatement(sql2);
                ResultSet rst = pstt.executeQuery(sql2);
                while (rst.next()) {
                    // System.out.println("in rst.next() of showupppertable");
                    // String pa = rst.getString("payment_amount");
                    /*
                     if ((pa != null) && (lastPaymentDate_flag == true)) {
                        System.out.println("in if of pa and lastPayment of showupppertable");
                        lastPayment = rst.getString("date");
                        lastPaymentDate_flag = false;
                    }*/

                    //if (remainingDate_flag == true) {
                    //System.out.println("in if of remainingDate of showupppertable");
                    remainingSalary = rst.getDouble("remaining_salary");
                    //  remainingDate_flag = false;
                    //  }

                }
                label_Eid.setText(rs.getString("eid"));
                label_Name.setText(rs.getString("ename"));
                label_Post.setText(rs.getString("post"));
                label_Salary.setText(String.valueOf(rs.getDouble("monthly_salary")));
                label_PreDueSalary.setText(String.valueOf(remainingSalary));
                textField_PaymentAmount.setText("");
                label_RemainingAmount.setText("remaining salary");

                remainingAmountValue();
                showLowerTable(eid);
                eid_static = rs.getString("eid");
            }
            }
        } catch (Exception e) {
            System.out.println("on use_upTable_for_selection of PayrollController.java");
            e.printStackTrace();
        }
    }

    
    /*for showing the lower table*/
    public void showLowerTable(String id) {
        try {
            con = dbc.connection();

            int temp_sn = 1;
            String from_date = String.valueOf(date_from.getValue());
            String to_date = String.valueOf(date_to.getValue());
            String sql = "SELECT * FROM `tbl_payroll` WHERE `eid`= '" + id + "' AND `date` >= '" + from_date + "' And `date` <= '" + to_date + "' ORDER BY `date` DESC";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            sn_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            eid_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            monthlySalary_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            empExp_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            paidAmo_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
            date_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, String>("s5"));
            remSal_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            table_Down.getItems().clear();

            while (rs.next()) {
                table payroll = new table();

                payroll.i1.set(temp_sn++);
                payroll.s1.set(rs.getString("eid"));

                if (rs.getString("monthly_salary_paid") == null || (Double.parseDouble(rs.getString("monthly_salary_paid"))) == 0) {
                    payroll.s2.set("-");
                } else {
                    payroll.s2.set(rs.getString("monthly_salary_paid"));
                }

                if (rs.getString("employee_expenses") == null || (Double.parseDouble(rs.getString("employee_expenses"))) == 0) {
                    payroll.s3.set("-");
                } else {
                    payroll.s3.set(rs.getString("employee_expenses"));
                }

                if (rs.getString("payment_amount") == null || (Double.parseDouble(rs.getString("payment_amount"))) == 0) {
                    payroll.s4.set("-");
                } else {
                    payroll.s4.set(rs.getString("payment_amount"));
                }

                payroll.s5.set(rs.getString("date"));
                payroll.d1.set(rs.getDouble("remaining_salary"));

                data2.add(payroll);
            }
            table_Down.setItems(data2);

        } catch (Exception e) {
            System.out.println("on showLowerTable of PayrollController.java");
            e.printStackTrace();
            
        }

    }

    
    /*for initial date for date pickers*/
    public void initial_date() {
        Database for_date = new Database();
        date_from.setValue(for_date.NOW_LOCAL_DATEsalaryPaymentDate());
        date_to.setValue(for_date.NOW_LOCAL_DATEcurrentDate());
    }

    /*for making the remaining salary*/
    public void remainingAmountValue() {
        try {
            //Double salary  = Double.parseDouble(label_Salary.getText());
            
            Double preDue = Double.parseDouble(label_PreDueSalary.getText());
            Double salPaid = Double.parseDouble(textField_PaymentAmount.getText());
            Double remainSal = preDue - salPaid;
            label_RemainingAmount.setText(String.valueOf(remainSal));
        } catch (Exception e) {
            //System.out.println("on remainingAccountValue method of PayrollController");
            //e.printStackTrace();

        }
    }

    /*for selection lower table*/
    public void getSelected_for_LowerTbl() {
        try {
            sn_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            eid_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            monthlySalary_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            empExp_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            paidAmo_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
            date_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, String>("s5"));
            remSal_ColumnTblDown.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));

            table_Down.setItems(data2);

            table_Down.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                    rowvalue2 = (data2.indexOf(newValue) + 1);
                    //System.out.println("ON RowValue for tbl 2 is : " + rowvalue2);
                }

            });
        } catch (Exception e) {
            System.out.println("on getSelected_for_LowerTbl method of payrollController.java");
            e.printStackTrace();
        }
    }

    /*for deleting the data selected using delete button*/
    public void delete_from_LowerTable(ActionEvent event) {
        getSelected_for_LowerTbl();
        if (rowvalue2 > 0) {
            remove();

            // clear_flag = false;
        } else {
            mess.alertMessage(Alert.AlertType.ERROR, "ERROR", "Nothing is selected");
        }

    }

    /*for removing which is called by delete_from_LowerTable*/
    public void remove() {
       boolean ans = mess.alertOptionMessage("DELETE", "Are you sure ??");
        if (ans) {
            rowvalue2--;
            String temp_id = null;
            if (rowvalue2 > -1) {
                try {
                    //System.out.println("rowvalue dsaa " + rowvalue2);
                    String from_date = String.valueOf(date_from.getValue());
                    String to_date = String.valueOf(date_to.getValue());
                    //System.out.println("rowvalue dsaa " + rowvalue2);
                    String sql = "SELECT * FROM `tbl_payroll` WHERE `eid`=" + eid_static + " AND `date`>= '" + from_date + "' AND `date`<= '" + to_date + "' ORDER BY `date` DESC LIMIT " + rowvalue2 + ",1";
                    String date = null;
                    int temp = 0;
                    Double salaryAmount = 0.0;
                    Double employeeExpenes = 0.0;
                    Double remainingSalary = 0.0;
                    Double paymentAmount = 0.0;

                    con = dbc.connection();
                    pst = (PreparedStatement) con.prepareStatement(sql);
                    ResultSet rs = pst.executeQuery(sql);
                    while (rs.next()) {
                        //System.out.println("inside rs.next of remove method");
                        temp = (rs.getInt("sn"));
                        temp_id = (rs.getString("eid"));
                        date = (rs.getString("date"));
                        salaryAmount = rs.getDouble("monthly_salary_paid");
                        paymentAmount = rs.getDouble("payment_amount");
                        employeeExpenes = rs.getDouble("employee_expenses");
                    }

                    String sql6 = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + temp_id + "' AND `date` < '" + date + "' ORDER BY `date` DESC LIMIT 0,1";
                    pstt = (PreparedStatement) con.prepareStatement(sql6);
                    ResultSet rstt = pstt.executeQuery(sql6);
                    while (rstt.next()) {
                        
                        remainingSalary = rstt.getDouble("remaining_salary");
                    }

                    //System.out.println("date on remove = " + date);
                    String sql2 = "DELETE FROM `tbl_payroll` WHERE `eid` = '" + temp_id + "' AND `date` = '" + date + "'";
                    
                    ps = (PreparedStatement) con.prepareStatement(sql2);
                    int i = ps.executeUpdate(sql2);
                    sqlForCashinhand cash = new sqlForCashinhand();
                    
                    cash.deleteInCashInhand(date, "Employee id: "+temp_id, paymentAmount, null);

                    int rowval = rowvalue;

                    //for current date from the system
                    Database for_date = new Database();
                    String salaryDate = String.valueOf(for_date.NOW_LOCAL_DATEsalaryPaymentDate());
                    String expenseDate = String.valueOf(for_date.NOW_LOCAL_DATEexpenseAddingDate());
                    if (date.equals(salaryDate)) {
                        Double remainingAmount = 0.0;

                        /*after deleting if date = Monthly salary date then automatically  inserted or updated*/
                        String sql1 = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + temp_id + "' AND `date` < '" + date + "' ORDER BY `date` DESC LIMIT 0,1";
                        pstt = (PreparedStatement) con.prepareStatement(sql1);
                        ResultSet rst = pstt.executeQuery(sql1);
                        while (rst.next()) {
                            remainingAmount = rst.getDouble("remaining_salary");
                            //System.out.println("Payment amount = " + remainingAmount);
                        }
                        remainingAmount = remainingAmount + salaryAmount;
                        remainingSalary = remainingAmount;

                        boolean bool_check = false;
                        String sql_check = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + temp_id + "' AND `date` = '" + salaryDate + "'";
                        pstt = (PreparedStatement) con.prepareCall(sql_check);
                        rs = pstt.executeQuery(sql_check);
                        while (rs.next()) {
                            bool_check = true;
                        }
                        /*if bool_check value is true then there is data in the temp_id and salary so instead of inserting it updates the value*/
                        if (bool_check == false) {
                            Insert_data insertData = new Insert_data();
                            insertData.insert_for_monthlySalary(temp_id, salaryAmount, date, remainingAmount);
                        } else {
                            Update_data updateData = new Update_data();
                            updateData.update_for_monthlySalary(temp_id, date, salaryAmount);
                        }
                    }

                    /*after deleting the data the deleted data can be of monthly expense adding day so it is ineerted automatically*/
                    if (date.equals(expenseDate)) {
                        Double remainingAmount = 0.0;

                        String sql1 = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + temp_id + "' AND `date` < '" + date + "'  ORDER BY `date` DESC LIMIT 0,1";
                        pstt = (PreparedStatement) con.prepareStatement(sql1);
                        ResultSet rst = pstt.executeQuery(sql1);
                        while (rst.next()) {
                            remainingAmount = rst.getDouble("remaining_salary");
                           // System.out.println("Payment amount = " + remainingAmount);
                        }

                        remainingAmount = remainingAmount + employeeExpenes;

                        boolean bool_check = false;
                        String sql_check = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + temp_id + "' AND `date` = '" + salaryDate + "'";
                        pstt = (PreparedStatement) con.prepareCall(sql_check);
                        rs = pstt.executeQuery(sql_check);
                        while (rs.next()) {
                            bool_check = true;
                        }
                        /* if bool_check is true then there must be data in monthly expenses adding date so it must be updated rather than inserting*/
                        if (bool_check == false) {
                            Insert_data insertData = new Insert_data();
                            insertData.insert_for_employeeExpenes(temp_id, employeeExpenes, date, remainingAmount);
                        } else {
                            Update_data updateData = new Update_data();
                            updateData.update_for_employeeExpenes(temp_id, date, employeeExpenes);
                        }

                    }

                    /*deleting/inserting/updating the data can past data must effect future data to make balance so for that updating the future data which are already inserted*/
                    Update_data update_data = new Update_data();
                    update_data.update_after_insertingDeleting(temp_id, date, remainingSalary);

                    //showLowerTable(temp_id);
                    use_upTable_for_selection();

                    showLowerTable(temp_id);
                    showUpperTable();
                    reSelected(rowval);
                } catch (Exception e) {
                    System.out.println("on remove method of PayrollController.java");
                    e.printStackTrace();
                }
            }

        }
    }

    /*refocusing on the selected item on upper table*/
    public void reSelected(int rowval) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int row = rowval - 1;
                table_Up.requestFocus();
                table_Up.getSelectionModel().select(row);
                table_Up.getFocusModel().focus(row);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initial_date();
        showUpperTable();
        getSelected();
        getSelected_for_LowerTbl();
    }

}
