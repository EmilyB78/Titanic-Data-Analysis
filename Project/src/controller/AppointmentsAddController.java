package controller;

import Access.AppointmentsAcc;
import Access.ContactsAcc;
import Access.CustomersAcc;
import Access.UserAcc;
import SQLDatabase.SQLDBConn;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Class and methods control the appointments add screen.
 */
public class AppointmentsAddController implements Initializable {

    public ComboBox<Contacts> addAppointmentContact;
    public DatePicker addAppointmentStartDate;
    public ComboBox<LocalTime> addAppointmentStartTime;
    public ComboBox<LocalTime> addAppointmentEndTime;
    public ComboBox<Customers> customerIDCB;
    public ComboBox<User> userIDCB;
    public TextField addAppointmentType;
    public TextField addAppointmentLocation;
    public TextField addAppointmentDescription;
    public TextField addAppointmentTitle;

    /**
     * Method to send new appointment data back to the main appointment screen controller
     * where the new appointment is displayed. Time conversions are provided.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addAppointmentContact.setItems(ContactsAcc.getAllContacts());
        customerIDCB.setItems((CustomersAcc.getAllCustomers()));
        userIDCB.setItems(UserAcc.getAllUsers());

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
            addAppointmentStartTime.getItems().add(start);
            addAppointmentEndTime.getItems().add(start);
            start = start.plusMinutes(15);
        }

    }

    /**
     * Method to verify the new appointment data is complete, check for appointment overlaps, send data
     *  to database, and transition user back to main appointment screen. Alerts for missing information
     *  or appointment overlap are provided.
     * @param event
     */


    public void onActionSaveAppointment(ActionEvent event) {
        // 1. Get the data from the GUI
        try {
            String title = addAppointmentTitle.getText();
            String description = addAppointmentDescription.getText();
            String location = addAppointmentLocation.getText();

            String type = addAppointmentType.getText();
            LocalDate startdate = addAppointmentStartDate.getValue();
            // enter start date
            LocalTime starttime = addAppointmentStartTime.getValue();
            LocalTime endtime = addAppointmentEndTime.getValue();

            Customers customers = customerIDCB.getValue();
            User users = userIDCB.getValue();
            Contacts contacts = addAppointmentContact.getValue();



            // 2. Validate the data
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

            //need to validate for business hours


            //need to validate for start before end
            if(!start.isBefore(end)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Start time must be before end time.");
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }

            //validate based on cust ID
            ObservableList<Appointments> alllist = AppointmentsAcc.getAllAppointments();
            boolean overlap = false;
            for(Appointments appointments:alllist) {
                if (appointments.getCustomerID() != customers.getCustomerID()) {
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


            String sql = "insert into appointments (Appointment_ID, Title, Description, Location, Contact_ID, Type, Start, End, Customer_ID, User_ID ) values(null, ?, ?, ?, ?, ?,?,?,?,?)";

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

    /**
     * Method to return to the main appointment screen with button push.
     * @param event
     * @throws IOException
     */


    public void onActionAppointmentsAddBack(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will return you to Main Appointments Records without saving, do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent one = FXMLLoader.load(getClass().getResource("/view/AppointmentsScreen.fxml"));
            Scene scene = new Scene(one);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            stage.setScene(scene);

            stage.show();
        }


    }

}
