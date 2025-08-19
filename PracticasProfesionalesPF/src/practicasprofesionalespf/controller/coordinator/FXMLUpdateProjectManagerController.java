package practicasprofesionalespf.controller.coordinator;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import practicasprofesionalespf.interFace.INotification;
import practicasprofesionalespf.model.dao.LinkedOrganizationDAO;
import practicasprofesionalespf.model.dao.ProjectManagerDAO;
import practicasprofesionalespf.model.pojo.LinkedOrganization;
import practicasprofesionalespf.model.pojo.OperationResult;
import practicasprofesionalespf.model.pojo.ProjectManager;
import practicasprofesionalespf.utils.Utils;
import practicasprofesionalespf.validation.FormValidator;

public class FXMLUpdateProjectManagerController implements Initializable {

    @FXML
    private TextField tfName;
    @FXML
    private TextField tfLastNameFather;
    @FXML
    private TextField tfLastNameMother;
    @FXML
    private TextField tfPosition;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfPhone;
    @FXML
    private ComboBox<LinkedOrganization> cbLinkedOrganization;

    private ProjectManager projectManagerUpdate;
    private INotification observer;
    private ObservableList<LinkedOrganization> linkedOrganizations;
    private List<TextField> fields;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadOrganizations();
        setIdFields();
        obtainListOfFields();
    }

    public void initializeInformation(ProjectManager projectManagerUpdate, INotification observer) {
        this.projectManagerUpdate = projectManagerUpdate;
        this.observer = observer;
        loadUpdateInformation();
    }
    
    private void setIdFields() {
        tfName.setId("tfName");
        tfLastNameFather.setId("tfLastNameFather");
        tfLastNameMother.setId("tfLastNameMother");
        tfPosition.setId("tfPosition");
        tfEmail.setId("tfEmail");
        tfPhone.setId("tfPhone");
    }

    private void obtainListOfFields() {
        this.fields = Arrays.asList(tfName, tfLastNameFather, tfLastNameMother, tfPosition, tfEmail, tfPhone);
    }
    
    private void loadOrganizations() {
        try {
            linkedOrganizations = FXCollections.observableArrayList();
            List<LinkedOrganization> linkedOrganizationsDAO = LinkedOrganizationDAO.obtainLinkedOrganizations();
            linkedOrganizations.addAll(linkedOrganizationsDAO);
            cbLinkedOrganization.setItems(linkedOrganizations);
        } catch (SQLException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Problemas de conexión",
                    "No hay conexión con la base de datos. Por favor inténtelo más tarde.");
        }
    }

    private void loadUpdateInformation() {
        if (projectManagerUpdate != null) {
            tfName.setText(projectManagerUpdate.getFirstName());
            tfLastNameFather.setText(projectManagerUpdate.getLastNameFather());
            tfLastNameMother.setText(projectManagerUpdate.getLastNameMother());
            tfPosition.setText(projectManagerUpdate.getPosition());
            tfEmail.setText(projectManagerUpdate.getEmail());
            tfPhone.setText(projectManagerUpdate.getPhone());

            for (LinkedOrganization org : cbLinkedOrganization.getItems()) {
                if (org.getIdLinkedOrganization() == projectManagerUpdate.getIdLinkedOrganization()) {
                    cbLinkedOrganization.getSelectionModel().select(org);
                    break;
                }
            }
        }
    }

    @FXML
    private void onAcceptButtonClicked(ActionEvent event) {
        if (validateForm()) {
            updateProjectManager();
        }
    }
    
    private void updateProjectManager() {
        ProjectManager projectManager = createProjectManagerFromForm();
        projectManager.setIdProjectManager(projectManagerUpdate.getIdProjectManager());

        try {
            OperationResult updateResult = ProjectManagerDAO.updateProjectManager(projectManager);
            if (!updateResult.isError()) {
                observer.successfulOperation("actualizado", projectManager.getFirstName());
                closeWindow();
            } else {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Actualización", "No se pudo actualizar el responsable de proyecto.");
            }
        } catch (SQLException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión",
                    "No hay conexión con la base de datos. Por favor inténtelo más tarde.");
            closeWindow();
        }
    }
    
    private boolean validateForm() {
        for (TextField field : fields) {
            field.setStyle("");
        }
        cbLinkedOrganization.setStyle("");

        Map<String, String> fieldsMap = Utils.buildFieldMap(fields);
        Set<String> emptyFields = FormValidator.checkEmptyFields(fieldsMap);
        Set<String> invalidFields = FormValidator.checkInvalidFieldsProjectManager(fieldsMap);

        invalidFields.addAll(emptyFields);
        int cbSelection = cbLinkedOrganization.getSelectionModel().getSelectedIndex();

        if (!invalidFields.isEmpty() || cbSelection == -1) {
            markFields(invalidFields);
            if (cbSelection == -1) {
                cbLinkedOrganization.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
            Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Datos Inválidos", "Ingreso datos inválidos, por favor corríjalos");
            return false;
        }
        return true;
    }
    
    private void markFields(Set<String> obtainedFields) {
        for (TextField field : fields) {
            if (field.getId() != null && obtainedFields.contains(field.getId())) {
                field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
        }
    }
    
    private ProjectManager createProjectManagerFromForm() {
        ProjectManager projectManager = new ProjectManager();
        projectManager.setFirstName(tfName.getText().trim());
        projectManager.setLastNameFather(tfLastNameFather.getText().trim());
        projectManager.setLastNameMother(tfLastNameMother.getText().trim());
        projectManager.setEmail(tfEmail.getText().trim());
        projectManager.setPhone(tfPhone.getText().trim());
        projectManager.setPosition(tfPosition.getText().trim());
        LinkedOrganization linkedOrganization = cbLinkedOrganization.getSelectionModel().getSelectedItem();
        projectManager.setIdLinkedOrganization(linkedOrganization.getIdLinkedOrganization());
        projectManager.setLinkedOrganization(linkedOrganization.getName());
        return projectManager;
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) tfName.getScene().getWindow()).close();
    }
}