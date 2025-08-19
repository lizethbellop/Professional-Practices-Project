package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import practicasprofesionalespf.model.DBConnection;

public class CoordinatorDAO {
    
    public static Integer getCoordinatorIdByIdUser(int idUser) throws SQLException {
        Integer idCoordinator = null;
        String query = "SELECT idCoordinator FROM coordinator WHERE idUser = ?";
        
        try (Connection connection = new DBConnection().createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, idUser);
            
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    idCoordinator = rs.getInt("idCoordinator");
                }
            }
        }
        return idCoordinator;
    }
}