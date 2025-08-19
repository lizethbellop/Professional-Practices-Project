package practicasprofesionalespf.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    
    public static String hashPassword(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }
    
    public static boolean checkPassword(String plainPassword, String storedPassword){
        return BCrypt.checkpw(plainPassword, storedPassword);
    }
    
}
