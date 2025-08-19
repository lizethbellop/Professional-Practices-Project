package practicasprofesionalespf.controller.coordinator;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import practicasprofesionalespf.model.dao.LinkedOrganizationDAO;
import practicasprofesionalespf.model.dao.ProjectManagerDAO;
import practicasprofesionalespf.model.dao.ProjectDAO;
import practicasprofesionalespf.model.pojo.LinkedOrganization;
import practicasprofesionalespf.model.pojo.OperationResult;
import practicasprofesionalespf.model.pojo.Project;
import practicasprofesionalespf.model.pojo.ProjectManager;
import practicasprofesionalespf.utils.Utils;
import practicasprofesionalespf.utils.WindowUtils;
import practicasprofesionalespf.validation.FormValidator;

public class FXMLUpdateProjectController implements Initializable {

    @FXML
    private Label lblWindowTitle;
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
    private ComboBox<ProjectManager> cbProjectManager;
    @FXML
    private ComboBox<LinkedOrganization> cbLinkedOrganization;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    private Project projectToUpdate;
    private List<TextField> textFields;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfAvailability.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfAvailability.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        textFields = Arrays.asList(tfName, tfDepartment, tfMethodology, tfAvailability);

        loadLinkedOrganizations();
        cbProjectManager.setDisable(true);

        cbLinkedOrganization.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                cbProjectManager.setDisable(false);
                loadProjectManagersForOrganization(newValue.getIdLinkedOrganization());
            }
        });
    }

    public void setProjectToUpdate(Project project) {
        this.projectToUpdate = project;
        tfName.setText(project.getName());
        tfDepartment.setText(project.getDeparment());
        taDescription.setText(project.getDescription());
        tfMethodology.setText(project.getMethodology());
        tfAvailability.setText(String.valueOf(project.getAvailability()));

        for (LinkedOrganization org : cbLinkedOrganization.getItems()) {
            if (org.getIdLinkedOrganization() == project.getIdLinkedOrganization()) {
                cbLinkedOrganization.setValue(org);
                break;
            }
        }

        for (ProjectManager manager : cbProjectManager.getItems()) {
            if (manager.getIdProjectManager() == project.getIdProjectManager()) {
                cbProjectManager.setValue(manager);
                break;
            }
        }
    }

    private void loadLinkedOrganizations() {
        try {
            ArrayList<LinkedOrganization> organizations = LinkedOrganizationDAO.obtainLinkedOrganizations();
            cbLinkedOrganization.getItems().addAll(organizations);
        } catch (SQLException e) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión", "No se pudieron cargar las organizaciones vinculadas.");
        }
    }

    private void loadProjectManagersForOrganization(int idOrganization) {
        cbProjectManager.getItems().clear();
        try {
            ArrayList<ProjectManager> managers = ProjectManagerDAO.obtainProjectManagersByOrganization(idOrganization);
            if (managers != null) {
                cbProjectManager.getItems().addAll(managers);
            }
        } catch (SQLException e) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión", "No se pudieron cargar los responsables de proyecto.");
        }
    }

    @FXML
    private void onSaveButtonClicked(ActionEvent event) {
        if (validateFields()) {
            Project updatedProject = new Project();
            updatedProject.setIdProject(this.projectToUpdate.getIdProject());
            updatedProject.setName(tfName.getText().trim());
            updatedProject.setDeparment(tfDepartment.getText().trim());
            updatedProject.setDescription(taDescription.getText().trim());
            updatedProject.setMethodology(tfMethodology.getText().trim());
            updatedProject.setAvailability(Integer.parseInt(tfAvailability.getText().trim()));
            updatedProject.setIdProjectManager(cbProjectManager.getValue().getIdProjectManager());
            updatedProject.setIdLinkedOrganization(cbLinkedOrganization.getValue().getIdLinkedOrganization());

            try {
                OperationResult result = ProjectDAO.updateProject(updatedProject);
                if (!result.isError()) {
                    Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Proyecto Actualizado", result.getMessage());
                    closeWindow();
                } else {
                    Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error al Actualizar", result.getMessage());
                }
            } catch (SQLException e) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión", "Ocurrió un error al conectar con la base de datos.");
            }
        }
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        closeWindow();
    }

    private void unmarkFields() {
        for (TextField textField : textFields) {
            textField.setStyle("");
        }
        taDescription.setStyle("");
        cbProjectManager.setStyle("");
        cbLinkedOrganization.setStyle("");
    }

    private void markFields(Set<String> invalidFields) {
        for (String invalidField : invalidFields) {
            for (TextField textField : textFields) {
                if (textField.getId().equals(invalidField)) {
                    textField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                }
            }
            if (taDescription.getId().equals(invalidField)) {
                taDescription.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
        }
    }

    private boolean validateFields() {
        unmarkFields();

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

        if (cbLinkedOrganization.getValue() == null || cbProjectManager.getValue() == null) {
            Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Selección requerida", "Debe seleccionar una organización y un responsable.");
            if (cbLinkedOrganization.getValue() == null) {
                cbLinkedOrganization.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
            if (cbProjectManager.getValue() == null) {
                cbProjectManager.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
            return false;
        }

        return true;
    }

    private void closeWindow() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }
}
