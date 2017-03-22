/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EmployeeAddEdit;

import UsableMethods.dbConnection;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class Employee_Database {
    private final String USER = "root";
    private final String PASSWORD = "";
     Connection con = null;
    PreparedStatement pst = null;
    PreparedStatement ps=null;
    dbConnection dbc = new dbConnection();
    
    
    
    
    public void insertData(String id,String name,String post,String address,String contact,String joined_date,String salary){
        try{
            String temp_id=null;
           con = dbc.connection();
           String sql ="SELECT * FROM tbl_employee_details";
             ps = (PreparedStatement) con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery(sql);
             while(rs.next()){
                 temp_id = rs.getString("eid");
                 System.out.println("temp_id value in database is "+temp_id);
                 if(temp_id.equals(id))
                     break;
             }
             if(!temp_id.equals(id)){
             pst = (PreparedStatement) con.prepareStatement("insert into tbl_employee_details values(?,?,?,?,?,?,?)");
            System.out.println("PreparedStatement created Successfully");

            
           pst.setString(1, id);
           pst.setString(2, name);
           pst.setString(3, post);
           pst.setString(4, address);
           pst.setString(5, contact);
           pst.setString(6, joined_date);
           pst.setString(7, salary);
            System.out.println("Executing Update to database");
            int i = pst.executeUpdate();
            System.out.println("Database Updated Successfully");

            if (i > 0) {
                ShowInfoMessage("Data saved successfully.", "Saved");
//                
            } else {
                ShowErrorMessage("data cannot be saved", "ERROR");

            }
        
             }
             else
                 ShowErrorMessage("Dublicate id can not saved data","ERROR");
            

        }           
            catch(Exception e){
            
        }
    }
    
   public void updateData(String id,String name,String post,String address,String contact,String joined_date,String salary,String staticId){
        try{
           con = dbc.connection();
           System.out.println("database staticId ="+staticId);
           String sql = "DELETE FROM tbl_employee_details WHERE eid = '" +staticId+"'";
            ps = (PreparedStatement) con.prepareStatement(sql);

            int j = ps.executeUpdate();
           
           
           
           
             pst = (PreparedStatement) con.prepareStatement("insert into tbl_employee_details values(?,?,?,?,?,?,?)");
            System.out.println("PreparedStatement created Successfully");

            
            pst.setString(1, id);
            pst.setString(2, name);
            pst.setString(3, post);
            pst.setString(4, address);
            pst.setString(5, contact);
            pst.setString(6, joined_date);
            pst.setString(7, salary);

            System.out.println("Executing Update to database");
            int i = pst.executeUpdate();
            System.out.println("Database Updated Successfully");

            if (i > 0) {
                ShowInfoMessage("Data saved successfully.", "Saved");
//                JOptionPane.showMessageDialog(null, "Data saved", "Saved", JOptionPane.INFORMATION_MESSAGE);
            } else {
                ShowErrorMessage("Cannot save data.", "ERROR");

            }
            
            }
            
            catch(Exception e){
            
        }

    }
    void deletedata(String id){
        
    }
    String getSeleted(){
        return"";
    }
    
   public void ShowErrorMessage(String message, String title) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void ShowInfoMessage(String message, String title) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    }
   
