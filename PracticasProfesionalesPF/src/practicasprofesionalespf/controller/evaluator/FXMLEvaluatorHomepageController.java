package practicasprofesionalespf.controller.evaluator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practicasprofesionalespf.PracticasProfesionalesPF;
import practicasprofesionalespf.utils.Utils;

public class FXMLEvaluatorHomepageController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void onEvaluateButtonClicked(ActionEvent event) {
        try{
            Stage chooseStudentStage = new Stage();
            FXMLLoader loader = new FXMLLoader(PracticasProfesionalesPF.class.getResource("view/evaluator/FXMLChooseStudent.fxml"));
            Parent view = loader.load();
            
            FXMLChooseStudentController controller = loader.getController();
            
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
    private void onLogOutButtonClicked(ActionEvent event) {
    }
    
    
    
}
