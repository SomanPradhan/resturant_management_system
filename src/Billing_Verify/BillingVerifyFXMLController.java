package Billing_Verify;

import UsableMethods.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import Billing.FXMLDocumentController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import static java.lang.System.err;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JRException;

/**
 * FXML Controller class
 *
 * @author Rubesh
 */
public class BillingVerifyFXMLController implements Initializable {

    @FXML
    private Label label_CompanyName;
    @FXML
    private Label label_Location;
    @FXML
    private Label label_PhoneNo;
    @FXML
    private Label label_PanNo;
    @FXML
    private Label label_BillNo;
    @FXML
    private Label label_Name;
    @FXML
    private Label label_Date;
    @FXML
    private Label label_Time;

    @FXML
    public TableView<table> table_Billing;

    @FXML
    public TableColumn<table, Double> sn;
    @FXML
    public TableColumn<table, String> foodCode;
    @FXML
    public TableColumn<table, String> foodName;
    @FXML
    public TableColumn<table, Double> quantity;
    @FXML
    public TableColumn<table, Double> price;
    @FXML
    public TableColumn<table, Double> amount;

    @FXML
    private TextField textField_Total;
    @FXML
    private JFXTextField textField_Discount;
    @FXML
    private TextField textField_SubTotal;
    @FXML
    private TextField textField_Vat;
    @FXML
    private TextField textField_ServiceCharge;
    @FXML
    private TextField textField_GrandTotal;
    @FXML
    private Label label_Discount;
    @FXML
    private Label label_Vat;
    @FXML
    private Label label_ServiceCharge;

    @FXML
    private JFXButton btn_printBill;
    @FXML
    private JFXButton btn_Credit;
    @FXML
    private JFXButton btn_Finish;

    private String companyName;
    private String location;
    private int phoneNo;
    private int panNo;
    private static double total;
    private double discountPercent;
    private static double discountAmount;
    private static double subTotal;
    private double vatPercent;
    private static double vat;
    private double serviceChargePercent;
    private static double serviceCharge;
    protected static double grandTotal;
    private static String name;
    private double roundedAmount;

    public final ObservableList<table> data = FXCollections.observableArrayList();

    Message m = new Message();
    Database db = new Database();
    FXMLDocumentController fc = new FXMLDocumentController();
    dbConnection dbc = new dbConnection();
    Integer tokenid = fc.listvalue;
    int billid = fc.getNextBillId();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        sn.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
        foodName.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
        quantity.setCellValueFactory(new PropertyValueFactory<table, Double>("d2"));
        price.setCellValueFactory(new PropertyValueFactory<table, Double>("d3"));
        amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d4"));
        table_Billing.setItems(data);

        //UDATE TOP LABELS LIKE NAME, LOCATION, PHONENO
        topLabels();

        //UPDATE TABLEVIEW
        updateBillingTable(tokenid);

        //UPDATE SUB-TOTAL
        textField_Total.setText(String.valueOf(fc.updateTotal(tokenid)));

//        textField_SubTotal.setText(String.valueOf(total));
        calculation();
    }

    /*
    *UPDATE THE BILLING_VERIFY'S TABLEVIEW
     */
    public boolean updateBillingTable(Integer listvalue) {
        //EMPTY THE TABLE FIRST
        data.removeAll(data);

        assert table_Billing != null : "fx:id=\"table_Billing\" was not injected: check your FXML file 'Billing_Verify.fxml'.";

        String sql = "select * from tbl_sales_temp where `token_id` =" + listvalue;
        dbConnection dbc = new dbConnection();

        try {
            Connection conn = dbc.connection();
            Statement s = conn.prepareStatement(sql);
            ResultSet rs = s.executeQuery(sql);

            //LOOP THROUGH ALL VALUES
            while (rs.next()) {
                table tbl = new table();

                tbl.d1.set(Double.valueOf(rs.getString("sn")));
                tbl.s1.set(rs.getString("item_name"));
                tbl.d2.set(Double.valueOf(rs.getString("quantity")));
                tbl.d3.set(Double.valueOf(rs.getString("price")));
                tbl.d4.set(Double.valueOf(rs.getString("amount")));
                data.add(tbl);

            }
            table_Billing.setItems(data);

            return true;

        } catch (SQLException | NumberFormatException e) {
            System.out.println("updateBillingTable");
            e.printStackTrace();
            m.alertMessage(AlertType.ERROR, "Cannot Update the Table", "Please retry again.");
        }
        return false;
    }

    @FXML
    public void printBill(ActionEvent event) {

        try {
            btn_Credit.setDisable(true);
            saveIntoDB();
//            int billid = bdb.getNextBillId() - 1;
            System.out.println("PrintBill " + billid);
            jasperBillView(billid);
            btn_printBill.setDisable(true);

        } catch (Exception e) {
            System.out.println("PrintBill");
            e.printStackTrace();
            m.alertMessage(AlertType.ERROR, "Cannot open report", "Please retry again.");

        }

    }

    /*
    ** UPDATES TOP LEBELS LIKE NAME, LOCATION, CONTACT PAN *
     */
    public void topLabels() {
        try {
            String sql = "select name,location,phone_no,pan_no from tbl_resturant_info";
            Connection conn = dbc.connection();
            Statement s = conn.prepareStatement(sql);
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {

                label_CompanyName.setText(rs.getString("name"));
                label_Location.setText(rs.getString("location"));
                label_PhoneNo.setText(rs.getString("phone_no"));
                label_PanNo.setText(rs.getString("pan_no"));

            }
            label_BillNo.setText(String.valueOf(fc.getNextBillId()));

            name = "Table " + tokenid;
            label_Name.setText(name);

        } catch (Exception e) {
            System.out.println("TopLabels");
            e.printStackTrace();
            m.alertMessage(AlertType.WARNING, "Labels are not set.", "Please check and retry again.");

        }
    }

    public void calculation() {
        subTotal();
        vatAndService();
        grandTotal();

    }

    public void discount(ActionEvent event) {
        total = Double.valueOf(textField_Total.getText());
        discountPercent = Double.valueOf(textField_Discount.getText());
        label_Discount.setText(String.valueOf(discountPercent));

        discountAmount = (discountPercent / 100) * total;
        double discountRight = discountAmount - ((int) (discountAmount / 10) * 10);
        double discountLeft = ((int) (discountAmount / 10) * 10);

        if (discountRight >= 0.0 && discountRight < 2.5) {

            discountAmount = discountLeft + 0;
            textField_Discount.setText(String.valueOf(discountAmount));

        } else if (discountRight >= 2.5 && discountRight < 5.0) {

            discountAmount = discountLeft + 5;
            textField_Discount.setText(String.valueOf(discountAmount));

        } else if (discountRight >= 5.0 && discountRight < 7.5) {

            discountAmount = discountLeft + 5;
            textField_Discount.setText(String.valueOf(discountAmount));

        } else if (discountRight >= 7.5 && discountRight <= 10.0) {

            discountAmount = discountLeft + 10;
            textField_Discount.setText(String.valueOf(discountAmount));
        }

        calculation();

    }

    //SUB-TOTAL
    public void subTotal() {

        total = Double.valueOf(textField_Total.getText());
        textField_SubTotal.setText(String.valueOf(total));
        if (textField_Discount.getText() == null) {
            subTotal = total;
            textField_SubTotal.setText(String.valueOf(subTotal));
        } else {
            subTotal = total - discountAmount;
            textField_SubTotal.setText(String.valueOf(subTotal));
        }
    }

    //CALCULATES VAT AND SERVICE AMOUNT 
    public void vatAndService() {
//        ArrayList<Double> vns = new ArrayList<Double>();

        try {
            String sql = "select vat,service_charge from tbl_resturant_info";
            Connection conn = dbc.connection();
            Statement s = conn.prepareStatement(sql);
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                vatPercent = Double.valueOf(rs.getString("vat"));
                serviceChargePercent = Double.valueOf(rs.getString("service_charge"));
            }

            //CALCULATE AND SETS VAT
            label_Vat.setText(String.valueOf(vatPercent));
            vat = ((vatPercent / 100) * subTotal);
            textField_Vat.setText(String.valueOf(round(vat, 2)));

            //CALCULATE AND SETS SERVICE CHARGE
            label_ServiceCharge.setText(String.valueOf(serviceChargePercent));
            serviceCharge = ((serviceChargePercent / 100) * subTotal);
            textField_ServiceCharge.setText(String.valueOf(round(serviceCharge, 2)));

//            vns.add(vat);
//            vns.add(serviceCharge);
//            System.out.println(vns);
//            return vns;
        } catch (SQLException | NumberFormatException e) {
            System.out.println("VatandService");
            e.printStackTrace();
            m.alertMessage(AlertType.WARNING, "Cannot retrive values from database.", "Please recheck and retry again.");

        }

//        return vns;
    }

    public void grandTotal() {
        grandTotal = subTotal + vat + serviceCharge;
//        System.out.println(grandTotal);

        double GTR = grandTotal - ((int) (grandTotal / 10) * 10);
        double GTL = ((int) (grandTotal / 10) * 10);

        if (GTR >= 0.0 && GTR < 2.5) {

            grandTotal = GTL + 0;
            textField_GrandTotal.setText(String.valueOf(grandTotal));
//            System.out.println(grandTotal);

        } else if (GTR >= 2.5 && GTR < 5.0) {

            grandTotal = GTL + 5;
            textField_GrandTotal.setText(String.valueOf(grandTotal));
//            System.out.println(grandTotal);

        } else if (GTR >= 5.0 && GTR < 7.5) {

            grandTotal = GTL + 5;
            textField_GrandTotal.setText(String.valueOf(grandTotal));
//            System.out.println(grandTotal);

        } else if (GTR >= 7.5 && GTR <= 10.0) {

            grandTotal = GTL + 10;
            textField_GrandTotal.setText(String.valueOf(grandTotal));
//            System.out.println(grandTotal);
        }
    }

    /*
    ** SAVE INTO MULTIPLE TABLES
     */
    public void saveIntoDB() {
        roundedAmount = subTotal + vat + serviceCharge - grandTotal;

        //INSERT INTO TBL_SALES
        ArrayList values1 = new ArrayList();
        values1.add(billid);
        values1.add(name);
        values1.add(db.getCurrentDate());
        values1.add(subTotal);
        values1.add(discountAmount);
        values1.add(vat);
        values1.add(serviceCharge);
        values1.add(grandTotal);
        values1.add(roundedAmount);

        db.insertData("tbl_sales", values1);

        //INSERT INTO TBL_SALES_DETAILS
        try {
            Connection conn = dbc.connection();
            String sql1 = "SELECT item_name,quantity,price,amount from tbl_sales_temp where token_id=" + tokenid;
            Statement s = conn.prepareStatement(sql1);
            ResultSet rs = s.executeQuery(sql1);

            String sql2 = "insert into tbl_sales_details(bill_id,item_name,quantity,price,amount) values(?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql2);

            while (rs.next()) {
                pst.setInt(1, billid);
                pst.setString(2, rs.getString("item_name"));
                pst.setDouble(3, rs.getDouble("quantity"));
                pst.setDouble(4, rs.getDouble("price"));
                pst.setDouble(5, rs.getDouble("amount"));

                int i = pst.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("saveIntoDB-TBL_SALES_DETAILS");
//            e.printStackTrace();
        }

        // DELETE FROM TBL_SALES_TEMP
        String sql3 = "delete from tbl_sales_temp where `token_id`=" + tokenid;
        db.deleteData(sql3);

        //INSERT INTO TBL_CASHINHND
        ArrayList values2 = new ArrayList();
        values2.add(null);
        values2.add(db.getCurrentDate());
        values2.add("Sales - Bill No: " + billid);
        values2.add(null);
        values2.add(grandTotal);
//        values2.add(db.getLastCashInhand() + grandTotal);

        int i = db.insertData("tbl_cashinhand_details", values2);

    }

    /*
    ** CLOSES BILLING_VERIFY WINDOW
     */
    public void finish(ActionEvent event) {

        updateBillingTable(tokenid);

        //CLOSES THE VERIFY WINDOW
        Stage stage = (Stage) btn_Finish.getScene().getWindow();
        stage.close();
        try {
            fc.updateFrontEndTable(tokenid);
        } catch (Exception x) {
            Throwable cause = x.getCause();
            err.format("invocation is failed: %s%n",
                    cause.getMessage());
        }
//        fc.table_FoodOrder.refresh();
//        fc.table_FoodOrder.requestFocus();
    }

    /*
    ** OPENS BILL REPORT
     */
    public void jasperBillView(Integer billid) {

        try {
            Connection conn = dbc.connection();
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/Billing_Verify/BillFormat.jrxml"));

            Map<String, Object> params = new HashMap<>();
            System.out.println("JasperBillView's bill_id : " + billid);

            params.put("mainParam", billid);

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            System.out.println("JasperBillView");
            e.printStackTrace();
            m.alertMessage(AlertType.ERROR, "Cannot open report.", "Please retry again.");
        }
    }

    @FXML
    public void openCreditEntry() {
        try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Billing_Verify/CreditEntry.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            final Stage stage = new Stage();

            root1.getStylesheets().add(this.getClass().getResource("/Billing/StyleAndColor.css").toExternalForm());

            stage.setTitle("Credit Entry");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();

            btn_printBill.setDisable(true);
            btn_Credit.setDisable(true);

        } catch (Exception e) {
            System.out.println("OpenCreditEntry");
            e.printStackTrace();
            m.alertMessage(AlertType.ERROR, "Cannot open Credit-Entry Wndow", "Please retry again.");
        }
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
