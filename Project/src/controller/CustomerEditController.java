package controller;

import Access.CountryAcc;
import Access.StateProvinceAcc;
import SQLDatabase.SQLDBConn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class to control editing and saving customer data.
 */
public class CustomerEditController implements Initializable {

    private Customers customerToEdit;

    Stage stage;
    Parent scene;
    @FXML
    private TextField customerIDEdit;
    @FXML
    private TextField customerNameEdit;
    @FXML
    private TextField customerPhoneEdit;
    @FXML
    private TextField customerAddressEdit;
    @FXML
    private ComboBox<StateProvince> customerStateEdit;
    @FXML
    private ComboBox<Countries> customerCountryEdit;
    @FXML
    private TextField customerPostalEdit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerStateEdit.setVisibleRowCount(5);
        customerCountryEdit.setItems(CountryAcc.getCountries());
        customerCountryEdit.setVisibleRowCount(5);

    }

    /**
     * Button to navigate from edit menu back to Main Customer Screen
     * @param event
     * @throws IOException
     */

    @FXML
    void onActionCustomerEditBack(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will return you to Main Customer Records without saving, do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent one = FXMLLoader.load(getClass().getResource("/view/CustomersScreen.fxml"));
            Scene scene = new Scene(one);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            stage.setScene(scene);

            stage.show();
        }
    }

    /**
     * Method to save customer data to database after it has been edited
     * and transition user back to main customer screen
     * @param event
     */


   @FXML
    void onActionCustomerEditSave(ActionEvent event) {

       // 1. Get the data from the GUI
       try {
           int id = Integer.parseInt(customerIDEdit.getText());

           String name = customerNameEdit.getText();
           String phone = customerPhoneEdit.getText();
           String address = customerAddressEdit.getText();
           StateProvince state = customerStateEdit.getValue();
           String postalCode = customerPostalEdit.getText();

           // 2. Validate the data
           if (name.isEmpty()) {
               Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter customer name.");
               Optional<ButtonType> result = alert.showAndWait();
               return;
           }
           if (phone.isEmpty()) {
               Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter customer phone number.");
               Optional<ButtonType> result = alert.showAndWait();
               return;
           }
           if (address.isEmpty()) {
               Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter customer address.");
               Optional<ButtonType> result = alert.showAndWait();
               return;
           }
           if (state == null) {
               Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a state/province.");
               Optional<ButtonType> result = alert.showAndWait();
               return;
           }
           if (postalCode.isEmpty()) {
               Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter customer postal code.");
               Optional<ButtonType> result = alert.showAndWait();
               return;
           }


           // 3. Insert data into data base

           String sql = "UPDATE customers SET customer_name = ?, address = ?, postal_code = ?, phone = ?, division_ID = ? WHERE customer_ID = ?";

           PreparedStatement ps = SQLDBConn.getConnection().prepareStatement(sql);
           ps.setString(1, name);
           ps.setString(2, address);
           ps.setString(3, postalCode);
           ps.setString(4, phone);
           ps.setInt(5, state.getDivisionID());
           ps.setInt(6, id);

           ps.execute();


           // 4. Switch to main screen

           Parent root = FXMLLoader.load(getClass().getResource("/view/CustomersScreen.fxml"));
           Scene scene = new Scene(root);
           Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

           stage.setScene(scene);

           stage.show();

       } catch (SQLException | IOException ex) {
           ex.printStackTrace();


       }
       return;
   }


    /**
     * Fill Combo Box with State/Province information
     * @param event
     * @throws SQLException
     */

    @FXML
    void onActionCustomerStateEdit(ActionEvent event) throws SQLException {

        StringBuilder cs = new StringBuilder("");

    }

    /**
     * Fill Combo Box with Country Information
     * @param event
     * @throws SQLException
     */

     public void onActionCustomerCountryEdit(ActionEvent event) throws SQLException {

         Countries c = customerCountryEdit.getValue();
         System.out.println("Selected Country is.." + c.getCountryName());

         customerStateEdit.setItems(StateProvinceAcc.getAllFirstLevelDiv(c.getCountryID()));

    }

    /**
     * Method to send edited customer information to database.
     * @param customersToModify
     */
    public void sendCustomers(Customers customersToModify) {


         customerIDEdit.setText(String.valueOf(customersToModify.getCustomerID()));
         customerNameEdit.setText(customersToModify.getCustomerName());
         customerPhoneEdit.setText(customersToModify.getCustomerPhonenumber());
         customerAddressEdit.setText(customersToModify.getCustomerAddress());
         customerPostalEdit.setText(customersToModify.getCustomerPostalCode());

         //customerCountryEdit.setText

         for(Countries c : customerCountryEdit.getItems()){

             if(c.getCountryID() == customersToModify.getCountryID()){
                 customerCountryEdit.setValue(c);
                 break;
             }
         }
        customerStateEdit.setItems(StateProvinceAcc.getAllFirstLevelDiv(customersToModify.getCountryID()));

         for(StateProvince sp : customerStateEdit.getItems()){

             if(sp.getDivisionID() == customersToModify.getDivisionID()){
                 customerStateEdit.setValue(sp);
                 break;
             }
         }


    }
}



