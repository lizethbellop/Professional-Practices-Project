package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.EvaluationCriteria;

public class EvaluationCriteriaDAO {
    
    public static ArrayList<EvaluationCriteria> obtainCriteria() throws SQLException{
        ArrayList<EvaluationCriteria> criteria = new ArrayList<>();
        
        String sqlQuery = "SELECT idCriteria, description, value FROM EvaluationCriteria";
        
        try(Connection dbConnection = new DBConnection().createConnection();
            PreparedStatement query = dbConnection.prepareStatement(sqlQuery)){
            
            try (ResultSet result = query.executeQuery()){
                while(result.next())
                    criteria.add(convertCriteriaCatalog(result));
            }
        }
        
        return criteria;
    }
    
    public static EvaluationCriteria convertCriteriaCatalog(ResultSet result) throws SQLException{
        EvaluationCriteria criteria = new EvaluationCriteria();
        criteria.setIdCriteria(result.getInt("idCriteria"));
        criteria.setDescription(result.getString("description"));
        criteria.setValue(result.getDouble("value"));
        
        return criteria;
    }
    
}
