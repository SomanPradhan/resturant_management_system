/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PurchaseEntry;

import UsableMethods.dbConnection;
import UsableMethods.sqlForCashinhand;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Soman
 */
public class InsertData {
    Connection con;
    PreparedStatement pst;
    dbConnection dbc = new dbConnection();
    Savepoint s;
    public void InsertDataInDistributerTable(String did , String dname, Double credit){
        try {
            String sql = "INSERT INTO `tbl_distributer`(`did`, `dname`,`remaining_amount`) VALUES ('"+did+"', '"+dname+"', '"+credit+"')";
            con = dbc.connection();
            pst = (PreparedStatement) con.prepareStatement(sql);
            int i = pst.executeUpdate(sql);
        } catch (Exception e) {
                System.out.println("in InsertDataInDistributerTable method of InseraData");
                e.printStackTrace();
        }
    }
    
    public void UpdateDataInDistributerTable(String did, Double credit){
        try{
            Double remainingAmount = null;
            String sql = "SELECT * FROM `tbl_distributer` WHERE `did` ='"+did+"'";
            con = dbc.connection();
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while(rs.next()){
                remainingAmount = rs.getDouble("remaining_amount");
            }
            remainingAmount += credit;
            sql = "UPDATE `tbl_distributer` SET `remaining_amount` = '"+remainingAmount+"' WHERE `did` = '"+did+"'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            int i = pst.executeUpdate(sql);
        }catch(Exception e){
            
        }
    }
    
    public void InsertDataIntoPurchase(Integer id, String date,String did,String dname,String particular,Double purchase_total,Double discount,Double vat,Double payment,Double credit){
        try {
            String sql = "INSERT INTO `tbl_purchase`(`invoice_id`, `date`, `did`, `dname`, `particular`, `purchase_total`, `discount`, `vat`, `payment`, `credit`) VALUES ('"+id+"', '"+date+"', '"+did+"', '"+dname+"', '"+particular+"', '"+purchase_total+"', '"+discount+"', '"+vat+"', '"+payment+"', '"+credit+"')";
            con = dbc.connection();
            pst = (PreparedStatement) con.prepareStatement(sql);
            int i = pst.executeUpdate(sql);          
            sqlForCashinhand cash = new sqlForCashinhand();
            cash.insertInCashInhand(date, "invoice id: "+id, payment, null);
//            Double remainingAmount = 0.0;
//            sql = "SELECT * FROM `tbl_distributer` WHERE `did` = '"+did+"'";
//            pst = (PreparedStatement) con.prepareStatement(sql);
//            ResultSet rs = pst.executeQuery(sql);
//            while(rs.next()){
//                remainingAmount = rs.getDouble("remaining_amount");
//            }
//            remainingAmount = remainingAmount + purchase_total - payment;
//            
//            sql = "UPDATE `tbl_distributer` SET `remaining_amount`= '"+remainingAmount+"' WHERE `did` = '"+did+"'";
//            pst = (PreparedStatement) con.prepareStatement(sql);
//            i = pst.executeUpdate(sql);
        } catch (Exception e) {
           
                System.out.println("in InsertDataIntoPurchase method of InseraData");
                e.printStackTrace();
        }
    }
    public void InsertDataIntoPurchaseDetails(Integer id,String item_code,String item_name,Integer quantity, Double rate, Double amount){
        try{
            String sql = "INSERT INTO `tbl_purchase_details`(`invoice_id`, `item_code`, `item_name`, `quantity`, `cost_price`, `amount`) VALUES ('"+id+"' ,'"+item_code+"' ,'"+item_name+"' ,'"+quantity+"' ,'"+rate+"' ,'"+amount+"')";
            con = dbc.connection();
            pst = (PreparedStatement) con.prepareStatement(sql);
            int i = pst.executeUpdate(sql);
            
        } catch (Exception e) {
            
                System.out.println("in InsertDataIntoPurchaseDetails method of InseraData");
                e.printStackTrace();
        
    }
}
}
