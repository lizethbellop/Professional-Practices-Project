package practicasprofesionalespf.controller.student;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practicasprofesionalespf.PracticasProfesionalesPF;
import practicasprofesionalespf.model.SessionManager;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.utils.Utils;

public class FXMLStudentHomepageController implements Initializable {

    @FXML
    private Label lbWelcomeMessage;
    
    private Student currentStudent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.currentStudent = SessionManager.getInstance().getLoggedInStudent();
        
        if (this.currentStudent != null) {
            lbWelcomeMessage.setText("Bienvenido, " + this.currentStudent.getFullName());
        }
    }

    @FXML
    private void onSubmitAssignmentClicked(ActionEvent event) {
          try {
            Stage assignmentStage = new Stage();
            FXMLLoader loader = new FXMLLoader(PracticasProfesionalesPF.class.getResource("view/student/FXMLAssignmentOption.fxml"));
            Parent view = loader.load();
            
            Scene scene = new Scene(view);
            assignmentStage.setScene(scene);
            assignmentStage.setTitle("Elegir el tipo de entrega");
            assignmentStage.initModality(Modality.APPLICATION_MODAL);
            assignmentStage.showAndWait();
        } catch (IOException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error con la interfaz", "No se pudo abrir la ventana, intentalo más tarde");
        }
    }
 

    @FXML
    private void onLogoutButtonClicked(ActionEvent event) {
        if (Utils.showConfirmationAlert("¿Seguro que quieres cerrar sesión?", "Cerrar Sesión")) {
            SessionManager.getInstance().cleanSession();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            try {
                Stage loginStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/practicasprofesionalespf/view/FXMLLogin.fxml"));

                Scene scene = new Scene(root);
                loginStage.setScene(scene);
                loginStage.setTitle("Iniciar Sesión");
                loginStage.show();

            } catch (IOException e) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Interfaz", "No se pudo cargar la ventana de inicio de sesión.");
            }
        }
    }
}