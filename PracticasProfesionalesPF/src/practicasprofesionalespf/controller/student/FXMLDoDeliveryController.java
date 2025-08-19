package practicasprofesionalespf.controller.student;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import practicasprofesionalespf.model.SessionManager;
import practicasprofesionalespf.model.dao.DeliveryDAO;
import practicasprofesionalespf.model.dao.InitialDocumentDAO;
import practicasprofesionalespf.model.dao.RecordDAO;
import practicasprofesionalespf.model.dao.StudentDeliveryDAO;
import practicasprofesionalespf.model.enums.Status;
import practicasprofesionalespf.model.pojo.Delivery;
import practicasprofesionalespf.model.pojo.InitialDocument;
import practicasprofesionalespf.model.pojo.OperationResult;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.utils.ConvertionUtils;
import practicasprofesionalespf.utils.Utils;
import practicasprofesionalespf.utils.WindowUtils;
import practicasprofesionalespf.validation.FormValidator;

public class FXMLDoDeliveryController implements Initializable {

    @FXML
    private Label lbDeliveryName;
    @FXML
    private Label lbStartDate;
    @FXML
    private Label lbEndDate;
    @FXML
    private TextArea taComments;
    
    private Student student;
    private Delivery delivery;
    File documentFile;
    File destinationDirectory;
    String filePath;
    Stage chooseStage;
    @FXML
    private Label lbFileName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.student = SessionManager.getInstance().getLoggedInStudent();
        this.destinationDirectory = new File("docs/initial");
    }

    public void initializeData(Delivery delivery, Stage chooseStage){
        this.delivery = delivery;
        this.chooseStage = chooseStage;
        loadData();
    }
    
    public void loadData(){
        String styledStartDate = ConvertionUtils.dateTimeChanger(delivery.getStartDate());
        String styledEndDate = ConvertionUtils.dateTimeChanger(delivery.getEndDate());
        lbDeliveryName.setText(delivery.getName());
        lbStartDate.setText(styledStartDate);
        lbEndDate.setText(styledEndDate);
    }
    
    private void closeWindow(){
      ((Stage) lbEndDate.getScene().getWindow()).close();
    }

    @FXML
    private void onUploadButtonClicked(ActionEvent event) {
        showFileDialog();
    }
    
    private void showFileDialog(){
        FileChooser dialogSelection = new FileChooser();
        dialogSelection.setTitle("Selecciona un archivo");
        FileChooser.ExtensionFilter fileFilter = new FileChooser.ExtensionFilter("Archivos pdf (.pdf)", "*.pdf");
        dialogSelection.getExtensionFilters().add(fileFilter);
        documentFile = dialogSelection.showOpenDialog(Utils.getStageComponent(taComments));
        if(documentFile != null){
           safeFile();
           lbFileName.setText(documentFile.getName()); 
        }    
    }
    
    private void safeFile(){
        
        try {
            if(!destinationDirectory.exists()){
                destinationDirectory.mkdirs();
            }
            
            Path destination = new File(destinationDirectory, documentFile.getName()).toPath();
            
            Files.copy(documentFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            filePath = destination.toString();
        } catch (IOException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error al subir el archivo", 
                    "No se pudo subir el archivo, intentelo más tarde");
            closeWindow();
        }
    }

    @FXML
    private void onDeliverButtonClicked(ActionEvent event) {
        
        if(FormValidator.isTextAreaEmpty(taComments.getText())){
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "No se proporcionó un comentario", 
                                "No se proporcionó un comentario, deja alguno por favor");
            return; 
        }
        
        if(!FormValidator.isLengthValid(taComments.getText(), 255)){
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Comentario invalido", 
                    "El comentario que proporcionaste es extenso, deja uno más corto");
            return;
        }
        
        
        if(documentFile == null){
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Archivo no seleccionado", 
                                "Debes seleccionar un archivo antes de realizar la entrega.");
            return; 
        }
        
        
        getDocumentInformation();
        WindowUtils.closeCurrentWindow(event);
        if(chooseStage != null)
            chooseStage.close();
    }
    
    
    
    private void getDocumentInformation(){
        
        InitialDocument initialDocument = new InitialDocument();
        
        initialDocument.setName(documentFile.getName());
        initialDocument.setDate(LocalDate.now().toString());
        initialDocument.setDelivered(true);
        initialDocument.setStatus(Status.ENTREGADO);
        initialDocument.setFilePath(filePath);
        
        try{
            OperationResult insertResult = InitialDocumentDAO.safeADocument(initialDocument);
            System.out.println("📄 ID del documento guardado: " + initialDocument.getIdInitialDocument());
            if(!insertResult.isError()){
                finishDelivery(initialDocument);
            }else{
               Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error al guardar documento", insertResult.getMessage()); 
            }
        }catch(SQLException ex){
            ex.printStackTrace();
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de conexión", 
                   "No hay conexión con la base de datos, inténtelo más tarde");
            closeWindow();
            
        }
    }
    
    private void finishDelivery(InitialDocument initialDocument){
    try {
        int initialDocumentId = initialDocument.getIdInitialDocument();
        int idRecord = RecordDAO.obtainIDRecordWithStudentId(student.getIdStudent());

        if (!DeliveryDAO.isDuplicate(idRecord, delivery.getIdDelivery())) {
            OperationResult updateResult = DeliveryDAO.makeADelivery(initialDocumentId, delivery.getIdDelivery(), taComments.getText());

            if (!updateResult.isError()) {
                
                OperationResult studentDeliveryResult = StudentDeliveryDAO.saveStudentDelivery(student.getIdStudent(), delivery.getIdDelivery());

                if (!studentDeliveryResult.isError()) {
                    Utils.showSimpleAlert(Alert.AlertType.CONFIRMATION, 
                            "Éxito", "Documento entregado correctamente.");
                } else {
                    Utils.showSimpleAlert(Alert.AlertType.WARNING, "Entrega registrada, pero...", 
                            "El documento se entregó, pero no se pudo registrar en el historial de entregas.");
                }

            } else {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error al realizar la entrega", updateResult.getMessage());
            }
        } else {
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Entrega duplicada", 
                    "Ya entregaste un documento para esta entrega");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de conexión", 
               "No hay conexión con la base de datos, inténtelo más tarde");
        closeWindow();
    }
}

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        WindowUtils.whenCancelClicked(lbEndDate);
    }
    
}
