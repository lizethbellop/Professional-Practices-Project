package practicasprofesionalespf.model;

import practicasprofesionalespf.model.pojo.Academic;
import practicasprofesionalespf.model.pojo.Evaluator;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.model.pojo.User;

public class SessionManager {

    private static SessionManager instance;
    private User loggedInUser;
    private Student loggedInStudent;
    private Evaluator loggedInEvaluator;
    private Academic loggedInAcademic;

    private SessionManager() { }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public Student getLoggedInStudent() {
        return loggedInStudent;
    }

    public void setLoggedInStudent(Student loggedInStudent) {
        this.loggedInStudent = loggedInStudent;
    }

    public void cleanSession() {
        this.loggedInUser = null;
        this.loggedInStudent = null;
    }
    
    public void setLoggedInEvaluator(Evaluator evaluator) {
        this.loggedInEvaluator = evaluator;
    }
    
    public void setLoggedInAcademic(Academic academic){
        this.loggedInAcademic = academic;
    }
    
    public Academic getLoggedInAcademic(){
        return loggedInAcademic;
    }

    public Evaluator getLoggedInEvaluator() {
        return loggedInEvaluator;
    }
}