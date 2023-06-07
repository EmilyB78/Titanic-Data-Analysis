package Access;

import SQLDatabase.SQLDBConn;
import model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * Class to obtain user data from database
 */
public class UserAcc {
    /**
     * Observable list of users from database
     * @return
     */

    public static ObservableList<User> getAllUsers(){
        ObservableList<User> ulist = FXCollections.observableArrayList();
        try {
            String sql  = "SELECT * from users";
            PreparedStatement ps = SQLDBConn.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String userPassword = rs.getString("Password");
                User U = new User (userId, userName, userPassword);
                ulist.add(U);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ulist;
    }
}



