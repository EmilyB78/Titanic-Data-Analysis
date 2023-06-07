package controller;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointments;
//import model.Contacts;
//import model.Countries;
//import model.Customers;
//import model.FirstLevelDiv;
//import model.Reports;
import model.User;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class and method to control main menu with access to appointments, customers, and reports screens.
 */
public class MainMenuController implements Initializable {

       Stage stage;
       Parent scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Method to access appointment screen with button press
     * @param event
     * @throws IOException
     */
       @FXML
       void onActionDisplayAppointments(ActionEvent event) throws IOException {

           Parent one = FXMLLoader.load(getClass().getResource("/view/AppointmentsScreen.fxml"));
           Scene scene = new Scene(one);
           Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
           stage.setScene(scene);
           stage.show();
       }

    /**
     * Method to access customers screen with button press.
     * @param event
     * @throws IOException
     */

    @FXML
    void onActionDisplayCustomers(ActionEvent event) throws IOException {
        Parent one = FXMLLoader.load(getClass().getResource("/view/CustomersScreen.fxml"));
        Scene scene = new Scene(one);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();    }

    /**
     * Method to access reports screen with button press.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionDisplayReports(ActionEvent event) throws IOException {
        Parent one = FXMLLoader.load(getClass().getResource("/view/ReportsScreen.fxml"));
        Scene scene = new Scene(one);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();


         }

    /**
     * Method to exit program when button pressed.
     * @param event
     */

    @FXML
    void onActionExit(ActionEvent event) {

        System.exit(0);
    }


}



