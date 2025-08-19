package practicasprofesionalespf.controller.coordinator;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import practicasprofesionalespf.interFace.INotification;
import practicasprofesionalespf.model.SessionManager;
import practicasprofesionalespf.model.dao.CoordinatorDAO;
import practicasprofesionalespf.model.dao.ProjectDAO;
import practicasprofesionalespf.model.pojo.LinkedOrganization;
import practicasprofesionalespf.model.pojo.OperationResult;
import practicasprofesionalespf.model.pojo.Project;
import practicasprofesionalespf.model.pojo.ProjectManager;
import practicasprofesionalespf.utils.Utils;
import practicasprofesionalespf.utils.WindowUtils;
import practicasprofesionalespf.validation.FormValidator;

public class FXMLProjectFormController implements Initializable {

    @FXML
    private TextField tfName;
    @FXML
    private TextField tfDepartment;
    @FXML
    private TextArea taDescription;
    @FXML
    private TextField tfMethodology;
    @FXML
    private TextField tfAvailability;
    @FXML
    private Button btnAccept;
    @FXML
    private Button btnCancel;
    @FXML
    private Label lbTitle;

    private LinkedOrganization selectedOrganization;
    private ProjectManager selectedManager;
    private INotification notifier;
    private List<TextField> textFields;

    public void setNotifier(INotification notifier) {
        this.notifier = notifier;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfAvailability.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfAvailability.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        setIdToFields();
        getFields();
        lbTitle.setText("Registrar proyecto"); 
    }
    
    private void setIdToFields() {
        tfName.setId("tfName");
        tfDepartment.setId("tfDepartment");
        taDescription.setId("taDescription");
        tfMethodology.setId("tfMethodology");
        tfAvailability.setId("tfAvailability");
    }

    private void getFields() {
        textFields = Arrays.asList(tfName, tfDepartment, tfMethodology, tfAvailability);
    }

    public void initializeData(LinkedOrganization org, ProjectManager manager, INotification notifier) {
        this.selectedOrganization = org;
        this.selectedManager = manager;
        this.notifier = notifier;
    }

    @FXML
    private void onAcceptButtonClicked(ActionEvent event) {
        if (validateFields()) {
            createProject();
        }
    }

    private void createProject() {
        try {
            Project newProject = new Project();
            newProject.setName(tfName.getText().trim());
            newProject.setDeparment(tfDepartment.getText().trim());
            newProject.setDescription(taDescription.getText().trim());
            newProject.setMethodology(tfMethodology.getText().trim());
            newProject.setAvailability(Integer.parseInt(tfAvailability.getText().trim()));
            newProject.setIdLinkedOrganization(selectedOrganization.getIdLinkedOrganization());
            newProject.setIdProjectManager(selectedManager.getIdProjectManager());

            Integer coordinatorId = CoordinatorDAO.getCoordinatorIdByIdUser(SessionManager.getInstance().getLoggedInUser().getIdUser());
            if (coordinatorId == null) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Coordinador", "No se pudo verificar la identidad del coordinador.");
                return;
            }
            newProject.setIdCoordinator(coordinatorId);

            OperationResult result = ProjectDAO.saveProject(newProject);

            if (!result.isError()) {
                Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Registro Exitoso", result.getMessage());
                if (notifier != null) {
                    notifier.successfulOperation("Registro", "proyecto");
                }
                closeWindow();
            } else {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error en el Registro", result.getMessage());
            }

        } catch (SQLException e) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión", "No se pudo conectar a la base de datos.");
        }  
    }

    private boolean validateFields() {
        for (TextField textField : textFields) {
            textField.setStyle("");
        }
        taDescription.setStyle("");

        Map<String, String> fieldsToValidate = new HashMap<>();
        fieldsToValidate.put(tfName.getId(), tfName.getText());
        fieldsToValidate.put(tfDepartment.getId(), tfDepartment.getText());
        fieldsToValidate.put(taDescription.getId(), taDescription.getText());
        fieldsToValidate.put(tfMethodology.getId(), tfMethodology.getText());
        fieldsToValidate.put(tfAvailability.getId(), tfAvailability.getText());

        Set<String> emptyFields = FormValidator.checkEmptyFields(fieldsToValidate);
        Set<String> invalidFields = FormValidator.checkInvalidFieldsProject(fieldsToValidate);

        invalidFields.addAll(emptyFields);

        if (!invalidFields.isEmpty()) {
            markFields(invalidFields);
            Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Datos inválidos",
                    "Ingresó datos inválidos, por favor corríjalos");
            return false;
        }
        return true; 
    }

    private void markFields(Set<String> invalidFields) {
        for (TextField textField : textFields) {
            if (invalidFields.contains(textField.getId())) {
                textField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
        }
        if (invalidFields.contains(taDescription.getId())) {
            taDescription.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        }
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        WindowUtils.whenCancelClicked(tfDepartment);
    }

    private void closeWindow() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }
}