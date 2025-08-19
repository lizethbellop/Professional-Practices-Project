package practicasprofesionalespf.model.pojo;

import java.time.LocalDate;

public class PresentationEvaluation {
    private int idEvaluation;
    private String title;
    private String date;
    private double grade;
    private String observations;
    private int idRecord;

    public PresentationEvaluation() {
    }

    public PresentationEvaluation(int idEvaluation, String title, String date, double grade, String observations, int idRecord) {
        this.idEvaluation = idEvaluation;
        this.title = title;
        this.date = date;
        this.grade = grade;
        this.observations = observations;
        this.idRecord = idRecord;
    }

    public int getIdEvaluation() {
        return idEvaluation;
    }

    public void setIdEvaluation(int idEvaluation) {
        this.idEvaluation = idEvaluation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public int getIdRecord() {
        return idRecord;
    }

    public void setIdRecord(int idRecord) {
        this.idRecord = idRecord;
    }

  
}
