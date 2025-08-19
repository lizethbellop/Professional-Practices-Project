package practicasprofesionalespf.controller.student;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import practicasprofesionalespf.PracticasProfesionalesPF;
import practicasprofesionalespf.model.SessionManager;
import practicasprofesionalespf.model.dao.DeliveryDAO;
import practicasprofesionalespf.model.pojo.Delivery;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.utils.ConvertionUtils;
import practicasprofesionalespf.utils.Utils;
import practicasprofesionalespf.utils.WindowUtils;

public class FXMLChooseAssignmentController implements Initializable {

    @FXML
    private TableView<Delivery> tvDeliveries;
    @FXML
    private TableColumn tcDeliveryName;
    @FXML
    private TableColumn tcStartDate;
    @FXML
    private TableColumn tcEndDate;
    @FXML
    private TableColumn tcType;
    
    private Student currentStudent;
    private ObservableList<Delivery> deliveries;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.currentStudent = SessionManager.getInstance().getLoggedInStudent();
        
        if (this.currentStudent != null) {
            setupTable();
            loadTableData();
        } else {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Sesión", "No se pudo recuperar la información del estudiante.");
        }
    } 
    
 
    private void setupTable(){
        tcDeliveryName.setCellValueFactory(new PropertyValueFactory("name"));
        tcStartDate.setCellValueFactory(new PropertyValueFactory("startDate"));
        tcEndDate.setCellValueFactory(new PropertyValueFactory("endDate"));
        tcType.setCellValueFactory(new PropertyValueFactory("deliveryType"));
        
        tcStartDate.setCellFactory(column -> new TableCell<Delivery, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(ConvertionUtils.dateTimeChanger(item));
                }
            }
        });
        
        tcEndDate.setCellFactory(column -> new TableCell<Delivery, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(ConvertionUtils.dateTimeChanger(item));
                }
            }
        });
    }
    
    private void loadTableData(){
      try{
          deliveries = FXCollections.observableArrayList();
          ArrayList<Delivery> deliveriesDAO = DeliveryDAO.obtainDelivery(currentStudent.getIdStudent());
          deliveries.addAll(deliveriesDAO);
          tvDeliveries.setItems(deliveries);
      }catch(SQLException ex){
          ex.printStackTrace();
         Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error al cargar la tabla", "Lo sentimos, "
            + "por el momento no se puede mostrar la información de las entregas pendientes. "
            + "Por favor inténtelo más tarde"); 
         closeWindow();
      }  
    }
    
    private void closeWindow(){
      ((Stage) tvDeliveries.getScene().getWindow()).close();
    }

    @FXML
    private void onAcceptButtonClicked(ActionEvent event) {
        Delivery delivery = tvDeliveries.getSelectionModel().getSelectedItem();
        
        if(delivery != null){
            isDateValid(delivery);
        }
        
        
    }
    
    private void isDateValid(Delivery delivery){
        if(LocalDateTime.now().isBefore(delivery.getEndDate()))
            goToDoDelivery(delivery);
        else
            Utils.showSimpleAlert(Alert.AlertType.WARNING, "Problemas con la entrega", 
                    "Estas fuera del rango de entrega del documento, verifica con tu maestro");
    }
    
    private void goToDoDelivery(Delivery delivery){
    try{
        Stage deliveryStage = new Stage();
        FXMLLoader loader = new FXMLLoader(PracticasProfesionalesPF.class.getResource("view/student/FXMLDoDelivery.fxml"));
        Parent view = loader.load();
        
        FXMLDoDeliveryController controller = loader.getController();
        Stage chooseStage = (Stage) tvDeliveries.getScene().getWindow();
        controller.initializeData(delivery,chooseStage); 
        
        Scene scene = new Scene(view);
        deliveryStage.setTitle("Completar entrega");
        deliveryStage.initModality(Modality.APPLICATION_MODAL);
        deliveryStage.setScene(scene);
        deliveryStage.showAndWait();
    } catch (IOException ex) {
        Logger.getLogger(FXMLChooseAssignmentController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        WindowUtils.whenCancelClicked(tvDeliveries);
    }
    
    
    
}
