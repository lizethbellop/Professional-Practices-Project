package practicasprofesionalespf.model.pojo;

public class DocumentCatalog {
    private int idDocumentCatalog;
    private String name;
    private String type; 

    public DocumentCatalog() {
    }

    public DocumentCatalog(int idDocumentCatalog, String name, String type) {
        this.idDocumentCatalog = idDocumentCatalog;
        this.name = name;
        this.type = type;
    }

    public int getIdDocumentCatalog() {
        return idDocumentCatalog;
    }

    public void setIdDocumentCatalog(int idDocumentCatalog) {
        this.idDocumentCatalog = idDocumentCatalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }
}