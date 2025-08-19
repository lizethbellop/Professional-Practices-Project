package practicasprofesionalespf.controller.coordinator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practicasprofesionalespf.model.dao.StudentDAO;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.utils.Utils;

public class FXMLScheduleDeliverableController implements Initializable {

    @FXML
    private TableView<Student> tvStudents;
    @FXML
    private TableColumn<Student, String> tcStudentName; 
    @FXML
    private TableColumn<Student, String> tcTuition;     
    @FXML
    private Button btnContinue;
    @FXML
    private Button btnExit;

    private ObservableList<Student> students;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTableView();
        loadStudentsWithProject();
    }

    private void configureTableView() {
        tcStudentName.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        tcTuition.setCellValueFactory(new PropertyValueFactory<>("enrollment"));
    }

    private void loadStudentsWithProject() {
        try {
            ArrayList<Student> studentsDB = StudentDAO.getStudentsWithProject();
            students = FXCollections.observableArrayList(studentsDB);
            tvStudents.setItems(students);
        } catch (SQLException e) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de conexión", "No se pudo conectar con la base de datos, inténtelo más tarde.");
        }
    }

    @FXML
    private void btnContinueClicked(ActionEvent event) {
        Student selectedStudent = tvStudents.getSelectionModel().getSelectedItem();

        if (selectedStudent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/practicasprofesionalespf/view/coordinator/FXMLDataDeliverable.fxml"));
                Parent view = loader.load();
                FXMLDataDeliverableController dataDeliverableController = loader.getController();

                dataDeliverableController.initializeData(selectedStudent);

                Stage stage = new Stage();
                stage.setScene(new Scene(view));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Datos del entregable");
                stage.showAndWait();

            } catch (IOException e) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana. Revise que el FXML y el controlador de 'DataDeliverable' estén correctos.");
                e.printStackTrace();
            }
        } else {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Selección requerida", "Debe seleccionar un alumno para continuar.");
        }
    }

    @FXML
    private void btnExitClicked(ActionEvent event) {
        Stage currentStage = (Stage) tvStudents.getScene().getWindow();
        currentStage.close();
    }
}
