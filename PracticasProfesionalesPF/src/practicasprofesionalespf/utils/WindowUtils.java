package practicasprofesionalespf.utils;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.stage.Stage;

public class WindowUtils {
    
    public static void closeCurrentWindow(ActionEvent event){
        Stage confirmStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        confirmStage.close();
    }
    
    public static void whenCancelClicked(Control component){
        boolean confirmCancel = Utils.showConfirmationAlert("Cancelar", "¿Estás seguro que deseas cancelar?");
       
       if(confirmCancel)
           Utils.getStageComponent(component).close();
    }
    
    public static void askForConfirmation(Control component, String action){
        String content = String.format("¿Estás seguro que quieres %s?", action);
        
        boolean confirmation = Utils.showConfirmationAlert("Confirma el registro", 
                content);
       
       if(confirmation)
           Utils.getStageComponent(component).close();
    }
    
    public static void whenCancelClicked(ActionEvent event){
        boolean confirmCancel = Utils.showConfirmationAlert("Cancelar", "¿Estás seguro que deseas cancelar?");
       
       if(confirmCancel){
         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         stage.close();  
       }
           
    }
    
    public static void closeWindow(Control component){
        Stage currentStage = (Stage) component.getScene().getWindow();
        currentStage.close();
    }
    
    public static void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        currentStage.close();
    }
    
}
