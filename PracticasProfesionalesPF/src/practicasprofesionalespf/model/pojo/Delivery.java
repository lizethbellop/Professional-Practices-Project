package practicasprofesionalespf.model.pojo;

import java.time.LocalDateTime;
import practicasprofesionalespf.model.enums.DeliveryType;

public class Delivery {
    private int idDelivery;
    private int idRecord;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private DeliveryType deliveryType;
    private int idInitialDocument;
    private int idfinalDocument;
    private int ReportDocument;
    
    private String filePath; 

    public Delivery() {
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public int getIdDelivery() {
        return idDelivery;
    }

    public void setIdDelivery(int idDelivery) {
        this.idDelivery = idDelivery;
    }

    public int getIdRecord() {
        return idRecord;
    }

    public void setIdRecord(int idRecord) {
        this.idRecord = idRecord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
     public String getDescription() { 
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public int getIdInitialDocument() {
        return idInitialDocument;
    }

    public void setIdInitialDocument(int idInitialDocument) {
        this.idInitialDocument = idInitialDocument;
    }

    public int getIdfinalDocument() {
        return idfinalDocument;
    }

    public void setIdfinalDocument(int idfinalDocument) {
        this.idfinalDocument = idfinalDocument;
    }

    public int getReportDocument() {
        return ReportDocument;
    }

    public void setReportDocument(int ReportDocument) {
        this.ReportDocument = ReportDocument;
    }
}