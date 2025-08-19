package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.OperationResult;

public class StudentDeliveryDAO {

    public static OperationResult saveStudentDelivery(int studentId, int deliveryId) throws SQLException {
        String sql = "INSERT INTO StudentDelivery (idStudent, idDelivery) VALUES (?, ?)";

        try (Connection conn = new DBConnection().createConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, deliveryId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return new OperationResult(false, "Entrega registrada con éxito");
            } else {
                return new OperationResult(true, "No se pudo registrar la entrega");
            }
        }
    }

    public static boolean isDeliveryAlreadyDone(int studentId, int deliveryId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM StudentDelivery WHERE student_id = ? AND delivery_id = ?";
        try (Connection conn = new DBConnection().createConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, deliveryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

   
}
