package practicasprofesionalespf.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ConvertionUtils {
    
    public static double convertToDouble(String numberStr){
        numberStr = numberStr.replace(",", ".");
        return Double.parseDouble(numberStr); 
    }
    
    public static String dateChanger(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String styledDate = date.format(formatter);
        return styledDate;
    }
    
    public static String dateTimeChanger(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String styledDateTime = dateTime.format(formatter);
        return styledDateTime;
    }
    
    public static String dateChanger(String dateString) throws DateTimeParseException{
        
            LocalDate date = LocalDate.parse(dateString);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String styledDate = date.format(outputFormatter);
            return styledDate;
    }
    
    
}
