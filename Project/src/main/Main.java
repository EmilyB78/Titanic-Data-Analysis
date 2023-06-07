package main;

import SQLDatabase.SQLDBConn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Locale;

/**
 * Class creates an application for management of data selected from and added to database.
 */
public class Main extends Application  {
    @Override
    public void start (Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginScreen.fxml"));
        stage.setTitle("Login Screen");
        stage.setScene(new Scene(root, 800, 600 ));
        stage.show();
    }


    public static void main(String[] args) throws SQLException {
       // Locale.setDefault(new Locale("fr"));
        SQLDBConn.connection();
       launch(args);
        SQLDBConn.closeConnection();
    }
}

