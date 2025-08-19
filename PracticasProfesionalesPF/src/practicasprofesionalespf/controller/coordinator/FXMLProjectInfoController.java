package practicasprofesionalespf.controller.coordinator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practicasprofesionalespf.model.dao.ProjectDAO;
import practicasprofesionalespf.model.pojo.Project;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.utils.Utils;
import practicasprofesionalespf.utils.WindowUtils;

public class FXMLProjectInfoController implements Initializable {

    @FXML
    private TextField tfStudentName;
    @FXML
    private TextField tfEnrollment;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfPhone;
    @FXML
    private ListView<String> lvProjectSelection;
    @FXML
    private TableView<Project> tvProjects;
    @FXML
    private TableColumn<Project, String> tcProjectName;
    @FXML
    private TableColumn<Project, String> tcDescription;
    @FXML
    private TableColumn<Project, String> tcDepartment;
    @FXML
    private TableColumn<Project, String> tcMethodology;
    @FXML
    private TableColumn<Project, Integer> tcAvailability;
    @FXML
    private Button btnAccept;
    @FXML
    private Button btnCancel;

    private Student student;
    private ObservableList<Project> projects;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        projects = FXCollections.observableArrayList();
        configureTable();
        loadAvailableProjects();
        Platform.runLater(() -> {
            tfStudentName.setFocusTraversable(false);
            tfEnrollment.setFocusTraversable(false);
            tfEmail.setFocusTraversable(false);
            tfPhone.setFocusTraversable(false);
            tvProjects.requestFocus();
        });
    }

    public void initializeData(Student student) {
        this.student = student;
        if (this.student != null) {
            tfStudentName.setText(this.student.getFullName());
            tfEnrollment.setText(this.student.getEnrollment());
            tfEmail.setText(this.student.getEmail());
            tfPhone.setText(this.student.getPhone());

            if (this.student.getProjectSelection() != null && !this.student.getProjectSelection().isEmpty()) {
                lvProjectSelection.getItems().addAll(Arrays.asList(this.student.getProjectSelection().split(",")));
            }
        }
    }

    private void configureTable() {
        tcProjectName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tcDepartment.setCellValueFactory(new PropertyValueFactory<>("deparment"));
        tcMethodology.setCellValueFactory(new PropertyValueFactory<>("methodology"));
        tcAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
        tvProjects.setItems(projects);
    }

    private void loadAvailableProjects() {
        try {
            ArrayList<Project> availableProjects = ProjectDAO.getAvailableProjects();
            projects.addAll(availableProjects);
        } catch (SQLException e) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión", "No se pudo cargar la lista de proyectos disponibles.");
        }
    }

    @FXML
    private void handleAcceptButton(ActionEvent event) {
        Project selectedProject = tvProjects.getSelectionModel().getSelectedItem();

        if (selectedProject != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/practicasprofesionalespf/view/coordinator/FXMLConfirmAssignment.fxml"));
                Parent root = loader.load();

                FXMLConfirmAssignmentController controller = loader.getController();
                controller.initializeData(this.student, selectedProject);

                Stage currentStage = (Stage) btnAccept.getScene().getWindow();

                Scene confirmationScene = new Scene(root);

                currentStage.setScene(confirmationScene);
                currentStage.setTitle("Confirmar Asignación"); // Opcional: Cambia el título de la ventana

            } catch (IOException e) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Interfaz", "No se pudo cargar la ventana de confirmación.");
            }
        } else {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Sin Selección", "Por favor, seleccione un proyecto de la tabla para continuar.");
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        WindowUtils.whenCancelClicked(event);
    }
}
