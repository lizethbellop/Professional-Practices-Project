package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import practicasprofesionalespf.model.pojo.PresentationEvaluation;

public class PresentationEvaluationDAO {
    
    public static int insertEvaluation(Connection dbConnection, 
            PresentationEvaluation evaluation) throws SQLException{
        
        String sqlQuery = "INSERT INTO PresentationEvaluation (title, date, grade, observations, idRecord, idEvaluator)"
                + " VALUES (?,?,?,?,?,?)";
        
        try(PreparedStatement insert = dbConnection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)){
            insert.setString(1, evaluation.getTitle());
            insert.setString(2, evaluation.getDate());
            insert.setDouble(3, evaluation.getGrade());
            insert.setString(4, evaluation.getObservations());
            insert.setInt(5, evaluation.getIdRecord());
            insert.setInt(6, evaluation.getIdEvaluation());
            int affectedRows = insert.executeUpdate();
            
            if(affectedRows == 1){
                try(ResultSet keys = insert.getGeneratedKeys()){
                    if(keys.next())
                        return keys.getInt(1);
                }
            }
        }
        
        throw new SQLException("No se pudo insertar la evaluación correctamente");
    }
    
}
