package controller;

import SQLDatabase.SQLDBConn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;

import static Access.AppointmentsAcc.getAllAppointments;
import static Access.CustomersAcc.getAllCustomers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import model.Customers;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class and method to control main appointments screen, display appointment by month or week,
 * allow users to choose to delete an appointment. User may also edit an existing appointment
 * and add new appointments by button selection and transition to appropriate screens.
 */
public class AppointmentsController implements Initializable {


    @FXML
    private RadioButton allAppointmentRadio;
    @FXML
    private RadioButton appointmentWeekRadio;
    @FXML
    private RadioButton appointmentMonthRadio;
    @FXML
    private TableView<Appointments> allAppointmentsTable;
    @FXML
    private TableColumn<?, ?> appointmentContact;
    @FXML
    private TableColumn<?, ?> appointmentCustomerID;
    @FXML
    private TableColumn<?, ?> appointmentDescription;
    @FXML
    private TableColumn<?, ?> appointmentEnd;
    @FXML
    private TableColumn<?, ?> appointmentID;
    @FXML
    private TableColumn<?, ?> appointmentLocation;
    @FXML
    private TableColumn<?, ?> appointmentStart;
    @FXML
    private TableColumn<?, ?> appointmentTitle;
    @FXML
    private TableColumn<?, ?> appointmentType;
    @FXML
    private TableColumn<?, ?> tableUserID;
    @FXML
    private Button backToMainMenu;
    @FXML
    private Button deleteAppointment;
    @FXML
    private Button editAppointment;
    @FXML
    private Button addAppointment;


    /**
     * Initialize controls and setup variables/observable lists.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Appointments> allAppointmentsList = getAllAppointments();

        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
        tableUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));

        allAppointmentsTable.setItems(allAppointmentsList);
    }

    /**
     * method to return to main menu by button push
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        Parent one = FXMLLoader.load(getClass().getResource("/view/MainMenuScreen.fxml"));
        Scene scene = new Scene(one);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to delete an appointment.
     * @param event
     * @throws IOException
     */

    @FXML
    void onActionDeleteAppointment(ActionEvent event) throws IOException {
        Appointments appointmentsToDelete = allAppointmentsTable.getSelectionModel().getSelectedItem();

        if (appointmentsToDelete == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a customer.");
            Optional<ButtonType> result = alert.showAndWait();
            return;

        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you certain you wish to delete this appointment?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() ==  ButtonType.OK) {
            String sqla = "Delete from appointments where appointment_ID = ?";


            try {


                PreparedStatement ps = SQLDBConn.getConnection().prepareStatement(sqla);
                ps.setInt(1, appointmentsToDelete.getAppointmentID());

                ps.execute();

                //Alert to show deleted appointment ID and type

                Alert alertc = new Alert(Alert.AlertType.INFORMATION, "You have deleted appointment ID:  " + appointmentsToDelete.getAppointmentID() + "  Type: " + appointmentsToDelete.getAppointmentType());
                Optional<ButtonType> resultc = alertc.showAndWait();



            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            allAppointmentsTable.setItems(getAllAppointments());

        }

    }

    /**
     * method to select appointment to edit and move to edit screen by button push
     *
     * @param event
     * @throws IOException
     */

    @FXML
    void onActionDisplayAppointmentsEditScreen(ActionEvent event) throws IOException {

        Appointments appointmentsToModify = allAppointmentsTable.getSelectionModel().getSelectedItem();


        if (appointmentsToModify == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an appointment.");
            Optional<ButtonType> result = alert.showAndWait();
            return;

        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AppointmentsEditScreen.fxml"));
        loader.load();

        AppointmentsEditController AECController = loader.getController();
        AECController.sendAppointments(appointmentsToModify);

        Parent one = loader.getRoot();
        Scene scene = new Scene(one);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();


    }
     /**
      * Method to go to screen to add a new appointment by button push
      * @param event
      * @throws IOException
      */

    @FXML
    void onActionDisplayAppointmentsAddScreen(ActionEvent event) throws IOException {

        Parent addParts = FXMLLoader.load(getClass().getResource("/view/AppointmentsAddScreen.fxml"));
        Scene scene = new Scene(addParts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

     /**
      * Method to display custom message for errors and validation input
      * @param alertType
      */
    private void displayAlert(int alertType) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Alert alertError = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1:
                alert.setTitle("Information");
                alert.setHeaderText("Please Select an Appointment to Edit");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Information");
                alert.setHeaderText("Please Select an Appointment to Delete");
                alert.showAndWait();
                break;

        }
    }




    /**
     * Displays appointment by month when radio button for "Month" is selected.
     * @throws SQLException
     */
    @FXML
    void appointmentMonthSelected(ActionEvent event) throws SQLException {
        try {
            ObservableList<Appointments> allAppointmentsList = getAllAppointments();
            ObservableList<Appointments> appointmentsMonth = FXCollections.observableArrayList();

            LocalDateTime currentMonthStart = LocalDateTime.now().minusMonths(1);
            LocalDateTime currentMonthEnd = LocalDateTime.now().plusMonths(1);


            if (allAppointmentsList != null)
                //IDE converted to forEach
                allAppointmentsList.forEach(appointment -> {
                    if (appointment.getEnd().isAfter(currentMonthStart) && appointment.getEnd().isBefore(currentMonthEnd)) {
                        appointmentsMonth.add(appointment);
                    }
                    allAppointmentsTable.setItems(appointmentsMonth);
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Diplays appointments by week when radio button for week is selected.
     * @throws SQLException
     */
    @FXML
    void appointmentWeekSelected(ActionEvent event) throws SQLException {
        try {

            ObservableList<Appointments> allAppointmentsList = getAllAppointments();
            ObservableList<Appointments> appointmentsWeek = FXCollections.observableArrayList();

            LocalDateTime weekStart = LocalDateTime.now().minusWeeks(1);
            LocalDateTime weekEnd = LocalDateTime.now().plusWeeks(1);

            if (allAppointmentsList != null)
                //IDE converted forEach
                allAppointmentsList.forEach(appointment -> {
                    if (appointment.getEnd().isAfter(weekStart) && appointment.getEnd().isBefore(weekEnd)) {
                        appointmentsWeek.add(appointment);
                    }
                    allAppointmentsTable.setItems(appointmentsWeek);
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to reset appointment table to view all appointments
     * after montly or weekly view was selected.
     * @param event
     */

    public void viewAllAgain(ActionEvent event) {
        ObservableList<Appointments> allAppointmentsList = getAllAppointments();

        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
        tableUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));

        allAppointmentsTable.setItems(allAppointmentsList);
    }
}

