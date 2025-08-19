package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.FinalDocument;
import practicasprofesionalespf.model.pojo.OperationResult;


public class FinalDocumentDAO {
    public static ArrayList<FinalDocument> obtainFinalDocument(int idRecord) throws SQLException{
        ArrayList<FinalDocument> finalDocuments = new ArrayList<>();
        
        
        String sqlQuery = "SELECT f.idFinalDocument, f.name, f.date, f.filePath FROM FinalDocument f "
                + "JOIN Delivery d ON f.idFinalDocument = d.idFinalDocument "
                + "WHERE d.idRecord = ? AND (f.grade IS NULL OR f.grade = 0);";
        
        
        try(Connection dbConnection = new DBConnection().createConnection();
            PreparedStatement query = dbConnection.prepareStatement(sqlQuery)){
            
            query.setInt(1, idRecord);
            
            try(ResultSet result = query.executeQuery()){
                while(result.next()){
                    FinalDocument finalDocument = new FinalDocument();
                    finalDocument.setIdFinalDocument(result.getInt("idFinalDocument"));
                    finalDocument.setName(result.getString("name"));
                    finalDocument.setDate(result.getDate("date").toString());
                    finalDocument.setFilePath(result.getString("filePath"));
                    finalDocuments.add(finalDocument);
                }
                    
            }
        }
        
        return finalDocuments;
    }
    
    public static OperationResult updateDocument(FinalDocument finalDocument)throws SQLException{
        OperationResult result = new OperationResult();
        String sqlQuery = "UPDATE FinalDocument SET Grade = ?, "
                + "Observations = ? WHERE idFinalDocument = ?";
        
        try(Connection dbConnection = new DBConnection().createConnection();
            PreparedStatement update = dbConnection.prepareStatement(sqlQuery)){
            
            update.setDouble(1, finalDocument.getGrade());
            update.setString(2, finalDocument.getObservations());
            update.setInt(3, finalDocument.getIdFinalDocument());
            
            int affectedRows = update.executeUpdate();
            
            if(affectedRows == 1){
                result.setError(false);
                result.setMessage("Documento validado correctamente");
            }else{
                result.setError(true);
                result.setMessage("Lo sentimos, no se pudo completar la validación");
            }
        }
        
        return result;
    }
}
