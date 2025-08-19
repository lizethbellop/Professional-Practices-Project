package practicasprofesionalespf;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PracticasProfesionalesPF extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try{
            Parent view = FXMLLoader.load(getClass().getResource("view/FXMLLogin.fxml"));
            Scene loginScene = new Scene(view);
            
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Inicio de sesion");
            primaryStage.show();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
