package practicasprofesionalespf.controller.student;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import practicasprofesionalespf.model.dao.DeliveryDAO;
import practicasprofesionalespf.model.dao.RecordDAO;
import practicasprofesionalespf.model.dao.ReportDAO;
import practicasprofesionalespf.model.enums.Status;
import practicasprofesionalespf.model.pojo.Delivery;
import practicasprofesionalespf.model.pojo.Record;
import practicasprofesionalespf.model.pojo.Report;
import practicasprofesionalespf.utils.Utils;

public class FXMLSubmitReportController implements Initializable {

    @FXML
    private Label lbDeliveryName;
    @FXML
    private Label lbDueDate;
    @FXML
    private Label lbFileName;
    @FXML
    private TextArea taComments;
    @FXML
    private TextField tfHours;

    private Delivery selectedDelivery;
    private int idStudent;
    private File selectedFile;

    @FXML
    private void btnSubmitClicked(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            Record studentRecord = RecordDAO.getRecordByStudent(idStudent);
            if (studentRecord == null) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error", "No se pudo encontrar tu expediente de estudiante.");
                return;
            }

            int hoursToReport = Integer.parseInt(tfHours.getText().trim());
            String comment = taComments.getText().trim(); 
            
            String newFilePath = copyFileToDocs(selectedFile);

            Report newReport = new Report();
            newReport.setReportedHours(hoursToReport);
            newReport.setName(selectedFile.getName());
            newReport.setFilePath(newFilePath);
            newReport.setGrade(0.0);
            newReport.setDate(LocalDate.now().toString());
            newReport.setStatus(Status.ENTREGADO);
            newReport.setDelivered(true);

            int newReportId = ReportDAO.saveReport(newReport);

            if (newReportId == -1) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error en la Base de Datos", "No se pudo guardar la información del reporte.");
                return;
            }

           
            DeliveryDAO.updateDeliveryWithReport(selectedDelivery.getIdDelivery(), newReportId, comment);
            
            int totalHours = studentRecord.getHoursCount() + hoursToReport;
            RecordDAO.updateHoursCount(studentRecord.getIdRecord(), totalHours);

            Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Entrega Exitosa", "Reporte Entregado con Éxito");
            closeWindow();

        } catch (IOException e) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Archivo", "No se pudo acceder a su gestor de archivos.");
        } catch (SQLException e) {
            e.printStackTrace(); 
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión", "No hay conexión con la base de datos.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void initializeData(Delivery delivery, int idStudent) {
        this.selectedDelivery = delivery;
        this.idStudent = idStudent;
        lbDeliveryName.setText(delivery.getName());
        lbDueDate.setText(delivery.getEndDate().toLocalDate().toString());
    }

    @FXML
    private void btnSelectFileClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Reporte");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage) lbFileName.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            lbFileName.setText(selectedFile.getName());
        }
    }

    private String copyFileToDocs(File file) throws IOException {
        File docsDir = new File("docs/reports");
        if (!docsDir.exists()) {
            docsDir.mkdirs();
        }
        String newFileName = selectedDelivery.getIdRecord() + "_" + selectedDelivery.getIdDelivery() + "_" + file.getName();
        File newFile = new File(docsDir.getPath() + "/" + newFileName);
        Files.copy(file.toPath(), newFile.toPath());
        return newFile.getPath();
    }

    @FXML
    private void btnGoBackClicked(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) lbFileName.getScene().getWindow();
        stage.close();
    }
    
    private boolean validateFields() {
        if (selectedFile == null) {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Archivo Faltante", "Debes seleccionar un archivo para entregar.");
            return false;
        }
        if (tfHours.getText().trim().isEmpty()) {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Campo Obligatorio", "Debes ingresar las horas a reportar.");
            return false;
        }

        try {
            int hours = Integer.parseInt(tfHours.getText().trim());
            if (hours < 0) {
                Utils.showSimpleAlert(Alert.AlertType.WARNING, "Dato Inválido", "Las horas reportadas no pueden ser un número negativo.");
                return false;
            }
        } catch (NumberFormatException e) {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Dato Inválido", "El campo de horas solo debe contener números enteros.");
            return false;
        }

        if (taComments.getText().trim().length() > 1000) {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Comentario muy largo", "Los comentarios no pueden exceder los 1000 caracteres.");
            return false;
        }
        
        return true;
    }
}