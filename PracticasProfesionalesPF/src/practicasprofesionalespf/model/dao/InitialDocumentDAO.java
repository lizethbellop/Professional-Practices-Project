package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.InitialDocument;
import practicasprofesionalespf.model.pojo.OperationResult;

public class InitialDocumentDAO {
    
    public static ArrayList<InitialDocument> obtainInitialDocument(int idRecord) throws SQLException{
        ArrayList<InitialDocument> initialDocuments = new ArrayList<>();
        String sqlQuery = "SELECT i.idInitialDocument, i.name, i.date, i.filePath FROM InitialDocument i "
                + "JOIN Delivery d ON i.idInitialDocument = d.idInitialDocument "
                + "WHERE d.idRecord = ? AND (i.grade IS NULL OR i.grade = 0);";
        
        
        try(Connection dbConnection = new DBConnection().createConnection();
            PreparedStatement query = dbConnection.prepareStatement(sqlQuery)){
            
            query.setInt(1, idRecord);
            
            try(ResultSet result = query.executeQuery()){
                while(result.next()){
                    InitialDocument initialDocument = new InitialDocument();
                    initialDocument.setIdInitialDocument(result.getInt("idInitialDocument"));
                    initialDocument.setName(result.getString("name"));
                    initialDocument.setDate(result.getDate("date").toString());
                    initialDocument.setFilePath(result.getString("filePath"));
                    initialDocuments.add(initialDocument);
                }
                    
            }
        }
        
        return initialDocuments;
    }
    
    public static OperationResult safeADocument(InitialDocument initialDocument) throws SQLException{
        OperationResult result = new OperationResult();
        String sqlQuery = "INSERT INTO InitialDocument (name, date, delivered, "
                + "status, filePath) VALUES (?,?,?,?,?)";
        
        try(Connection dbConnection = new DBConnection().createConnection();
            PreparedStatement insert = dbConnection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);){
           
            insert.setString(1, initialDocument.getName());
            insert.setString(2, initialDocument.getDate());
            insert.setBoolean(3, initialDocument.isDelivered());
            insert.setString(4, initialDocument.getStatus().name());
            insert.setString(5, initialDocument.getFilePath());
            int affectedRows = insert.executeUpdate();
            
            if (affectedRows == 0) {
                result.setError(true);
                result.setMessage("No se pudo guardar la información del documento");
                return result;
            }
            
            try (ResultSet generatedKeys = insert.getGeneratedKeys()){
                if(generatedKeys.next()){
                    initialDocument.setIdInitialDocument(generatedKeys.getInt(1));
                    result.setError(false);
                    result.setMessage("Información del "
                        + "documento guardada correctamente");
                } else{
                    result.setError(true);
                result.setMessage("Error: No se obtuvo el ID del documento");
                }
                    
                
            }
        }
        
        return result;
    }
    
    public static OperationResult updateDocument(InitialDocument initialDocument)throws SQLException{
        OperationResult result = new OperationResult();
        String sqlQuery = "UPDATE InitialDocument SET Grade = ?, Observations = ? "
                + "WHERE idInitialDocument = ?";
        
        try(Connection dbConnection = new DBConnection().createConnection();
            PreparedStatement update = dbConnection.prepareStatement(sqlQuery)){
            
            update.setDouble(1, initialDocument.getGrade());
            update.setString(2, initialDocument.getObservations());
            update.setInt(3, initialDocument.getIdInitialDocument());
            
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
