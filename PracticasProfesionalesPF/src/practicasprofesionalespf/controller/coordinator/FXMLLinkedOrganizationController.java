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
import practicasprofesionalespf.model.dao.LinkedOrganizationDAO;
import practicasprofesionalespf.model.pojo.LinkedOrganization;
import practicasprofesionalespf.utils.Utils;

public class FXMLLinkedOrganizationController implements Initializable {

    @FXML
    private TableView<LinkedOrganization> tvOrganizations;
    @FXML
    private TableColumn<LinkedOrganization, String> tcName;
    @FXML
    private TableColumn<LinkedOrganization, String> tcStatus;
    @FXML
    private TableColumn<LinkedOrganization, String> tcAddress;
    @FXML
    private TableColumn<LinkedOrganization, String> tcPhone;
    @FXML
    private Button btnAccept;
    @FXML
    private Button btnCancel;

    private ObservableList<LinkedOrganization> organizations;
    private INotification notifier;

    public void setNotifier(INotification notifier) {
        this.notifier = notifier;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpTable();
        loadOrganizationsData();
    }

    private void setUpTable() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tcAddress.setCellValueFactory(cellData -> {
            LinkedOrganization org = cellData.getValue();
            return new SimpleStringProperty(org.getFullAddress());
        });

        tcPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        tcStatus.setCellValueFactory(cellData -> {
            boolean isActive = cellData.getValue().getIsActive();
            String status = isActive ? "Activo" : "Inactivo";
            return new SimpleStringProperty(status);
        });
    }

    private void loadOrganizationsData() {
        try {
            organizations = FXCollections.observableArrayList();
            ArrayList<LinkedOrganization> organizationsFromDB = LinkedOrganizationDAO.obtainLinkedOrganizations();
            if (organizationsFromDB != null) {
                organizations.addAll(organizationsFromDB);
                tvOrganizations.setItems(organizations);
            }
        } catch (SQLException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error en la conexión",
                    "Hubo un error al conectar con la base de datos. Por favor, inténtelo más tarde.");
            closeWindow();
        }
    }

    @FXML
    private void onAcceptButtonClicked(ActionEvent event) {
        LinkedOrganization selectedOrganization = tvOrganizations.getSelectionModel().getSelectedItem();
        if (selectedOrganization != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/practicasprofesionalespf/view/coordinator/FXMLProjectManager.fxml"));
                Parent root = loader.load();
                FXMLProjectManagerController controller = loader.getController();

                controller.initializeData(selectedOrganization, notifier);

                Stage stage = new Stage();
                stage.setTitle("Registrar Proyecto (Paso 2: Responsable)");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.show();
                this.closeWindow();

            } catch (IOException e) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Interfaz", "No se pudo cargar la ventana de selección de responsable.");
                e.printStackTrace();
            }
        } else {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Selección requerida", "Por favor, seleccione una organización de la tabla.");
        }
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) tvOrganizations.getScene().getWindow();
        stage.close();
    }
}
