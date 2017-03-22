  /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Payroll;
import UsableMethods.*;
//import DatabaseConnection.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author Soman
 */
public class Insert_data {

    Connection con=null;
    PreparedStatement pst=null;
    dbConnection dbc = new dbConnection();
    PreparedStatement ps = null;

    /*used when the program submit button is used*/
    public void insert_for_click_submitButton(String eid, Double paymentAmount, String paymentDate, Double remainingSalary) {
        try {
            con = dbc.connection();
            String sql = "INSERT INTO `tbl_payroll`(`eid`, `payment_amount`, `date`, `remaining_salary`) VALUES(" + eid + ", " + paymentAmount + ", '" + paymentDate + "', " + remainingSalary + ")";
            pst = (PreparedStatement) con.prepareStatement(sql);
            int i = pst.executeUpdate(sql);
            sqlForCashinhand cash = new sqlForCashinhand();
            cash.insertInCashInhand(paymentDate, "Employee id: "+eid,paymentAmount, null);
        } catch (SQLException e) {
            System.out.println("on insert_data_for_submitbutton method of Insert_data");
          //  e.printStackTrace();
        }

    }

    /*insert on one day of the month in which salary is given*/
    public void insert_for_monthlySalary(String eid, Double monthlySalary, String date, Double remainingSalary) {
            //System.out.println("outside try");
        try {
            con = (Connection) dbc.connection();
            //System.out.println("in side try");
            String sql = "INSERT INTO `tbl_payroll`(`eid`, `monthly_salary_paid`,`date`, `remaining_salary`) VALUES('"+eid+"', '"+monthlySalary+"', '" +date+ "', '"+remainingSalary+"')";
            pst = (PreparedStatement) con.prepareStatement(sql);
            int i = pst.executeUpdate(sql);
           
        } catch (Exception e) {
           System.out.println("on insert_for_monthlySalary method of Insert_data.java");
           e.printStackTrace();
        }
    }

    /*insert on one day of which employee expenes is added in the database*/
    public void insert_for_employeeExpenes(String eid, Double employeeExpenes, String date, Double remainingSalary) {
        try {
            con = dbc.connection();
            String sql ="INSERT INTO `tbl_payroll`(`eid`, `employee_expenses`, `date`, `remaining_salary`) VALUES('"+eid+"' ,'"+employeeExpenes+"' ,'"+date+"' ,'"+remainingSalary+"')";
            pst = (PreparedStatement) con.prepareStatement(sql);       
            int i = pst.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("on insert_for_employeeExpenes  method of Insert_data.java");
            e.printStackTrace();
        }
    }
    
   

}
