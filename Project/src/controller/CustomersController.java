 package controller;

import Access.*;
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
import javafx.stage.Stage;
import SQLDatabase.SQLDBConn;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static Access.CustomersAcc.getAllCustomers;

 /**
 * Class and method to view customer data and delete or access menus to edit or add new.
 */

 public class CustomersController implements Initializable {
    @FXML private TableView<Customers> customerRecordsTable;
    @FXML private TableColumn<Customers,Integer> customerRecordsTableID;
    @FXML private TableColumn<Customers,String> customerRecordsTableName;
    @FXML private TableColumn<Customers,String> customerRecordsTableAddress;
    @FXML private TableColumn<Customers,Integer> customerRecordsTablePhone;
    @FXML private TableColumn<Customers,String> customerRecordsTableState;
    @FXML private TableColumn<Customers,String> customerRecordsTableCountry;
    @FXML private TableColumn<Customers,Integer> customerRecordsTablePostalCode;


    @FXML private Button editCustomer;
    @FXML private Button addCustomer;
    @FXML private Button deleteCustomer;
    @FXML private Button backtoMainMenu;

     /**
      * Class and method to control customer screen, diplay customer data,
      * and all customers to be added or edited.
      * @param url
      * @param resourceBundle
      */
     @Override
     public void initialize(URL url, ResourceBundle resourceBundle) {

             ObservableList<Customers> allCustomersList = getAllCustomers();

             customerRecordsTableID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
             customerRecordsTableName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
             customerRecordsTableAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
             customerRecordsTablePostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
             customerRecordsTablePhone.setCellValueFactory(new PropertyValueFactory<>("customerPhonenumber"));
             customerRecordsTableState.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
             customerRecordsTableCountry.setCellValueFactory(new PropertyValueFactory<>("CustomerCountry"));


             customerRecordsTable.setItems(allCustomersList);


         }


     /**
      * Method to go back to main menu when back to main menu button is pressed.
      * @throws IOException
      */

     @FXML
     void onActionDisplayMainMenuScreen(ActionEvent event) throws IOException {
         Parent one = FXMLLoader.load(getClass().getResource("/view/MainMenuScreen.fxml"));
         Scene scene = new Scene(one);
         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         stage.setScene(scene);
         stage.show();
     }

     /**
      * Method to go to screen to add customer when add button is pressed.
      * @param event
      * @throws IOException
      */

     @FXML
     void onActionCustomerAddScreen(ActionEvent event) throws IOException { Parent addParts = FXMLLoader.load(getClass().getResource("/view/CustomerAddScreen.fxml"));
         Scene scene = new Scene(addParts);
         Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
         window.setScene(scene);
         window.show();
     }

     /**
      * Method to go to screen to edit customer when a customer is selected and button pressed.
      * @param event
      * @throws IOException
      */
     @FXML
     void onActionDisplayCustomerEditScreen(ActionEvent event) throws IOException {
         Customers customersToModify = customerRecordsTable.getSelectionModel().getSelectedItem();

         if (customersToModify == null) {
             Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a customer.");
             Optional<ButtonType> result = alert.showAndWait();
             return;

         }


         FXMLLoader loader = new FXMLLoader();
         loader.setLocation(getClass().getResource("/view/CustomerEditScreen.fxml"));
         loader.load();

         CustomerEditController MPMController = loader.getController();
         MPMController.sendCustomers(customersToModify);

         Parent one = loader.getRoot();
         Scene scene = new Scene(one);
         Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
         stage.setScene(scene);
         stage.show();


     }

     /**
      * method to delete selected customer with appointments deleted first
      * @param event
      * @throws IOException
      */
     @FXML
     void onActionDeleteCustomer (ActionEvent event) throws IOException {

         Customers customersToDelete = customerRecordsTable.getSelectionModel().getSelectedItem();

         if (customersToDelete == null) {
             Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a customer.");
             Optional<ButtonType> result = alert.showAndWait();
             return;

         }
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you certain you wish to delete this record?");
         Optional<ButtonType> result = alert.showAndWait();

         if(result.get() ==  ButtonType.OK) {
             String sqla = "Delete from appointments where customer_ID = ?";
             String sqlc = "Delete from customers where customer_ID = ?";

             try {


                 PreparedStatement ps = SQLDBConn.getConnection().prepareStatement(sqla);
                 ps.setInt(1, customersToDelete.getCustomerID());

                 ps.execute();

                 PreparedStatement psc = SQLDBConn.getConnection().prepareStatement(sqlc);
                 psc.setInt(1, customersToDelete.getCustomerID());

                 psc.execute();

                 Alert alertc = new Alert(Alert.AlertType.INFORMATION, "You have deleted " + customersToDelete.getCustomerName());
                 Optional<ButtonType> resultc = alertc.showAndWait();


             } catch (SQLException ex) {
                 ex.printStackTrace();
             }
             customerRecordsTable.setItems(getAllCustomers());

         }
     }


     /**
      * method to display custom message for errors and validation input
      * @param alertType
      */
     private void displayAlert(int alertType) {

             Alert alert = new Alert(Alert.AlertType.INFORMATION);
             Alert alertError = new Alert(Alert.AlertType.ERROR);

             switch (alertType) {
                 case 1:
                     alert.setTitle("Information");
                     alert.setHeaderText("Please Select a Customer Record to Edit");
                     alert.showAndWait();
                     break;
                 case 2:
                     alert.setTitle("Information");
                     alert.setHeaderText("Please Select a Customer Record to Delete");
                     alert.showAndWait();
                     break;

             }
         }
    }


