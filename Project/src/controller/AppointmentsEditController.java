package controller;

import Access.AppointmentsAcc;
import Access.ContactsAcc;
import Access.CustomersAcc;
import Access.UserAcc;
import SQLDatabase.SQLDBConn;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;


import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Class to control editing and saving appointment data.
 */

public class AppointmentsEditController implements Initializable {

    public DatePicker editAppointmentStartDate;
    public ComboBox<LocalTime> editAppointmentStartTime;
    public ComboBox<LocalTime>editAppointmentEndTime;
    public TextField editAppointmentLocation;
    public Button appointmentsEditBack;
    private Appointments appointmentsToModify;

    Stage stage;
    Parent scene;
    @FXML
    private TextField editAppointmentID;
    @FXML
    private TextField editAppointmentTitle;
    @FXML
    private TextField editAppointmentDescription;
    @FXML
    private TextField editAppointmentType;
    @FXML
    private ComboBox<Contacts> editAppointmentContact;
    @FXML
    private ComboBox<Customers> editAppointmentCustID ;
    @FXML
    private ComboBox<User>editAppointmentUserID;

    /**
     * lamba overrides display of customer ID combo box
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Callback<ListView<Customers>, ListCell<Customers>> factory = lv -> new ListCell<Customers>() {
        @Override
        protected void updateItem(Customers item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty ? "Nothing" : ("Use : " + item.getCustomerID()));
         }
        };
        editAppointmentCustID.setCellFactory(factory);



    }

    /**
     * Method to send edited appointment data back to the main appointment screen controller
     *  where the edited appointment is displayed. Time conversions are provided.
     * @param appointmentsToModify
     */


    public void sendAppointments(Appointments appointmentsToModify) {

        editAppointmentID.setText(String.valueOf(appointmentsToModify.getAppointmentID()));
        editAppointmentTitle.setText(appointmentsToModify.getAppointmentTitle());
        editAppointmentDescription.setText(appointmentsToModify.getAppointmentDescription());
        editAppointmentLocation.setText(appointmentsToModify.getAppointmentLocation());
        editAppointmentType.setText(appointmentsToModify.getAppointmentType());
        editAppointmentContact.setItems(ContactsAcc.getAllContacts());
        editAppointmentCustID.setItems((CustomersAcc.getAllCustomers()));
        editAppointmentUserID.setItems(UserAcc.getAllUsers());

        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);


        //make a local date time with start, use local data.now
        LocalDate nydate = LocalDate.now();
        LocalTime nytime = start;
        LocalTime nyetime = end;


        //make a zoned date time from the local date time using atzone "America/New_York"
        ZoneId nyZoneId = ZoneId.of("America/New_York");

        //make another zone date time from that zone date time using method with zone same instant
        ZonedDateTime nyZDT = ZonedDateTime.of(nydate, nytime, nyZoneId);
        ZonedDateTime nyeZDT = ZonedDateTime.of(nydate, nyetime, nyZoneId);

        //use zone ID system default
        ZoneId LocalZoneId = ZoneId.of(TimeZone.getDefault().getID());

        //convert zoned date time to a local time
        //Instant nytotxInstant = nyZDT.toInstant();
        ZonedDateTime nytoLocalZDT = nyZDT.withZoneSameInstant(LocalZoneId);
        ZonedDateTime nyetoLocalZDT = nyeZDT.withZoneSameInstant(LocalZoneId);

        start = nytoLocalZDT.toLocalTime();
        end = nyetoLocalZDT.toLocalTime();


        while (start.isBefore(end.plusSeconds(1))) {
            editAppointmentStartTime.getItems().add(start);
            editAppointmentEndTime.getItems().add(start);
            start = start.plusMinutes(15);
        }
        editAppointmentStartTime.setValue(LocalTime.of(8, 0));
        editAppointmentEndTime.setValue(LocalTime.of(22, 0));


        editAppointmentStartDate.setValue(appointmentsToModify.getStart().toLocalDate());
        editAppointmentStartTime.setValue(appointmentsToModify.getStart().toLocalTime());
        editAppointmentEndTime.setValue((appointmentsToModify.getEnd().toLocalTime()));

        for (Customers c : editAppointmentCustID.getItems()) {


            if (c.getCustomerID() == appointmentsToModify.getCustomerID()) {
                editAppointmentCustID.setValue(c);
                break;
            }
        }

        for (User u : editAppointmentUserID.getItems()) {
            if (u.getUserID() == appointmentsToModify.getUserID()) {
                editAppointmentUserID.setValue(u);
                break;
            }
        }

        for (Contacts a : editAppointmentContact.getItems()) {
            if (a.getContactsID() == appointmentsToModify.getContactID()) {
                editAppointmentContact.setValue(a);
                break;
            }
        }
    }

    /**
     * Method to exit back to main appointment screen with button press.
     * @param event
     * @throws IOException
     */

        @FXML
        void onActionAppointmentsEditBack (ActionEvent event) throws IOException {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will return you to Main Appointment Records without saving, do you want to continue?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Parent one = FXMLLoader.load(getClass().getResource("/view/AppointmentsScreen.fxml"));
                Scene scene = new Scene(one);
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

                stage.setScene(scene);

                stage.show();
            }
        }

    /**
     * Method to verify the edited appointment data is complete, check for appointment overlaps, send data
     * to database, and transition user back to main appointment screen. Alerts for missing information
     * or appointment overlap are provided.
     * @param event
     */

    public void onActionAppointmentsEditSave(ActionEvent event) {

        // 1. Get the data from the GUI
        try {
            int id = Integer.parseInt(editAppointmentID.getText());
            String title = editAppointmentTitle.getText();
            String description = editAppointmentDescription.getText();
            String location = editAppointmentLocation.getText();

            String type = editAppointmentType.getText();
            LocalDate startdate = editAppointmentStartDate.getValue();
            // enter start date
            LocalTime starttime = editAppointmentStartTime.getValue();
            LocalTime endtime = editAppointmentEndTime.getValue();

            Customers customers = editAppointmentCustID.getValue();
            User users = editAppointmentUserID.getValue();
            Contacts contacts = editAppointmentContact.getValue();




            if (customers == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a customer.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
            if (users == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a user.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
            if (contacts == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a contact.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }

            if (title.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter an appointment title.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
            if (description.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter an appointment description.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
            if (location.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter an appointment location.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }

            if (type.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter an appointment type.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
            if (startdate == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a start date.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }

            if (starttime == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a start time.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
            if (endtime == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an end time.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }

            LocalDateTime start = LocalDateTime.of(startdate,starttime);
            LocalDateTime end = LocalDateTime.of(startdate, endtime);

            if(!start.isBefore(end)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Start time must be before end time.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }
            //validate for overlap based on cust Id
            ObservableList<Appointments> alllist = AppointmentsAcc.getAllAppointments();
            boolean overlap = false;
            for(Appointments appointments:alllist) {
                if (appointments.getCustomerID() != customers.getCustomerID()) {
                    continue;
                }
                if(appointments.getAppointmentID() == id) {
                    continue;
                }
                LocalDateTime astart = appointments.getStart();
                LocalDateTime aend = appointments.getEnd();
                if(astart.isAfter(start) && astart.isBefore(end))
                    overlap = true;
                else if(astart.equals(start) || aend.equals(end))
                    overlap = true;
                else if(aend.isAfter(start) && aend.isBefore(end))
                    overlap = true;
                else if(astart.isBefore(start) && aend.isAfter(end))
                    overlap = true;

            }
            if(overlap){
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have an overlapping appointment");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }


            // 3. Insert data into data base

            String sql = "UPDATE appointments SET title = ?, description = ?, location = ?, contact_id = ?, type = ?, start = ?, end = ?, customer_id = ?, user_id = ? WHERE appointment_ID = ?";

            PreparedStatement ps = SQLDBConn.getConnection().prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3,location);
            ps.setInt(4,contacts.getContactsID());
            ps.setString(5, type);
            ps.setTimestamp(6, Timestamp.valueOf(start));
            ps.setTimestamp(7, Timestamp.valueOf(end));
            ps.setInt(8,customers.getCustomerID() );
            ps.setInt(9,users.getUserID());
            ps.setInt(10, id);

            ps.execute();


            // 4. Switch to main screen

            Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentsScreen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            stage.setScene(scene);

            stage.show();

        } catch (SQLException | IOException ex) {
            ex.printStackTrace();


        }
        return;
    }
}







