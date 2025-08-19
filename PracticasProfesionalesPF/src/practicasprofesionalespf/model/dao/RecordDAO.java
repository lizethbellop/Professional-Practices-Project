package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.OperationResult;
import practicasprofesionalespf.model.pojo.Record;


public class RecordDAO {
    
    public static int obtainIDRecordWithStudentId(int idStudent) throws SQLException{
        int idRecord = 0;
        String sqlQuery = "SELECT idRecord FROM Record WHERE idStudent = ?";
        
        try(Connection dbConnection = new DBConnection().createConnection();
            PreparedStatement query = dbConnection.prepareStatement(sqlQuery)){
            
            query.setInt(1, idStudent);
            
            try(ResultSet result = query.executeQuery()){
                if(result.next())
                    idRecord = result.getInt("idRecord");
            }
        }
        
        return idRecord;
    }
    
    public static Record getRecordByStudent(int idStudent) throws SQLException {
        Record record = null;
        String query = "SELECT * FROM Record WHERE idStudent = ?";
        
        try (Connection connection = new DBConnection().createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, idStudent);
            
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    record = new Record();
                    record.setIdRecord(rs.getInt("idRecord"));
                    record.setIdStudent(rs.getInt("idStudent"));
                    record.setIdSubjectGroup(rs.getInt("idSubjectGroup"));
                    record.setHoursCount(rs.getInt("hoursCount"));
                    record.setPresentationPath(rs.getString("presentationPath"));
                    record.setIdTerm(rs.getInt("idTerm"));
                    record.setIdProject(rs.getInt("idProject"));
                }
            }
        }
        return record;
    }
    

    public static OperationResult assignProjectToRecord(int idRecord, int idProject) throws SQLException {
        OperationResult result = new OperationResult();
        String query = "UPDATE Record SET idProject = ? WHERE idRecord = ?";
        try (Connection connection = new DBConnection().createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, idProject);
            preparedStatement.setInt(2, idRecord);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                result.setError(false);
                result.setMessage("Asignación realizada con éxito.");
            } else {
                result.setError(true);
                result.setMessage("No se pudo realizar la asignación.");
            }
        }
        return result;
    }
    
    public static OperationResult updateHoursCount(int idRecord, int totalHours) throws SQLException {
        OperationResult result = new OperationResult();
        String query = "UPDATE Record SET hoursCount = ? WHERE idRecord = ?";
        try (Connection connection = new DBConnection().createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, totalHours);
            preparedStatement.setInt(2, idRecord);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                result.setError(false);
            } else {
                result.setError(true);
                result.setMessage("No se pudo actualizar el contador de horas.");
            }
        }
        return result;
    }
}