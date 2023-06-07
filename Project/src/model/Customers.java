 package model;

 /**
  * Customers class that contains customerID, customerName, customerAddress, customerPostalCode,
  * customerPhonenumber, divisionID, divisionName, countryID, customerCountry. These values
  * reflected needed customer information. Getters and setters are also included.
  */
 public class Customers {


    private int customerID;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhonenumber;
    private int divisionID;
    private String divisionName;
    private int  countryID;
    private String customerCountry;

    public Customers(int customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhonenumber, int divisionID, String divisionName, int countryID, String customerCountry) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhonenumber = customerPhonenumber;
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
        this.customerCountry = customerCountry;
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
      * @return customerName
      */

    public String getCustomerName() {
        return customerName;
    }

     /**
      *
      * @return customerAddress
      */

    public String getCustomerAddress() {
        return customerAddress;
    }

     /**
      *
      * @return customerPostalCode
      */

    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

     /**
      *
      * @return customerPhonenumber
      */

    public String getCustomerPhonenumber() {
        return customerPhonenumber;
    }

     /**
      *
      * @return divisionID
      */

    public int getDivisionID() {
        return divisionID;
    }

     /**
      *
      * @return divisionName
      */

    public String getDivisionName() {
        return divisionName;
    }

     /**
      *
      * @return countryID
      */

    public int getCountryID() {
        return countryID;
    }

     /**
      *
      * @return customerCountry
      */

    public String getCustomerCountry() {
        return customerCountry;
    }

     /**
      *
      * @return String customerID
      */

    @Override
    public String toString (){
        return String.valueOf(customerID);
    }
}




