package practicasprofesionalespf.controller.coordinator;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import practicasprofesionalespf.model.dao.LinkedOrganizationDAO;
import practicasprofesionalespf.model.pojo.LinkedOrganization;
import practicasprofesionalespf.model.pojo.OperationResult;
import practicasprofesionalespf.utils.Utils;
import practicasprofesionalespf.validation.FormValidator; 

public class FXMLRegistrationFormLOController implements Initializable {

    @FXML
    private TextField tfName;
    @FXML
    private TextField tfStreet;
    @FXML
    private TextField tfNeighborhood;
    @FXML
    private TextField tfCity;
    @FXML
    private TextField tfState;
    @FXML
    private TextField tfPostalCode;
    @FXML
    private TextField tfPhone;
    @FXML
    private CheckBox cbStatus;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { }    

    @FXML
    private void btnContinueClicked(ActionEvent event) {
        if (!validateFields()) {
            return;
        }
        
        try {
            if(LinkedOrganizationDAO.organizationExists(tfName.getText().trim())) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Duplicidad", "La organización '" + tfName.getText().trim() + "' ya existe en el sistema.");
                return;
            }
            
            LinkedOrganization org = createOrganizationFromForm();

            if (showConfirmationDialog(org)) {
                OperationResult result = LinkedOrganizationDAO.saveLinkedOrganization(org);
                if (!result.isError()) {
                    Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Registro Exitoso", result.getMessage());
                    closeWindow();
                } else {
                    Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error al Guardar", result.getMessage());
                }
            }

        } catch(SQLException e) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión", "No se pudo establecer la conexión con la base de datos. Intente más tarde.");
            e.printStackTrace();
        }
    }
    
    private LinkedOrganization createOrganizationFromForm() {
        LinkedOrganization org = new LinkedOrganization();
        org.setName(tfName.getText().trim());
        org.setStreet(tfStreet.getText().trim());
        org.setNeighborhood(tfNeighborhood.getText().trim());
        org.setCity(tfCity.getText().trim());
        org.setState(tfState.getText().trim());
        org.setPostalCode(tfPostalCode.getText().trim());
        org.setPhone(tfPhone.getText().trim());
        org.setIsActive(cbStatus.isSelected());
        return org;
    }

    private boolean showConfirmationDialog(LinkedOrganization org) {
        StringBuilder confirmationMessage = new StringBuilder();
        confirmationMessage.append("Por favor, verifique que los datos son correctos:\n\n");
        confirmationMessage.append("Nombre: ").append(org.getName()).append("\n");
        confirmationMessage.append("Calle: ").append(org.getStreet()).append("\n");
        confirmationMessage.append("Colonia: ").append(org.getNeighborhood()).append("\n");
        confirmationMessage.append("Ciudad: ").append(org.getCity()).append("\n");
        confirmationMessage.append("Estado: ").append(org.getState()).append("\n");
        confirmationMessage.append("Código Postal: ").append(org.getPostalCode()).append("\n");
        confirmationMessage.append("Teléfono: ").append(org.getPhone()).append("\n");
        confirmationMessage.append("Estatus: ").append(org.getIsActive() ? "Activo" : "Inactivo").append("\n");

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmar Registro");
        confirmationAlert.setHeaderText("¿Desea registrar esta Organización Vinculada?");
        confirmationAlert.setContentText(confirmationMessage.toString());

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private boolean validateFields() {
        Map<String, String> fields = new HashMap<>();
        fields.put("Nombre", tfName.getText());
        fields.put("Calle", tfStreet.getText());
        fields.put("Colonia", tfNeighborhood.getText());
        fields.put("Ciudad", tfCity.getText());
        fields.put("Estado", tfState.getText());
        fields.put("Código Postal", tfPostalCode.getText());
        fields.put("Teléfono", tfPhone.getText());

        Set<String> emptyFields = FormValidator.checkEmptyFields(fields);
        if (!emptyFields.isEmpty()) {
            String message = "Los siguientes campos son obligatorios y no pueden estar vacíos:\n\n";
            for (String fieldName : emptyFields) {
                message += "- " + fieldName + "\n";
            }
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Campos Vacíos", message);
            return false;
        }

       
        Map<String, String> fieldsForFormatValidation = new HashMap<>();
        fieldsForFormatValidation.put("tfName", tfName.getText());
        fieldsForFormatValidation.put("tfStreet", tfStreet.getText());
        fieldsForFormatValidation.put("tfNeighborhood", tfNeighborhood.getText());
        fieldsForFormatValidation.put("tfCity", tfCity.getText());
        fieldsForFormatValidation.put("tfState", tfState.getText());
        fieldsForFormatValidation.put("tfPostalCode", tfPostalCode.getText());
        fieldsForFormatValidation.put("tfPhone", tfPhone.getText());

        Set<String> invalidFields = FormValidator.checkInvalidFieldsLinkedOrganization(fieldsForFormatValidation);
        if (!invalidFields.isEmpty()) {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Datos Inválidos", "Uno o más campos tienen un formato o longitud inválida. Por favor, revíselos.");
            return false;
        }
        
        return true;
    }

    @FXML
    private void btnExitClicked(ActionEvent event) {
        if(Utils.showConfirmationAlert("¿Está seguro de que desea salir?", "No se guardarán los datos no registrados.")) {
            closeWindow();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) tfName.getScene().getWindow();
        stage.close();
    }
}