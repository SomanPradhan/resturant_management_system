package UsableMethods;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Database {

    Connection conn = null;
    PreparedStatement pst = null;
    dbConnection dbc = new dbConnection();
    Message m = new Message();
    
    
    /**
     * RETURNS CURRENT DATE *
     **/
    public String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = new Date();
        return dateFormat.format(date);

    }
    
    /**
     * For taking current date *
     **/
    public LocalDate NOW_LOCAL_DATEcurrentDate() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }
    
    
    /**
     * RETURNS FIRST DATE OF THE MONTH *
     **/
    public LocalDate NOW_LOCAL_DATEfirstDateOfMonth() {
        String date = new SimpleDateFormat("yyyy-MM-01").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }
    
    /**
     * RETURNS CURENT DATE OF THE MONTH *
     **/
    public LocalDate NOW_LOCAL_DATE_TO() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }
 

    /**
     * For taking specific date for Salary payment once in a month *
     **/
    public LocalDate NOW_LOCAL_DATEsalaryPaymentDate() {
        String date = new SimpleDateFormat("yyyy-MM-01").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    /**
     * For taking Specific date for add employee expense once in a month *
     **/
    public LocalDate NOW_LOCAL_DATEexpenseAddingDate() {
        String date = new SimpleDateFormat("yyyy-MM-01").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    
    /**
     * ********************************************
     * INSERTING DATA INTO DATABASE TABLE *
     * ********************************************
     */
    public int insertData(String tblName, ArrayList values) {
        int nv = values.size();
        String numOfvalues = getNumOfvalues(nv);

        try {
            conn = dbc.connection();

            String sql = "insert into " + tblName + "  " + numOfvalues + " ";
            System.out.println(sql);
            PreparedStatement pst = conn.prepareStatement(sql);

            for (int i = 1; i <= values.size(); i++) {

                pst.setObject(i, values.get(i - 1));
            }

            int i = pst.executeUpdate();

            if (i > 0) {
//                Alert.AlertType alertType = Alert.AlertType.CONFIRMATION;
//                m.alertMessage(alertType, "Data inserted.", null);
            } else {
                
                m.alertMessage(AlertType.ERROR, "Cannot save data.", null);

            }
            return i;
        } catch (Exception e) {
            System.out.println("InsertData");
            e.printStackTrace();
            Alert.AlertType alertType = Alert.AlertType.ERROR;
            m.alertMessage(alertType, "Error while inserting into database.", "Please retry again.");

        }
        return 0;
    }

    /**
     * RETURNS REQUIRED VALUES(?,?,?...) FOR SQL QUERY
     **/
    public String getNumOfvalues(int nv) {

        String numOfvalues = "";

        switch (nv) {
            case 2:
                return "values(?,?)";
            case 3:
                return "values(?,?,?)";
            case 4:
                return "values(?,?,?,?)";
            case 5:
                return "values(?,?,?,?,?)";
            case 6:
                return "values(?,?,?,?,?,?)";
            case 7:
                return "values(?,?,?,?,?,?,?)";
            case 8:
                return "values(?,?,?,?,?,?,?,?)";
            case 9:
                return "values(?,?,?,?,?,?,?,?,?)";
            case 10:
                return "values(?,?,?,?,?,?,?,?,?,?)";
            case 11:
                return "values(?,?,?,?,?,?,?,?,?,?,?)";
            case 12:
                return "values(?,?,?,?,?,?,?,?,?,?,?,?)";
            default:
                return numOfvalues;

        }
    }
    
    /**
     * ********************************************
     * DELETING DATA FROM DATABASE TABLE *
     * ********************************************
     */
    public int deleteData(String sql) {
//         String sql1 = "delete from `tbl_sales_temp` where `sn` =1.02";
        try {
            conn = dbc.connection();
            
//            String sql = "delete from " + tblName + " where " + paramOrId + " ";
            PreparedStatement pst = conn.prepareStatement(sql);

            int i = pst.executeUpdate();
            return i;

        } catch (Exception e) {
            System.out.println("deleteData");
            e.printStackTrace();
            m.alertMessage(AlertType.ERROR, "Error while deleting data.", "Please retry again.");

        }
        return 0;
    }
    
   
    /**
     * RETURNS LAST ROW CASH-IN-HAND VALUE *
     **/
    /*
    public double getLastCashInhand() {
        double cashinhand = 0.00;
        try {
            conn = dbc.connection();
            String sql = "select `cashinhand` from tbl_cashinhand order by sn desc LIMIT 1";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while (rs.next()) {
                cashinhand = Double.parseDouble(rs.getString("cashinhand"));
                System.out.println("cashinhand " + cashinhand);
            }
            return cashinhand;

        } catch (Exception e) {
            System.out.println("getLastCashInhand");
            e.printStackTrace();
            Alert.AlertType alertType = Alert.AlertType.ERROR;
            m.alertMessage(alertType, "Error while gettinf CashInHand.", "Please retry again.");

        }
        return cashinhand;
    }

    /*
    *SELECT QUERY TAKES A LOT OF PROCESSING SO NOT MAKING A METHOD FOR IT
    ***
    /
    public void selectData(String tblName, String sql) {
        ArrayList column = new ArrayList();
        try {
            conn = dbc.connection();
            column = getTableColumns(tblName);
            int cs = column.size();
//            ArrayList
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while (rs.next()) {
               
                
            }
        } catch (SQLException e) {

        }

    }
    
    public ArrayList getTableColumns(String tblName){
        ArrayList values = new ArrayList();
        try {
             
            conn = dbc.connection();
            String sql = "select * from INFORMATION_SCHEMA.COLUMNS where table_schema like 'restu%' and table_name = 'tbl_assets' ";
             pst = (PreparedStatement) conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery(sql);
             while (rs.next()) {
                
                 values.add(rs.getString("column_name"));
                 System.out.println(rs.getString("column_name"));
                
            }
             System.out.println(values);
             return values;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

*/
    

}
