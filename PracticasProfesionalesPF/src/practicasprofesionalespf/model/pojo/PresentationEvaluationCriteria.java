package practicasprofesionalespf.model.pojo;

public class PresentationEvaluationCriteria {
   private int idEvaluation;
   private int idCriteria;
   private boolean criteriaMet;
   private double valueEarned;

    public PresentationEvaluationCriteria() {
    }

    public PresentationEvaluationCriteria(int idEvaluation, int idCriteria, boolean criteriaMet, double valueEarned) {
        this.idEvaluation = idEvaluation;
        this.idCriteria = idCriteria;
        this.criteriaMet = criteriaMet;
        this.valueEarned = valueEarned;
    }

    public int getIdEvaluation() {
        return idEvaluation;
    }

    public void setIdEvaluation(int idEvaluation) {
        this.idEvaluation = idEvaluation;
    }

    public int getIdCriteria() {
        return idCriteria;
    }

    public void setIdCriteria(int idCriteria) {
        this.idCriteria = idCriteria;
    }

    public boolean isCriteriaMet() {
        return criteriaMet;
    }

    public void setCriteriaMet(boolean criteriaMet) {
        this.criteriaMet = criteriaMet;
    }

    public double getValueEarned() {
        return valueEarned;
    }

    public void setValueEarned(double valueEarned) {
        this.valueEarned = valueEarned;
    }
   
   
}
