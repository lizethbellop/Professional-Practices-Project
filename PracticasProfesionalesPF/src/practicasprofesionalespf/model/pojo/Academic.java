package practicasprofesionalespf.model.pojo;

public class Academic {
    private int idAcademic;
    private int idUser;

    public Academic() {
    }

    public Academic(int idAcademic, int idUser) {
        this.idAcademic = idAcademic;
        this.idUser = idUser;
    }

    public int getIdAcademic() {
        return idAcademic;
    }

    public void setIdAcademic(int idAcademic) {
        this.idAcademic = idAcademic;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    
    
    
}
