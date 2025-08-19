package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.OperationResult;
import practicasprofesionalespf.model.pojo.Project;

public class ProjectDAO {

 
    public static String obtainProjectName(int idStudent) throws SQLException {
        String sqlQuery = "SELECT p.name FROM project p JOIN record r "
                + "ON p.idProject = r.idProject WHERE r.idStudent = ?"; // <-- JOIN MODIFICADO
        String projectName = null;

        try (Connection dbConnection = new DBConnection().createConnection(); PreparedStatement query = dbConnection.prepareStatement(sqlQuery)) {

            query.setInt(1, idStudent);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    projectName = result.getString("name");
                }
            }
        }

        return projectName;
    }


    public static ArrayList<Project> obtainProjects() throws SQLException {
        ArrayList<Project> projects = new ArrayList<>();
        String sqlQuery = "SELECT p.idProject, p.idProjectManager, p.idLinkedOrganization, " // <-- idRecord ELIMINADO
                + "p.idCoordinator, p.name, p.department, p.description, p.methodology, p.availability, "
                + "lo.name AS linkedOrganizationName, "
                + "CONCAT(pm.firstName, ' ', pm.lastNameFather) AS projectManagerName "
                + "FROM project p "
                + "LEFT JOIN linkedorganization lo ON p.idLinkedOrganization = lo.idLinkedOrganization "
                + "LEFT JOIN projectmanager pm ON p.idProjectManager = pm.idProjectManager";

        try (Connection dbConnection = new DBConnection().createConnection(); PreparedStatement query = dbConnection.prepareStatement(sqlQuery); ResultSet result = query.executeQuery()) {
            while (result.next()) {
                projects.add(convertProject(result));
            }
        }
        return projects;
    }

    
    public static ArrayList<Project> getAvailableProjects() throws SQLException {
        ArrayList<Project> projects = new ArrayList<>();
        String sqlQuery = "SELECT p.idProject, p.idProjectManager, p.idLinkedOrganization, p.idCoordinator, " 
                + "p.name, p.department, p.description, p.methodology, p.availability, lo.name AS linkedOrganizationName, "
                + "CONCAT(pm.firstName, ' ', pm.lastNameFather) AS projectManagerName "
                + "FROM project p "
                + "LEFT JOIN linkedorganization lo ON p.idLinkedOrganization = lo.idLinkedOrganization "
                + "LEFT JOIN projectmanager pm ON p.idProjectManager = pm.idProjectManager "
                + "WHERE p.availability > 0";

        try (Connection dbConnection = new DBConnection().createConnection(); PreparedStatement query = dbConnection.prepareStatement(sqlQuery); ResultSet result = query.executeQuery()) {

            while (result.next()) {
                projects.add(convertProject(result));
            }
        }
        return projects;
    }

    private static Project convertProject(ResultSet result) throws SQLException {
        Project project = new Project();
        project.setIdProject(result.getInt("idProject"));
        project.setIdProjectManager(result.getInt("idProjectManager"));
        project.setIdLinkedOrganization(result.getInt("idLinkedOrganization"));
        project.setIdCoordinator(result.getInt("idCoordinator"));
        project.setName(result.getString("name"));
        project.setDeparment(result.getString("department"));
        project.setDescription(result.getString("description"));
        project.setMethodology(result.getString("methodology"));
        project.setAvailability(result.getInt("availability"));
        project.setLinkedOrganizationName(result.getString("linkedOrganizationName"));
        project.setProjectManagerName(result.getString("projectManagerName"));

        return project;
    }
    
    public static OperationResult saveProject(Project newProject) throws SQLException {
        OperationResult result = new OperationResult();
        String query = "INSERT INTO project (name, department, description, methodology, availability, idProjectManager, idLinkedOrganization, idCoordinator) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = new DBConnection().createConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, newProject.getName());
            preparedStatement.setString(2, newProject.getDeparment());
            preparedStatement.setString(3, newProject.getDescription());
            preparedStatement.setString(4, newProject.getMethodology());
            preparedStatement.setInt(5, newProject.getAvailability());
            preparedStatement.setInt(6, newProject.getIdProjectManager());
            preparedStatement.setInt(7, newProject.getIdLinkedOrganization());
            preparedStatement.setInt(8, newProject.getIdCoordinator());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                result.setError(false);
                result.setMessage("Proyecto registrado con éxito.");
            } else {
                result.setError(true);
                result.setMessage("No se pudo registrar el proyecto.");
            }
        }
        return result;
    }

    public static OperationResult updateProject(Project projectToUpdate) throws SQLException {
        OperationResult result = new OperationResult();
        String query = "UPDATE project SET name = ?, department = ?, description = ?, "
                + "methodology = ?, availability = ?, idLinkedOrganization = ?, "
                + "idProjectManager = ? WHERE idProject = ?";

        try (Connection connection = new DBConnection().createConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, projectToUpdate.getName());
            preparedStatement.setString(2, projectToUpdate.getDeparment());
            preparedStatement.setString(3, projectToUpdate.getDescription());
            preparedStatement.setString(4, projectToUpdate.getMethodology());
            preparedStatement.setInt(5, projectToUpdate.getAvailability());
            preparedStatement.setInt(6, projectToUpdate.getIdLinkedOrganization());
            preparedStatement.setInt(7, projectToUpdate.getIdProjectManager());
            preparedStatement.setInt(8, projectToUpdate.getIdProject());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                result.setError(false);
                result.setMessage("Proyecto actualizado con éxito.");
            } else {
                result.setError(true);
                result.setMessage("No se pudo actualizar el proyecto. Es posible que no se encontrara o que los datos no cambiaran.");
            }
        }
        return result;
    }

}