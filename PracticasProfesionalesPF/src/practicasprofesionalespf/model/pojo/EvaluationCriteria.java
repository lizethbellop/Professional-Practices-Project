package practicasprofesionalespf.model.pojo;

public class EvaluationCriteria {
    private int idCriteria;
    private String description;
    private double value;

    public EvaluationCriteria() {
    }

    public EvaluationCriteria(int idCriteria, String description, double value) {
        this.idCriteria = idCriteria;
        this.description = description;
        this.value = value;
    }

    public int getIdCriteria() {
        return idCriteria;
    }

    public void setIdCriteria(int idCriteria) {
        this.idCriteria = idCriteria;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    
    
}
