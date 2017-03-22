package CashInHand;

//import DatabaseConnection.Database;
import UsableMethods.Database;
import UsableMethods.Message;
import UsableMethods.dbConnection;
import UsableMethods.table;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLCashInHandController implements Initializable {
    
     @FXML
    private TableColumn<table, Integer> column_Sn;

    @FXML
    private TableColumn<table, String> column_Particular;

    @FXML
    private TableColumn<table, Double> column_Debit;

    @FXML
    private TextField textField_TotalCash;

    @FXML
    private TextField textField_TotalCashUpto;
    
    @FXML
    private Label label_TotalAmount;

    @FXML
    private DatePicker date_to;

    @FXML
    private TableView<table> table_CashInHand;

    @FXML
    private TableColumn<table, String> column_Date;

    @FXML
    private TableColumn<table, Double> column_Credit;

    @FXML
    private DatePicker date_from;

    public static int rowvalue = 0;
    public static int listvalue = 0;
    Connection con = null;
    PreparedStatement pst = null;
    dbConnection dbc = new dbConnection();
    private final IntegerProperty tableIndex = new SimpleIntegerProperty();
    Message msg= new Message();
    final ObservableList<table> data = FXCollections.observableArrayList();
    
    

//table view function 
     public void show_Data() {
        try {
            String from_date = String.valueOf(date_from.getValue());
            String to_date = String.valueOf(date_to.getValue());
            String sql = "SELECT * FROM `tbl_cashinhand_details` WHERE `date` >= '" + from_date + "' And `date` <= '" + to_date + "'";
            
            con = dbc.connection();
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            int temp_sn = 1;

            column_Sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            column_Date.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            column_Particular.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            column_Debit.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            column_Credit.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
            
            table_CashInHand.getItems().clear();
            
            Double total_debitAmount = 0.0;
            Double total_creditAmount = 0.0;
            Double cashInHand = 0.0;

            while (rs.next()) {
                table as = new table();

                as.i1.set(temp_sn++);
                as.s1.set(rs.getString("date"));
                as.s2.set(rs.getString("particular"));
                as.d1.set(rs.getDouble("debit_amount"));
                as.d2.set(rs.getDouble("credit_amount"));
                total_debitAmount += rs.getDouble("debit_amount");
                total_creditAmount += rs.getDouble("credit_amount");
                

                data.add(as);
            }
            cashInHand = total_creditAmount - total_debitAmount;
            textField_TotalCash.setText(String.valueOf(cashInHand));

            table_CashInHand.setItems(data);
            
            sql = "SELECT * FROM `tbl_cashinhand`";
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while(rs.next()){
                textField_TotalCashUpto.setText(rs.getString("cashinhand"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

   
   
    public void initialDate() {
     Database db = new Database();
       
        date_from.setValue(db.NOW_LOCAL_DATEfirstDateOfMonth());
        date_to.setValue(db.NOW_LOCAL_DATEcurrentDate());
    }
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        initialDate();
        show_Data();
        
        
    }
}

