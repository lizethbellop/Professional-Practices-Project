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
import practicasprofesionalespf.utils.WindowUtils;

public class FXMLStudentsWithoutProjectController implements Initializable {

    @FXML
    private TableView<Student> tvStudents;
    @FXML
    private TableColumn<Student, String> tcFullName;
    @FXML
    private Button btnAccept;
    @FXML
    private Button btnCancel;

    private ObservableList<Student> students;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        students = FXCollections.observableArrayList();
        configureTable();
        loadStudents();
    }    

    private void configureTable() {
        tcFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        tvStudents.setItems(students);
    }

    private void loadStudents() {
        try {
            ArrayList<Student> studentsDAO = StudentDAO.getStudentsWithoutProject();
            students.addAll(studentsDAO);
        } catch (SQLException e) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión", "No se pudo cargar la lista de estudiantes.");
        }
    }

    @FXML
    private void handleAcceptButton(ActionEvent event) {
        Student selectedStudent = tvStudents.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/practicasprofesionalespf/view/coordinator/FXMLProjectInfo.fxml"));
                Parent view = loader.load();

                FXMLProjectInfoController controller = loader.getController();
                controller.initializeData(selectedStudent);

                Stage projectInfoStage = new Stage();
                Scene scene = new Scene(view);
                projectInfoStage.setScene(scene);
                projectInfoStage.setTitle("Asignar Proyecto a Estudiante");
                projectInfoStage.initModality(Modality.APPLICATION_MODAL);
                
                projectInfoStage.showAndWait();
                
                Stage currentStage = (Stage) btnAccept.getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Interfaz", "No se pudo cargar la siguiente ventana.");
                e.printStackTrace();
            }
        } else {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Sin Selección", "Por favor, seleccione un estudiante de la lista.");
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        WindowUtils.whenCancelClicked(event);
    }
}