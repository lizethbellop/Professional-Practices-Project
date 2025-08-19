package practicasprofesionalespf.model.pojo;

import practicasprofesionalespf.model.enums.Status;

public class Report {
    private int idReport;
    private int reportedHours;
    private String date;
    private double grade;
    private String name;
    private boolean delivered;
    private Status status;
    private String filePath;

    public Report() {
    }

    public Report(int idReport, int reportedHours, String date, double grade, String name, boolean delivered, Status status, String filePath) {
        this.idReport = idReport;
        this.reportedHours = reportedHours;
        this.date = date;
        this.grade = grade;
        this.name = name;
        this.delivered = delivered;
        this.status = status;
        this.filePath = filePath;
    }

    public int getIdReport() {
        return idReport;
    }

    public void setIdReport(int idReport) {
        this.idReport = idReport;
    }

    public int getReportedHours() {
        return reportedHours;
    }

    public void setReportedHours(int reportedHours) {
        this.reportedHours = reportedHours;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Report{" + "idReport=" + idReport + ", reportedHours=" + reportedHours + ", date=" + date + ", grade=" + grade + ", name=" + name + ", delivered=" + delivered + ", status=" + status + ", filePath=" + filePath + '}';
    }
    
    
    
}
