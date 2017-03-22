/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UsableMethods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Soman
 */
public class sqlForCashinhand {
    
    
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    dbConnection dbc = new dbConnection();
    public void insertInCashInhand(String date,String particular,Double debit_amount,Double credit_amount){
        Database db = new Database();
        ArrayList value = new ArrayList();
        
        value.add(null);
        value.add(date);
        value.add(particular);
        value.add(debit_amount);
        value.add(credit_amount);
        db.insertData("tbl_cashinhand_details", value);
//        Double totalCash = getTotalCashInHand();
//        if(debit_amount==null)
//            debit_amount = 0.0;
//        
//        if (credit_amount==null)
//            credit_amount = 0.0;
//        
//        Double newTotalCash = totalCash - debit_amount + credit_amount;//should be changed if wrong
//        setTotalCashInHand(newTotalCash,totalCash);
    }
    public void deleteInCashInhand(String date,String particular,Double debit_amount,Double credit_amount){
        try{
        con = dbc.connection();
        String sql;
        if(credit_amount==null)
        sql = "DELETE FROM `tbl_cashinhand_details` WHERE `date`='"+date+"' AND `particular`='"+particular+"' AND `debit_amount` = "+debit_amount;//+" AND `credit_amount`= "+credit_amount;
        else
        sql = "DELETE FROM `tbl_cashinhand_details` WHERE `date`='"+date+"' AND `particular`='"+particular+"' AND `credit_amount`= "+credit_amount;    
        
        pst = (PreparedStatement) con.prepareStatement(sql);
        pst.executeUpdate(sql);
        Double totalCash = getTotalCashInHand();
        if(debit_amount==null)
            debit_amount = 0.0;
        if (credit_amount==null)
            credit_amount = 0.0;
        Double newTotalCash = totalCash + debit_amount - credit_amount;//should be changed if wrong
        setTotalCashInHand(newTotalCash,totalCash);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void updateInCashInhand(String date,String particular,Double debit,Double credit,String newDate,String newParticular,Double newDebit,Double newCredit){
       try{
           con = dbc.connection();
           String sql;
           if(credit==null)
           sql= "UPDATE `tbl_cashinhand_details` SET `date`='"+newDate+"',`particular`='"+newParticular+"',`debit_amount` = "+newDebit+",`credit_amount` = "+newCredit+" WHERE `date`='"+date+"' AND `particular`= '"+particular+"' AND `debit_amount` = "+debit;
           else
           sql= "UPDATE `tbl_cashinhand_details` SET `date`='"+newDate+"',`particular`='"+newParticular+"',`debit_amount` = "+newDebit+",`credit_amount` = "+newCredit+" WHERE `date`='"+date+"' AND `particular`= '"+particular+"' AND `credit_amount` = "+credit;    
           
           pst = (PreparedStatement) con.prepareStatement(sql);
           pst.executeUpdate(sql);
           Double totalCash = getTotalCashInHand();
           if(debit==null)
            debit = 0.0;
        if (credit==null)
            credit = 0.0;
        if(newCredit==null)
            newCredit= 0.0;
        if (newDebit == null)
            newDebit =0.0;
        Double newTotalCash = totalCash - debit + credit + newDebit- newCredit;//should be changed if wrong
        setTotalCashInHand(newTotalCash,totalCash);   
       }catch(Exception e){
           
       }
    }
    private Double getTotalCashInHand(){
        
        con = dbc.connection();
        Double cashinhand = 0.0;
        try{
        String sql = "SELECT * FROM `tbl_cashinhand`";
        pst = (PreparedStatement) con.prepareStatement(sql);
        rs = pst.executeQuery(sql);
        while(rs.next()){
            cashinhand = rs.getDouble("cashinhand");
        }
        //return cashinhand;
        }catch(Exception e){
            e.printStackTrace();
        }
        return cashinhand; 
    }
    
    private void setTotalCashInHand(Double newTotalCash,Double totalCash){
        con = dbc.connection();
        try{
            String sql = "UPDATE `tbl_cashinhand` SET `cashinhand`= '"+newTotalCash+"' WHERE `cashinhand`='"+totalCash+"'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            pst.executeUpdate(sql);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
