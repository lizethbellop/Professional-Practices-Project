package practicasprofesionalespf.utils;

public class ValidationUtils {
    public static boolean isOnlyLetters(String input){
        return input.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+");
    }
    
    public static boolean isOnlyNumber(String input){
        return input.matches("\\d+");
    }
    
    public static boolean isPhoneValid(String input){
    return input.matches("\\d{10}");
    }
    
    public static boolean isValidEmail(String input){
        return input.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }
    
    public static boolean isEmpty(String input){
        return input.isEmpty();
    }
    
    public static boolean isBetween0And10(double value){
        return value >= 0 && value <= 10;
    }
    
    public static boolean isPostalCodeValid(String input) {
        return input.matches("\\d{5}");
    }
    
}
