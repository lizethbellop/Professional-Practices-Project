package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.PresentationEvaluation;
import practicasprofesionalespf.model.pojo.PresentationEvaluationCriteria;

public class EvaluationDAO {
    
    public static boolean registerEvaluationWithCriteria(PresentationEvaluation evaluation, 
            List<PresentationEvaluationCriteria> criteriaList) throws SQLException{
        
        boolean success = false;
        Connection dbConnection = null;
        
        try{
            dbConnection = new DBConnection().createConnection();
            dbConnection.setAutoCommit(false);
            
            int idEvaluation = 
                    PresentationEvaluationDAO.insertEvaluation(dbConnection, evaluation);
            
            
            String criteriaQuery = "INSERT INTO presentationevaluationcriteria (idEvaluation, idCriteria, "
                    + "criteriaMet, valueEarned) VALUES (?,?,?,?)";
            
            try(PreparedStatement query = dbConnection.prepareStatement(criteriaQuery)){
                for(PresentationEvaluationCriteria c : criteriaList){
                    query.setInt(1, idEvaluation);
                    query.setInt(2, c.getIdCriteria());
                    query.setBoolean(3, c.isCriteriaMet());
                    query.setDouble(4, c.getValueEarned());
                    query.addBatch();
                }
                
                query.executeBatch();
            }
            
            dbConnection.commit();
            success = true;
        }catch(SQLException e){
            if (dbConnection != null) {
                try {
                    dbConnection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
        
        return success;
    }
    
}
