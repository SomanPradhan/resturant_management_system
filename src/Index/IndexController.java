package Index;

import UsableMethods.Message;
import br.com.supremeforever.suprememdiwindow.MDICanvas;
import br.com.supremeforever.suprememdiwindow.MDIWindow;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class IndexController implements Initializable {
       
    @FXML
     public JFXButton button_switch;
    
    MDICanvas canvas = new MDICanvas();
    
    @FXML
    AnchorPane pane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        canvas.setPrefSize(1370, 620);
        pane.getChildren().add(canvas);
    }    
    
    public void close(){
        Platform.exit();
    }
    
    public void switchFXML(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/Login.fxml"));
            
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("User Login");
            stage.sizeToScene();
            button_switch.setDisable(true);
            stage.showAndWait();
            button_switch.setDisable(false);

        }catch(Exception e){
            System.out.println("switching account");
            e.printStackTrace();
        }
    }
    
    public void salesDetailByBill(){
         try {
             
             

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Sales_ByBill/SalesByBill.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("salesDetailByBill",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Sales Detail-By Bill",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Sales Detail-By Bill ");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("salesDetailByBill");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    public void salesDetailByCategory(){
         try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Sales_ByCategory/SalesByCategory.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("salesDetailByCategory",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Sales Detail-By Category",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Sales Detail-By Category ");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("salesDetailByCategory");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    public void purchaseEntry(){
         try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PurchaseEntry/PurchaseEntry.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("purchaseEntry",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Purchase Entry",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Purchase Entry ");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("purchaseEntry");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    public void purchaseReport(){
         try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PurchaseReport/PurchaseReport.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("salesDetailByCategory",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Sales Detail-By Category",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Sales Detail-By Category ");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("salesDetailByCategory");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    public void creditSalesEntry(){
         try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Billing_Verify/CreditEntry.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("creditSalesEntry",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Credit Sales Entry",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
//            Stage stage = new Stage();
//            stage.setTitle("Credit Sales Entry");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("creditSalesEntry");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    public void creditSalesTransaction(){
         try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/credit/Credit.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("creditSalesTransaction",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Credit Sales Transaction",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Credit Sales Transaction");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("creditSalesTransaction");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    public void cashInHand(){
         try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CashInHand/FXMLDocument.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("cashInHand",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Cash In Hand",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Credit Sales Transaction");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("cashInHand");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }        
    }
 
    public void items(){
        try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Items/Items.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("items",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Items",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Credit Sales Transaction");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("Items");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    public void assetsfx(){
        try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/assetsfx/assets.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("assets",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Assets",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Credit Sales Transaction");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("assets");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    public void wastage(){
       try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/wastage/wastageRepair.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("wastageRepair",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Wastage and Repair",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Credit Sales Transaction");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("wastage and Repair");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    
    }
    
    public void creditPurchase(){
         try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CreditPurchase/CreditPurchase.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("creditPurchase",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Credit Purchase",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Credit Purchase ");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("creditPurchase");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
   
    
    public void accountSetting(){
         try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AccountSetting/FXMLAccounting.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("accountSetting",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Account Setting",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Account Setting");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("accountSetting");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    public void employeeDetails(){
         try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EmployeeDetails/Employee.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("employeeDetails",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Employee Details",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Employee Details");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("employeeDetails");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    public void employeePayroll(){
         try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Payroll/Payroll.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("payroll",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Employee PayRoll",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Employee Payroll");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("employeePayroll");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    public void employeeExpenses(){
         try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/expens/emp_expenses.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("employeeExpense",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Employee Expense",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Employee Expenses");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("employeeExpenses");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    public void expensesEntry(){
         try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/General_Expenses/ExpensesFXML.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("expensesEntry",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Expenses Entry",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Employee Expenses");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("employeeExpenses");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    public void saving(){
         try {

            //PATH STARTS FROM THE CURRENT PACKAGE IN WHICH THE CODE RESIDE NOT THE ROOT OF THE PROJECT
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/saving/FXMLDocument.fxml"));
            AnchorPane root1 = fxmlLoader.load();
            MDIWindow window = new MDIWindow("saving",new ImageView("https://upload.wikimedia.org/Animated_PNG_example_bouncing_beach_ball.png"),"Saving",root1);
            window.setPrefSize(1370, 570);
            canvas.addMDIWindow(window);
            
//            Stage stage = new Stage();
//            stage.setTitle("Employee Expenses");
//            stage.setScene(new Scene(root1));
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.show();

        } catch (Exception e) {
            System.out.println("employeeExpenses");
            e.printStackTrace();
//            m.alertMessage(Alert.AlertType.ERROR, "Cannot open Credit Window","Please Re-try Again.");
        }
    }
    
    
    
}
