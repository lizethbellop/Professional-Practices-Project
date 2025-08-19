package practicasprofesionalespf.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import practicasprofesionalespf.model.DBConnection;
import practicasprofesionalespf.model.pojo.Student;
import practicasprofesionalespf.model.pojo.StudentWithProject;

public class StudentDAO {

    public static ArrayList<Student> obtainStudentWithoutEvaluation(int idEvaluator) throws SQLException {
        ArrayList<Student> students = new ArrayList<>();
        String sqlQuery = "SELECT s.idStudent, s.firstName, s.lastNameFather, s.lastNameMother, s.enrollment " +
                          "FROM Student s JOIN Record r ON s.idStudent = r.idStudent " +
                          "WHERE (s.isAssignedToProject = 1)";

        try (Connection dbConnection = new DBConnection().createConnection(); 
             PreparedStatement query = dbConnection.prepareStatement(sqlQuery)) {

            try (ResultSet result = query.executeQuery()) {
                while (result.next()) {
                    students.add(convertStudentRegistration(result));
                }
            }
        }

        return students;
    }

    public static ArrayList<Student> obtainStudent() throws SQLException {
        ArrayList<Student> students = new ArrayList<>();
        String sqlQuery = "SELECT idStudent, firstName, lastNameFather, lastNameMother, enrollment FROM Student";

        try (Connection dbConnection = new DBConnection().createConnection(); PreparedStatement query = dbConnection.prepareStatement(sqlQuery)) {

            try (ResultSet result = query.executeQuery()) {
                while (result.next()) {
                    students.add(convertStudentRegistration(result));
                }
            }
        }

        return students;
    }
    
    public static ArrayList<Student> obtainStudentsByGroup(int idAcademic) throws SQLException{
        ArrayList<Student> students = new ArrayList<>();
        
        String sqlQuery = "SELECT s.idStudent, s.enrollment, s.firstName, s.lastNameFather, s.lastNameMother "
        + "FROM Student s JOIN StudentInGroup sg "
        + "ON s.idStudent = sg.idStudent JOIN TeachingAssignment ta "
        + "ON sg.idSubjectGroup = ta.idSubjectGroup WHERE ta.idAcademic = ?;";
        
        try (Connection dbConnection = new DBConnection().createConnection(); 
                PreparedStatement query = dbConnection.prepareStatement(sqlQuery)) {
            
            query.setInt(1, idAcademic);

            try (ResultSet result = query.executeQuery()) {
                while (result.next()) {
                    students.add(convertStudentRegistration(result));
                }
            }
        }
        
        return students;
        
    }

    public static Student obtainStudentByUser(int idUser) throws SQLException {
        Student student = new Student();
        String sqlQuery = "SELECT idStudent, firstName, lastNameFather, lastNameMother, enrollment FROM Student WHERE idUser = ?";

        try (Connection dbConnection = new DBConnection().createConnection(); PreparedStatement query = dbConnection.prepareStatement(sqlQuery)) {

            query.setInt(1, idUser);

            try (ResultSet result = query.executeQuery()) {
                if (result.next()) {
                    student = convertStudentRegistration(result);
                }
            }
        }

        return student;

    }

    private static Student convertStudentRegistration(ResultSet result) throws SQLException {
        Student student = new Student();
        student.setIdStudent(result.getInt("idStudent"));
        student.setEnrollment(result.getString("enrollment"));
        student.setFirstName(result.getString("firstName"));
        student.setLastNameFather(result.getString("lastNameFather"));
        student.setLastNameMother(result.getString("lastNameMother"));

        return student;
    }

 
    public static ArrayList<Student> getStudentsWithProject() throws SQLException {
        ArrayList<Student> students = new ArrayList<>();
        String query = "SELECT s.idStudent, s.firstName, s.lastNameFather, s.lastNameMother, s.enrollment "
                + "FROM Student s "
                + "JOIN Record r ON s.idStudent = r.idStudent "
                + "JOIN Project p ON r.idProject = p.idProject"; 

        try (Connection connection = new DBConnection().createConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Student student = new Student();
                student.setIdStudent(resultSet.getInt("idStudent"));
                student.setFirstName(resultSet.getString("firstName"));
                student.setLastNameFather(resultSet.getString("lastNameFather"));
                student.setLastNameMother(resultSet.getString("lastNameMother"));
                student.setEnrollment(resultSet.getString("enrollment"));
                students.add(student);
            }
        }
        return students;
    }

   
    public static ArrayList<StudentWithProject> getStudentsWithProjectInfo() throws SQLException {
        ArrayList<StudentWithProject> students = new ArrayList<>();
        String query = "SELECT s.*, p.name as projectName FROM Student s "
                + "JOIN Record r ON s.idStudent = r.idStudent "
                + "JOIN Project p ON r.idProject = p.idProject"; 

        try (Connection connection = new DBConnection().createConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                StudentWithProject student = new StudentWithProject();
                student.setIdStudent(resultSet.getInt("idStudent"));
                student.setFirstName(resultSet.getString("firstName"));
                student.setLastNameFather(resultSet.getString("lastNameFather"));
                student.setLastNameMother(resultSet.getString("lastNameMother"));
                student.setEnrollment(resultSet.getString("enrollment"));
                student.setProjectName(resultSet.getString("projectName"));
                students.add(student);
            }
        }
        return students;
    }

    public static ArrayList<Student> getStudentsWithoutProject() throws SQLException {
        ArrayList<Student> students = new ArrayList<>();
        String query = "SELECT * FROM Student WHERE isAssignedToProject = 0";

        try (Connection connection = new DBConnection().createConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Student student = new Student();
                student.setIdStudent(resultSet.getInt("idStudent"));
                student.setFirstName(resultSet.getString("firstName"));
                student.setLastNameFather(resultSet.getString("lastNameFather"));
                student.setLastNameMother(resultSet.getString("lastNameMother"));
                student.setEnrollment(resultSet.getString("enrollment"));
                student.setEmail(resultSet.getString("email"));
                student.setPhone(resultSet.getString("phone"));
                student.setCredits(resultSet.getInt("credits"));
                student.setSemester(resultSet.getString("semester"));

                student.setIsAssignedToProject(resultSet.getInt("isAssignedToProject") != 0);

                student.setProjectSelection(resultSet.getString("projectSelection"));

                if (resultSet.getObject("grade") != null) {
                    student.setGrade(resultSet.getDouble("grade"));
                }

                students.add(student);
            }
        }
        return students;
    }

}