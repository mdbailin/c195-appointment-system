package controller;

import helper.JDBC;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/***
 * This class creates a welcome screen for the user.
 */
public class Welcome implements Initializable {

    @FXML
    private AnchorPane mainScreen;

    @FXML
    private Button customersButton;

    @FXML
    private Button appointmentsButton;

    @FXML
    private Button reportsButton;

    @FXML
    private Button exitButton;

    Parent root;
    Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void onCustomersButton(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
        stage = (Stage)customersButton.getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onAppointmentsButton(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AppointmentMain.fxml"));
        stage = (Stage)appointmentsButton.getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onReportsButton(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Report.fxml"));
        stage = (Stage)reportsButton.getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onExitButton(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Required");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            JDBC.closeConnection();
            System.out.println("Program Exit.");
            System.exit(0);
        }
        else{
            System.out.println("Exit canceled.");
        }
    }

}
