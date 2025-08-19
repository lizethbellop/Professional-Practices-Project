package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.Evaluator;

public class EvaluatorDAO {
    
    public static Evaluator obtainEvaluatorByUserId(int idUser) throws SQLException {
        Evaluator evaluator = null;
        String sqlQuery = "SELECT idEvaluator, idUser FROM Evaluator WHERE idUser = ?";
    
        try (Connection conn = new DBConnection().createConnection();
             PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setInt(1, idUser);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    evaluator = new Evaluator();
                    evaluator.setIdEvaluator(rs.getInt("idEvaluator"));
                    evaluator.setIdUser(rs.getInt("idUser"));
                }
            }
        }
    
    return evaluator;
}
    
}
