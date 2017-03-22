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


/**
 *
 * @author Soman
 */
public class Update_data {

    Connection con;
    PreparedStatement pst;
    PreparedStatement ps;
    dbConnection dbc = new dbConnection();

    /*used when the program submit button is used*/
    public Double update_for_click_submitButton(String eid, Double paidAmount, String date, Double remainingSalary) {
        try {
            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + eid + "' AND `date` = '" + date + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while (rs.next()){
                Double salaryAmount = rs.getDouble("monthly_salary_paid");
                Double expense = rs.getDouble("employee_expenses");
               Double prePaidAmount = rs.getDouble("payment_amount");
               Double totalPaidAmount = prePaidAmount + paidAmount;
               if(salaryAmount == null)
                   salaryAmount = 0.0;
               if(expense == null)
                   expense = 0.0;
               
               
               remainingSalary = remainingSalary + salaryAmount - expense;
               String sql2 = "UPDATE `tbl_payroll` SET `payment_amount`= '"+totalPaidAmount+"', `remaining_salary`=  '"+remainingSalary+"' WHERE `eid` = '" + eid + "' AND `date` = '" + date + "'";
               
               ps = (PreparedStatement) con.prepareStatement(sql2);
               int i = ps.executeUpdate(sql2);
               sqlForCashinhand cash = new sqlForCashinhand();
               cash.updateInCashInhand(date, "Employee id: "+eid, prePaidAmount, null, date, "Employee id: "+eid, totalPaidAmount, null);
            }
            return remainingSalary;
        } catch (Exception e) {
            System.out.println("on update_for_click_submitButton of Update_data.java");
            e.printStackTrace();
        }
        return remainingSalary;

    }

    /*update on one day of the month in which salary is given*/
    public void update_for_monthlySalary(String eid ,String date, Double monthlySalary) {
        try {
            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + eid + "' AND `date` = '" + date + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while(rs.next()){
                Double remainingSalary = rs.getDouble("remaining_salary");
                if(remainingSalary == null)
                    remainingSalary = 0.0;
                Double totalRemainingSalary = remainingSalary + monthlySalary;
                   String sql2 = "UPDATE `tbl_payroll` SET `monthly_salary_paid`= '"+monthlySalary+"', `remaining_salary`=  '"+totalRemainingSalary+"' WHERE `eid` = '" + eid + "' AND `date` = '" + date + "'";
               ps = (PreparedStatement) con.prepareStatement(sql2);
               int i = ps.executeUpdate(sql2);
            }
            
        } catch (Exception e) {
            System.out.println("on update_for_monthlySalary of Update_data.java");
            e.printStackTrace();
        }

    }

    /*update on one day of which employee expenes is added in the database*/
    public void update_for_employeeExpenes(String eid, String date , Double employeeExpenes) {
        try {
            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_payroll` WHERE `eid` = '" + eid + "' AND `date` = '" + date + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while(rs.next()){
                Double remainingSalary = rs.getDouble("remaining_salary");
                if(remainingSalary == null)
                    remainingSalary = 0.0;
                Double totalRemainingSalary = remainingSalary - employeeExpenes;
                   String sql2 = "UPDATE `tbl_payroll` SET `employee_expenses`= '"+employeeExpenes+"', `remaining_salary`=  '"+totalRemainingSalary+"' WHERE `eid` = '" + eid + "' AND `date` = '" + date + "'";
               ps = (PreparedStatement) con.prepareStatement(sql2);
               int i = ps.executeUpdate(sql2);
            }
        } catch (Exception e) {
            System.out.println("on update_for_employeeExpenes of Update_data.java");
            e.printStackTrace();
        }

    }

    /*update may be requried after inserting or deleting the data so for that using this function*/
    public void update_after_insertingDeleting(String eid, String date, Double remainingSalary){
        try {
            con = dbc.connection();
            
            String sql2 = "SELECT * FROM `tbl_payroll` WHERE `eid` = '"+eid+"' AND `date`>'"+date+"' ORDER BY `date` ASC";
                    ps = (PreparedStatement) con.prepareStatement(sql2);
            ResultSet rs = ps.executeQuery(sql2);

            while(rs.next()){
                Double monSalary = rs.getDouble("monthly_salary_paid");
                Double expenAmount  = rs.getDouble("employee_expenses");
                Double paidAmount = rs.getDouble("payment_amount");
                String date2 = rs.getString("date");
                
                if (monSalary==null){
                    monSalary=0.0;
                }
                if(expenAmount==null){
                    expenAmount =0.0;
                }
                if(paidAmount==null){
                    paidAmount = 0.0;
                }
                
                remainingSalary = monSalary - expenAmount - paidAmount +remainingSalary;
                
            String sql = "UPDATE `tbl_payroll` SET `remaining_salary`=  '"+remainingSalary+"' WHERE `eid` = '" + eid + "' AND `date` = '" + date2 + "'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            int i = pst.executeUpdate(sql);
            }
        } catch (Exception e) {
            System.out.println("on update_after_insertingDeleting of Update_data.java");       
        }
    }
    
    
    
}
