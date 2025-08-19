package practicasprofesionalespf.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import practicasprofesionalespf.PracticasProfesionalesPF;
import practicasprofesionalespf.model.SessionManager; 
import practicasprofesionalespf.model.dao.AcademicDAO;
import practicasprofesionalespf.model.dao.EvaluatorDAO;
import practicasprofesionalespf.model.dao.LoginDAO;
import practicasprofesionalespf.model.dao.StudentDAO;
import practicasprofesionalespf.model.pojo.Academic;
import practicasprofesionalespf.model.pojo.Evaluator;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.model.pojo.User;
import practicasprofesionalespf.utils.Utils;

public class FXMLLoginController implements Initializable {

    @FXML
    private TextField tfUser;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Label lbUserError;
    @FXML
    private Label lbPasswordError;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void onLoginButtonClicked(ActionEvent event) {
        String username = tfUser.getText();
        String password = pfPassword.getText();
        
        if(validateFields(username, password))
            validateCredentials(username, password);
    }
    
    private void validateCredentials(String username, String password){
        try{
            User sessionUser = LoginDAO.verifyCredentials(username, password);
            
            if(sessionUser != null){
                Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Credenciales correctas", "Bienvenid@ al sistema");
                SessionManager.getInstance().setLoggedInUser(sessionUser);
                defineRoleAndGo(sessionUser);
            }else{
                Utils.showSimpleAlert(Alert.AlertType.WARNING, "Credenciales incorrectas", "Usuario y/o contraseña incorrectos, por favor verifica tu informacion");
            }
        }catch(SQLException ex){
            ex.printStackTrace();
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Problemas de conexión", 
                    "Hubo un problema al intentar conectar con la base de datos.");
        }
    }
    
    private void defineRoleAndGo(User sessionUser){
        String fxmlPath = "";
        switch(sessionUser.getRole()){
            case "COORDINATOR":
                fxmlPath = "view/coordinator/FXMLCoordinatorHomepage.fxml";
                break;
            case "EVALUATOR":
                Evaluator evaluator = obtainEvaluator(sessionUser.getIdUser());
                if (evaluator != null) {
                    SessionManager.getInstance().setLoggedInEvaluator(evaluator);
                    fxmlPath = "view/evaluator/FXMLEvaluatorHomepage.fxml";
                } else {
                    Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Perfil", "No se encontró al evaluador asociado.");
                    return;
                }
                
                break;
            case "STUDENT":
                Student student = obtainStudent(sessionUser.getIdUser());
                if (student != null) {
                    SessionManager.getInstance().setLoggedInStudent(student);
                    fxmlPath = "view/student/FXMLStudentHomepage.fxml";
                } else {
                    Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Perfil", "No se encontraron los datos del estudiante.");
                    return;
                }
                break;
            case "TEACHER":
                Academic academic = obtainAcademic(sessionUser.getIdUser());
                if(academic != null){
                    SessionManager.getInstance().setLoggedInAcademic(academic);
                    fxmlPath = "view/teacher/FXMLTeacherHomepage.fxml";
                }else{
                    Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Perfil", "No se encontraron los datos del académico.");
                    return;
                }
                
                break;
        }

        if (!fxmlPath.isEmpty()) {
            goToHomepage(fxmlPath);
        }
    }
    
    private void goToHomepage(String fxmlPath){
        try{
            Stage baseStage = (Stage) tfUser.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(PracticasProfesionalesPF.class.getResource(fxmlPath));
            Parent view = loader.load();
            
            Scene homepageScene = new Scene(view);
            baseStage.setScene(homepageScene);
            baseStage.setTitle("Home");
            baseStage.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    private Student obtainStudent(int idUser){
        Student student = null;
        try {
            student = StudentDAO.obtainStudentByUser(idUser);
        } catch (SQLException ex) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de conexión", 
                    "No se pudo realizar el inicio de sesión por problemas en la base de datos");
        }
        return student;
    }
    
    private Evaluator obtainEvaluator(int idUser){
        Evaluator evaluator = null;
        
        try {
            evaluator = EvaluatorDAO.obtainEvaluatorByUserId(idUser);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de conexión", 
                    "No se pudo realizar el inicio de sesión por problemas en la base de datos");
        }
        
        return evaluator;
    }
    
    private Academic obtainAcademic(int idUser){
        Academic academic = null;
        
        try {
            academic = AcademicDAO.obtainAcademicByUserId(idUser);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de conexión", 
                    "No se pudo realizar el inicio de sesión por problemas en la base de datos");
        }
        
        return academic;
    }
    

    // ... otros métodos como validateFields y onForgottenDataClick se quedan igual ...
    private boolean validateFields(String username, String password){
        lbUserError.setText("");
        lbPasswordError.setText("");
        boolean validFields = true;
        
        if(username.isEmpty()){
            lbUserError.setText("Usuario requerido");
            validFields = false;
        }
        
        if(password.isEmpty()){
            lbPasswordError.setText("Contraseña requerida");
            validFields = false;
        }
        
        return validFields;
    }
    
    @FXML
    private void onForgottenDataClick(ActionEvent event) {
        Utils.showSimpleAlert(Alert.AlertType.INFORMATION, "Datos olvidados", 
                "Por favor contacta con administración para recibir ayuda");
    }
}