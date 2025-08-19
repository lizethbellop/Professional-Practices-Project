package practicasprofesionalespf.controller.coordinator;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import practicasprofesionalespf.model.dao.RecordDAO;
import practicasprofesionalespf.model.dao.ProjectDAO;
import practicasprofesionalespf.model.dao.ProjectManagerDAO;
import practicasprofesionalespf.model.pojo.Project;
import practicasprofesionalespf.model.pojo.ProjectManager;
import practicasprofesionalespf.model.pojo.Record;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.utils.Utils;
import practicasprofesionalespf.utils.WindowUtils;

public class FXMLConfirmAssignmentController implements Initializable {

    @FXML
    private TextField tfStudentName;
    @FXML
    private TextField tfStudentEnrollment;
    @FXML
    private TextField tfStudentEmail;
    @FXML
    private TextField tfStudentPhone;
    @FXML
    private TextField tfProjectName;
    @FXML
    private TextArea taProjectDescription;
    @FXML
    private TextField tfProjectManager;
    @FXML
    private TextField tfProjectPhase;
    @FXML
    private Button btnAssign;
    @FXML
    private Button btnCancel;

    private Student selectedStudent;
    private Project selectedProject;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnAssign.setOnAction(this::handleAssignButtonClick);
        btnCancel.setOnAction(this::handleCancelButtonClick);
    }    

    public void initializeData(Student student, Project project) {
        this.selectedStudent = student;
        this.selectedProject = project;
        populateUI();
    }
    
    private void populateUI() {
        if (selectedStudent != null && selectedProject != null) {
            tfStudentName.setText(selectedStudent.getFullName());
            tfStudentEnrollment.setText(selectedStudent.getEnrollment());
            tfStudentEmail.setText(selectedStudent.getEmail());
            tfStudentPhone.setText(selectedStudent.getPhone());
            
            tfProjectName.setText(selectedProject.getName());
            taProjectDescription.setText(selectedProject.getDescription());
            tfProjectPhase.setText(selectedProject.getDeparment());
            
            try {
                ProjectManager manager = ProjectManagerDAO.getProjectManagerById(selectedProject.getIdProjectManager());
                if (manager != null) {
                    tfProjectManager.setText(manager.getFullName());
                }
            } catch (SQLException e) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión", 
                        "No se pudo obtener la información del responsable.");
            }
        }
    }

 
    private void handleAssignButtonClick(ActionEvent event) {
        boolean confirmation = Utils.showConfirmationAlert("Confirmar Asignación", 
                "¿Está seguro de que desea asignar al estudiante " + selectedStudent.getFullName() 
                + " al proyecto " + selectedProject.getName() + "?");

        if (confirmation) {
            try {
                Record record = RecordDAO.getRecordByStudent(selectedStudent.getIdStudent());
                if (record == null) {
                    Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Datos", "No se pudo encontrar el expediente del estudiante.");
                    return;
                }
                
                RecordDAO.assignProjectToRecord(record.getIdRecord(), selectedProject.getIdProject());
                                
                Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Asignación Exitosa", "El estudiante ha sido asignado al proyecto correctamente.");
                closeWindow();

            } catch (SQLException e) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Base de Datos", "Ocurrió un error al guardar la asignación: " + e.getMessage());
            }
        }
    }

    private void handleCancelButtonClick(ActionEvent event) {
        WindowUtils.whenCancelClicked(event);
    }
    
    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}