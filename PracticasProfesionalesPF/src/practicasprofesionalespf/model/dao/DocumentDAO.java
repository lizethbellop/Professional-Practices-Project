package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.DocumentCatalog;
import practicasprofesionalespf.model.wrapper.DocumentWrapper;

public class DocumentDAO {

    public static ArrayList<DocumentWrapper> getAllDocumentsForStudent(int idStudent) throws SQLException {
        ArrayList<DocumentWrapper> documents = new ArrayList<>();

        String query
                = "(SELECT id.name, 'INITIAL' as type, id.date, id.filePath "
                + "FROM InitialDocument id "
                + "JOIN Delivery d ON id.idInitialDocument = d.idInitialDocument "
                + "JOIN Record r ON d.idRecord = r.idRecord "
                + "WHERE r.idStudent = ? AND id.filePath IS NOT NULL) "
                + "UNION ALL "
                + "(SELECT rd.name, 'REPORT' as type, rd.date, rd.filePath "
                + "FROM ReportDocument rd "
                + "JOIN Delivery d ON rd.idReportDocument = d.idReportDocument "
                + "JOIN Record r ON d.idRecord = r.idRecord "
                + "WHERE r.idStudent = ? AND rd.filePath IS NOT NULL) "
                + "UNION ALL "
                + "(SELECT fd.name, 'FINAL' as type, fd.status, fd.filePath "
                + "FROM FinalDocument fd "
                + "JOIN Delivery d ON fd.idFinalDocument = d.idFinalDocument "
                + "JOIN Record r ON d.idRecord = r.idRecord "
                + "WHERE r.idStudent = ? AND fd.filePath IS NOT NULL)";

        try (Connection connection = new DBConnection().createConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idStudent);
            preparedStatement.setInt(2, idStudent);
            preparedStatement.setInt(3, idStudent);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    DocumentWrapper doc = new DocumentWrapper();

                    doc.setName(rs.getString("name"));
                    doc.setFilePath(rs.getString("filePath"));
                    doc.setDate(rs.getString("date"));

                    String typeString = rs.getString("type");
                    switch (typeString) {
                        case "INITIAL":
                            doc.setType(DocumentWrapper.DocumentType.INITIAL);
                            break;
                        case "REPORT":
                            doc.setType(DocumentWrapper.DocumentType.REPORT);
                            break;
                        case "FINAL":
                            doc.setType(DocumentWrapper.DocumentType.FINAL);
                            break;
                    }

                    documents.add(doc);
                }
            }
        }
        return documents;
    }

    public static ArrayList<DocumentCatalog> getAllDocumentCatalog() throws SQLException {
        ArrayList<DocumentCatalog> catalogList = new ArrayList<>();
        String query = "SELECT idDocumentCatalog, name, type FROM DocumentCatalog ORDER BY name ASC";
        try (Connection dbConnection = new DBConnection().createConnection(); PreparedStatement preparedStatement = dbConnection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                DocumentCatalog doc = new DocumentCatalog();
                doc.setIdDocumentCatalog(resultSet.getInt("idDocumentCatalog"));
                doc.setName(resultSet.getString("name"));
                doc.setType(resultSet.getString("type"));
                catalogList.add(doc);
            }
        } catch (SQLException e) {
            throw e;
        }
        return catalogList;
    }

}
