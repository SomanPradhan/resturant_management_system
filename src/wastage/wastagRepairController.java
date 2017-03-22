/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wastage;



import NumberTextField.NumberTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import UsableMethods.*;
import com.jfoenix.controls.JFXTextField;
import java.sql.Date;

/**
 *
 * @author SujanDhakal
 */
public class wastagRepairController implements Initializable {
    public boolean checker= false; 
    public String Particular;
    public Integer rowvalue = 0;
    String id,name;
    public Double cost = 0.0;
    public Integer quantity = 0;
   
     
    Message msg = new Message();
    Connection con= null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    
    dbConnection dbc = new dbConnection();
   
    @FXML
    private JFXTextField textField_Id;
    
    @FXML
    private ComboBox comboBox_Name;
    
    @FXML
    private ComboBox comboBox_Particular;
    
    @FXML
    private ComboBox comboBox_viewBy;
    
    
    @FXML
    private NumberTextField textField_cost_Price;   
   
    @FXML
    private NumberTextField textField_Quantity;
    
    @FXML
    private Label label_Totalamount;
    
    @FXML
    private Label label_WastageAmt;
    
    @FXML
    private Label label_RepairAmt;
    
    @FXML
    private Label label_InventoryAmt;
    
    @FXML
    private Label label_Total;
    
    
    @FXML
    public TableView<table> tbl_Wastage;
    
    @FXML
    public TableColumn<table, Integer>column_Sn;
    @FXML
    public TableColumn<table, String> column_Date;
    @FXML
    public TableColumn<table, String> column_Id;
    @FXML
    public TableColumn<table, String> column_Name;
    @FXML
    public TableColumn<table, String> column_Particular;
    @FXML
    public TableColumn<table, Double> column_Price;
    @FXML
    public TableColumn<table, Integer> column_Quantity;
    @FXML
    public TableColumn<table, Double> column_Totalamount;
    
    @FXML
    public TableColumn<table, String>column_PurchaseDate;
    
    
    @FXML
    public DatePicker dateFrom;
     
    @FXML
    public DatePicker dateTo;
    
    
    Date  purchase_Date;
    
    
    
    final ObservableList<String> particularList = FXCollections
            .observableArrayList("Wastage","Repair");
     
    final ObservableList<String> viewByList = FXCollections
            .observableArrayList("All","Wastage","Repair");
    
    ObservableList<String> nameList = FXCollections.observableArrayList();
    
    final ObservableList<table > dataWastage = FXCollections.observableArrayList();
    
    public void comboBoxFunction(){
    comboBox_Particular.setItems(particularList);
     comboBox_Particular.setValue("Wastage");
     
     comboBox_viewBy.setItems(viewByList);
     comboBox_viewBy.setValue("All");
    
    
    }
    //displays assets name from subassets details in a combobox when id is entered 
    public void whileTypingId(){
        try{
            boolean checker1 = true;
           String id = textField_Id.getText();
        con = dbc.connection();
        String sql = "SELECT * FROM `tbl_subassets` WHERE `id` = '"+id+"'";
        pst = (PreparedStatement) con.prepareStatement(sql);
        rs = pst.executeQuery(sql);
        while(rs.next()){
            String name = rs.getString("name");
            nameList.add(name);
            checker1 = false;
        }
        comboBox_Name.setItems(nameList);
        
        if(checker1){
            comboBox_Name.getItems().clear();
        }
   }catch(Exception e){
        System.out.println("in whileTypingId method in ");
        e.printStackTrace();
    }
    }
    
    public void comboBox(){
           showdata();
          tbl_Wastage.getItems().clear();
           tbl_Wastage.getItems().clear();
       }
    
    
    //calculates total amount ie total=price*quantity
     public void totalamt(){
          try{
            String price= textField_cost_Price.getText();
            String quantity= textField_Quantity.getText();
            if(!(quantity.isEmpty())&&!(price.isEmpty())){
             Double Totalamt=Double.parseDouble(quantity)* Double.parseDouble (price);
            label_Totalamount.setText(String.valueOf(Totalamt));
            }
            else{
            label_Totalamount.setText("0");
            
            }   
          }catch(Exception e){
              
              
          }
            
            
            }
    
     
    @FXML
    private Button button_Save;
    
    @FXML
    private Button button_Delete;
    
    @FXML
    private Button button_Edit;
   
    
    
    
    
    @FXML
    private void Save(ActionEvent event) {
        try {
           
            con= (Connection) dbc.connection();
            //  Fixed assets not savable when particular equals wastage ie fixed assets goes to repair 
            String sql28 = "SELECT * FROM `tbl_assets` WHERE `id`= '"+textField_Id.getText()+"'";;
            pst = (PreparedStatement) con.prepareStatement(sql28);
            ResultSet rs28 = pst.executeQuery(sql28);
            String type = "";
            while(rs28.next()){
                type = rs28.getString("type");
            }
            if(type.equalsIgnoreCase("Fixed") && String.valueOf(comboBox_Particular.getValue())=="Wastage"){
            //System.out.println("Error;Change particular to Repair!!");
            //msg.alertOptionMessage("Error;Change particular to Repair!!");
            msg.alertMessage(Alert.AlertType.ERROR, "ERROR", " Change particular to Repair!!");
             
            }
            else{
            // for editing the assets, checker(flag) used in edit function
            if (checker==true){
               
             getSelectedWastage();
             editDelete();
             
                          }
            // gets purchase date from subassets details and inserted into wastage table for deletion of corresponding assets in subassets details when deleted from wastage
            if(String.valueOf(comboBox_Particular.getValue())=="Repair"){
                String sql8 = "SELECT * FROM `tbl_subassets_details` WHERE id = '"+id+"' AND `name`='"+name+"'AND `remaining_quantity` > 0";
            PreparedStatement pst8 = (PreparedStatement) con.prepareStatement(sql8);
            rs = pst8.executeQuery(sql8);
            while(rs.next()){
                purchase_Date = rs.getDate(3);
                break;
            }
            insertData(); 
             }
           
            // Entered quantity in wastage mustn't exceed quantity in assets
            if(String.valueOf(comboBox_Particular.getValue())!="Repair"){
           
            Integer SubassetsQuantity=0;
            String  sql7 ="SELECT quantity FROM `tbl_subassets` WHERE name='"+name+"' AND quantity>0";
            pst = (PreparedStatement) con.prepareStatement(sql7);
                rs= pst.executeQuery(sql7);
                while(rs.next()){
                    
                 SubassetsQuantity=rs.getInt("quantity");
                 }
            //System.out.println("Rem= "+Remquantity);
            //System.out.println("qua= "+wastageQuantity);
            
           //when quantity from tbl_subassets is greater than entered wastage quantity,insertion is allowed 
            if (SubassetsQuantity>=Integer.parseInt(textField_Quantity.getText())){
            Integer temWastage = Integer.parseInt(textField_Quantity.getText());
            //System.out.println(temWastage);
            Integer temWast = Integer.parseInt(textField_Quantity.getText());
            Double assetsAmount = 0.0;
            String sql8 = "SELECT * FROM `tbl_subassets_details` WHERE id = '"+id+"' AND `name`='"+name+"'AND `remaining_quantity` > 0";
            PreparedStatement pst8 = (PreparedStatement) con.prepareStatement(sql8);
            Double cprice = 0.0;
            
            ResultSet rs  = pst8.executeQuery(sql8);
            while(rs.next()){
                Integer RemQuantity = rs.getInt("remaining_quantity");
                purchase_Date=rs.getDate(3);
                cost = rs.getDouble("cost_price");
                
                assetsAmount += cost;
                //remaining quantity(RemQuantity) fom subassets details table less than entered quantity(temWast) 
                if(RemQuantity<temWast){
                    quantity = RemQuantity;
                    temWast -= RemQuantity;// entered quantity-remaining quantity from subassets details table
                }
                else{
                    quantity = temWast;
                }
                RemQuantity -= temWastage;// remaining quantity from subassets details from subassets details -temWastage(entered quantity in wastage)
                temWastage = 0;
                Integer tempRem = RemQuantity;
                if(RemQuantity<0){// if the subtracted value is negative, it's made positive 
                    RemQuantity = - RemQuantity;
                    tempRem = 0;
                    temWastage = RemQuantity;
                }
                insertData();
               // cprice = cost*tempRem;
               //updates the remaining quantity in subassets details table when temwastage is positive
                if(temWastage>-1){
                String  sql6="UPDATE `tbl_subassets_details` SET `remaining_quantity`= '"+tempRem+"'  WHERE id='"+id+"' AND name='"+name+"' AND `remaining_quantity` > 0  ORDER BY date ASC LIMIT 1";
                System.out.println("temwastage");
                pst = (PreparedStatement) con.prepareStatement(sql6);
                int s =pst.executeUpdate(sql6);
                }
                if(temWastage==0){
                    break;
                }
            }
            Double remqu=0.0;
            // again updated remaining quantity and total amount from subassets details is updated in subassets table 
            sql8 = "SELECT * FROM `tbl_subassets_details` WHERE id = '"+id+"' AND `name`='"+name+"'AND `remaining_quantity` > 0";
            pst8 = (PreparedStatement) con.prepareStatement(sql8);
            Double temTotalAmount = 0.0;
            rs  = pst8.executeQuery(sql8);
            while(rs.next()){
                Double remq = rs.getDouble("remaining_quantity");
                Double cos = rs.getDouble("cost_price");
                temTotalAmount = temTotalAmount + (remq*cos);// reamining quantity * cost price from subassets details 
                remqu+=remq;
            }
            String name = String.valueOf(comboBox_Name.getValue());
            String id = textField_Id.getText();
            Integer quantit = Integer.parseInt(textField_Quantity.getText());
//            String sql ="SELECT * FROM `tbl_subassets` WHERE `name` = '"+name+"'";
//            pst = (PreparedStatement) con.prepareStatement(sql);
//            rs = pst.executeQuery(sql);
//            Double amount = 0.0;
//            Integer quan = 0;
//            while(rs.next()){
//                amount = rs.getDouble("amount");
//                quan = rs.getInt("quantity");
//            }
//            Double temamount = quantit * assetsAmount;
//            amount = amount - temamount;
//            quan = quan - quantit;
            String sql = "UPDATE `tbl_subassets` SET `quantity`='"+remqu+"',`amount`='"+temTotalAmount+"' WHERE `name`='"+name+"'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            pst.executeUpdate(sql);
                //System.out.println(Remquantity);
                //System.out.println(wastageQuantity);
                
            //updated amount and quantity from subassets table is finally updated in assets table     
            sql = "SELECT * FROM `tbl_subassets` WHERE `id`='"+id+"'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            Double amount = 0.0;
            Integer quan = 0;
            while(rs.next()){
                amount += rs.getDouble("amount");
                quan += rs.getInt("quantity");
            }
//            temamount = quantit * assetsAmount;
//            amount = amount - temamount;
//            quan = quan - quantit;
//            
            sql = "UPDATE `tbl_assets` SET `remaining_quantity`='"+quan+"',`amount`='"+amount+"' WHERE `id`='"+id+"'";
            pst = (PreparedStatement) con.prepareStatement(sql);
            pst.executeUpdate(sql);    
            }else {
                
                  msg.alertMessage(Alert.AlertType.ERROR, "ERROR", " Error");
            
            //System.out.println("Error");
            
            
            }
            }
            /*String sql6="SELECT * FROM `tbl_subassets_details` WHERE id='"+id+"' AND name='"+name+"' ORDER BY date ASC LIMIT 1";
                pst = (PreparedStatement) con.prepareStatement(sql6);
                
                 rs = pst.executeQuery(sql6);
                while(rs.next()){
                   
                     Remquantity= rs.getInt("remaining_quantity");
                }
              */  
            /*pst = (PreparedStatement) con.prepareStatement("insert into tbl_wastage values(?,?,?,?,?,?,?)");
           
            pst.setInt(1, Integer.valueOf(textField_Id.getText()));
             
            pst.setString(2, textField_Name.getText());
            
            java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
            pst.setDate(3, date);
            
            pst.setString(4, String.valueOf(comboBox_Particular.getValue()));
              
            pst.setFloat(5, Float.valueOf(textField_cost_Price.getText()));
              
            pst.setInt(6, Integer.valueOf(textField_Quantity.getText())); 
              
            pst.setDouble(7, Double.valueOf(label_Totalamount.getText())); 
              
            pst.executeUpdate();
            
            
            showdata();   
                 */ 
            
           cancelFunction(event);
        }} catch (Exception e) {
           
           e.printStackTrace();
           //System.out.println("Another error");
        }
        
        
    }
    
    public void insertData() {
    
   try {
           
            
             
           
           
            
            
            pst = (PreparedStatement) con.prepareStatement("insert into tbl_wastage values(?,?,?,?,?,?,?,?)");
           
            pst.setString(1,textField_Id.getText());
             
            pst.setString(2, String.valueOf(comboBox_Name.getValue()));
            Database db = new Database();
            pst.setString(3, String.valueOf (db.getCurrentDate()));
            
            //java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
            //pst.setDate(3, date);
            
            pst.setString(4, String.valueOf(comboBox_Particular.getValue()));
              //Particular = String.valueOf(comboBox_Particular.getValue());
              
              //when particular= repair costprice, quantity are entered by the user 
              if(checker == true || (String.valueOf(comboBox_Particular.getValue())=="Repair")){
            pst.setDouble(5, Double.valueOf(textField_cost_Price.getText()));
              
            pst.setDouble(6, Double.valueOf(textField_Quantity.getText())); 
              //quantity=Integer.parseInt(textField_Quantity.getText());
              
            pst.setDouble(7, Double.valueOf(label_Totalamount.getText())); 
              }
              // when particular equals wastage cost gets pulled from subassets details table and quantity is entered 
              else{
                  pst.setDouble(5, cost);
              
            pst.setDouble(6, quantity); 
              
              Double total= Double.parseDouble(String.valueOf(quantity)) * cost ;
            pst.setDouble(7, total); 
            
                  
              }
              //System.out.println("date - "+purchase_Date);
              pst.setDate(8, purchase_Date);
              
            pst.executeUpdate();
            
             // fixed assets Repair detail goes to cash in hand
            if ((String.valueOf(comboBox_Particular.getValue())=="Repair")){
           Double pricee = Double.parseDouble(textField_cost_Price.getText());
           String date= String.valueOf(db.getCurrentDate());
           String id=String.valueOf(textField_Id.getText());
           String name=String.valueOf(comboBox_Name.getValue());
            sqlForCashinhand cash = new sqlForCashinhand();
            cash.insertInCashInhand(date, name+": "+id,pricee, null);
           // System.out.println(date+ "Fixed Assets Repair: "+id + pricee + null);
            }
            showdata();   
            
        } catch (Exception e) {
           
           e.printStackTrace();
           //System.out.println("Another error");
        }
        
        
    }
    // gets the costprice of assets from subassets details table when particular= wastage 
    //and sets the costprice value into price field of wastage 
    public void particular() throws SQLException{
    if(String.valueOf(comboBox_Particular.getValue())=="Wastage"){
                  id   = textField_Id.getText();
                  name = String.valueOf(comboBox_Name.getValue());
                  int Remquantity=0;
                Double subassetsPrice = 0.0; 
                 String sql="SELECT * FROM `tbl_subassets_details` WHERE id='"+id+"' AND name='"+name+"' AND remaining_quantity>0 ORDER BY date ASC LIMIT 1";
                pst = (PreparedStatement) con.prepareStatement(sql);
                rs= pst.executeQuery(sql);
                while(rs.next()){
                    
                subassetsPrice = rs.getDouble("cost_price");  
                Remquantity= rs.getInt("remaining_quantity");  
                 }

                textField_cost_Price.setText(String.valueOf(subassetsPrice));
                textField_cost_Price.setEditable(false);
    
    }
    else if(String.valueOf(comboBox_Particular.getValue())=="Repair"){
        
        textField_cost_Price.setEditable(true);
        textField_cost_Price.setText(" ");
    
    
    }
        
        
        
        
        }    
    
    
    
    public void showdata() {
        try {
            /* String view= (String) comboBox_viewBy.getValue();
           String sql = "select * from `tbl_wastage` where particular = '"+view+"'";
             if(view=="All"){
                sql= "select * from `tbl_wastage`";
            }*/
            //String sql = "SELECT * FROM `tbl_wastage` ";
            
            String from_date = String.valueOf(dateFrom.getValue());
            String to_date = String.valueOf(dateTo.getValue());
            String sql = "select * from tbl_wastage where `date` >= '" + from_date + "' And `date` <= '" + to_date + "'" ;
            
            Integer temp_sn= 1;

            con = dbc.connection();
           
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
           
            column_Sn.setCellValueFactory(new PropertyValueFactory< table, Integer>("i1"));
            column_Date.setCellValueFactory(new PropertyValueFactory< table, String>("s1"));
            column_Id.setCellValueFactory(new PropertyValueFactory< table, String>("s2"));
            column_Name.setCellValueFactory(new PropertyValueFactory< table, String>("s3"));
            column_Particular.setCellValueFactory(new PropertyValueFactory< table, String>("s4"));
            column_Price.setCellValueFactory(new PropertyValueFactory< table, Double>("d1"));
            column_Quantity.setCellValueFactory(new PropertyValueFactory< table, Integer>("i2"));
            column_Totalamount.setCellValueFactory(new PropertyValueFactory< table, Double>("d3"));
            column_PurchaseDate.setCellValueFactory(new PropertyValueFactory< table, String>("s5"));

           tbl_Wastage.getItems().clear();
           
           Double wastageTotal = 0.0;
           Double repairTotal = 0.0;
           Double inventoryTotal = 0.0;
           Double total=0.0;
           while(rs.next()){
               table tbl = new table();
               
               tbl.i1.set(temp_sn++);
               tbl.s1.set(rs.getString("date"));
               tbl.s2.set(rs.getString("id"));
               tbl.s3.set(rs.getString("name"));
               tbl.s4.set(rs.getString("particular"));
               tbl.d1.set(rs.getDouble("cost_price"));
               tbl.i2.set(rs.getInt("quantity"));
               tbl.d3.set(rs.getDouble("total_amount"));
               tbl.s5.set(rs.getString("purchase_date"));
               
               dataWastage.add(tbl);
               
               
               String particular = rs.getString("particular");
               total = total+rs.getDouble("total_amount");  
                    
               if(particular.equalsIgnoreCase("Wastage")){
                   wastageTotal =wastageTotal + rs.getDouble("total_amount");
                   
               }
               else if(particular.equalsIgnoreCase("Repair")){
                   repairTotal += rs.getDouble("total_amount");
                   //System.out.println("wastage total "+wastageTotal);
               }
               else{
                   inventoryTotal += rs.getDouble("total_amount");
               }
           }
           
//           String sql2="SELECT * FROM `tbl_wastage`";
//            pst = (PreparedStatement) con.prepareStatement(sql2);
//                rs= pst.executeQuery(sql2);
//                while(rs.next()){
//                
//                 }
//                
           label_WastageAmt.setText(String.valueOf(wastageTotal));
           label_RepairAmt.setText(String.valueOf(repairTotal));
           label_InventoryAmt.setText(String.valueOf(inventoryTotal));
           label_Total.setText(String.valueOf(total));
           tbl_Wastage.setItems(dataWastage);
                
                    

                
            } catch (Exception e) {
                e.printStackTrace();
        }

            }
    //filters data to be viewed as view by wastage or repair or all
    public void viewBy(){
    try {
    String view= (String) comboBox_viewBy.getValue();
           String sql5 = "select * from `tbl_wastage` where particular = '"+view+"'";
             if(view=="All"){
                sql5= "select * from `tbl_wastage`";
            }
         
            
            
            
            Integer temp_sn= 1;

            con = dbc.connection();
           
            pst = (PreparedStatement) con.prepareStatement(sql5);
            ResultSet rs = pst.executeQuery(sql5);
           
            column_Sn.setCellValueFactory(new PropertyValueFactory< table, Integer>("i1"));
            column_Date.setCellValueFactory(new PropertyValueFactory< table, String>("s1"));
            column_Id.setCellValueFactory(new PropertyValueFactory< table, String>("s2"));
            column_Name.setCellValueFactory(new PropertyValueFactory< table, String>("s3"));
            column_Particular.setCellValueFactory(new PropertyValueFactory< table, String>("s4"));
            column_Price.setCellValueFactory(new PropertyValueFactory< table, Double>("d1"));
            column_Quantity.setCellValueFactory(new PropertyValueFactory< table, Integer>("i2"));
            column_Totalamount.setCellValueFactory(new PropertyValueFactory< table, Double>("d3"));
           
            column_PurchaseDate.setCellValueFactory(new PropertyValueFactory< table, String>("s5"));

           tbl_Wastage.getItems().clear();
           
           Double wastageTotal = 0.0;
           Double repairTotal = 0.0;
           Double inventoryTotal = 0.0;
           Double total=0.0;
           while(rs.next()){
               table tbl = new table();
               
               tbl.i1.set(temp_sn++);
               tbl.s1.set(rs.getString("date"));
               tbl.s2.set(rs.getString("id"));
               tbl.s3.set(rs.getString("name"));
               tbl.s4.set(rs.getString("particular"));
               tbl.d1.set(rs.getDouble("cost_price"));
               tbl.i2.set(rs.getInt("quantity"));
               tbl.d3.set(rs.getDouble("total_amount"));
               tbl.s5.set(rs.getString("purchase_date"));
               
               dataWastage.add(tbl);
               
           } 
             
//           String sql2="SELECT * FROM `tbl_wastage`";
//            pst = (PreparedStatement) con.prepareStatement(sql2);
//                rs= pst.executeQuery(sql2);
//                while(rs.next()){
//                
//                 }
//                
           
           tbl_Wastage.setItems(dataWastage);
                
                    

                
            } catch (Exception e) {
                e.printStackTrace();
        }

            }
        
    
    
    //for selecting the particular row in the table
     public void getSelectedWastage(){
            try {
            column_Date.setCellValueFactory(new PropertyValueFactory< table, String>("s1"));
            column_Id.setCellValueFactory(new PropertyValueFactory< table, String>("s2"));
            column_Name.setCellValueFactory(new PropertyValueFactory< table, String>("s3"));
            column_Particular.setCellValueFactory(new PropertyValueFactory< table, String>("s4"));
            column_Price.setCellValueFactory(new PropertyValueFactory< table, Double>("d1"));
            column_Quantity.setCellValueFactory(new PropertyValueFactory< table, Integer>("i2"));
            column_Totalamount.setCellValueFactory(new PropertyValueFactory< table, Double>("d3"));
            
            column_PurchaseDate.setCellValueFactory(new PropertyValueFactory< table, String>("s5"));

            tbl_Wastage.setItems(dataWastage);

            tbl_Wastage.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                    
                     rowvalue = (dataWastage.indexOf(newValue)+1);
                          
             
                    
                                

                }

            });
        } catch (Exception e) {
            System.out.println("on getSelected method of payrollController.java");
            e.printStackTrace();
        }
        }
    
    
    
  
    
    
    
    
    
    
    
    public void DeleteFunction(ActionEvent event){
         
         getSelectedWastage();
         delete();
     }
 
    
    public void delete(){
         
             try {
                boolean var =  msg.alertOptionMessage("DELETE", "Are you sure?");
                 if(var){
                 Integer row = rowvalue-1;
                 if(row!=-1){
                 
                 System.out.println(row);
                 table table = dataWastage.get(row);
                 Integer sn=table.i1.get();
                 String id = table.s2.get();
                 String name = table.s3.get();
                 String date = table.s1.get();
                 
                 Integer quantity = table.i2.get();
                 Double costPrice = table.d1.get();
                 Double amount = table.d3.get();
                 String purchaseDate = table.s5.get();
                 Double amountvariable = 0.0;
                 con = dbc.connection();
                 String sql = "SELECT * FROM `tbl_subassets_details` WHERE `id` = '"+id+"' AND `cost_price` = '"+costPrice+"' AND `date` ='"+purchaseDate+"' AND `name` = '"+name+"' LIMIT 1";
                 Double remquanvariable = 0.0;
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 rs = pst.executeQuery(sql);
                 while(rs.next()){
//                     amountvariable = rs.getDouble("total_amount");
                     remquanvariable = rs.getDouble("remaining_quantity");
                 }
                
                // remquanvariable (remaining quantity from subassets details + quantity value of this particular row selected)
                // and updates the remaining quantity value from subassets details to remquanvariable 
                remquanvariable = remquanvariable + quantity;
                sql = "UPDATE `tbl_subassets_details` SET `remaining_quantity`='"+remquanvariable+"' WHERE `id` = '"+id+"' AND `cost_price` = '"+costPrice+"' AND `date` ='"+purchaseDate+"' AND `name` = '"+name+"' LIMIT 1";
                pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
                 
                 Double price = 0.0;
                 sql = "SELECT * FROM `tbl_subassets` WHERE `name` ='"+name+"'";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 rs = pst.executeQuery(sql);
                 while(rs.next()){
                     remquanvariable = rs.getDouble("quantity");
                     price = rs.getDouble("amount");
                 }
                 // updates the qunatity and amount of subassets table when a row is updated in wastage 
                  price = price + amount;
                 remquanvariable = remquanvariable + quantity;
                 
                 sql = "UPDATE `tbl_subassets` SET `quantity`= '"+remquanvariable+"',`amount`= '"+price+"' WHERE `name` = '"+name+"'";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
               //  sql = "SELECT * FROM `tbl_subassets` WHERE ";
                
               sql = "SELECT * FROM `tbl_assets` WHERE `id` = '"+id+"'";
               
               pst = (PreparedStatement) con.prepareStatement(sql);
                 rs = pst.executeQuery(sql);
                 while(rs.next()){
                    remquanvariable = rs.getDouble("remaining_quantity");
                     price = rs.getDouble("amount");
                 }
                 
               // updates remaining quantity and total amount in assets table when a row is updated in wastage 
               remquanvariable = remquanvariable + quantity;
               price = price + amount;
               sql = "UPDATE `tbl_assets` SET `remaining_quantity`= '"+remquanvariable+"',`amount`= '"+price+"' WHERE `id` = '"+id+"'";
               pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
               
               
                 sql = "DELETE FROM `tbl_wastage` WHERE `id` ='"+id+"' AND `name`='"+name+"' AND `date`= '"+date+"' AND `cost_price` = '"+costPrice+"' AND `quantity` = '"+quantity+"' AND `total_amount` = '"+amount+"'  LIMIT 1";
                 
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
                   

                    // Deletes repair details from cash in hand  
                    sqlForCashinhand cash = new sqlForCashinhand();
                    cash.deleteInCashInhand(date, name+": "+id,costPrice, null); 
                 //System.out.println(date +"Fixed Assets Repair: "+id  +costPrice + null);
                
                showdata();
                 }
                 }   
                 
             } catch (Exception e) {
                 //System.out.println("on remove method");
                 e.printStackTrace();
             }
         
     }
    // for editing/updating  a row in the table  
    public void editDelete()
    {
    
     
         
             try {
                
               
                 Integer row = rowvalue-1;
                 if(row!=-1){
                 
                 System.out.println(row);
                 table table = dataWastage.get(row);
                 Integer sn=table.i1.get();
                 String id = table.s2.get();
                 String name = table.s3.get();
                 String date = table.s1.get();
                 String sql;
                 Integer quantity = table.i2.get();
                 Double costPrice = table.d1.get();
                 Double amount = table.d3.get();
                 String purchaseDate = table.s5.get();
                   
                 int quanti = Integer.parseInt(textField_Quantity.getText());
                 Double amountvariable = 0.0;
                 int subAssetQuantity = 0;
                 con = dbc.connection();
                 sql = "SELECT * FROM `tbl_subassets_details` WHERE `name` ='"+name+"' ";
           con = dbc.connection();
           pst = (PreparedStatement) con.prepareStatement(sql);
           rs = pst.executeQuery(sql);
           while(rs.next()){
               subAssetQuantity += rs.getInt("quantity");
           }
           // entered quantity in wastage is equal or less than quantity from subassets details 
           if(quanti<=subAssetQuantity)
           {
                  sql = "SELECT * FROM `tbl_subassets_details` WHERE `id` = '"+id+"' AND `cost_price` = '"+costPrice+"' AND `date` ='"+purchaseDate+"' AND `name` = '"+name+"' LIMIT 1";
                 Double remquanvariable = 0.0;
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 rs = pst.executeQuery(sql);
                 while(rs.next()){
//                     amountvariable = rs.getDouble("total_amount");
                     remquanvariable = rs.getDouble("remaining_quantity");
                 }
                
                // remquanvariable (remaining quantity from subassets details + quantity value of this particular row selected)
                // and updates the remaining quantity value from subassets details to remquanvariable 
                remquanvariable = remquanvariable + quantity;
                sql = "UPDATE `tbl_subassets_details` SET `remaining_quantity`='"+remquanvariable+"' WHERE `id` = '"+id+"' AND `cost_price` = '"+costPrice+"' AND `date` ='"+purchaseDate+"' AND `name` = '"+name+"' LIMIT 1";
                pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
                 
                 // updates the qunatity and amount of subassets table when a row is updated in wastage 
                 Double price = 0.0;
                 sql = "SELECT * FROM `tbl_subassets` WHERE `name` ='"+name+"'";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 rs = pst.executeQuery(sql);
                 while(rs.next()){
                     remquanvariable = rs.getDouble("quantity");
                     price = rs.getDouble("amount");
                 }
                  price = price + amount;
                 remquanvariable = remquanvariable + quantity;
                 
                 sql = "UPDATE `tbl_subassets` SET `quantity`= '"+remquanvariable+"',`amount`= '"+price+"' WHERE `name` = '"+name+"'";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
               //  sql = "SELECT * FROM `tbl_subassets` WHERE ";
                
               // updates remaining quantity and total amount in assets table when a row is updated in wastage 
               sql = "SELECT * FROM `tbl_assets` WHERE `id` = '"+id+"'";
               
               pst = (PreparedStatement) con.prepareStatement(sql);
                 rs = pst.executeQuery(sql);
                 while(rs.next()){
                    remquanvariable = rs.getDouble("remaining_quantity");
                     price = rs.getDouble("amount");
                 }
               remquanvariable = remquanvariable + quantity;
               price = price + amount;
               sql = "UPDATE `tbl_assets` SET `remaining_quantity`= '"+remquanvariable+"',`amount`= '"+price+"' WHERE `id` = '"+id+"'";
               pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
               
               // after updating the row in wastage, old wastage row is deleted and new updated row is entered via save function 
                 sql = "DELETE FROM `tbl_wastage` WHERE `id` ='"+id+"' AND `name`='"+name+"' AND `date`= '"+date+"' AND `cost_price` = '"+costPrice+"' AND `quantity` = '"+quantity+"' AND `total_amount` = '"+amount+"'  LIMIT 1";
                 
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
                 
                  // deletes fixed assets info from cash in hand
                  sqlForCashinhand cash = new sqlForCashinhand();
                  cash.deleteInCashInhand(date, "Fixed Assets Repair: "+id,costPrice, null); 
                 
                showdata();
                 
                 }
                 }
                 
             } catch (Exception e) {
                 //System.out.println("on remove method");
                 e.printStackTrace();
             }
         
     }
    
    
    
 
     // gets the selected row values when edit button is pressed
     public void editFunction(ActionEvent event){
         
         
             try {
                 // flag used to enterd new updated data in the wastage via save function 
                 checker=true;
                 getSelectedWastage();
         Integer row = rowvalue-1;
         if(row!=-1){
                 table table = dataWastage.get(row);
                 //comboBox_Particular.setValue("Wastage");
                 //con = dbc.connection();
                 textField_Id.setText(table.s2.get());
                 //pst = (PreparedStatement) con.prepareStatement("SELECT * FROM ``tbl_wastage`` WHERE `id` ='"+textField_Id.getText()+"'");
                 //rs = pst.executeQuery();
                 //while(rs.next()){
                    //textField_Name.setText(rs.getString("name"));    
                 //}
                 
                 // for identifying if the row contains wastage, repair or inventory as particular 
                if(table.s4.get().equals("Wastage"))
                {
                comboBox_Particular.setValue("Wastage");
                }
                else if(table.s4.get().equals("Repair")){
                comboBox_Particular.setValue("Repair");
                }
                else{
                
                comboBox_Particular.setValue("Inventory");
                }       
                 
                
                 comboBox_Name.setValue(table.s3.get());
                 textField_cost_Price.setText(String.valueOf(table.d1.get()));
                 textField_Quantity.setText(String.valueOf(table.i2.get()));
                 label_Totalamount.setText(String.valueOf(table.d3.get()));
                 //checker=true;
                 
             }
             }catch ( Exception ex) {
                 System.out.println("on editButtonFunction method of FXMLDocumentController.java");
             }
         
         
                 
     }
    
    
    
  
    
   
       
  
  /*  public LocalDate NOW_LOCAL_DATEcurrentDate() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

 
    public LocalDate NOW_LOCAL_DATEFirstDate() {
        String date = new SimpleDateFormat("yyyy-MM-01").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }
   */   
   public void initialDate(){
      // System.out.println("First Date" +NOW_LOCAL_DATEFirstDate());
      Database db = new Database();
      dateFrom.setValue(db.NOW_LOCAL_DATEfirstDateOfMonth());
   dateTo.setValue(db.NOW_LOCAL_DATEcurrentDate());
   
   
   
   } 
   
   
           // sets default values into the fields when cancel button is pressed 
           public void cancelFunction(ActionEvent event){
           comboBox_Particular.setValue("Wastage");
          
           textField_Id.clear();
           comboBox_Name.getItems().clear();
           comboBox_Name.setValue("");
           textField_Quantity.clear();
           textField_cost_Price.clear();
           
           label_Totalamount.setText("0");
           
           
       }
   
   
   
    
  
     

       @Override
   public void initialize(URL url, ResourceBundle rb) {
        initialDate();
        comboBoxFunction();
        viewBy();
        showdata();
        getSelectedWastage();
        
    }    
    
}
 
      
    
    
    
    
    
    
    
    
    
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
     
    
   
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
 