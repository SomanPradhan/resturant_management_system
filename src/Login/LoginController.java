/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import Payroll.Insert_data;
import Payroll.Update_data;
import UsableMethods.Database;
import UsableMethods.Message;
import UsableMethods.dbConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Rubesh
 */
public class LoginController implements Initializable {

    public static boolean checkerForAdmin = true;
    public static boolean checkerForUser = true;
    @FXML
    private JFXButton btn_SignIn;
    @FXML
    private JFXTextField textField_Username;
    @FXML
    private JFXPasswordField textField_Password;
    @FXML
    private ImageView imageView_Logo;
    @FXML
    private Label label_name;

    Connection con = null;

    PreparedStatement pst = null;
    PreparedStatement ps = null;
    dbConnection dbc = new dbConnection();
    Message m = new Message();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        this.imageView_Logo = new ImageView(new Image(getClass().getResourceAsStream("/src/Images/logo_cup.jpg")));
        //SET LABEL-NAME
        checkingDate();
        forMonthlyReport();
        getLabel();
    }

    /*
    * SET THE NAME LABEL
     */
    private void getLabel() {
        try {

            con = dbc.connection();
            String sql = "SELECT `name` FROM `tbl_resturant_info` ";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                String name = rs.getString("name");
                label_name.setText(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * METHOD FOR SIGN-IN BUTTON
     */
    public void signIn(ActionEvent event) throws Exception {
        String un = textField_Username.getText();
        String pwd = textField_Password.getText();
        try {

            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_user` where `username` ='" + un + "' ";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                String username = rs.getString("username");
                String password = rs.getString("password");
                String privilege = rs.getString("privileges");

                if (username.equals(un)) {
                    if (password.equals(pwd)) {

                        if (privilege.equalsIgnoreCase("Admin")) {
                            try {

                                //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
                                if (checkerForAdmin == true) {
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Index/Index.fxml"));
                                    Parent root = (Parent) fxmlLoader.load();
                                    Stage stage = new Stage();
                                    stage.setTitle("The Chef's Cafe");
                                    stage.getIcons().add(new Image("/Images/logo_cup.png"));
                                    stage.setScene(new Scene(root));
                                    checkerForAdmin = false;
                                    Stage stage1 = (Stage) btn_SignIn.getScene().getWindow();
                                    stage1.close();
                                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                        public void handle(WindowEvent we) {
                                            checkerForAdmin = true;
                                        }
                                    });
                                    
                                    stage.show();
                                    
                                } else {
                                    Message msg = new Message();
                                    msg.alertMessage(AlertType.ERROR, "ERROR", "Already Opened Admin panel");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (privilege.equalsIgnoreCase("User")) {
                            try {
                                //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
                                if (checkerForUser == true) {
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Billing/FXMLDocument.fxml"));
                                    Parent root = (Parent) fxmlLoader.load();
                                    Stage stage = new Stage();
                                    stage.getIcons().add(new Image("/Images/logo_cup.png"));
                                    stage.setTitle("The Chef's Cafe");
                                    stage.setScene(new Scene(root));
                                    stage.setResizable(false);
                                    stage.sizeToScene();
                                    checkerForUser = false;
                                    Stage stage1 = (Stage) btn_SignIn.getScene().getWindow();
                                    stage1.close();
                                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                        public void handle(WindowEvent we) {
                                            checkerForUser = true;
                                        }
                                    });
                                    stage.show();
                                } else {
                                    Message msg = new Message();
                                    msg.alertMessage(AlertType.ERROR, "ERROR", "Already Opened User panel");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } else {
                        m.alertMessage(AlertType.ERROR, "Incorrect Password.", "Please check your password and retry again.");
                    }
                } else {
                    m.alertMessage(AlertType.ERROR, "Invalid Username and Password.", "Please retry again.");
                }
            } else {
                m.alertMessage(AlertType.ERROR, "Invalid Username and Password.", "Please retry again.");
            }

        } catch (Exception e) {
            m.alertMessage(AlertType.ERROR, "Connection failed.", "Make sure the connection is established.");
            e.printStackTrace();
        }

        /*
            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Billing/FXMLDocument.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            final Stage stage = new Stage();
            stage.setTitle("The Chef's Cafe");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();

            Stage stage1 = (Stage) btn_SignIn.getScene().getWindow();
            stage1.close();

        }catch (Exception e) {
                e.printStackTrace();
            }
    }

    else if (un.equals ( 
        "Admin") && pass.equals("admin")) {
            try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Index/Index.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("The Chef's Cafe");
            stage.setScene(new Scene(root));
            stage.show();

            Stage stage1 = (Stage) btn_SignIn.getScene().getWindow();
            stage1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    
        else {
            EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, "Username/Password incorrect.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
//            JOptionPane.showMessageDialog(null, "Username/Password incorrect.");

        });

 
}
         */
    }

    public void checkForMonthlyStatement() {
        try {
            Database db = new Database();
            java.sql.PreparedStatement pst = null;
            java.sql.Connection con = null;
            ResultSet rs = null;
            Boolean checker = true;
            LocalDate currentDate = db.NOW_LOCAL_DATEcurrentDate();
            LocalDate monthFirstDay = db.NOW_LOCAL_DATEfirstDateOfMonth();
            int currentDay = currentDate.getDayOfMonth();
            int currentMonth = currentDate.getMonthValue();
            String sql = "SELECT * FROM `tbl_monthly_report` WHERE `month` >= '" + monthFirstDay + "' AND `month` <= '" + currentDate + "'";
            pst = (java.sql.PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while (rs.next()) {
                checker = false;
                break;
            }
            if (checker == true) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkingDate() {

        Database forDate = new Database();
        LocalDate currentDate = forDate.NOW_LOCAL_DATEcurrentDate();
        LocalDate expensDate = forDate.NOW_LOCAL_DATEexpenseAddingDate();
        LocalDate salaryDate = forDate.NOW_LOCAL_DATEsalaryPaymentDate();

        check_for_monthlySalary(currentDate, salaryDate);
        check_for_employeeExpenes(currentDate, expensDate);

    }

    public void check_for_monthlySalary(LocalDate currentDate, LocalDate salaryDate) {

        con = dbc.connection();
        int currentDay = currentDate.getDayOfMonth();
        //System.out.println("day of the month"+ currentDay);
        int salaryDay = salaryDate.getDayOfMonth();
        //System.out.println("Day of the month"+ salaryDay);
        String salarydate = String.valueOf(salaryDate);
        int currentMonth = currentDate.getMonthValue();
        int salaryMonth = salaryDate.getDayOfMonth();
        if (currentDay >= salaryDay && currentMonth >= salaryMonth) {
            try {
                String sql = "SELECT * FROM `tbl_employee_details`";
                pst = (PreparedStatement) con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery(sql);
                while (rs.next()) {
                    Double remainingAmount = 0.0;
                    boolean checkbool = false;
                    String checker = null;
                    String eid = rs.getString("eid");
                    Double salaryAmount = rs.getDouble("monthly_salary");
                    String sql1 = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + eid + "' GROUP BY `eid` DESC ORDER BY `date` LIMIT 0,1";
                    ps = (PreparedStatement) con.prepareStatement(sql1);
                    ResultSet rst = ps.executeQuery(sql1);
                    while (rst.next()) {
                        remainingAmount = rst.getDouble("remaining_salary");
                        //System.out.println("Payment amount = "+remainingAmount);
                    }
                    String sql2 = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + eid + "' AND `date` = '" + salarydate + "'";
                    ps = (PreparedStatement) con.prepareStatement(sql2);
                    ResultSet rsl = ps.executeQuery(sql2);
                    Double remainingSalary = remainingAmount + salaryAmount;
                    while (rsl.next()) {
                        checkbool = true;
                        if (rsl.getString("monthly_salary_paid") != null) {
                            checker = "there is data";
                        }
                    }
                    if (checker == null && checkbool == false) {
                        /*inserting the salary if not inserted*/

                        Insert_data insertData = new Insert_data();
                        insertData.insert_for_monthlySalary(eid, salaryAmount, salarydate, remainingSalary);

                    }
                    if (checker == null && checkbool == true) {
                        Update_data updateData = new Update_data();
                        updateData.update_for_monthlySalary(eid, salarydate, salaryAmount);
                    }
                }
            } catch (Exception e) {
                System.out.println(" on check_for_monthlySalary of for_date.java");
                e.printStackTrace();
            }

        }
    }

    /*checking whether the employee expenses is inserted in the database or not*/
    public void check_for_employeeExpenes(LocalDate currentDate, LocalDate expenseDate) {
        con = dbc.connection();
        int currentDay = currentDate.getDayOfMonth();
        //System.out.println("day of the month"+ currentDay);
        int expenseDay = expenseDate.getDayOfMonth();
        //System.out.println("Day of the month"+ expenseDay);
        String expensedate = String.valueOf(expenseDate);
        int currentMonth = currentDate.getMonthValue();
        int expenseMonth = expenseDate.getMonthValue();
        if (currentDay >= expenseDay && currentMonth >= expenseMonth) {
            try {
                String sql = "SELECT * FROM `tbl_employee_expenses`";
                pst = (PreparedStatement) con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery(sql);
                while (rs.next()) {

                    Double remainingAmount = 0.0;
                    String checker = null;
                    boolean checkbool = false;
                    String eid = rs.getString("eid");
                    String nameForSale = "Employees id: " + eid;
                    Double expenseAmount = rs.getDouble("total_amount");
                    String sql1 = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + eid + "' ORDER BY `date` DESC LIMIT 0,1";
                    ps = (PreparedStatement) con.prepareStatement(sql1);
                    ResultSet rst = ps.executeQuery(sql1);
                    //System.out.println("sql1 is not exception");
                    while (rst.next()) {
                        remainingAmount = rst.getDouble("remaining_salary");
                        //System.out.println("Payment amount = "+remainingAmount);
                    }
                    String sql2 = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + eid + "' AND `date` = '" + expensedate + "'";
                    ps = (PreparedStatement) con.prepareStatement(sql2);
                    ResultSet rsl = ps.executeQuery(sql2);
                    Double remainingSalary = remainingAmount - expenseAmount;
                    while (rsl.next()) {
                        checkbool = true;
                        if (rsl.getString("employee_expenses") != null) {
                            checker = "there is data";
                        }
                    }
                    if (checker == null && checkbool == false) {
                        /*inserting the data if not inserted*/
                        Insert_data insertData = new Insert_data();
                        insertData.insert_for_employeeExpenes(eid, expenseAmount, expensedate, remainingSalary);

                    }
                    if (checker == null && checkbool == true) {
                        Update_data updateData = new Update_data();
                        updateData.update_for_employeeExpenes(eid, expensedate, expenseAmount);
                    }
                    boolean checkForSale = false;
                    forSales(checkForSale, nameForSale, currentDate, expenseDate, expenseAmount);

                }
            } catch (Exception e) {
                System.out.println(" on check_for_employeeExpense of for_date.java");
                e.printStackTrace();
            }

        }
    }

    public void forSales(boolean checkForSale, String nameForSale, LocalDate currentDate, LocalDate expenseDate, Double expenseAmount) {
        try {
            String sql99 = "SELECT * FROM `tbl_sales` WHERE `name` = '" + nameForSale + "'  AND `date`<= '" + currentDate + "' AND `date`>= '" + expenseDate + "'";
            //String sql99 ="SELECT * FROM `tbl_sales` WHERE `name`= '" + nameForSale + "'";
            pst = (PreparedStatement) con.prepareStatement(sql99);
            ResultSet rst1 = pst.executeQuery(sql99);

            while (rst1.next()) {
                checkForSale = true;

            }

            if (checkForSale == false) {
                int billno = 1;
                String sql = "select `bill_id` from tbl_sales order by `bill_id` desc LIMIT 1";
                ResultSet rs = con.createStatement().executeQuery(sql);
                while (rs.next()) {
                    billno = rs.getInt("bill_id");
                    billno++;
                }
                Database db = new Database();
                ArrayList value = new ArrayList();
                value.add(billno++);
                value.add(nameForSale);
                value.add(currentDate);
                value.add(expenseAmount);
                value.add(0);
                value.add(0);
                value.add(0);
                value.add(expenseAmount);
                value.add(0);
                db.insertData("tbl_sales", value);
            }
        } catch (Exception e) {

        }
    }

    public void forMonthlyReport() {
        try {
            con = dbc.connection();
            Database db = new Database();
            LocalDate firstDate = db.NOW_LOCAL_DATEfirstDateOfMonth();
            String from_date = String.valueOf(firstDate);
            LocalDate currentDate = db.NOW_LOCAL_DATEcurrentDate();
            String to_date = String.valueOf(currentDate);
            boolean checker = true;

            Double sales = 0.0;
            Double purchase = 0.0;
            Double salary = 0.0;
            Double saving = 0.0;
            Double grExpense = 0.0;
            Double adExpense = 0.0;
            Double wastage = 0.0;
            Double newAssets = 0.0;
            String sql = "SELECT * FROM `tbl_monthly_report` WHERE `month` >= '" + from_date + "' And `month` <= '" + to_date + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs1 = pst.executeQuery(sql);
            System.out.println("AAA");

            while (rs1.next()) {
                checker = false;
                System.out.println(rs1.getString("month"));
            }

            int currentmonth = currentDate.getMonthValue();
            int currentyear = currentDate.getYear();
            int lastmonth;
            int year = 0;
            if (currentmonth > 1) {
                lastmonth = currentmonth - 1;
                year = currentyear;
            } else {
                lastmonth = 12;
                year = currentyear - 1;
            }
            String lastMonDate = year + "-" + lastmonth + "-" + 01;
            String currentMonDate = String.valueOf(firstDate);
            if (checker) {
                sql = "SELECT * FROM `tbl_sales` WHERE `date` >= '" + lastMonDate + "' AND `date` < '" + currentMonDate + "'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery(sql);
                while (rs.next()) {
                    sales = sales + rs.getDouble("grand_total_amount");
                }

                sql = "SELECT * FROM `tbl_purchase` WHERE `date` >= '" + lastMonDate + "' AND `date` < '" + currentMonDate + "'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while (rs.next()) {
                    purchase = purchase + rs.getDouble("payment");
                }

                sql = "SELECT * FROM `tbl_payroll` WHERE `date` >= '" + lastMonDate + "' AND `date` < '" + currentMonDate + "'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while (rs.next()) {
                    salary = salary + rs.getDouble("payment_amount");
                }

                sql = "SELECT * FROM `tbl_saving` WHERE `date` >= '" + lastMonDate + "' AND `date` < '" + currentMonDate + "'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while (rs.next()) {
                    saving = saving + rs.getDouble("total");
                }

                sql = "SELECT * FROM `tbl_wastage` WHERE `date` >= '" + lastMonDate + "' AND `date` < '" + currentMonDate + "'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while (rs.next()) {
                    wastage = wastage + rs.getDouble("total_amount");
                }

                sql = "SELECT * FROM `tbl_subassets_details` WHERE `date` >= '" + lastMonDate + "' AND `date` < '" + currentMonDate + "'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while (rs.next()) {
                    newAssets = newAssets + rs.getDouble("total_amount");
                }

                sql = "SELECT * FROM `tbl_misc` WHERE `date` >= '" + lastMonDate + "' AND `date` < '" + currentMonDate + "' AND `expenses_type` = 'Miscelloneous Expenses'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while (rs.next()) {
                    grExpense = grExpense + rs.getDouble("total_amount");
                }

                sql = "SELECT * FROM `tbl_misc` WHERE `date` >= '" + lastMonDate + "' AND `date` < '" + currentMonDate + "' AND `expenses_type` = 'Administrative Expenses'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while (rs.next()) {
                    adExpense = adExpense + rs.getDouble("total_amount");
                }

                sql = "INSERT INTO `tbl_monthly_report`(`month`, `sales`, `purchase`, `salary`, `saving`, `general_expenses`, `adminstrative_expenses`, `wastage`, `new_assets`) VALUES ('" + from_date + "', '" + sales + "', '" + purchase + "', '" + salary + "', '" + saving + "', '" + grExpense + "', '" + adExpense + "', '" + wastage + "', '" + newAssets + "')";
                pst = (PreparedStatement) con.prepareStatement(sql);
                pst.executeUpdate();

            }
        } catch (Exception e) {
            System.out.println("Exception in forMonthlyReport function of LoginController");
            e.printStackTrace();
        }
    }

}
