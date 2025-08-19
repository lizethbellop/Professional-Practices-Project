package practicasprofesionalespf.model.pojo;

public class Evaluator {
    private int idEvaluator;
    private int idUser;

    public Evaluator() {
    }

    public Evaluator(int idEvaluator, int idUser) {
        this.idEvaluator = idEvaluator;
        this.idUser = idUser;
    }

    public int getIdEvaluator() {
        return idEvaluator;
    }

    public void setIdEvaluator(int idEvaluator) {
        this.idEvaluator = idEvaluator;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    
    
    
}
