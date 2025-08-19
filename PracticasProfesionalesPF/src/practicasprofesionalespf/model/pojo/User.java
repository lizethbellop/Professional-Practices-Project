package practicasprofesionalespf.model.pojo;

public class User {
    
    private int idUser;
    private String username;
    private String role;

    public User() {
    }

    public User(int idUser, String username, String role) {
        this.idUser = idUser;
        this.username = username;
        this.role = role;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    
   
}
