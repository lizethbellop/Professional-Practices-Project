package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.Academic;

public class AcademicDAO {
    
    public static Academic obtainAcademicByUserId(int idUser) throws SQLException{
        Academic academic = null;
        
        String sqlQuery = "SELECT idAcademic, idUser FROM Academic WHERE idUser = ?";
    
        try (Connection conn = new DBConnection().createConnection();
             PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setInt(1, idUser);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    academic = new Academic();
                    academic.setIdAcademic(rs.getInt("idAcademic"));
                    academic.setIdUser(rs.getInt("idUser"));
                }
            }
        }
        
        return academic;
    }
    
}
