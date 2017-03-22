/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assetsfx;


import NumberTextField.NumberTextField;
import UsableMethods.*;
import com.jfoenix.controls.JFXTextField;
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

/**
 *
 * @author SujanDhakal
 */
public class assetsController implements Initializable {
    
    public Integer rowvalue=0;
    public Integer rowvalue2 = 0;
    public Integer rowvalue3 = 0;
    public boolean checking = true;
    Message msg = new Message();
    
    int rowval1, rowval2;
    
    
    Connection con= null;
    
    PreparedStatement pst = null;
    ResultSet rs = null;
    
   dbConnection dbc = new dbConnection();
    
    private Integer Remquantity;
    private Double  Totalamount;
     
    @FXML
    private JFXTextField textField_Id;
    
    @FXML
    private JFXTextField textField_AssetsName;
    
    @FXML
    private JFXTextField textField_subName;
    
    @FXML
    private ComboBox comboBox_Type;
    
    
    @FXML
    private ComboBox comboBox_View;
    
    @FXML
    private ComboBox comboBox_entryType;
    
    @FXML
    private ComboBox comboBox_subAsset;
    
    @FXML
    private ComboBox comboBox_subAssetName;
    
    
    //@FXML
    //private TextField textField_Type;
    
    @FXML
    private NumberTextField textField_Price;   
   
    @FXML
    private NumberTextField textField_Quantity;
    
    @FXML
    private Label label_Totalamount;
    
    @FXML
    private Button button_Save;
    
    
    
    
    //@FXML
    //private TextField textField_remquantity;
    
    @FXML
    public TableView<table> table_SubAssetsDetails;// Table subAssets Details 

    @FXML
    public TableColumn<table, String>tableView_Id;
    @FXML
    public TableColumn<table, String> tableView_Name;
    @FXML
    public TableColumn<table, String> tableView_Date;
    @FXML
    public TableColumn<table, Double> tableView_Costprice;
    @FXML
    public TableColumn<table, Integer> tableView_Quantity;
    @FXML
    public TableColumn<table, Double> tableView_Totalamount;
    @FXML
    public TableColumn<table, Integer> tableView_Remainingquantity;
    
    
    // Declaration For upper table 
    @FXML
    public TableView<table> Tbl_UpAssets;// Table Assets

    @FXML
    public TableColumn<table, Integer>tableColumn_Sn;
    @FXML
    public TableColumn<table, String> tableColumn_IdUp;
    @FXML
    public TableColumn<table, String> tableColumn_NameUp;
    @FXML
    public TableColumn<table, String> tableColumn_TypeUp;
    @FXML
    public TableColumn<table, Integer> tableColumn_RemainingquantityUp;
    @FXML
    public TableColumn<table, Double> tableColumn_AmountUp;
    
    @FXML
    public TableView<table> table_SubAssets;// Table subAssets
    @FXML
    public TableColumn<table, Integer>column_Sn;
    @FXML
    public TableColumn<table,String>column_Id;
    @FXML
    public TableColumn<table, String>column_Name;
    @FXML
    public TableColumn<table, Integer>column_Quantity;
    @FXML
    public TableColumn<table, Double>column_Amount;


    
    
    
    
    
    
    
    @FXML
    public DatePicker dateFrom;
     
    @FXML
    public DatePicker dateTo;
    
    
    
    
    final ObservableList<table> dataSubAssets = FXCollections.observableArrayList();
   

    final ObservableList<table> dataSubAssetsDetails = FXCollections.observableArrayList();
    final ObservableList<table> dataAssets = FXCollections.observableArrayList();
    final ObservableList<String> typeList = FXCollections
            .observableArrayList("Fixed","Current");
    
     final ObservableList<String> viewList = FXCollections
            .observableArrayList("All","Fixed","Current");
    
      final ObservableList<String> entryList = FXCollections
            .observableArrayList("Old","New");
      
        ObservableList<String> nameList = FXCollections.observableArrayList();
    
    // Function to calculate total amount and display the amount 
      public void totalamt(){
          try{
            String price= textField_Price.getText();
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
    
      public void comboBoxFunctionList(){
          comboBox_Type.setItems(typeList);
          comboBox_Type.setValue("Current");
           comboBox_View.setItems(viewList);
           comboBox_View.setValue("All");
           comboBox_subAsset.setItems(entryList);
           comboBox_subAsset.setValue("Old");
           comboBox_entryType.setItems(entryList);
           comboBox_entryType.setValue("Old");
           
      }
      
       
    
    @FXML
    private void Save(ActionEvent event) {
        
        
        try {
            boolean idChecker = true;
        boolean assetNameChecker = true;
        boolean nameChecker = true;
        String id = textField_Id.getText();
        String assetsName = textField_AssetsName.getText();
        //System.out.println(id);
        String name = textField_subName.getText();
        String subAssName = String.valueOf(comboBox_subAsset.getValue());// comboBox_subAsset= comboBox Subasset entry
        if(subAssName.equalsIgnoreCase("Old")){
            name = String.valueOf(comboBox_subAssetName.getValue());
            //System.out.println("name ="+name);
        }
        String query = "SELECT * FROM `tbl_assets` WHERE `id`= '"+id+"'";
        pst = (PreparedStatement) con.prepareStatement(query);
        rs = pst.executeQuery(query);
        while(rs.next()){
            idChecker = false;
        }
        
        query = "SELECT * FROM `tbl_assets` WHERE `name`= '"+assetsName+"'";
        pst = (PreparedStatement) con.prepareStatement(query);
        rs = pst.executeQuery(query);
        while(rs.next()){
            assetNameChecker = false;
        }
        
        query = "SELECT * FROM `tbl_subassets` WHERE `name`= '"+name+"'";
        pst = (PreparedStatement) con.prepareStatement(query);
        rs = pst.executeQuery(query);
        while(rs.next()){
            nameChecker = false;
            //System.out.println("namechecker ="+nameChecker);
        }
        if((idChecker == true && (String.valueOf(comboBox_entryType.getValue()).equalsIgnoreCase("New"))) || (idChecker== false && (String.valueOf(comboBox_entryType.getValue()).equalsIgnoreCase("Old")))){
            
            if((assetNameChecker == true && (String.valueOf(comboBox_entryType.getValue()).equalsIgnoreCase("New"))) || (assetNameChecker == false && (String.valueOf(comboBox_entryType.getValue()).equalsIgnoreCase("Old")))){
                if((nameChecker==true && (String.valueOf(comboBox_subAsset.getValue()).equalsIgnoreCase("New"))) || (nameChecker==false && (String.valueOf(comboBox_subAsset.getValue()).equalsIgnoreCase("Old")))){
           //for editing the selected row        
           if(checking == false){
            //System.out.println("checking conditon for edit option");
            // Not editable when assets is used in wastage
           // Integer row = 0;
           // table table = dataSubAssetsDetails.get(row);
                
             
           // Integer quantity = table.tableView_Quantity.get();
                 
          // Integer remaining = table.tableView_Remainingquantity.get();
            //if(quantity==remaining){
        editRemove();
         //}else{
            
            // msg.alertMessage(Alert.AlertType.ERROR, "ERROR", " Asset selected is in use!!");
            
           // }
            
            
        }
            String sql1;
            con= (Connection) dbc.connection();
            //when entry type equals new, data is inserted into assets table 
            if(String.valueOf(comboBox_entryType.getValue()).equalsIgnoreCase("New")){
                 pst = (PreparedStatement) con.prepareStatement("insert into tbl_assets values(?,?,?,?,?)");  
                // System.out.println("value of checker is true.....");
               
                 pst.setString(1, textField_Id.getText());
            

             
                pst.setString(2, textField_AssetsName.getText());
                pst.setString(3, String.valueOf(comboBox_Type.getValue()));
                
                pst.setInt(4, Integer.valueOf(textField_Quantity.getText()));
                pst.setDouble(5, Double.valueOf(label_Totalamount.getText()));
                int p=pst.executeUpdate();
                
                 
             }
             else{
                //when entry type is old, amount and remaining quantity are updated in assets table 
                 String global_id = textField_Id.getText();
                Integer Remquantity=0;
                 Double Totalamount= 0.0 ;
                 //System.out.println("running update");
                Double amount = Double.parseDouble(label_Totalamount.getText());
                Integer remain = 0;
                String sql = "SELECT * FROM `tbl_assets` WHERE  `id`='"+global_id+"'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                //System.out.println(global_id);
                rs = pst.executeQuery(sql);
                while(rs.next()){
                    Totalamount = rs.getDouble("amount");
                     Remquantity= rs.getInt("remaining_quantity");
                }
                amount += Totalamount;
                remain = Remquantity+Integer.parseInt(textField_Quantity.getText()); 
                //System.out.println(global_id);
                sql="UPDATE `tbl_assets` SET `remaining_quantity`= '"+remain+"', `amount`= '"+amount+"' WHERE `id` ='"+id+"'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                int s =pst.executeUpdate(sql);
             }
            
            //subassets entry = comboBox_SubAsset
            if(String.valueOf(comboBox_subAsset.getValue()).equals("New")){
                Integer quantity= Integer.parseInt(textField_Quantity.getText());
                Double amount = Double.parseDouble(label_Totalamount.getText());
                String sql = "INSERT INTO `tbl_subassets`(`id`, `name`, `quantity`, `amount`) VALUES ('"+id+"', '"+name+"', '"+quantity+"', '"+amount+"')";
                pst = (PreparedStatement) con.prepareStatement(sql);
                pst.executeUpdate();
            }//when subassets entry equals old, amount and quantity are updated in subassets table 
            if(String.valueOf(comboBox_subAsset.getValue()).equals("Old")){
                Double temp_amount = 0.0;
                Integer temp_quantity = 0;
                Integer quantity= Integer.parseInt(textField_Quantity.getText());
                Double amount = Double.parseDouble(label_Totalamount.getText());
                String sql = "SELECT * FROM `tbl_subassets` WHERE `name`= '"+name+"'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                rs= pst.executeQuery(sql);
                while(rs.next()){
                    temp_amount = rs.getDouble("amount");
                    temp_quantity = rs.getInt("quantity");
                    break;
                }
                quantity += temp_quantity;
                amount += temp_amount;
                sql = "UPDATE `tbl_subassets` SET `quantity`= '"+quantity+"',`amount`= '"+amount+"' WHERE `name` = '"+name+"'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                pst.executeUpdate();
            }
            
            
             sql1 = "insert into `tbl_subassets_details` values(?,?,?,?,?,?,?)";
            pst = (PreparedStatement) con.prepareStatement(sql1);
             
            
            pst.setString(1, id);
            
            pst.setString(2, name);
            Database db = new Database();
            pst.setString(3, String.valueOf (db.getCurrentDate()));
            
            pst.setDouble(4, Double.valueOf(textField_Price.getText()));
              
            pst.setInt(5, Integer.valueOf(textField_Quantity.getText())); 
              
            pst.setDouble(6, Double.valueOf(label_Totalamount.getText())); 
            
            pst.setInt(7, Integer.valueOf(textField_Quantity.getText()));
              
            pst.executeUpdate();
            
            // for entry of subassets details values  into cash in hand
            Double pricee = Double.parseDouble(label_Totalamount.getText());
            String date= String.valueOf(db.getCurrentDate());
            sqlForCashinhand cash = new sqlForCashinhand();
            cash.insertInCashInhand(date, name+": "+ id,pricee, null);
            /*
            String sql="SELECT * FROM `tbl_subassets_details` WHERE `id`='"+id+"'";
            pst=con.prepareStatement(sql);
            rs=pst.executeQuery(sql);
            while (rs.next()){
            Remquantity=rs.getInt("quantity");
            //Totalamount=rs.getDouble("total_amount");
              
            }*/
          
            
            //pst.setInt(7, Integer.valueOf(Remquantity));
            
             //pst = (PreparedStatement) con.prepareStatement("UPDATE `tbl_assets_details` SET `remaining_quantity`='"+Remquantity+"' WHERE `id`='"+id+"'");
              
             //int i =pst.executeUpdate();
             
             //TblUpAssets table = new TblUpAssets();
             //int count = 0;
             //boolean checker = true;
             /*int max = Tbl_UpAssets.getItems().size();
             
             System.out.println("max value="+max);
             while(count<=max-1){
                 table = dataAssets.get(count);
                 System.out.println();
                 String value_id = table.tableColumn_IdUp.get();
                 if(value_id.equals(textField_Id.getText())){
                     checker=false;
                     break;
                 }
                 count++;
             }*/
             
             
                
              show_tblup_data();
              table_SubAssets.getItems().clear();
              table_SubAssetsDetails.getItems().clear();
              showSubAssets(false,id);
              show_tbl1_data(false,name);
              
              defaultValue();
                }
                else{
                    msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "SubAssets Name Error");
                }
        }
            else{
                    msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Assets Id Error");
                }
                }
        else{
                    msg.alertMessage(Alert.AlertType.ERROR, "ERROR", "Assets Name Error");
                }
        
        } catch (Exception e) {
          e.printStackTrace();
           //System.out.println("Error");
        }
    }
    
    public void onAssetsIdWrite(){
         //when entry type equals old, name of the asset appears on textfield asset name which is pulled from assets table when id is entered
        if(String.valueOf(comboBox_entryType.getValue()).equalsIgnoreCase("Old")){
            try {
                boolean check = false;
                String id =  textField_Id.getText();
                String name = "";
                String sql = "SELECT * FROM `tbl_assets` WHERE `id`= '"+id+"'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while(rs.next()){
                    name = rs.getString("name");
                    check = true;
                }
                if(check){
                    textField_AssetsName.setText(name);
                }
                onSubAssetsNameWrite();
            } catch (Exception e) {
                System.out.println("on onAssetsIdWrite method");
                e.printStackTrace();
            }
        }
    }
    
    public void onAssetsNameWrite(){
        
        if(String.valueOf(comboBox_entryType.getValue()).equalsIgnoreCase("Old")){
            try {
                boolean check = false;
                String id =  "";
                String name = textField_AssetsName.getText();
                String sql = "SELECT * FROM `tbl_assets` WHERE `name`= '"+name+"'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while(rs.next()){
                    id = rs.getString("id");
                    name = rs.getString("name");
                    check = true;
                }
                if(check){
                    textField_Id.setText(id);
                    textField_AssetsName.setText(name);
                    textField_AssetsName.end();
                }
                onSubAssetsNameWrite();
            } catch (Exception e) {
                System.out.println("on onAssetsIdWrite method");
                e.printStackTrace();
            }
        }
    }
    
    public void onSubAssetsNameWrite(){
         try {
         //subassets entry(ComboBox_sunAsset) equals old ,subassets name comboxBox contains assets name pulled from subassets table
         if(String.valueOf(comboBox_subAsset.getValue())=="Old"){
           
                textField_subName.setVisible(false);
                comboBox_subAssetName.setVisible(true);
               comboBox_subAssetName.getItems().clear();
                String id = textField_Id.getText();
                String sql = "SELECT * FROM `tbl_subassets` WHERE  `id`= '"+id+"'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while(rs.next()){
                    
                    String name = rs.getString("name");
                    nameList.add(name);
                 
                //    System.out.println(name);
                }
        }
                else{
            textField_subName.setText("");
                        textField_subName.setVisible(true);
                comboBox_subAssetName.setVisible(false);
                        }
                comboBox_subAssetName.setItems(nameList);
                
             
            }catch (Exception e) {
                System.out.println("on onAssetsIdWrite method");
                e.printStackTrace();
        }
    }
    
    public void useTblUpForTblDown(){
        getSelected();
        rowval1 = rowvalue;
        showSubAssets(true,null);
        //show_tbl1_data();
    }
   
       // Displays subassets details table 
       public void show_tbl1_data(boolean checker,String global_name) {
        try {
            if(checker){
            getSelected();
            Integer row = null;
            row = rowval2 - 1;
            global_name=null;
            if(row > -1){
              
            table tbl1 = dataSubAssets.get(row);
                   global_name = tbl1.s2.get();
            }
            }
            if(global_name !=null){
                String from_date = String.valueOf(dateFrom.getValue());
            String to_date = String.valueOf(dateTo.getValue());
            String sql = "select * from tbl_subassets_details where `name` = '"+global_name+"' AND `date` >= '" + from_date + "' And `date` <= '" + to_date + "' ORDER BY `date` DESC";
            //String sql2 = "SELECT * FROM `tbl_employee_details` AS `ed`,`tbl_employee_expenses`AS`ee` WHERE ed.Eid = ee.Eid";
            System.out.println("PreparedStatement created Successfully");

            con = dbc.connection();
           // pst = (PreparedStatement) con.prepareStatement(sql2);
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            //ResultSet rs2 = ps.executeQuery(sql2);
            tableView_Id.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            tableView_Name.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            tableView_Date.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            tableView_Costprice.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            tableView_Quantity.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            tableView_Totalamount.setCellValueFactory(new PropertyValueFactory<table, Double>("d3"));
            tableView_Remainingquantity.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));

            table_SubAssetsDetails.getItems().clear();
            
           while(rs.next()){
               table tbl = new table();
              //Tbl_for_assets tbl = new table();
               
               tbl.s2.set(rs.getString("id"));
               tbl.s3.set(rs.getString("name"));
               tbl.s1.set(rs.getString("date"));
               tbl.d1.set(rs.getFloat("cost_price"));
               tbl.i1.set(rs.getInt("quantity"));
               tbl.d3.set(rs.getDouble("total_amount"));
               tbl.i2.set(rs.getInt("remaining_quantity"));
                
               
              dataSubAssetsDetails.add(tbl);
           }
                  table_SubAssetsDetails.setItems(dataSubAssetsDetails);
                
                    

            }
            } catch (Exception e) {
            System.out.println("on show lower table");
            e.printStackTrace();
            }

            }
       
       public void comboBoxFunction(){
           show_tblup_data();
           table_SubAssets.getItems().clear();
           table_SubAssetsDetails.getItems().clear();
           
       }
       
       public void cancelButtonFunction(ActionEvent event){
           defaultValue();
       }
       
       public void defaultValue(){
           comboBox_Type.setValue("Current");
           comboBox_entryType.setValue("Old");
           textField_Id.clear();
           textField_AssetsName.clear();
           comboBox_subAsset.setValue("Old");
           textField_subName.clear();
           textField_Price.clear();
           textField_Quantity.clear();
           label_Totalamount.setText("0");
           comboBox_subAssetName.getItems().clear();
           comboBox_subAssetName.setValue("");
           checking = true;
       }

       
   
       //Displays assets table 
        public void show_tblup_data() {
        try {
            String viewBy= (String) comboBox_View.getValue(); 
                 // viewBy= (String) comboBox_View.getValue();
           // System.out.println(comboBox_View.getValue());
            
            String sql = "select * from tbl_assets where type = '"+viewBy+"'";
            if(viewBy=="All"){
                sql = "select * from tbl_assets";
            }
            //String sql2 = "SELECT * FROM `tbl_employee_details` AS `ed`,`tbl_employee_expenses`AS`ee` WHERE ed.Eid = ee.Eid";
            
            
            Integer temp_sn= 1;

            con = dbc.connection();
           // pst = (PreparedStatement) con.prepareStatement(sql2);
            pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            //ResultSet rs2 = ps.executeQuery(sql2);
            tableColumn_Sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            tableColumn_IdUp.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            tableColumn_NameUp.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            tableColumn_TypeUp.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            tableColumn_RemainingquantityUp.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
            tableColumn_AmountUp.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
           

           Tbl_UpAssets.getItems().clear();
            
           while(rs.next()){
               table tbl = new table();
               //TblUpAssets tbl = new TblUpAssets();
               
               tbl.i1.set(temp_sn++);
               tbl.s1.set(rs.getString("id"));
               tbl.s2.set(rs.getString("name"));
               tbl.s3.set(rs.getString("type"));
               tbl.i2.set(rs.getInt("remaining_quantity"));
               tbl.d1.set(rs.getDouble("amount"));
               
               
               dataAssets.add(tbl);
           }
                 Tbl_UpAssets.setItems(dataAssets);
                
            if(String.valueOf(comboBox_subAsset.getValue())=="Old"){
           
                textField_subName.setVisible(false);
                comboBox_subAssetName.setVisible(true);
               comboBox_subAssetName.getItems().clear();}
            else{
                textField_subName.setVisible(true);
                comboBox_subAssetName.setVisible(false);
            }         

                
            } catch (Exception e) {
                e.printStackTrace();
        }

            }
        
        
        
        public void getSelectedSubAssetsDetails(){
            try {
            tableView_Id.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            tableView_Name.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            tableView_Date.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            tableView_Costprice.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            tableView_Quantity.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            tableView_Totalamount.setCellValueFactory(new PropertyValueFactory<table, Double>("d3"));
            tableView_Remainingquantity.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));

            table_SubAssetsDetails.setItems(dataSubAssetsDetails);

            table_SubAssetsDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                    
                     rowvalue3 = (dataSubAssetsDetails.indexOf(newValue)+1);
                              // System.out.println("rowvalue " +rowvalue3);
                          //System.out.println("glOBLA"+global_id);

             
                    
                                
//System.out.println("ON RowValue is : " + rowvalue);
                }

            });
        } catch (Exception e) {
            System.out.println("on getSelected method of payrollController.java");
            e.printStackTrace();
        }
        }
        
        
        
     public void DeleteFunction(ActionEvent event){
         //System.out.println("row value");
         getSelectedSubAssetsDetails();
         remove();
     }
     
     
     //gets the selected row values into relevant field for editing 
     public void editButtonFunction(ActionEvent event){
         
         
             try {
                 
                 getSelectedSubAssetsDetails();
         Integer row = rowvalue3-1;
         if(row!=-1){
                 table table = dataSubAssetsDetails.get(row);
                 // selected row can be edited only if quantity and remaining quantity are equal
                 if(table.i1.get()==table.i2.get()){
                 comboBox_entryType.setValue("Old");
                 con = dbc.connection();
                 textField_Id.setText(table.s2.get());
                 pst = (PreparedStatement) con.prepareStatement("SELECT * FROM `tbl_assets` WHERE `id` ='"+textField_Id.getText()+"'");
                 rs = pst.executeQuery();
                 while(rs.next()){
                    textField_AssetsName.setText(rs.getString("name"));    
                 }
                 comboBox_subAsset.setValue("Old");
                 comboBox_subAssetName.setValue(table.s3.get());
                 textField_Price.setText(String.valueOf(table.d1.get()));
                 textField_Quantity.setText(String.valueOf(table.i1.get()));
                 label_Totalamount.setText(String.valueOf(table.d3.get()));
                 
                 checking = false;// checking (flag) used to edit selected row via save button 
                 }
                 else{
                     msg.alertMessage(Alert.AlertType.ERROR, "ERROR", " Asset selected is in use!!");
                 }
             }
             }catch (SQLException ex) {
                 System.out.println("on editButtonFunction method of FXMLDocumentController.java");
             }
         
         
                 
     }
     
     
     public void remove(){
         
             try {
                 boolean var =  msg.alertOptionMessage("DELETE", "Are you sure?");
                 if (var){
                     
                 Integer row = rowvalue3-1;
                 if(row!=-1){
                 
                 //System.out.println(row);
                 table table = dataSubAssetsDetails.get(row);
                 String id = table.s2.get();
                 String name = table.s3.get();
                 String date = table.s1.get();
                 Integer quantity = table.i1.get();
                 Double costPrice = table.d1.get();
                 Double amount = table.d3.get();
                 Integer remaining = table.i2.get();
                 // if value of quantity and remaining quantity is not equal, deletion cannot performed
                 //since the assets is in use in wastage & repair
                 if(quantity==remaining){
                 con = dbc.connection();
                 String sql = "DELETE FROM `tbl_subassets_details` WHERE `id` ='"+id+"' AND `name`='"+name+"' AND `date`= '"+date+"' AND `cost_price` = '"+costPrice+"' AND `quantity` = '"+quantity+"' AND `total_amount` = '"+amount+"' AND `remaining_quantity` = '"+remaining+"' LIMIT 1";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
                  //deletes subassets details entry from cash in hand
                  sqlForCashinhand cash = new sqlForCashinhand();
                  cash.deleteInCashInhand(date, name+": "+ id,amount, null);
                 
                Integer temp_quantity = 0;
                Double temp_amount=0.0;
                 sql ="SELECT * FROM `tbl_subassets` WHERE `name`='"+name+"'";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 rs = pst.executeQuery(sql);
                 while(rs.next()){
                     temp_quantity = rs.getInt("quantity");
                     temp_amount = rs.getDouble("amount");
                 }// quantity and amount are subtracted(subassets details table and subassets table)
                 //and updated in subassets table 
                 temp_quantity -=remaining;
                 temp_amount -= (remaining * costPrice);
                 sql = "UPDATE `tbl_subassets` SET `quantity`= '"+temp_quantity+"',`amount`= '"+temp_amount+"' WHERE `name`= '"+name+"'";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
                 
                temp_quantity = 0;
                temp_amount=0.0;
                sql ="SELECT * FROM `tbl_assets` WHERE `id` = '"+id+"'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                 rs = pst.executeQuery(sql);
                 while(rs.next()){
                     temp_quantity = rs.getInt("remaining_quantity");
                     temp_amount = rs.getDouble("amount");
                 }
                 //again quantity and amount are subtracted(subassets details table and assets table)
                 //and updated in assets table
                 temp_quantity -=remaining;
                 temp_amount -= (remaining * costPrice);
                 sql = "UPDATE `tbl_assets` SET `remaining_quantity`= '"+temp_quantity+"', `amount` = '"+temp_amount+"' WHERE `id`= '"+id+"'";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
                 
                 showSubAssets(false,id);
                 show_tbl1_data(false,name);
                 show_tblup_data();
                 //usingSubAssetsToShowSubAssetsDetails();
                 }
                 else{
                 msg.alertMessage(Alert.AlertType.ERROR, "ERROR", " Asset selected is in use!!");
                 
                 }
                 }
                 }
             } catch (Exception e) {
                 System.out.println("on remove method");
                 e.printStackTrace();
             }
         
     }
      
     public void editRemove(){
     
      
         
             try {
                 
                
                 Integer row = rowvalue3-1;
                 if(row!=-1){
                 
                 System.out.println(row);
                 table table = dataSubAssetsDetails.get(row);
                 String id = table.s2.get();
                 String name = table.s3.get();
                 String date = table.s1.get();
                 Integer quantity = table.i1.get();
                 Double costPrice = table.d1.get();
                 Double amount = table.d3.get();
                 Integer remaining = table.i2.get();
                 
                 con = dbc.connection();
                 String sql = "DELETE FROM `tbl_subassets_details` WHERE `id` ='"+id+"' AND `name`='"+name+"' AND `date`= '"+date+"' AND `cost_price` = '"+costPrice+"' AND `quantity` = '"+quantity+"' AND `total_amount` = '"+amount+"' AND `remaining_quantity` = '"+remaining+"' LIMIT 1";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
                  //deletes subassets details entry from cash in hand
                  sqlForCashinhand cash = new sqlForCashinhand();
                  cash.deleteInCashInhand(date, name+": "+ id,amount, null);
                 
                 
                Integer temp_quantity = 0;
                Double temp_amount=0.0;
                 sql ="SELECT * FROM `tbl_subassets` WHERE `name`='"+name+"'";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 rs = pst.executeQuery(sql);
                 while(rs.next()){
                     temp_quantity = rs.getInt("quantity");
                     temp_amount = rs.getDouble("amount");
                 }
                 // quantity and amount are subtracted(subassets details table and subassets table)
                 //and updated in subassets table
                 temp_quantity -=quantity;
                 temp_amount -= amount;
                 sql = "UPDATE `tbl_subassets` SET `quantity`= '"+temp_quantity+"',`amount`= '"+temp_amount+"' WHERE `name`= '"+name+"'";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
                 
                temp_quantity = 0;
                temp_amount=0.0;
                sql ="SELECT * FROM `tbl_assets` WHERE `id` = '"+id+"'";
                pst = (PreparedStatement) con.prepareStatement(sql);
                 rs = pst.executeQuery(sql);
                 while(rs.next()){
                     temp_quantity = rs.getInt("remaining_quantity");
                     temp_amount = rs.getDouble("amount");
                 }
                 //again quantity and amount are subtracted(subassets details table and assets table)
                 //and updated in assets table
                 temp_quantity -=quantity;
                 temp_amount -= amount;
                 sql = "UPDATE `tbl_assets` SET `remaining_quantity`= '"+temp_quantity+"', `amount` = '"+temp_amount+"' WHERE `id`= '"+id+"'";
                 pst = (PreparedStatement) con.prepareStatement(sql);
                 pst.executeUpdate(sql);
                 
                 showSubAssets(false,id);
                 show_tbl1_data(false,name);
                 show_tblup_data();
                 
                 }
                 
             } catch (Exception e) {
                 System.out.println("on remove method");
                 e.printStackTrace();
             }
         
     }
     
     
   
      public void getSelected() {
        try {
//            tableColumn_Sn.setCellValueFactory(new PropertyValueFactory<TblUpAssets, Integer>("tableColumn_Sn"));
//            tableColumn_IdUp.setCellValueFactory(new PropertyValueFactory<TblUpAssets, String>("tableColumn_IdUp"));
//            tableColumn_NameUp.setCellValueFactory(new PropertyValueFactory<TblUpAssets, String>("tableColumn_NameUp"));
//            tableColumn_TypeUp.setCellValueFactory(new PropertyValueFactory<TblUpAssets, String>("tableColumn_TypeUp"));
//            tableColumn_RemainingquantityUp.setCellValueFactory(new PropertyValueFactory<TblUpAssets, Integer>("tableColumn_RemainingquantityUp"));
//            tableColumn_AmountUp.setCellValueFactory(new PropertyValueFactory<TblUpAssets, Double>("tableColumn_AmountUp"));

            
            tableColumn_Sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
            tableColumn_IdUp.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
            tableColumn_NameUp.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
            tableColumn_TypeUp.setCellValueFactory(new PropertyValueFactory<table, String>("s3"));
            tableColumn_RemainingquantityUp.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
            tableColumn_AmountUp.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
            Tbl_UpAssets.setItems(dataAssets);

            Tbl_UpAssets.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                    
                     rowvalue = (dataAssets.indexOf(newValue))+1;
                               //System.out.println("rowvalue " +rowvalue);
                          //System.out.println("glOBLA"+global_id);

             
                    
                                
//System.out.println("ON RowValue is : " + rowvalue);
                }

            });
        } catch (Exception e) {
            System.out.println("on getSelected method of payrollController.java");
            e.printStackTrace();
        }
    }    
      public void getSelectedSubAssetsTable(){
            try {
//       column_Sn.setCellValueFactory(new PropertyValueFactory<SubAssets, Integer>("column_Sn"));
//       column_Id.setCellValueFactory(new PropertyValueFactory<SubAssets, String>("column_Id"));
//       column_Name.setCellValueFactory(new PropertyValueFactory<SubAssets, String>("column_Name"));
//       column_Quantity.setCellValueFactory(new PropertyValueFactory<SubAssets, Integer>("column_Quantity"));
//       column_Amount.setCellValueFactory(new PropertyValueFactory<SubAssets, Double>("column_Amount"));

       column_Sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
       column_Id.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
       column_Name.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
       column_Quantity.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
       column_Amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
       table_SubAssets.setItems(dataSubAssets);

            table_SubAssets.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

                @Override
                public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                    rowvalue2 = (dataSubAssets.indexOf(newValue)+1);
                }

            });
        } catch (Exception e) {
            System.out.println("on getSelected method of payrollController.java");
            e.printStackTrace();
        } 
      }
       
     
      
      
    
   public void initialDate(){
   //dateFrom.setValue(NOW_LOCAL_DATEFirstDate());
 // dateTo.setValue( NOW_LOCAL_DATEcurrentDate());
        Database db = new Database();
        dateFrom.setValue(db.NOW_LOCAL_DATEfirstDateOfMonth());
        dateTo.setValue(db.NOW_LOCAL_DATEcurrentDate());
   
   
   } 
      
   public void usingSubAssetsToShowSubAssetsDetails(){
       getSelectedSubAssetsTable();
       rowval2 = rowvalue2;
       //showSubAssets();
       show_tbl1_data(true,null);
   }
   //Displays subassets table 
   public void showSubAssets(boolean checker,String id){
      
      
       try{
           if(checker){
       Integer row = null;
      id = null;
      row = rowval1-1;
       if(row!=-1){
           
           table table = dataAssets.get(row);
           id = table.s1.get();
       }
       }
//       column_Sn.setCellValueFactory(new PropertyValueFactory<SubAssets, Integer>("column_Sn"));
//       column_Id.setCellValueFactory(new PropertyValueFactory<SubAssets, String>("column_Id"));
//       column_Name.setCellValueFactory(new PropertyValueFactory<SubAssets, String>("column_Name"));
//       column_Quantity.setCellValueFactory(new PropertyValueFactory<SubAssets, Integer>("column_Quantity"));
//       column_Amount.setCellValueFactory(new PropertyValueFactory<SubAssets, Double>("column_Amount"));
//       table_SubAssets.getItems().clear();
       
       column_Sn.setCellValueFactory(new PropertyValueFactory<table, Integer>("i1"));
       column_Id.setCellValueFactory(new PropertyValueFactory<table, String>("s1"));
       column_Name.setCellValueFactory(new PropertyValueFactory<table, String>("s2"));
       column_Quantity.setCellValueFactory(new PropertyValueFactory<table, Integer>("i2"));
       column_Amount.setCellValueFactory(new PropertyValueFactory<table, Double>("d1"));
       table_SubAssets.getItems().clear();
       
       if(id!=null){
           String sql = "SELECT * FROM `tbl_subassets` WHERE `id` ='"+id+"'";
           con = dbc.connection();
           pst = (PreparedStatement) con.prepareStatement(sql);
           rs = pst.executeQuery(sql);
           
           int temp_sn = 1;
           
           while(rs.next()){
               table tbl = new table();
               tbl.i1.set(temp_sn++);
               tbl.s1.set(rs.getString("id"));
               tbl.s2.set(rs.getString("name"));
               tbl.i2.set(rs.getInt("quantity"));
               tbl.d1.set(rs.getDouble("amount"));
               
               dataSubAssets.add(tbl);
           }
           table_SubAssets.setItems(dataSubAssets);
           getSelectedSubAssetsDetails();
   }
       }catch(Exception e){
           System.out.println("in showsubAssets of fxmlDocumentController.java");
           e.printStackTrace();
       }
   }
      
       
       
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialDate();
        comboBoxFunctionList();
        show_tblup_data();
        getSelected();
        getSelectedSubAssetsTable();
        getSelectedSubAssetsDetails();
        
       // System.out.println("total number of row = "+Tbl_UpAssets.getItems().size());
    }

    }

