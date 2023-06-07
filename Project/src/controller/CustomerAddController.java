package controller;

import Access.CountryAcc;
import Access.CustomersAcc;
import Access.StateProvinceAcc;
import SQLDatabase.SQLDBConn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Countries;
import model.Customers;
import model.StateProvince;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class to control adding and saving new customer data.
 */
public class CustomerAddController implements Initializable {
    private Customers customerToAdd;

    Stage stage;
    Parent scene;

    @FXML
    private TextField customerIDAdd;
    @FXML
    private TextField customerNameAdd;
    @FXML
    private TextField customerPhoneAdd;
    @FXML
    private TextField customerAddressAdd;
    @FXML
    private ComboBox<StateProvince> customerStateAdd;
    @FXML
    private ComboBox<Countries> customerCountryAdd;
    @FXML
    private TextField customerPostalAdd;
    @FXML
    private Button customerAddBack;
    @FXML
    private Button customerAddSave;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        customerStateAdd.setVisibleRowCount(5);
        customerStateAdd.setPromptText("Please Select a State/Province...");


        customerCountryAdd.setItems(CountryAcc.getCountries());
        customerCountryAdd.setVisibleRowCount(5);
        customerCountryAdd.setPromptText("Please Select a Country...");



    }

    /**
     * Fill Combo Box with State/Province Information
     * @param event
     * @throws IOException
     */

    public void onActionCustomerStateAdd (ActionEvent event) throws IOException {

        StringBuilder cs = new StringBuilder("");

    }

    /**
     * Fill Combo Box with Country Information
     * @param event
     * @throws IOException
     */
    public void onActionCustomerCountryAdd (ActionEvent event) throws IOException {

        Countries c = customerCountryAdd.getValue();
        System.out.println("Selected Country is.." + c.getCountryName());

       customerStateAdd.setItems(StateProvinceAcc.getAllFirstLevelDiv(c.getCountryID()));
    }

    /**
     * Method to return to the main customer screen with button push.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionCustomerAddBack(ActionEvent event) throws IOException {

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
     * Method to save new customer data to database after it has been added
     * and transition user back to main customer screen
     * @param event
     * @throws IOException
     */


    @FXML
    public void onActionCustomerAddSave(ActionEvent event) throws IOException {

        // 1. Get the data from the GUI
        try {

            String name = customerNameAdd.getText();
            String phone = customerPhoneAdd.getText();
            String address = customerAddressAdd.getText();
            StateProvince state = customerStateAdd.getValue();
            String postalCode = customerPostalAdd.getText();

            // 2. Validate the data
            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter customer name.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
            if (phone.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter customer phone number.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
            if (address.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter customer address.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
            if(state == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a state/province.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
            if (postalCode.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter customer postal code.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }


            // 3. Insert data into data base

            String sql = "INSERT into customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID)" + "VALUES (NULL, ?,?,?,?,?)";

            PreparedStatement ps = SQLDBConn.getConnection().prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, state.getDivisionID());

            ps.execute();


            // 4. Switch to main screen

            Parent root = FXMLLoader.load(getClass().getResource("/view/CustomersScreen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            stage.setScene(scene);

            stage.show();

        } catch (SQLException ex) {
            ex.printStackTrace();



        }
        return;
    }


}






