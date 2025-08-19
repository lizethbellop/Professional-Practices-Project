package practicasprofesionalespf.controller.evaluator;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import practicasprofesionalespf.interFace.INotification;
import practicasprofesionalespf.model.SessionManager;
import practicasprofesionalespf.model.dao.EvaluationCriteriaDAO;
import practicasprofesionalespf.model.dao.EvaluationDAO;
import practicasprofesionalespf.model.dao.RecordDAO;
import practicasprofesionalespf.model.pojo.EvaluationCriteria;
import practicasprofesionalespf.model.pojo.Evaluator;
import practicasprofesionalespf.model.pojo.OperationResult;
import practicasprofesionalespf.model.pojo.PresentationEvaluation;
import practicasprofesionalespf.model.pojo.PresentationEvaluationCriteria;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.utils.ConvertionUtils;
import practicasprofesionalespf.utils.Utils;
import practicasprofesionalespf.utils.WindowUtils;
import practicasprofesionalespf.validation.FormValidator;

public class FXMLEvaluateStudentController implements Initializable {

    @FXML
    private Label lbEnrollmentId;
    @FXML
    private Label lbStudentName;
    @FXML
    private TextArea taObservations;
    @FXML
    private VBox vbCriteriaContainer;
    private List<CheckBox> criteriaCheckBoxes;
    private List<EvaluationCriteria> criteriaFromDB;
    private double grade;
    private PresentationEvaluation evaluation;
    private Student student;
    INotification observer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void initializeData(Student student, INotification observer){
       this.student = student;
       this.observer = observer;
       loadCriteriaDynamically();
       loadInfo();
    }
    
    
    
    private void loadInfo(){
       lbEnrollmentId.setText(student.getEnrollment());
       lbStudentName.setText(student.getFirstName() + " " + 
               student.getLastNameFather() + " " + student.getLastNameMother());
    }
    
    private void loadCriteriaDynamically(){
        vbCriteriaContainer.getChildren().clear();
        criteriaCheckBoxes = new ArrayList<>();
        
        try{
            criteriaFromDB = EvaluationCriteriaDAO.obtainCriteria();
            
            for(EvaluationCriteria criteria : criteriaFromDB){
                CheckBox cb = new CheckBox(criteria.getDescription());
                cb.setPadding(new Insets(0, 0, 0, 8));
                vbCriteriaContainer.getChildren().add(cb);
                criteriaCheckBoxes.add(cb);
            }
        }catch(SQLException ex){
           Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error con la base de datos", 
                   "No se pudieron cargar los criterios de evaluación.");
            ex.printStackTrace(); 
        }
        
    }
    
    private void calculateGrade(){
        grade = 0;
        for(int i = 0; i < criteriaCheckBoxes.size(); i++){
            if(criteriaCheckBoxes.get(i).isSelected())
                grade += 10;
        }
    }
    
    private int obtainRecord(){
        int idRecord = 0;
        
        try {
            idRecord = RecordDAO.obtainIDRecordWithStudentId(student.getIdStudent());
            
        } catch (SQLException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de conexión", 
                    "No hay conexión con la base de datos, inténtelo más tarde"); 
        }
      
        return idRecord;
    }
    
    private void safeEvaluationWithCriteria(){
        int idRecord = obtainRecord();
        String styledDate = ConvertionUtils.dateChanger(LocalDate.now());
        String title = String.format("Evaluacion: %s - %s", lbStudentName.getText(), 
            styledDate);
        Evaluator evaluator = SessionManager.getInstance().getLoggedInEvaluator();
        
        if (evaluator == null) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de sesión", 
                "No se encontró un evaluador en sesión. Por favor vuelve a iniciar sesión.");
        return;
        }
        evaluation = new PresentationEvaluation();
        evaluation.setTitle(title);
        evaluation.setDate(LocalDate.now().toString());
        evaluation.setGrade(grade);
        evaluation.setObservations(taObservations.getText());
        evaluation.setIdRecord(idRecord);
        evaluation.setIdEvaluation(evaluator.getIdEvaluator());
        
        
        List<PresentationEvaluationCriteria> evaluatedCriteria = new ArrayList<>();
        
        for(int i = 0; i < criteriaFromDB.size(); i++){
            EvaluationCriteria criteria = criteriaFromDB.get(i);
            CheckBox cb = criteriaCheckBoxes.get(i);
            
            PresentationEvaluationCriteria evaluated = 
                    new PresentationEvaluationCriteria();
            
            evaluated.setIdCriteria(criteria.getIdCriteria());
            evaluated.setCriteriaMet(cb.isSelected());
            evaluated.setValueEarned(cb.isSelected() ? 10.00 : 0.00);
            evaluatedCriteria.add(evaluated);
        }
        
        try{
            boolean success = EvaluationDAO.registerEvaluationWithCriteria(evaluation, evaluatedCriteria);
            if(success){
               Utils.showSimpleAlert(Alert.AlertType.NONE, "Evaluación guardada", 
                       "Evaluación registrada con éxito."); 
               observer.successfulOperation("Evaluación", title);
               Stage stage = (Stage) lbEnrollmentId.getScene().getWindow();
               stage.close();
            }else{
               Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error", 
                       "No se pudo registrar la evaluación.");
            }
        }catch(SQLException ex){
           Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de conexión", 
                   "No hay conexión con la base de datos");
            ex.printStackTrace(); 
        }
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        WindowUtils.whenCancelClicked(taObservations);
    }

    @FXML
    private void onEvaluateButtonClicked(ActionEvent event) {
        if(!FormValidator.isTextAreaEmpty(taObservations.getText()) && 
                FormValidator.isLengthValid(taObservations.getText(), 255)){
            calculateGrade(); 
            safeEvaluationWithCriteria();
        }else{
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "No se puede guardar la evaluación", 
                    "Necesitas proporcionar observaciones para poder guardar la evaluación");
        }
    }
    
}
