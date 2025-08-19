package practicasprofesionalespf.controller.teacher;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import practicasprofesionalespf.PracticasProfesionalesPF;
import practicasprofesionalespf.model.SessionManager;
import practicasprofesionalespf.utils.Utils;

public class FXMLTeacherHomepageController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onValidateDocsButtonClicked(ActionEvent event) {
        try{
            Stage chooseStudentStage = new Stage();
            Parent view = FXMLLoader.load(PracticasProfesionalesPF.class.getResource("view/teacher/FXMLPickStudent.fxml"));
            Scene scene = new Scene(view);
            chooseStudentStage.setScene(scene);
            chooseStudentStage.setTitle("Elegir estudiante");
            chooseStudentStage.initModality(Modality.APPLICATION_MODAL);
            chooseStudentStage.showAndWait();
        }catch(IOException ex){
            ex.printStackTrace();
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error con la interfaz", 
                    "No se pudo abrir la ventana, intentalo más tarde");
        }
    }
    
    @FXML
    private void btnConsultRecordClicked(ActionEvent event) {
        try {
            Stage stage = new Stage();
            Parent view = FXMLLoader.load(getClass().getResource("/practicasprofesionalespf/view/teacher/FXMLStudentRecord.fxml"));
            Scene scene = new Scene(view);
            stage.setScene(scene);
            stage.setTitle("Consultar Expediente de Estudiante");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch(IOException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error con la interfaz", "No se pudo abrir la ventana.");
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
