package controller;

import Access.AppointmentsAcc;
import Access.ContactsAcc;
import Access.CustomersAcc;
import SQLDatabase.SQLDBConn;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.Contacts;
import model.Customers;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static Access.AppointmentsAcc.*;

public class ReportsController implements Initializable {
    public ComboBox<String> monthMonthTypeReports;
    public ComboBox<String> typeMonthTypeReports;
    public TextField totalMonthTypeReports;
    public ComboBox<Contacts> contactContactScheduleReports;
    public ComboBox<Customers> customerCustomerScheduleReports;
    public Button viewContactsScheduleReports;
    public Button viewCustomerScheduleReports;
    public TableView<Appointments> ReportsAppointments;
    public TableColumn apptIDReports;
    public TableColumn titleReports;
    public TableColumn descriptionReports;
    public TableColumn locationsReports;
    public TableColumn contactReports;
    public TableColumn typeReports;
    public TableColumn startdatetimeReports;
    public TableColumn enddatetimeReports;
    public TableColumn custIDReports;
    public TableColumn userIDReports;

    /**
     * Class and method to control and display reports data.
     * @param url
     * @param resourceBundle
     */

    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Appointments> allAppointmentsList = getAllAppointments();

        apptIDReports.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleReports.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        descriptionReports.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        locationsReports.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        typeReports.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        startdatetimeReports.setCellValueFactory(new PropertyValueFactory<>("start"));
        enddatetimeReports.setCellValueFactory(new PropertyValueFactory<>("end"));
        custIDReports.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        contactReports.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
        userIDReports.setCellValueFactory(new PropertyValueFactory<>("userID"));

        ReportsAppointments.setItems(allAppointmentsList);
        contactContactScheduleReports.setItems(ContactsAcc.getAllContacts());
        customerCustomerScheduleReports.setItems(CustomersAcc.getAllCustomers());
        monthMonthTypeReports.setItems(Appointments.monthlist);
        typeMonthTypeReports.setItems(getAllAppointmentsbyType());

    }

    /**
     * Method to prompt the user to select a contact for report display
     * @param event
     */
    public void onActionDisplayContactsSchedule(ActionEvent event) {


           Contacts contacts = contactContactScheduleReports.getValue();

            // 2. Validate the data
           if (contacts == null) {
               Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a contact.");
               Optional<ButtonType> result = alert.showAndWait();
               return;
           }

           ReportsAppointments.setItems(getAllAppointmentsbyContact(contacts.getContactsID()));




    }

    /**
     * Method to prompt a user to select a customer for report display.
     * @param event
     */

    public void onActionDisplayCustomerSchedule(ActionEvent event) {

        Customers customers = customerCustomerScheduleReports.getValue();

        // 2. Validate the data
        if (customers == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a customer.");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        ReportsAppointments.setItems(getAllAppointmentsbyCustomers(customers.getCustomerID()));




    }

    /**
     * Method to return to the main menu from the reports page.
     * @param event
     * @throws IOException
     */

    public void onActionReportsBack(ActionEvent event) throws IOException {

        Parent one = FXMLLoader.load(getClass().getResource("/view/MainMenuScreen.fxml"));
        Scene scene = new Scene(one);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to display reports by month and type.
     * @param event
     */

    public void onActionViewMonthTypeReports(ActionEvent event) {

        String month = monthMonthTypeReports.getValue();
        String type = typeMonthTypeReports.getValue();

        // 2. Validate the data
        if (month == null || type == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a month and type.");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        totalMonthTypeReports.setText(AppointmentsAcc.getmonthtypecount(month, type));




    }
}
