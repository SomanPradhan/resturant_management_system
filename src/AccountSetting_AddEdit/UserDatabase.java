/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccountSetting_AddEdit;

import UsableMethods.dbConnection;
import UsableMethods.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;

/**
 *
 * @author Soman
 */
public class UserDatabase {
    Message mess = new Message();
     Connection con = null;
    PreparedStatement pst = null;
    dbConnection dbc = new dbConnection();
   PreparedStatement ps=null;
     
    public void insertData(String id,String username,String password,String privileges,String date_modified){
        try{
            String tempo_id="";
           con = dbc.connection();
           String sql ="SELECT * FROM `tbl_user`";
             ps = (PreparedStatement) con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery(sql);
             while(rs.next()){
                 tempo_id = rs.getString("id");
                 System.out.println("temp_id value in database is "+tempo_id);
                 if(tempo_id.equals(id))
                     break;
             }
             if(!tempo_id.equals(id)){
             pst = (PreparedStatement) con.prepareStatement("insert into tbl_user values(?,?,?,?,?)");
            System.out.println("PreparedStatement created Successfully");

            
            pst.setString(1, id);
            pst.setString(2, username);
            pst.setString(3, password);
            pst.setString(4, privileges);
            pst.setString(5, date_modified);
            System.out.println("Executing Update to database");
            int i = pst.executeUpdate();
            System.out.println("Database Updated Successfully");

            if (i <= 0) {
                mess.alertMessage(Alert.AlertType.ERROR, "ERROR", "Cannot save data");
                
            } 
        
             }
             else
                 mess.alertMessage(Alert.AlertType.ERROR, "ERROR", "Dublicate id re-enter the values");
                
            }
            
            catch(Exception e){
            
        }


   
 
    }

    public void updateData(String id,String username,String password,String privileges,String date_modified,String staticId){
        try{
           con = dbc.connection();
           
           String sql = "UPDATE `tbl_user` SET `id`='"+id+"',`username` = '"+username+"',`password`='"+password+"',`privileges`='"+privileges+"',`date_modified`='"+date_modified+"' WHERE id = '" +staticId+"'";
            ps = (PreparedStatement) con.prepareStatement(sql);

            int j = ps.executeUpdate();
           //insertData( id, username, password, privileges, date_modified);
            /* pst = (PreparedStatement) con.prepareStatement("insert into tbl_usertype values(?,?,?,?,?)");
            System.out.println("PreparedStatement created Successfully");

            
            pst.setString(1, id);
            pst.setString(2, username);
            pst.setString(3, password);
            pst.setString(4, privileges);
            pst.setString(5, date_modified);

            System.out.println("Executing Update to database");
            int i = pst.executeUpdate();
            System.out.println("Database Updated Successfully");

            ShowErrorMessage("Cannot save data.", "ERROR");

            }*/
           
           
            }
            
            catch(Exception e){
            mess.alertMessage(Alert.AlertType.ERROR, "ERROR", "Dublicate Id");    
            
        }

    }
    public String SelectedSn(Integer rowvalue){
         String id=null;
        try {
           
            con = dbc.connection();
           
            String sql = "SELECT * FROM `tbl_user` LIMIT "+rowvalue+",1";
            ResultSet rs = con.createStatement().executeQuery(sql);
              while (rs.next()) {

                id = rs.getString("id");
                System.out.println("THE SELECTED id IS " + id);
                
            }
            
        } catch (Exception ex) {
            mess.alertMessage(Alert.AlertType.ERROR, "ERROR", "No item Selected");
        }
        return id;
      }
    public void DeleteRow(String id){
         try {
             String privilegesState=returnPrivileges(id);
             int count=returnCount();
            
            dbc.connection();
             
             
             if(count>1 || !privilegesState.equals("Admin")){
            String sql = "DELETE FROM `tbl_user` WHERE id = '" +id+"'";
            pst = (PreparedStatement) con.prepareStatement(sql);

            int i = pst.executeUpdate();
             }
             else{
                 mess.alertMessage(Alert.AlertType.ERROR, "ERROR", "THERE SHOULD BE ATLEAST 1 Admin");
                 
             }
        } catch (Exception e) {
            

        }
    }
 

    
    public String returnPrivileges(String id){
        String privilegesState=null;
        con = dbc.connection();
        try {
            String sql ="SELECT * FROM `tbl_user` WHERE id = '" +id+"'";
            ResultSet resultSet = con.createStatement().executeQuery(sql);
            while(resultSet.next()){
                privilegesState=resultSet.getString("privileges");
            }
            return privilegesState;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return privilegesState;
    }
    public int returnCount(){
        int count=0;
        try {
             con = dbc.connection();
            String sql ="SELECT * FROM `tbl_user` WHERE privileges='Admin'";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while(rs.next()){
                count++;
                System.out.println("the value of the count is "+count);
            }
            return count;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return count;
    }
    }
    

