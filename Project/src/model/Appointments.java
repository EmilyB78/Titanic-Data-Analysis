package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import java.time.LocalDateTime;

/**
 * Appointments class contains appointmentID, appointmentTitle,
 * appointmentDescription, appointmentLocation, appointmentType, start date/time, end data/time.
 * These values reflect needed appointment information. This class also contains setters and getters.
 */
public class Appointments {

    public static ObservableList<String> monthlist = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    private int appointmentID;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private String appointmentType;
    private LocalDateTime start;
    private LocalDateTime end;
    public int customerID;
    public int userID;
    public int contactID;

    public Appointments (int appointmentID, String appointmentTitle, String appointmentDescription, String appointmentLocation, String appointmentType, LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID){
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentType = appointmentType;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     *
     * @return appointmentID
     */
    public int getAppointmentID() {
        return appointmentID;

    }

    /**
     *
     * @param appointmentID
     */
    public void setAppointmentID(int appointmentID){
        this.appointmentID = appointmentID;
    }

    /**
     *
     * @return appointmentTitle
     */

    public String getAppointmentTitle(){
        return appointmentTitle;
    }

    /**
     *
     * @return appointmnetDescription
     */
    public String getAppointmentDescription(){
        return appointmentDescription;
    }

    /**
     *
     * @return appointmentLocation
     */

    public String getAppointmentLocation(){
        return appointmentLocation;
    }

    /**
     *
     * @return appointmentType
     */

    public String getAppointmentType(){
        return appointmentType;
    }

    /**
     *
     * @return start
     */

    public LocalDateTime getStart() {
        System.out.println("start" + start);
        return start;
    }

    /**
     *
     * @return end
     */

    public LocalDateTime getEnd() {
        return end;
    }

    /**
     *
     * @return customerID
     */

    public int getCustomerID() {
        return customerID;
    }

    /**
     *
     * @return userID
     */

    public int getUserID() {
        return userID;
    }

    /**
     *
     * @return contactID
     */

    public int getContactID() {
        return contactID;
    }

}

