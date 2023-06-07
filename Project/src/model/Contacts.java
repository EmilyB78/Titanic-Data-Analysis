package model;

/**
 * Contacts class that contains contactsID and contactsName.
 * These values reflect needed contact information. Getters and Setters are also part of this class.
 */
public class Contacts {
    private int contactsID;
    private String contactsName;

    public Contacts(int contactsID, String contactsName){
        this.contactsID= contactsID;
        this.contactsName = contactsName;
    }

    /**
     *
     * @return contactsID
     */
    public int getContactsID() {
        return contactsID;
    }

    /**
     *
     * @return contactsName
     */


    public String getContactsName() {
        return contactsName;
    }

    /**
     *
     * @return String contactsID
     */

    @Override
    public String toString (){
        return String.valueOf(contactsID);
    }


}
