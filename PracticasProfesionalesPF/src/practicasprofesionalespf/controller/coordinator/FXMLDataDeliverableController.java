package practicasprofesionalespf.controller.coordinator;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import practicasprofesionalespf.model.dao.DeliveryDAO;
import practicasprofesionalespf.model.dao.DocumentDAO;
import practicasprofesionalespf.model.dao.RecordDAO;
import practicasprofesionalespf.model.enums.DeliveryType;
import practicasprofesionalespf.model.pojo.Delivery;
import practicasprofesionalespf.model.pojo.DocumentCatalog;
import practicasprofesionalespf.model.pojo.OperationResult;
import practicasprofesionalespf.model.pojo.Record;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.utils.Utils;

public class FXMLDataDeliverableController implements Initializable {

    @FXML
    private ComboBox<String> cbTipoEntrega;
    @FXML
    private ComboBox<DocumentCatalog> cbNombreDocumento;
    @FXML
    private TextArea taDescription;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private ComboBox<String> cbStartHour;
    @FXML
    private ComboBox<String> cbStartMinute;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private ComboBox<String> cbEndHour;
    @FXML
    private ComboBox<String> cbEndMinute;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnGoBack;

    private Student selectedStudent;
    private ArrayList<DocumentCatalog> fullCatalog;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateTimeComboBoxes();
    }
    
    public void initializeData(Student student) {
        this.selectedStudent = student;
        loadFullCatalog();
        configureTypeComboBox();
        configureListeners();
    }

    private void loadFullCatalog() {
        try {
            fullCatalog = DocumentDAO.getAllDocumentCatalog();
        } catch (SQLException e) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de conexión", "No se pudo conectar con la base de datos. Por favor, inténtelo más tarde.");
            closeWindow();
        }
    }
    
    private void populateTimeComboBoxes() {
        for (int i = 0; i < 24; i++) {
            String hour = String.format("%02d", i);
            cbStartHour.getItems().add(hour);
            cbEndHour.getItems().add(hour);
        }
        for (int i = 0; i < 60; i++) {
            String minute = String.format("%02d", i);
            cbStartMinute.getItems().add(minute);
            cbEndMinute.getItems().add(minute);
        }
    }

    private void configureTypeComboBox() {
        cbTipoEntrega.getItems().addAll("Documento inicial", "Reporte");
        cbNombreDocumento.setDisable(true);
    }

    private void configureListeners() {
        cbTipoEntrega.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue == null) {
                cbNombreDocumento.setDisable(true);
                return;
            }
            
            cbNombreDocumento.setDisable(false);
            cbNombreDocumento.getSelectionModel().clearSelection();
            cbNombreDocumento.getItems().clear();
            
            String typeToFilter = newValue.equals("Documento inicial") ? "INITIAL" : "REPORT";
            

            ObservableList<DocumentCatalog> filteredDocs = FXCollections.observableArrayList(
                fullCatalog.stream()
                    .filter(doc -> doc.getType().equals(typeToFilter))
                    .collect(Collectors.toList())
            );
            cbNombreDocumento.setItems(filteredDocs);
        });
    }

    @FXML
    private void btnSubmitClicked(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            Record studentRecord = RecordDAO.getRecordByStudent(selectedStudent.getIdStudent());
            if (studentRecord == null) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error", "No se pudo encontrar el expediente del estudiante.");
                return;
            }

            DocumentCatalog selectedDoc = cbNombreDocumento.getSelectionModel().getSelectedItem();
            
            Delivery newDelivery = new Delivery();
            newDelivery.setIdRecord(studentRecord.getIdRecord());
            newDelivery.setDescription(taDescription.getText().trim());
            
            newDelivery.setName(selectedDoc.getName());
            if (selectedDoc.getType().equals("INITIAL")) {
                newDelivery.setDeliveryType(DeliveryType.INITIAL_DOCUMENT);
            } else if (selectedDoc.getType().equals("REPORT")) {
                newDelivery.setDeliveryType(DeliveryType.REPORT);
            }
            
            // Construimos el LocalDateTime completo
            LocalDateTime startDate = LocalDateTime.of(dpStartDate.getValue(), 
                    LocalTime.of(Integer.parseInt(cbStartHour.getValue()), Integer.parseInt(cbStartMinute.getValue())));
            
            LocalDateTime endDate = LocalDateTime.of(dpEndDate.getValue(), 
                    LocalTime.of(Integer.parseInt(cbEndHour.getValue()), Integer.parseInt(cbEndMinute.getValue())));
            
            newDelivery.setStartDate(startDate);
            newDelivery.setEndDate(endDate);

            OperationResult result = DeliveryDAO.scheduleDelivery(newDelivery);

            if (!result.isError()) {
                Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Éxito", "Entregable programado correctamente.");
                closeWindow();
            } else {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error", result.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión", "ERROR: No hay conexión con la Base de Datos. Inténtelo nuevamente.");
        }
    }

    private boolean validateFields() {
        if (cbTipoEntrega.getSelectionModel().isEmpty() || cbNombreDocumento.getSelectionModel().isEmpty()) {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Campos Vacíos", "Debe seleccionar un tipo y nombre de entregable.");
            return false;
        }
        if (dpStartDate.getValue() == null || dpEndDate.getValue() == null || 
            cbStartHour.getSelectionModel().isEmpty() || cbStartMinute.getSelectionModel().isEmpty() ||
            cbEndHour.getSelectionModel().isEmpty() || cbEndMinute.getSelectionModel().isEmpty()) {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Campos Vacíos", "Debe seleccionar fecha y hora tanto de inicio como de fin.");
            return false;
        }
        
        LocalDateTime startDateTime = LocalDateTime.of(dpStartDate.getValue(), 
                    LocalTime.of(Integer.parseInt(cbStartHour.getValue()), Integer.parseInt(cbStartMinute.getValue())));
        LocalDateTime endDateTime = LocalDateTime.of(dpEndDate.getValue(), 
                    LocalTime.of(Integer.parseInt(cbEndHour.getValue()), Integer.parseInt(cbEndMinute.getValue())));

        if (endDateTime.isBefore(startDateTime)) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Fechas Inválidas", "La fecha y hora de fin no pueden ser anteriores a la de inicio.");
            return false;
        }
        if (taDescription.getText().trim().length() > 255) {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Descripción muy larga", "La descripción no puede exceder los 255 caracteres.");
            return false;
        }
        return true;
    }

    @FXML
    private void goBack(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnSubmit.getScene().getWindow();
        stage.close();
    }
}