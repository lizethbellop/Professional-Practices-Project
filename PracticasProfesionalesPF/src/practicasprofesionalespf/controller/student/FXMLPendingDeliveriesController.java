package practicasprofesionalespf.controller.student;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import practicasprofesionalespf.model.SessionManager;
import practicasprofesionalespf.model.dao.DeliveryDAO;
import practicasprofesionalespf.model.pojo.Delivery;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.utils.Utils;

public class FXMLPendingDeliveriesController implements Initializable {

    @FXML
    private TableView<Delivery> tvPendingDeliveries;
    @FXML
    private TableColumn<Delivery, String> tcName;
    @FXML
    private TableColumn<Delivery, String> tcStartDate;
    @FXML
    private TableColumn<Delivery, String> tcEndDate;
    @FXML
    private TableColumn<Delivery, String> tcStatus;
    @FXML
    private Button btnContinue;

    private Student currentStudent; 
    private ObservableList<Delivery> deliveries;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.currentStudent = SessionManager.getInstance().getLoggedInStudent();
        
        deliveries = FXCollections.observableArrayList();
        configureTable();
        
        if (this.currentStudent != null) {
            loadPendingDeliveries();
        } else {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Sesión", "No se pudo recuperar la información del estudiante.");
            closeWindow();
        }
        
        btnContinue.setDisable(true);
        tvPendingDeliveries.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnContinue.setDisable(newSelection == null);
        });
    }     
    
    
    
    private void configureTable() {
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcStartDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDate().format(formatter)));
        tcEndDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndDate().format(formatter)));
        tcStatus.setCellValueFactory(cellData -> new SimpleStringProperty("Pendiente"));
    }

    private void loadPendingDeliveries() {
        try {
            deliveries.addAll(DeliveryDAO.getPendingReportsByStudent(this.currentStudent.getIdStudent()));
            tvPendingDeliveries.setItems(deliveries);
        } catch (SQLException e) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión", "No se pudo cargar la información de las entregas.");
            closeWindow();
        }
    }
    
    @FXML
    private void btnContinueClicked(ActionEvent event) {
        Delivery selectedDelivery = tvPendingDeliveries.getSelectionModel().getSelectedItem();
        
        // RN-01: No se puede entregar un reporte después de la fecha de cierre
        if (LocalDateTime.now().isAfter(selectedDelivery.getEndDate())) {
            Utils.showSimpleAlert( Alert.AlertType.WARNING, "Fecha Límite Excedida", "No puedes realizar esta entrega porque la fecha límite ha pasado.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/practicasprofesionalespf/view/student/FXMLSubmitReport.fxml"));
            Parent view = loader.load();
            
            FXMLSubmitReportController controller = loader.getController();
            controller.initializeData(selectedDelivery, this.currentStudent.getIdStudent()); 

            Stage stage = new Stage();
            stage.setScene(new Scene(view));
            stage.setTitle("Subir Reporte");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            deliveries.clear();
            loadPendingDeliveries();

        } catch (IOException e) {
            Utils.showSimpleAlert( Alert.AlertType.ERROR, "Error de Interfaz", "No se pudo abrir la ventana de entrega.");
        }
    }

    @FXML
    private void btnExitClicked(ActionEvent event) {
        if(Utils.showConfirmationAlert("¿Seguro que quieres salir?", "Salir")) {
            closeWindow();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) tvPendingDeliveries.getScene().getWindow();
        stage.close();
    }
}