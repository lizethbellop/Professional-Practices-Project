package practicasprofesionalespf.controller.teacher;

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
import practicasprofesionalespf.model.SessionManager;
import practicasprofesionalespf.model.dao.StudentDAO;
import practicasprofesionalespf.model.pojo.Academic;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.utils.Utils;
import practicasprofesionalespf.utils.WindowUtils;

public class FXMLPickStudentController implements Initializable {

    @FXML
    private TableView<Student> tvStudents;
    @FXML
    private TableColumn tcEnrollmentId;
    @FXML
    private TableColumn tcStudentName;
    @FXML
    private TableColumn tcLastNameFather;
    @FXML
    private TableColumn tcLastNameMother;
    private ObservableList<Student> students;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpTable();
        loadTableData();
    } 
    
    private void setUpTable(){
        tcEnrollmentId.setCellValueFactory(new PropertyValueFactory("enrollment"));
        tcStudentName.setCellValueFactory(new PropertyValueFactory("firstName"));
        tcLastNameFather.setCellValueFactory(new PropertyValueFactory("lastNameFather"));
        tcLastNameMother.setCellValueFactory(new PropertyValueFactory("lastNameMother"));
    }
    
    private void loadTableData(){
        Academic academic = SessionManager.getInstance().getLoggedInAcademic();
        if(academic == null){
           Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de sesión", "No se encontró el académico en sesión.");
            return; 
        }
        
        int idAcademic = academic.getIdAcademic();
        try{
            students = FXCollections.observableArrayList();
            ArrayList<Student> studentsDAO = StudentDAO.obtainStudentsByGroup(idAcademic);
            students.addAll(studentsDAO);
            tvStudents.setItems(students);
        }catch(SQLException ex){
            ex.printStackTrace();
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error al cargar la tabla", "Lo sentimos, "
            + "por el momento no se puede mostrar la información de los estudiantes. "
            + "Por favor inténtelo más tarde");
            closeWindow();
        }
    }
    
    private void closeWindow(){
      ((Stage) tvStudents.getScene().getWindow()).close();
    }

    @FXML
    private void onAcceptButtonClicked(ActionEvent event) {
        Student student = tvStudents.getSelectionModel().getSelectedItem();
        
        if(student != null){
            goToPickDocument(student);
        }else{
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Selecciona un estudiante", 
                    "Para continuar con la evaluación, necesitas elegir un estudiante");
        }
    }
    
    private void goToPickDocument(Student student){
        try {
            Stage pickDocumentStage = new Stage();
            FXMLLoader loader = new FXMLLoader(PracticasProfesionalesPF.class.getResource("view/teacher/FXMLPickDocument.fxml"));
            Parent view = loader.load();
            
            FXMLPickDocumentController controller = loader.getController();
            controller.initializeData(student);
            Scene scene = new Scene(view);
            pickDocumentStage.setTitle("Elige un documento");
            pickDocumentStage.initModality(Modality.APPLICATION_MODAL);
            pickDocumentStage.setScene(scene);
            pickDocumentStage.show();
        } catch (IOException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error con la interfaz", 
                    "No se pudo abrir la ventana, intentalo más tarde");
        }
        
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        WindowUtils.closeWindow(tvStudents);
    }
    
}
