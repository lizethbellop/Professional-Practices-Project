package practicasprofesionalespf.controller.teacher;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import practicasprofesionalespf.model.pojo.StudentWithProject;
import practicasprofesionalespf.utils.Utils;

public class FXMLStudentRecordController implements Initializable {

    @FXML
    private TableView<StudentWithProject> tvStudents;
    @FXML
    private TableColumn<StudentWithProject, String> tcStudentName;
    @FXML
    private TableColumn<StudentWithProject, String> tcEnrollment;
    @FXML
    private TableColumn<StudentWithProject, String> tcProject;
    @FXML
    private Button btnConsult;
    
    private ObservableList<StudentWithProject> students;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        students = FXCollections.observableArrayList();
        configureTable();
        loadStudents();
        btnConsult.setDisable(true);
        tvStudents.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnConsult.setDisable(newVal == null);
        });
    }    
    
    private void configureTable() {
        tcStudentName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        tcEnrollment.setCellValueFactory(new PropertyValueFactory<>("enrollment"));
        tcProject.setCellValueFactory(new PropertyValueFactory<>("projectName"));
    }

    private void loadStudents() {
        try {
            students.addAll(StudentDAO.getStudentsWithProjectInfo());
            tvStudents.setItems(students);
            if (students.isEmpty()) {
                Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Sin Registros", "No hay ningún expediente para mostrar");
                closeWindow();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión", "No hay conexión con la base de datos.");
            closeWindow();
        }
    }

    @FXML
    private void btnConsultClicked(ActionEvent event) {
        StudentWithProject selectedStudent = tvStudents.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Sin Selección", "Por favor, selecciona un estudiante de la lista.");
            return;
        }

        try {
            // Abre la SEGUNDA ventana del flujo: la lista de documentos del estudiante.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/practicasprofesionalespf/view/teacher/FXMLConsultRecord.fxml"));
            Parent view = loader.load();
            
            FXMLConsultRecordController controller = loader.getController();
            // Le pasamos el ID del estudiante para que sepa qué expediente cargar.
            controller.initializeData(selectedStudent.getIdStudent());

            Stage stage = new Stage();
            stage.setScene(new Scene(view));
            stage.setTitle("Expediente de " + selectedStudent.getFirstName());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Interfaz", "No se pudo abrir la ventana del expediente.");
        }
    }

    @FXML
    private void btnExitClicked(ActionEvent event) {
        if(Utils.showConfirmationAlert("¿Seguro que quieres salir?", "Salir")) {
            closeWindow();
        }
    }
    
    private void closeWindow() {
        Stage stage = (Stage) tvStudents.getScene().getWindow();
        stage.close();
    }
}