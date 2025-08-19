package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.OperationResult;
import practicasprofesionalespf.model.pojo.ProjectManager;

public class ProjectManagerDAO {

    public static ArrayList<ProjectManager> obtainProjectManager() throws SQLException {
        ArrayList<ProjectManager> projectManagers = new ArrayList<>();
        String sqlQuery = "SELECT pm.idProjectManager, pm.firstName, pm.lastNameFather, "
                + "pm.lastNameMother, pm.idLinkedOrganization, lo.name AS linkedOrganization, pm.position, "
                + "pm.email, pm.phone FROM ProjectManager pm "
                + "JOIN LinkedOrganization lo ON pm.idLinkedOrganization = lo.idLinkedOrganization";

        try (Connection dbConnection = new DBConnection().createConnection(); PreparedStatement query = dbConnection.prepareStatement(sqlQuery)) {

            try (ResultSet result = query.executeQuery()) {
                while (result.next()) {
                    projectManagers.add(convertManagerRegistration(result));
                }
            }
        }

        return projectManagers;

    }

    private static ProjectManager convertManagerRegistration(ResultSet result) throws SQLException {
        ProjectManager projectManager = new ProjectManager();
        projectManager.setIdProjectManager(result.getInt("idProjectManager"));
        projectManager.setIdLinkedOrganization(result.getInt("idLinkedOrganization"));
        projectManager.setFirstName(result.getString("firstName"));
        projectManager.setLastNameFather(result.getString("lastNameFather"));
        projectManager.setLastNameMother(result.getString("lastNameMother"));
        projectManager.setPosition(result.getString("position"));
        projectManager.setEmail(result.getString("email"));
        projectManager.setPhone(result.getString("phone"));
        projectManager.setLinkedOrganization(result.getString("linkedOrganization"));

        return projectManager;
    }

    public static OperationResult registerManager(ProjectManager projectManager) throws SQLException {
        OperationResult result = new OperationResult();
        String sqlQuery = "INSERT INTO projectmanager (idLinkedOrganization, firstName, lastNameFather, "
                + "lastNameMother, email, phone, position) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection dbConnection = new DBConnection().createConnection(); PreparedStatement insert = dbConnection.prepareStatement(sqlQuery)) {

            insert.setInt(1, projectManager.getIdLinkedOrganization());
            insert.setString(2, projectManager.getFirstName());
            insert.setString(3, projectManager.getLastNameFather());
            insert.setString(4, projectManager.getLastNameMother());
            insert.setString(5, projectManager.getEmail());
            insert.setString(6, projectManager.getPhone());
            insert.setString(7, projectManager.getPosition());
            int affectedRows = insert.executeUpdate();

            if (affectedRows == 1) {
                result.setError(false);
                result.setMessage("Responsable de proyecto registrado con exito");
            } else {
                result.setError(true);
                result.setMessage("Lo sentimos, no se pudo registrar el Responsable de Proyecto");
            }
        }

        return result;
    }

    public static OperationResult updateProjectManager(ProjectManager projectManager) throws SQLException {
        OperationResult result = new OperationResult();
        String sqlQuery = "UPDATE projectmanager SET idLinkedOrganization = ?, firstName = ?, lastNameFather = ?, "
                + "lastNameMother = ?, email = ?, phone = ?, position = ? WHERE idProjectManager = ?";

        try (Connection dbConnection = new DBConnection().createConnection(); PreparedStatement update = dbConnection.prepareStatement(sqlQuery)) {

            update.setInt(1, projectManager.getIdLinkedOrganization());
            update.setString(2, projectManager.getFirstName());
            update.setString(3, projectManager.getLastNameFather());
            update.setString(4, projectManager.getLastNameMother());
            update.setString(5, projectManager.getEmail());
            update.setString(6, projectManager.getPhone());
            update.setString(7, projectManager.getPosition());
            update.setInt(8, projectManager.getIdProjectManager());
            int affectedRows = update.executeUpdate();

            if (affectedRows == 1) {
                result.setError(false);
                result.setMessage("Project Manager updated successfully.");
            } else {
                result.setError(true);
                result.setMessage("Failed to update the Project Manager.");
            }
        }

        return result;
    }

    public static ProjectManager getProjectManagerById(int managerId) throws SQLException {
        ProjectManager manager = null;
        String sqlQuery = "SELECT * FROM projectmanager WHERE idProjectManager = ?";
        try (Connection dbConnection = new DBConnection().createConnection(); PreparedStatement statement = dbConnection.prepareStatement(sqlQuery)) {

            statement.setInt(1, managerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    manager = new ProjectManager();
                    manager.setIdProjectManager(resultSet.getInt("idProjectManager"));
                    manager.setIdLinkedOrganization(resultSet.getInt("idLinkedOrganization"));
                    manager.setFirstName(resultSet.getString("firstName"));
                    manager.setLastNameFather(resultSet.getString("lastNameFather"));
                    manager.setLastNameMother(resultSet.getString("lastNameMother"));
                    manager.setPosition(resultSet.getString("position"));
                    manager.setEmail(resultSet.getString("email"));
                    manager.setPhone(resultSet.getString("phone"));
                }
            }
        }
        return manager;
    }

    public static ArrayList<ProjectManager> obtainProjectManagersByOrganization(int idLinkedOrganization) throws SQLException {
        ArrayList<ProjectManager> projectManagers = new ArrayList<>();
        String sqlQuery = "SELECT pm.idProjectManager, pm.firstName, pm.lastNameFather, "
                + "pm.lastNameMother, pm.idLinkedOrganization, lo.name AS linkedOrganization, pm.position, "
                + "pm.email, pm.phone FROM ProjectManager pm "
                + "JOIN LinkedOrganization lo ON pm.idLinkedOrganization = lo.idLinkedOrganization "
                + "WHERE pm.idLinkedOrganization = ?";

        try (Connection dbConnection = new DBConnection().createConnection(); PreparedStatement query = dbConnection.prepareStatement(sqlQuery)) {

            query.setInt(1, idLinkedOrganization);

            try (ResultSet result = query.executeQuery()) {
                while (result.next()) {
                    projectManagers.add(convertManagerRegistration(result));
                }
            }
        }

        return projectManagers;
    }

}
