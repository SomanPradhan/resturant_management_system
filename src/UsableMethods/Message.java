package UsableMethods;

import java.awt.EventQueue;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.swing.JOptionPane;

public class Message {
    
    public void alertMessage(Alert.AlertType alertType, String headerText, String contentText) {
//            Alert alert = new Alert(AlertType.ERROR, contentText, buttons);
        Alert alert = new Alert(alertType, contentText, ButtonType.OK);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    public boolean alertOptionMessage(String headerText, String contentText) {
//        Alert alert = new Alert(AlertType.CONFIRMATION, contentText);
        Alert alert = new Alert(Alert.AlertType.NONE, contentText, ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(headerText);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
//            System.out.println(alert.getResult());
            return true;
        } else if (alert.getResult() == ButtonType.NO) {
//            System.out.println(alert.getResult());
            return false;
        }
        return false;
    }
    
    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = new Date();
        return dateFormat.format(date);

    }

    public void showErrorMessage(String message, String title) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void showInfoMessage(String message, String title) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public void showOptionMessage(String message, String title) {

        boolean[] result = {false};
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String[] options = {"Yes", "No"};
                int ans = JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
//                JOptionPane.setModal(false);
                if (ans == 0) {
                    result[0] = true;
                } else {
                    result[0] = false;
                }
            }
        });

    }

    

}
