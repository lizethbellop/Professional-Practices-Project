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
import practicasprofesionalespf.interFace.INotification;
import practicasprofesionalespf.model.dao.ProjectDAO;
import practicasprofesionalespf.model.pojo.Project;
import practicasprofesionalespf.utils.Utils;

public class FXMLProjectsController implements Initializable, INotification {

    @FXML
    private TableView<Project> tvProjects;
    @FXML
    private TableColumn<Project, String> tcName;
    @FXML
    private TableColumn<Project, String> tcDepartment;
    @FXML
    private TableColumn<Project, String> tcDescription;
    @FXML
    private TableColumn<Project, String> tcMethodology;
    @FXML
    private TableColumn<Project, Integer> tcAvailability;
    @FXML
    private TableColumn<Project, String> tcLinkedOrganization;
    @FXML
    private TableColumn<Project, String> tcProjectManager;

    private ObservableList<Project> projects;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpTable();
        loadTableData();
    }

    private void setUpTable() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcDepartment.setCellValueFactory(new PropertyValueFactory<>("deparment"));
        tcLinkedOrganization.setCellValueFactory(new PropertyValueFactory<>("linkedOrganizationName"));
        tcProjectManager.setCellValueFactory(new PropertyValueFactory<>("projectManagerName"));
        tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tcMethodology.setCellValueFactory(new PropertyValueFactory<>("methodology"));
        tcAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }

    private void loadTableData() {
        try {
            projects = FXCollections.observableArrayList();
            ArrayList<Project> projectsFromDB = ProjectDAO.obtainProjects();
            if (projectsFromDB != null) {
                projects.addAll(projectsFromDB);
                tvProjects.setItems(projects);
            }
        } catch (SQLException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error en la conexión",
                    "Hubo un error al conectar con la base de datos. Por favor, inténtelo más tarde.");
            closeWindow();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) tvProjects.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onRegisterButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/practicasprofesionalespf/view/coordinator/FXMLLinkedOrganization.fxml"));
            Parent root = loader.load();

            FXMLLinkedOrganizationController controller = loader.getController();
            controller.setNotifier(this);

            Stage stage = new Stage();
            stage.setTitle("Organizaciones Vinculadas");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error", "No se pudo cargar la ventana de organizaciones vinculadas.");
        }
    }

  @FXML
    private void onUpdateButtonClicked(ActionEvent event) {
        Project selectedProject = tvProjects.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/practicasprofesionalespf/view/coordinator/FXMLUpdateProject.fxml"));
                Parent root = loader.load();

                FXMLUpdateProjectController controller = loader.getController();
                
                controller.setProjectToUpdate(selectedProject);

                Stage stage = new Stage();
                stage.setTitle("Actualizar Proyecto");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();
                
                loadTableData();

            } catch (IOException e) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Interfaz", "No se pudo cargar la ventana de actualización.");
                e.printStackTrace();
            }
        } else {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Selección requerida", "Por favor, seleccione un proyecto de la tabla para poder actualizarlo.");
        }
    }

    @FXML
    private void onComeBackButtonClicked(ActionEvent event) {
        closeWindow();
    }

    @Override
    public void successfulOperation(String type, String name) {
        loadTableData();
    }
}
