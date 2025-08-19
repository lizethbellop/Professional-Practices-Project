package practicasprofesionalespf.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Utils {
    
    public static void showSimpleAlert(Alert.AlertType type, String title, String content){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        
        if (type == Alert.AlertType.NONE) {
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        }
        alert.showAndWait();
    }
    
    public static boolean showConfirmationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        ButtonType okButton = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == okButton;
    }
    
    public static Map<String, String> buildFieldMap(List<TextField> fields) {
        Map<String, String> fieldMap = new HashMap<>();
    
        for (TextField field : fields) {
            if (field.getId() != null) {
                fieldMap.put(field.getId(), field.getText().trim());
            }
        }
    
        return fieldMap;
    }
    
    public static Stage getStageComponent(Control component){
        return (Stage) component.getScene().getWindow();
    }
    
    
}
