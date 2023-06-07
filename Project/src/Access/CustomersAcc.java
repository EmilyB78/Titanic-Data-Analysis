package Access;

import SQLDatabase.SQLDBConn;
import javafx.collections.ObservableList;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import model.Customers;

import java.sql.*;

/**
 * Class to obtain list of customers from database.
 */
public class CustomersAcc {
    /**
     * Observable list for all customers in database
     * @return
     */
    public static ObservableList<Customers> getAllCustomers(){
        ObservableList<Customers> customerslist = FXCollections.observableArrayList();
        try {
            String sql  = "Select Customer_ID, Customer_Name, Address, Postal_Code, Phone, customers.Division_ID, Division, first_level_divisions.Country_ID, Country from customers, first_level_divisions, countries where customers.Division_ID = first_level_divisions.Division_ID and first_level_divisions.Country_ID = countries.Country_ID";
            PreparedStatement ps = SQLDBConn.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");

                String customerPostalCode = rs.getString("Postal_Code");

                String customerPhone = rs.getString("Phone");
                int divisionID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryID = rs.getInt("Country_ID");
                String customerCountry = rs.getString("Country");

                Customers customers = new Customers(customerID, customerName, customerAddress, customerPostalCode, customerPhone, divisionID, divisionName,countryID, customerCountry);

                customerslist.add(customers);
            }
        } catch (SQLException throwables)  {

            throwables.printStackTrace();
        }        return customerslist;
    }

}
