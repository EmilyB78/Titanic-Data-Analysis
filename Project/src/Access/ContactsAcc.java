package Access;

import SQLDatabase.SQLDBConn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to obtain contacts from database.
 */
public class ContactsAcc {
    /**
     * Observable list for all contacts from database
     * @return
     */
    public static ObservableList<Contacts> getAllContacts() {
        ObservableList<Contacts> contactsObservableList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * from contacts";
            PreparedStatement ps = SQLDBConn.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                Contacts contacts = new Contacts(contactId, contactName);

                contactsObservableList.add(contacts);
            }
        } catch (SQLException throwables) {

            throwables.printStackTrace();
        }
        return contactsObservableList;
    }

}




