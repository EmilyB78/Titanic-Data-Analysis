package model;

/**
 * User class that contains userID, userName, and userPassword.
 * These values reflect needed user information. Getters and setters are also
 * included.
 */
public class User {
    public int userID;
    public String userName;
    public String userPassword;

    public User(int userID, String userName, String userPassword){
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**
     *
     * @return userID
     */

    public int getUserID() {        return userID;    }

    /**
     *
     * @return userName
     */

    public String getUserName() {        return userName;    }

    /**
     *
     * @return userPassword
     */

    public String getUserPassword() {        return userPassword;    }

    /**
     *
     * @return String userID
     */

    @Override
    public String toString (){
        return String.valueOf(userID);
    }

}
