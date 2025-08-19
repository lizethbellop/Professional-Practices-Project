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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practicasprofesionalespf.PracticasProfesionalesPF;
import practicasprofesionalespf.interFace.INotification;
import practicasprofesionalespf.model.dao.ProjectManagerDAO;
import practicasprofesionalespf.model.pojo.ProjectManager;
import practicasprofesionalespf.utils.Utils;

public class FXMLAdminProjectManagerController implements Initializable, INotification {

    @FXML
    private TableView<ProjectManager> tvProjectManagers;
    @FXML
    private TableColumn<ProjectManager, String> tcName;
    @FXML
    private TableColumn<ProjectManager, String> tcLastNameFather;
    @FXML
    private TableColumn<ProjectManager, String> tcLastNameMother;
    @FXML
    private TableColumn<ProjectManager, String> tcLinkedOrganization;
    @FXML
    private TableColumn<ProjectManager, String> tcEmail;
    @FXML
    private TableColumn<ProjectManager, String> tcPosition;

    private ObservableList<ProjectManager> projectManagers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpTable();
        loadTableData();
    }

    private void setUpTable() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tcLastNameFather.setCellValueFactory(new PropertyValueFactory<>("lastNameFather"));
        tcLastNameMother.setCellValueFactory(new PropertyValueFactory<>("lastNameMother"));
        tcLinkedOrganization.setCellValueFactory(new PropertyValueFactory<>("linkedOrganization"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
    }

    private void loadTableData() {
        try {
            projectManagers = FXCollections.observableArrayList();
            ArrayList<ProjectManager> projectManagersDAO = ProjectManagerDAO.obtainProjectManager();
            projectManagers.addAll(projectManagersDAO);
            tvProjectManagers.setItems(projectManagers);
        } catch (SQLException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error al cargar la tabla", "Lo sentimos, "
                    + "por el momento no se puede mostrar la información de los responsables de proyecto. "
                    + "Por favor inténtelo más tarde");
            closeWindow();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) tvProjectManagers.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onRegisterButtonClicked(ActionEvent event) {
        goToProjectManagerForm(false, null);
    }

    @FXML
    private void onUpdateButtonClicked(ActionEvent event) {
        ProjectManager selectedManager = tvProjectManagers.getSelectionModel().getSelectedItem();
        if (selectedManager != null) {
            goToUpdateProjectManagerForm(selectedManager);
        } else {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Selección requerida", 
                    "Por favor, seleccione un responsable de proyecto para actualizar.");
        }
    }
    

    private void goToProjectManagerForm(boolean isUpdate, ProjectManager projectManagerUpdate) {
        try {
            Stage formStage = new Stage();
            FXMLLoader loader = new FXMLLoader(PracticasProfesionalesPF.class.getResource("view/coordinator/FXMLProjectManagerRegistrationForm.fxml"));
            Parent view = loader.load();

            FXMLProjectManagerRegistrationFormController controller = loader.getController();
            controller.initializeInformation(this);
            
            Scene scene = new Scene(view);
            String title = isUpdate ? "Formulario de actualización de Responsable" : "Formulario de registro de Responsable";
            formStage.setTitle(title);
            formStage.setScene(scene);
            formStage.initModality(Modality.APPLICATION_MODAL);
            formStage.showAndWait();
        } catch (IOException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error con la interfaz",
                    "No se pudo abrir el formulario. Inténtalo más tarde.");
        }
    }

    private void goToUpdateProjectManagerForm(ProjectManager projectManager) {
        try {
            Stage formStage = new Stage();
            FXMLLoader loader = new FXMLLoader(PracticasProfesionalesPF.class.getResource("view/coordinator/FXMLUpdateProjectManager.fxml"));
            Parent view = loader.load();

            FXMLUpdateProjectManagerController controller = loader.getController();
            controller.initializeInformation(projectManager, this);

            Scene scene = new Scene(view);
            formStage.setTitle("Formulario de Actualización de Responsable");
            formStage.setScene(scene);
            formStage.initModality(Modality.APPLICATION_MODAL);
            formStage.showAndWait();
        } catch (IOException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error con la interfaz",
                    "No se pudo abrir el formulario de actualización. Inténtalo más tarde.");
            ex.printStackTrace();
        }
    }

    @Override
    public void successfulOperation(String type, String name) {
        System.out.println("Se ha " + type.toLowerCase() + " correctamente a " + name);
        loadTableData();
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        closeWindow();
    }
}