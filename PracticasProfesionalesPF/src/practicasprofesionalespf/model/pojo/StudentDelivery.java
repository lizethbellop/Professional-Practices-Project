package practicasprofesionalespf.model.pojo;

import java.time.LocalDateTime;

public class StudentDelivery {
    private int idStudentDelivery;
    private int studentId;
    private int deliveryId;
    private LocalDateTime deliveredAt;

    public StudentDelivery() {
    }

    public StudentDelivery(int idStudentDelivery, int studentId, int deliveryId, LocalDateTime deliveredAt) {
        this.idStudentDelivery = idStudentDelivery;
        this.studentId = studentId;
        this.deliveryId = deliveryId;
        this.deliveredAt = deliveredAt;
    }

    public int getIdStudentDelivery() {
        return idStudentDelivery;
    }

    public void setIdStudentDelivery(int idStudentDelivery) {
        this.idStudentDelivery = idStudentDelivery;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }
    
    
    
}
