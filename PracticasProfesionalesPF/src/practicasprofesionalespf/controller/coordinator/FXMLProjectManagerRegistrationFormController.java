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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practicasprofesionalespf.PracticasProfesionalesPF;
import practicasprofesionalespf.interFace.INotification;
import practicasprofesionalespf.model.dao.LinkedOrganizationDAO;
import practicasprofesionalespf.model.dao.ProjectManagerDAO;
import practicasprofesionalespf.model.pojo.LinkedOrganization;
import practicasprofesionalespf.model.pojo.OperationResult;
import practicasprofesionalespf.model.pojo.ProjectManager;
import practicasprofesionalespf.utils.Utils;
import practicasprofesionalespf.utils.WindowUtils;
import practicasprofesionalespf.validation.FormValidator;

public class FXMLProjectManagerRegistrationFormController implements Initializable {

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
    
    INotification observer;
    private ObservableList<LinkedOrganization> linkedOrganizations;
    private Map<String, String> fieldsMap;
    private List<TextField> fields;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadOrganizations();
        setIdFields();
        obtainListOfFields();
    } 
    
    public void initializeInformation(INotification observer){
        this.observer = observer;
    }
    
    private void setIdFields(){
        
        tfName.setId("tfName");
        tfLastNameFather.setId("tfLastNameFather");
        tfLastNameMother.setId("tfLastNameMother");
        tfPosition.setId("tfPosition");
        tfEmail.setId("tfEmail");
        tfPhone.setId("tfPhone");
    }
    
    private void obtainListOfFields(){
      this.fields = Arrays.asList(tfName, tfLastNameFather, tfLastNameMother, tfPosition, tfEmail, tfPhone);  
    }
    
    
    
    private void loadOrganizations(){
        try{
           linkedOrganizations = FXCollections.observableArrayList();
           List<LinkedOrganization> linkedOrganizationsDAO = LinkedOrganizationDAO.obtainLinkedOrganizations();
           linkedOrganizations.addAll(linkedOrganizationsDAO);
           cbLinkedOrganization.setItems(linkedOrganizations);
        }catch(SQLException ex){
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Problemas de conexión", 
                    "No hay conexión con la base de datos. Inténtelo nuevamente");
        }
    }

    
    private void obtainMapOfFields(){
       this.fieldsMap = Utils.buildFieldMap(fields); 
    }
    
    private boolean areFieldsComplete(){
        obtainMapOfFields();
        Set<String> emptyFields = FormValidator.checkEmptyFields(fieldsMap);
        int cbSelection = cbLinkedOrganization.getSelectionModel().getSelectedIndex();
        
        if(!emptyFields.isEmpty() || cbSelection == -1){
            markFields(emptyFields);
            Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Datos incompletos", 
                    "Ingresó datos incompletos, por favor corregir");
            return false;
        } else{
            return true;
        }
    }
    
    private boolean areFieldsValid(){
        obtainMapOfFields();
        Set<String> invalidFields = FormValidator.checkInvalidFieldsProjectManager(fieldsMap);
        
        if(!invalidFields.isEmpty()){
            markFields(invalidFields);
            Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Datos invalidos", 
                    "Ingresó datos invalidos, por favor corregir");
            return false;
        } else{
            return true;
        }
    }
    
    private void clearFields(){
        for(TextField field : fields){
            field.setStyle("");
        }
    }
    
    private void markFields(Set<String> obtainedFields){
        clearFields();
       for(TextField field : fields){
           if(field.getId() != null && obtainedFields.contains(field.getId())){
               field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
           }
       } 
    }
    
    private ProjectManager createProjectManager(){
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
    
    private void safeProjectManager(ProjectManager projectManager){
        try {
            OperationResult insertResult = ProjectManagerDAO.registerManager(projectManager);
            if(!insertResult.isError()){
                Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Registro exitoso", insertResult.getMessage());
                Utils.getStageComponent(tfLastNameMother).close();
                observer.successfulOperation("Insert", projectManager.getFirstName());
            }else{
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error al registrar", insertResult.getMessage());
            }
        } catch (SQLException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de conexión", 
                    "No hay conexión con la base de datos, inténtelo más tarde");
            closeWindow();
        }
    }
    
    private void closeWindow(){
      ((Stage) tfEmail.getScene().getWindow()).close();
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        WindowUtils.whenCancelClicked(tfPhone);
    }

    @FXML
    private void onSavetButtonClickled(ActionEvent event) {
        if(areFieldsComplete()){
           if(areFieldsValid()){
               ProjectManager projectManager = createProjectManager();
               String action = "completar el registro de " + projectManager.getFullName();
               WindowUtils.askForConfirmation(tfPhone, action);
               safeProjectManager(projectManager);
           }
        }
    }
    
}
