package saving;

//import DatabaseConnection.Database;
import UsableMethods.Database;
import UsableMethods.Message;
import UsableMethods.dbConnection;
import UsableMethods.sqlForCashinhand;
import UsableMethods.table;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;

public class FXMLSavingController implements Initializable {

    @FXML
    private DatePicker date_from;

    @FXML
    private DatePicker date_to;

    @FXML
    private Label label_TotalSaving;

    @FXML
    private Label label_TotalWithdraw;

    @FXML
    private Label label_grandTotalAmount;

    @FXML
    private Label label_TotalAmount;

    @FXML
    private ToggleGroup paticular;
    @FXML
    private JFXTextField textField_SavingAmount;

    @FXML
    private JFXTextField textField_WithdrawAmount;

    @FXML
    private JFXTextField textField_InstituteName;

    @FXML
    private JFXTextField textField_TransactionID;

//    @FXML
//    private TableColumn<SavingVariables, String> column_GrandTotal;
    @FXML
    private TableColumn<table, Integer> column_Sn;

//     @FXML
//    private TableColumn<SavingVariables, String> column_Total;
    @FXML
    private TableColumn<table, String> column_Saving;

    @FXML
    private TableView<table> table_Saving;

    @FXML
    private TableColumn<table, String> column_Particular;

    @FXML
    private TableColumn<table, String> column_InstituteName;

    @FXML
    private JFXButton button_Add;

    @FXML
    private JFXButton button_Delete;

    @FXML
    private TableColumn<table, String> column_TransactionID;

    @FXML
    private TableColumn<table, String> column_Withdraw;

    @FXML
    private TableColumn<table, String> column_Date;

    @FXML
    private JFXRadioButton radioBtn_withdrawal;

    @FXML
    private JFXRadioButton radioBtn_saving;

    public static int rowvalue = 0;
    public static int listvalue = 0;
    Connection con = null;
    PreparedStatement pst = null;
    dbConnection dbc = new dbConnection();
    Database db=new Database();    
    String staticId = null;
    private final IntegerProperty tableIndex = new SimpleIntegerProperty();
    Message msg= new Message();
    final ObservableList<table> data = FXCollections.observableArrayList();

    @FXML
    void add_details(ActionEvent event) {

        try {

            java.util.Date currentDate = new java.util.Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String date = dateFormat.format(currentDate);
            
            Double grandTotalAmount=0.0;
            String particular;
            String transaction_id = textField_TransactionID.getText();
            String financial_institute_name = textField_InstituteName.getText();
            
            
            if(!(label_grandTotalAmount.getText().isEmpty())){                
            grandTotalAmount = Double.parseDouble(label_grandTotalAmount.getText());
            Double amount = 0.0;
            if (!(textField_SavingAmount.getText().isEmpty())) {
                amount = Double.parseDouble(textField_SavingAmount.getText());
                grandTotalAmount = grandTotalAmount + amount;
            } else if (!(textField_WithdrawAmount.getText().isEmpty())) {
                amount = Double.parseDouble(textField_WithdrawAmount.getText());
                grandTotalAmount = grandTotalAmount - amount;                
            }
            
                     
            
            
            }
            
                           
            
            String grand_total = String.valueOf(grandTotalAmount);
          String saving = textField_SavingAmount.getText();
           if (saving.isEmpty()) {
               saving = "0";
          }
//
            String withdrawl = textField_WithdrawAmount.getText();
            if (withdrawl.isEmpty()) {
                withdrawl = "0";
            }
            if (radioBtn_saving.isSelected()) {
                
                particular = "Saving";
            } else {
                particular = "Withdrawal";
            }

          
            
                if ( (!financial_institute_name.isEmpty()) && (!textField_TransactionID.getText().isEmpty()) && (!(textField_SavingAmount.getText().isEmpty())) && radioBtn_saving.isSelected()){
                    insertData(date, transaction_id, financial_institute_name, particular, saving, withdrawl, grand_total);
                    textField_TransactionID.setText("");
                    textField_InstituteName.setText("");
                    textField_SavingAmount.setText("");
                    textField_WithdrawAmount.setText(""); 
                    

                }else if( (!financial_institute_name.isEmpty()) && !(textField_WithdrawAmount.getText().isEmpty()) && radioBtn_withdrawal.isSelected())  {
                   Database db = new Database();
                   insertData(date, transaction_id, financial_institute_name, particular, saving, withdrawl, grand_total);
                   con = dbc.connection();
                   String curDate = String.valueOf(db.NOW_LOCAL_DATEcurrentDate());
                   
                   String sql = "INSERT INTO `tbl_cashinhand_details`(`date`, `particular`, `debit_amount`, `credit_amount`, `cashinhand`) VALUES ('"+curDate+"', 'Withdrawl', '0', '"+withdrawl+"', '"+withdrawl+"')";
                   pst = (PreparedStatement) con.prepareStatement(sql);
                   pst.executeUpdate(sql);
                   textField_TransactionID.setText("");
                    textField_InstituteName.setText("");
                    textField_SavingAmount.setText("");
                    textField_WithdrawAmount.setText(""); 
                    msg.alertMessage(Alert.AlertType.NONE, "hurray", "data saved successfully");

                } else {
                    msg.alertMessage(Alert.AlertType.NONE, "ërror", "the required field are empty");
                }
                
                //INSERT INTO TBL_CASHINHND
        ArrayList values2 = new ArrayList();
        
        
            values2.add(null);
        values2.add(db.getCurrentDate());
        values2.add("Finanancial Institute- " +financial_institute_name);
        if (radioBtn_saving.isSelected()) {
            values2.add(saving);
        }else{ 
                    values2.add(null);
                    
        }  
        
        //values2.add(null);
        values2.add(withdrawl);
       // values2.add(db.getLastCashInhand() + Double.valueOf(grand_total));
        System.out.println(values2);

        int i = db.insertData("tbl_cashinhand_details", values2);
        } catch (Exception e) {
            System.out.println("did't work");

        }
        iniGrandTotal();
        show_Data();
    }
    

    @FXML
    void delete_details(ActionEvent event) {
        getSelected();
        if (rowvalue > 0) {
            remove();
        } else {
            msg.alertMessage(Alert.AlertType.NONE, "Error", "No Item selected");
            
        }
        show_Data();
    }

    public void getSelected() {
        try {
            column_Date.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));    
            column_TransactionID.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));    
            column_InstituteName.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));  
            column_Particular.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
            column_Saving.setCellValueFactory(new PropertyValueFactory<table, String>("s5"));
            column_Withdraw.setCellValueFactory(new PropertyValueFactory<table, String>("s6"));
            table_Saving.setItems(data);

            table_Saving.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {

                    rowvalue = (data.indexOf(newValue) + 1);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove() {
        String[] options = {"Yes", "No"};
        con = dbc.connection();
        int ans = JOptionPane.showOptionDialog(null, "Sure to Delete??", "Delete Confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (ans == 0) {
            rowvalue--;
            if (rowvalue > -1) {
                System.out.println("rowvalue is=" + rowvalue);
                table tbl = new table();
                tbl = table_Saving.getItems().get(rowvalue);
                
//                forFunction valuepass = new forFunction();
//                
//                valuepass.DeleteRow(valuepass.SelectedSn(rowvalue));
                 try{
                   String id = tbl.s2.get();
                 

                String sql = "DELETE FROM `tbl_saving` WHERE `transaction_id` = '"+id+"'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                pst.executeUpdate(sql);
                sql ="SELECT * FROM `tbl_saving_total`";
                pst = (PreparedStatement) con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery(sql);
                Double totalAmount = 0.0;
                while(rs.next()){
                    totalAmount = rs.getDouble("grand_total_amount");
                }
                totalAmount = totalAmount - Double.parseDouble(tbl.s5.get()) + Double.parseDouble(tbl.s6.get());
                sql = "UPDATE `tbl_saving_total` SET `grand_total_amount` = '"+totalAmount+"'";
                pst.executeUpdate(sql);
                
                Double savingDouble = Double.parseDouble(tbl.s5.get());
                Double withdrawlDouble = Double.parseDouble(tbl.s6.get());
                if(tbl.s5.get().isEmpty() || tbl.s5.get() == "" || Double.parseDouble(tbl.s5.get())==0.0){
                    savingDouble = null;
                }
                if(tbl.s6.get().isEmpty() || tbl.s6.get() == "" || Double.parseDouble(tbl.s6.get())==0.0){
                    withdrawlDouble = null;
                }
                sqlForCashinhand cash = new sqlForCashinhand();
                cash.deleteInCashInhand(tbl.s1.get(), "Transaction id: "+tbl.s2.get(), totalAmount, totalAmount);
                
                 }catch(Exception e){
                     System.out.println("on delete");
                     e.printStackTrace();
                 }
                 
                show_Data();
                iniGrandTotal();

            }
        }
    }


    public void chose_radio() {

        if (radioBtn_saving.isSelected()) {
            textField_SavingAmount.clear();
            textField_WithdrawAmount.clear();
            textField_WithdrawAmount.setDisable(true);
            textField_SavingAmount.setDisable(false);

        }
        if (radioBtn_withdrawal.isSelected()) {

            textField_SavingAmount.clear();
            textField_WithdrawAmount.clear();
            textField_WithdrawAmount.setDisable(false);
            textField_SavingAmount.setDisable(true);

        }
    }

    //table view function 
    public void show_Data() {
        try {
            String from_date = String.valueOf(date_from.getValue());
            String to_date = String.valueOf(date_to.getValue());
            String sql = "SELECT * FROM `tbl_saving` WHERE `date` >= '" + from_date + "' And `date` <= '" + to_date + "'";
            
            con = dbc.connection();
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            int temp_sn = 1;
            
            column_Sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            column_Date.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));    
            column_TransactionID.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));    
            column_InstituteName.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));  
            column_Particular.setCellValueFactory(new PropertyValueFactory<table, String>("s4"));
            column_Saving.setCellValueFactory(new PropertyValueFactory<table, String>("s5"));
            column_Withdraw.setCellValueFactory(new PropertyValueFactory<table, String>("s6"));

            table_Saving.getItems().clear();
            Double grandTotalAmount = 0.0;
            Double grandTotal = 0.0;
            Double totalSaving = 0.0;
            Double totalWithdrawl = 0.0;
            Double totalAmount = 0.0;
            while (rs.next()) {
                table as = new table();

                as.i1.set(temp_sn++);
                as.s1.set(rs.getString("date"));
                as.s2.set(rs.getString("transaction_id"));
                as.s3.set(rs.getString("financial_institute_name"));
                as.s4.set(rs.getString("particular"));
                as.s5.set(rs.getString("saving"));                
                as.s6.set(rs.getString("withdrawl"));
                
                totalSaving = totalSaving + rs.getDouble("saving");
                totalWithdrawl = totalWithdrawl + rs.getDouble("withdrawl");
                totalAmount = totalAmount + rs.getDouble("total");
             
                data.add(as);       

            }
             String sql2="select * from tbl_saving_total";
             pst = (PreparedStatement) con.prepareStatement(sql2);
             rs = pst.executeQuery(sql2);
             while(rs.next()){
             grandTotalAmount = rs.getDouble("grand_total_amount");
             label_grandTotalAmount.setText(String.valueOf(grandTotalAmount)); 
             
             }
            table_Saving.setItems(data);
            label_TotalSaving.setText(String.valueOf(totalSaving));              
            label_TotalWithdraw.setText(String.valueOf(totalWithdrawl));
            label_TotalAmount.setText(String.valueOf(totalAmount));
                

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
    
    public void insertData(String date,String transaction_id,String financial_institute_name,String particular,String saving, String withdrawl, String grand_total){
        try{
            String temp_id=null;
           con = dbc.connection();
           String sql ="SELECT * FROM tbl_saving";
             pst = (PreparedStatement) con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery(sql);
             while(rs.next()){
                 temp_id = rs.getString("transaction_id");
                 System.out.println("temp_id value in database is "+temp_id);
                 if(temp_id.equalsIgnoreCase(transaction_id))
                     break;
             }
             if(temp_id == null || !temp_id.equalsIgnoreCase(transaction_id)){
             pst = (PreparedStatement) con.prepareStatement("insert into tbl_saving values(?,?,?,?,?,?,?)");
             
            System.out.println("PreparedStatement created Successfully");

            Double total = Double.parseDouble(saving)-Double.parseDouble(withdrawl);
            //Double grand_total=100.00;
            //grand_total=total+grand_total;
            pst.setString(1, date);
            pst.setString(2, transaction_id);
            pst.setString(3, financial_institute_name);
            pst.setString(4, particular);
            pst.setString(5, saving);
            pst.setString(6, withdrawl);
            pst.setDouble(7, (total));
            //pst.setDouble(8, Double.parseDouble(grand_total));
                 //System.out.println(total);
            
            
            System.out.println("Executing Update to database");
            int i = pst.executeUpdate();
            Double savingDouble = Double.parseDouble(saving);
            if(saving.isEmpty()){
                saving = null;
            }
            
            Double withdrawlDouble = Double.parseDouble(withdrawl);
            if(withdrawl.isEmpty()){
                withdrawlDouble = null;
            }
            sqlForCashinhand cash = new sqlForCashinhand();
            cash.insertInCashInhand(date, "Transaction id: "+transaction_id, savingDouble, withdrawlDouble);
            
            if (i <= 0) {
                msg.alertMessage(Alert.AlertType.NONE, "ERROR", "Cannot save data");
            }
        
            
             Double totalAmount = 0.0;
             sql = "SELECT * FROM `tbl_saving_total`";
             pst = (PreparedStatement) con.prepareStatement(sql);
             rs = pst.executeQuery(sql);
             while(rs.next()){
                 totalAmount = rs.getDouble("grand_total_amount");
             }
             totalAmount = totalAmount + total;
              
             sql = "UPDATE `tbl_saving_total` SET `grand_total_amount`= '"+totalAmount+"'";
             pst = (PreparedStatement) con.prepareStatement(sql);
             i = pst.executeUpdate(sql);
             }
             else
                 msg.alertMessage(Alert.AlertType.NONE, "ERROR", "Dublicate id re-enter the values");
                 
             
            }
            
            catch(Exception e){
                e.printStackTrace();
            
        }
}
    

    public void iniGrandTotal() {
        try {
            con = dbc.connection();
            String sql = "SELECT * FROM `tbl_saving_total`";
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            while (rs.next()) {
                label_grandTotalAmount.setText(rs.getString("grand_total_amount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

   
    public void initialDate() {
       
        date_from.setValue(db.NOW_LOCAL_DATEfirstDateOfMonth());
        date_to.setValue(db.NOW_LOCAL_DATEcurrentDate());
    }
//    public class forFunction {
//    
//    Connection con = null;
//    PreparedStatement pst = null;
//    
//    
//    public String SelectedSn(Integer rowvalue){
//         String id=null;
//        try {
//           
//            con = dbc.connection();
//            String sql = "SELECT * FROM tbl_saving LIMIT "+rowvalue+",1";
//            ResultSet rs = con.createStatement().executeQuery(sql);
//              while (rs.next()) {
//
//                id = rs.getString("transaction_id");
//                System.out.println("THE SELECTED id IS " + id);
//                
//            }
//            
//        } catch (Exception ex) {
//            msg.alertMessage(Alert.AlertType.NONE, "ERROR", "No item Selected");
//            
//        }
//        return id;
//      }
//    public void DeleteRow(String id){
//         try {
//            dbc.connection();
//
//            String sql = "DELETE FROM tbl_saving WHERE transaction_id = '" +id+"'";
//            pst = (PreparedStatement) con.prepareStatement(sql);
//
//            int i = pst.executeUpdate();
//        } catch (Exception e) {
//            
//
//        }
//    }
//    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialDate();
        getSelected();
        show_Data();
        chose_radio();
        iniGrandTotal();
    }
}

