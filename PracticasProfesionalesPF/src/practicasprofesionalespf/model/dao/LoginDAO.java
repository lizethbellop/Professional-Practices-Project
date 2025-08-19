package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.User;
import practicasprofesionalespf.utils.PasswordUtils;

public class LoginDAO {
    
    
    
    public static User verifyCredentials(String username, String password) throws SQLException{
        User sessionUser = null;
        String sqlQuery = "SELECT idUser, role, password FROM useraccount WHERE username = ?";
        
        try(Connection dbConnection = new DBConnection().createConnection(); 
            PreparedStatement query = dbConnection.prepareStatement(sqlQuery)){
            
            query.setString(1, username);
            
            try(ResultSet result = query.executeQuery()){
                if(result.next()){
                    String storedPassword = result.getString("password");
                    String userRole = result.getString("role");
                    int userId = result.getInt("idUser");
                    
                    if(PasswordUtils.checkPassword(password, storedPassword)){
                        sessionUser = convertUserRegistration(userRole, userId);
                    }
                }
            }
            
        }
        
        return sessionUser;
   
    }
    
    private static User convertUserRegistration(String role, int idUser){
        User user = new User();
        user.setIdUser(idUser);
        user.setRole(role);
        
        return user;
    }
    
    
}
