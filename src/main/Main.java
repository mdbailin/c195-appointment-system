package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/***
 * Entry to main program
 */
public class Main extends Application {

    //Creates new JavaFX scene
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(root, 400, 200));
        stage.show();
    }

    // Starts program
    public static void main(String[] args){
        JDBC.openConnection();
        launch(args);

    }
}
