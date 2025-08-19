package practicasprofesionalespf.model;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDBConnection {
    
    public Connection createConnection() throws SQLException;
    
}
