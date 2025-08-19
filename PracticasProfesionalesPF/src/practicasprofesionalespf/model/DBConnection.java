package practicasprofesionalespf.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection implements IDBConnection{
    
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String CONFIG_FILE_PATH = "config/db_config.properties";

    @Override
    public Connection createConnection() throws SQLException {
       Properties dbProperties = new Properties();
       Connection dbConnection = null;
       
       try(InputStream is = new FileInputStream(CONFIG_FILE_PATH)){
           
               dbProperties.load(is);
               String url = dbProperties.getProperty("db.url");
               String user = dbProperties.getProperty("db.user");
               String password = dbProperties.getProperty("db.password");
               
               if(url == null || user == null || password == null){
                   System.err.println("Error: One or more connection properties (db.url, db.user, db.password) "
                           + "are missing in the file '" + CONFIG_FILE_PATH + " '.");
                   throw new SQLException("Database configuration properties are missing.");
               }

                Class.forName(DRIVER);
                dbConnection = DriverManager.getConnection(url, user, password);
     
       } catch (FileNotFoundException ex) {
            System.err.println("Error: Configuration file not found. "
                    + "Ensure it exists and the path is correct");
            throw new SQLException("Configuration file not found.", ex);
        } catch (IOException ex) {
            System.err.println("Error reading the configuration file. Detail: " + ex.getMessage());
            throw new SQLException("Error reading configuration file.", ex);
        }catch (ClassNotFoundException ex){
            System.err.println("Error: JDBC driver class not found. "
                    + "Ensure the MySQL driver JAR is in your project's classpath");
            throw new SQLException("JDBC Driver not found.", ex);
        } catch (SQLException ex) {
            System.err.println("SQL Error: Could not establish a database connection "
                    + "Verify the URL, username, and password");
            throw ex;
        }
       
       return dbConnection;
    }
    
}
