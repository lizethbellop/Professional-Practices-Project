package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.OperationResult;
import practicasprofesionalespf.model.pojo.Report;


public class ReportDAO {
    
    public static ArrayList<Report> obtainReport(int idRecord) throws SQLException{
        ArrayList<Report> reports = new ArrayList<>();
        
        String sqlQuery = "SELECT r.idReportDocument, r.name, r.date, r.filePath FROM ReportDocument r "
                + "JOIN Delivery d ON r.idReportDocument = d.idReportDocument "
                + "WHERE d.idRecord = ? AND (r.grade IS NULL OR r.grade = 0);";
        
        
        try(Connection dbConnection = new DBConnection().createConnection();
            PreparedStatement query = dbConnection.prepareStatement(sqlQuery)){
            
            query.setInt(1, idRecord);
            
            try(ResultSet result = query.executeQuery()){
                while(result.next()){
                    Report report = new Report();
                    report.setIdReport(result.getInt("idRecordDocument"));
                    report.setName(result.getString("name"));
                    report.setDate(result.getDate("date").toString());
                    report.setFilePath(result.getString("filePath"));
                    reports.add(report);
                }
                    
            }
        }
        
        return reports;
    }
    
    public static OperationResult updateDocument(Report report)throws SQLException{
        OperationResult result = new OperationResult();
        String sqlQuery = "UPDATE ReportDocument SET Grade = ?, "
                + "WHERE idReportDocument = ?";
        
        try(Connection dbConnection = new DBConnection().createConnection();
            PreparedStatement update = dbConnection.prepareStatement(sqlQuery)){
            
            update.setDouble(1, report.getGrade());
            update.setInt(2, report.getIdReport());
            
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
    
    

  
   public static ArrayList<Report> obtainReportsByRecord(int idRecord) throws SQLException{
        ArrayList<Report> reports = new ArrayList<>();
        
        String sqlQuery = "SELECT r.idReportDocument, r.name, r.date, r.filePath FROM ReportDocument r "
                + "JOIN Delivery d ON r.idReportDocument = d.idReportDocument "
                + "WHERE d.idRecord = ? AND (r.grade IS NULL OR r.grade = 0);";
        
        try(Connection dbConnection = new DBConnection().createConnection();
            PreparedStatement query = dbConnection.prepareStatement(sqlQuery)){
            
            query.setInt(1, idRecord);
            
            try(ResultSet result = query.executeQuery()){
                while(result.next()){
                    Report report = new Report();
                    report.setIdReport(result.getInt("idReportDocument"));
                    report.setName(result.getString("name"));
                    report.setDate(result.getDate("date").toString());
                    report.setFilePath(result.getString("filePath"));
                    reports.add(report);
                }
            }
        }
        return reports;
    }
   
    public static int saveReport(Report report) throws SQLException {
        int generatedId = -1;
        String query = "INSERT INTO ReportDocument (reportedHours, date, grade, name, delivered, status, filePath) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = new DBConnection().createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setInt(1, report.getReportedHours());
            
            preparedStatement.setDate(2, Date.valueOf(report.getDate())); 
            
            preparedStatement.setDouble(3, report.getGrade());
            preparedStatement.setString(4, report.getName());
            preparedStatement.setBoolean(5, report.isDelivered());
            preparedStatement.setString(6, report.getStatus().toString());
            preparedStatement.setString(7, report.getFilePath());

            preparedStatement.executeUpdate();
            
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        }
        return generatedId;
    }
    
    public static int obtainReportWithHours(int idReport) throws SQLException{
        int hours = -1;
        String sqlQuery = "SELECT reportedHours FROM ReportDocument WHERE idReportDocument = ?";
        
        try(Connection dbConnection = new DBConnection().createConnection();
            PreparedStatement query = dbConnection.prepareStatement(sqlQuery)){
            
            query.setInt(1, idReport);
            
            try(ResultSet result = query.executeQuery()){
                if(result.next()){
                    hours = result.getInt("reportedHours");
                }
                    
            }
        }
        
        return hours;
    }
    
}

