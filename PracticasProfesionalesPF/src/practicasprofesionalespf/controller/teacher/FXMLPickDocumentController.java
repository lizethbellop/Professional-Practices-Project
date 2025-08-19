package practicasprofesionalespf.controller.teacher;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practicasprofesionalespf.PracticasProfesionalesPF;
import practicasprofesionalespf.interFace.INotification;
import practicasprofesionalespf.model.dao.DocumentDAO;
import practicasprofesionalespf.model.dao.FinalDocumentDAO;
import practicasprofesionalespf.model.dao.InitialDocumentDAO;
import practicasprofesionalespf.model.dao.ProjectDAO;
import practicasprofesionalespf.model.dao.RecordDAO;
import practicasprofesionalespf.model.dao.ReportDAO;
import practicasprofesionalespf.model.pojo.FinalDocument;
import practicasprofesionalespf.model.pojo.InitialDocument;
import practicasprofesionalespf.model.pojo.Report;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.model.wrapper.DocumentWrapper;
import practicasprofesionalespf.utils.Utils;
import practicasprofesionalespf.utils.WindowUtils;

public class FXMLPickDocumentController implements Initializable, INotification {
    private Student student;
    @FXML
    private TableView<DocumentWrapper> tvDocuments;
    @FXML
    private TableColumn tcName;
    @FXML
    private TableColumn tcDate;
    @FXML
    private Label lbName;
    @FXML
    private Label lbEnrollment;
    ObservableList<DocumentWrapper> documents;
    String projectName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void initializeData(Student student){
        this.student = student;
        setUpTable();
        loadTableData();
    }
    
    private void setUpTable(){
        tcName.setCellValueFactory(new PropertyValueFactory("name"));
        tcDate.setCellValueFactory(new PropertyValueFactory("date"));
    }
    
    private void loadTableData(){
        try {
            int idRecord = RecordDAO.obtainIDRecordWithStudentId(student.getIdStudent());
            List<InitialDocument> initialDocs = InitialDocumentDAO.obtainInitialDocument(idRecord);
            List<FinalDocument> finalDocs = FinalDocumentDAO.obtainFinalDocument(idRecord);

            List<Report> reports = ReportDAO.obtainReportsByRecord(idRecord); 
            
            documents = FXCollections.observableArrayList();
            addInitialDocuments(initialDocs);
            addFinalDocuments(finalDocs);
            addReports(reports);
            
            if (documents.isEmpty()) {
                Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Sin entregas", 
                    "No hay entregas de este estudiante");
            }
            tvDocuments.setItems(documents);
            
        } catch (SQLException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error al cargar la tabla", "Lo sentimos, "
            + "por el momento no se puede mostrar la información de los documentos. "
            + "Por favor inténtelo más tarde");
        }
    }
    
    private void addInitialDocuments(List<InitialDocument> initialDocs){
        for (InitialDocument initialDoc : initialDocs){
            documents.add(new DocumentWrapper(DocumentWrapper.DocumentType.INITIAL, 
                    initialDoc.getName(), initialDoc.getDate(), 
                    initialDoc.getFilePath(), initialDoc));
        }
    }
    
    private void addFinalDocuments(List<FinalDocument> finalDocs){
        for (FinalDocument finalDoc : finalDocs){
            documents.add(new DocumentWrapper(DocumentWrapper.DocumentType.FINAL, 
                    finalDoc.getName(), finalDoc.getDate(), 
                    finalDoc.getFilePath(), finalDoc));
        }
    }
    
    private void addReports(List<Report> reports){
       for (Report report : reports){
            documents.add(new DocumentWrapper(DocumentWrapper.DocumentType.REPORT, 
                    report.getName(), report.getDate(), 
                    report.getFilePath(), report));
        } 
    }
    
    private void closeWindow(){
      ((Stage) tvDocuments.getScene().getWindow()).close();
    }
    
    private void obtainProjectName(){
        try{
          projectName = ProjectDAO.obtainProjectName(student.getIdStudent());
        }catch(SQLException ex){
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de conexión", 
                   "No hay conexión con la base de datos, inténtelo más tarde");
            closeWindow();
        }
    }

    @FXML
    private void onAcceptButtonClicked(ActionEvent event) {
        obtainProjectName();
        DocumentWrapper selectedDoc = tvDocuments.getSelectionModel().getSelectedItem();
        
        if(selectedDoc != null)
            goToValidateDocument(selectedDoc);
        else
          Utils.showSimpleAlert(Alert.AlertType.WARNING, "Selecciona un documento", 
                    "Para continuar con la evaluación, necesitas elegir un documento");  
        
    }
    
    private void goToValidateDocument(DocumentWrapper document){
        try {
            Stage validateStage = new Stage();
            FXMLLoader loader = new FXMLLoader(PracticasProfesionalesPF.class.getResource("view/teacher/FXMLValidateDocument.fxml"));
            Parent view = loader.load();
            
            FXMLValidateDocumentController controller = loader.getController();
            Stage currentStage = (Stage) tvDocuments.getScene().getWindow();
            controller.initializeData(student, document, projectName, currentStage, this);
            Scene scene = new Scene(view);
            validateStage.setTitle("Validar documento");
            validateStage.initModality(Modality.APPLICATION_MODAL);
            validateStage.setScene(scene);
            validateStage.show();
        } catch (IOException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error con la interfaz", 
                    "No se pudo abrir la ventana, intentalo más tarde");
        }
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        WindowUtils.closeWindow(tvDocuments);
    }

    @Override
    public void successfulOperation(String type, String name) {
        System.out.println("Operation: " + type + "with the title: " + name + "succesful");
        loadTableData();
    }
    
}
