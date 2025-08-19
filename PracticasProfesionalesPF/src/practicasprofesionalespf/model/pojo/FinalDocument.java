package practicasprofesionalespf.model.pojo;

import practicasprofesionalespf.model.enums.Status;

public class FinalDocument {
    private int idFinalDocument;
    private String name;
    private String date;
    private boolean delivered;
    private Status status;
    private String filePath;
    private String observations;
    private double grade;

    public FinalDocument() {
    }

    public FinalDocument(int idFinalDocument, String name, String date, boolean delivered, Status status, String filePath, String observations, double grade) {
        this.idFinalDocument = idFinalDocument;
        this.name = name;
        this.date = date;
        this.delivered = delivered;
        this.status = status;
        this.filePath = filePath;
        this.observations = observations;
        this.grade = grade;
    }

    public int getIdFinalDocument() {
        return idFinalDocument;
    }

    public void setIdFinalDocument(int idFinalDocument) {
        this.idFinalDocument = idFinalDocument;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
    
    
    
}
