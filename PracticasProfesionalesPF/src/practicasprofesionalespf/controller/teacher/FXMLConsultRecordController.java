package practicasprofesionalespf.controller.teacher;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import practicasprofesionalespf.model.dao.DocumentDAO;
import practicasprofesionalespf.model.wrapper.DocumentWrapper;
import practicasprofesionalespf.utils.Utils;

public class FXMLConsultRecordController implements Initializable {

    @FXML
    private Label lbTitle;
    @FXML
    private TableView<DocumentWrapper> tvDocuments;
    @FXML
    private TableColumn<DocumentWrapper, String> tcDocumentName;
    @FXML
    private TableColumn<DocumentWrapper, String> tcDocumentType;
    @FXML
    private TableColumn<DocumentWrapper, String> tcDocumentDate;
    @FXML
    private Button btnShow;

    private ObservableList<DocumentWrapper> documents;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        documents = FXCollections.observableArrayList();
        configureTable();
        btnShow.setDisable(true);
        tvDocuments.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnShow.setDisable(newVal == null || newVal.getFilePath() == null || newVal.getFilePath().isEmpty());
        });
    }    
    
    public void initializeData(int idStudent) {
        loadDocuments(idStudent);
    }
    
    private void configureTable() {
        tcDocumentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcDocumentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tcDocumentDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void loadDocuments(int idStudent) {
        try {
            documents.addAll(DocumentDAO.getAllDocumentsForStudent(idStudent));
            tvDocuments.setItems(documents);
        } catch (SQLException e) {
            Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error de Conexión", "No se pudo cargar la lista de documentos.");
        }
    }

   @FXML
    private void btnShowClicked(ActionEvent event) {
        DocumentWrapper selectedDoc = tvDocuments.getSelectionModel().getSelectedItem();
        if (selectedDoc != null && selectedDoc.getFilePath() != null && !selectedDoc.getFilePath().isEmpty()) {
            try {
                File file = new File(selectedDoc.getFilePath());
                if (file.exists()) {
                    Desktop.getDesktop().open(file); 
                } else {
                    Utils.showSimpleAlert( Alert.AlertType.ERROR, "Archivo no encontrado", "El archivo no se encontró en la ruta especificada.");
                }
            } catch (IOException e) {
                Utils.showSimpleAlert(Alert.AlertType.ERROR, "Error al abrir", "No se pudo abrir el archivo.");
            }
        }
    }

    @FXML
    private void btnExitClicked(ActionEvent event) {
        Stage stage = (Stage) lbTitle.getScene().getWindow();
        stage.close();
    }
}