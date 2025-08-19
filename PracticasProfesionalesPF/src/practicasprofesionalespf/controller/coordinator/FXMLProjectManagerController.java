package practicasprofesionalespf.controller.coordinator;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import practicasprofesionalespf.interFace.INotification;
import practicasprofesionalespf.model.dao.ProjectManagerDAO;
import practicasprofesionalespf.model.pojo.LinkedOrganization;
import practicasprofesionalespf.model.pojo.ProjectManager;
import practicasprofesionalespf.utils.Utils;

public class FXMLProjectManagerController implements Initializable {

    @FXML
    private TableView<ProjectManager> tvProjectManagers;
    @FXML
    private TableColumn<ProjectManager, String> tcName;
    @FXML
    private TableColumn<ProjectManager, String> tcEmail;
    @FXML
    private TableColumn<ProjectManager, String> tcPosition;
    @FXML
    private TableColumn<ProjectManager, String> tcPhone;
    @FXML
    private Button btnAccept;
    @FXML
    private Button btnCancel;

    private LinkedOrganization selectedOrganization;
    private INotification notifier;

    public void setNotifier(INotification notifier) {
        this.notifier = notifier;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTable();
    }

    // Método para inicializar los datos para seleccionar un responsable
    public void initializeData(LinkedOrganization organization, INotification notifier) {
        this.selectedOrganization = organization;
        this.notifier = notifier;
        loadProjectManagers(); 
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        closeWindow();
    }

    private void configureTable() {
        tcName.setCellValueFactory(cellData -> {
            ProjectManager manager = cellData.getValue();
            String fullName = manager.getFirstName() + " " + manager.getLastNameFather() + " " + manager.getLastNameMother();
            return new SimpleStringProperty(fullName);
        });

        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

    private void loadProjectManagers() {
        if (selectedOrganization == null) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "No hay Organizacion Vinculada", 
                    "Debes elegir una organización vinculada para continuar");
            return;
        }
        try {
            ArrayList<ProjectManager> projectManagers = ProjectManagerDAO.obtainProjectManagersByOrganization(selectedOrganization.getIdLinkedOrganization());
            
            if (projectManagers.isEmpty()) {
                Utils.showSimpleAlert(Alert.AlertType.WARNING, "Sin responsables de proyecto",
                        "Esta organización no tiene ningún responsable de proyecto registrado.");
                tvProjectManagers.setItems(FXCollections.observableArrayList()); 
            } else {
                ObservableList<ProjectManager> observableProjectManagers = FXCollections.observableArrayList(projectManagers);
                tvProjectManagers.setItems(observableProjectManagers);
            }
        } catch (SQLException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error en la conexión",
                    "No se pudo conectar con la base de datos. Por favor, inténtalo más tarde.");
            ex.printStackTrace();
        }
    }

    @FXML
    private void onAcceptButtonClicked(ActionEvent event) {
        ProjectManager selectedManager = tvProjectManagers.getSelectionModel().getSelectedItem();
        if (selectedManager != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/practicasprofesionalespf/view/coordinator/FXMLProjectForm.fxml"));
                Parent root = loader.load();
                FXMLProjectFormController controller = loader.getController();

                controller.initializeData(selectedOrganization, selectedManager, notifier);

                Stage stage = new Stage();
                stage.setTitle("Registrar Nuevo Proyecto"); 
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.show();
                this.closeWindow();

            } catch (IOException e) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Interfaz", "No se pudo cargar el formulario del proyecto.");
                e.printStackTrace();
            }
        } else {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Selección requerida", "Por favor, seleccione un responsable de proyecto de la tabla.");
        }
    }

    private void closeWindow() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }
}