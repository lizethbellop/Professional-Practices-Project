package practicasprofesionalespf.model.wrapper;

public class DocumentWrapper {
    public enum DocumentType {
        INITIAL, REPORT, FINAL
    }
    
    private DocumentType type;
    private String name;
    private String date;
    private String filePath;
    private Object originalDocument;

    public DocumentWrapper() {
    }

    public DocumentWrapper(DocumentType type, String name, String date, String filePath, Object originalDocument) {
        this.type = type;
        this.name = name;
        this.date = date;
        this.filePath = filePath;
        this.originalDocument = originalDocument;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Object getOriginalDocument() {
        return originalDocument;
    }

    public void setOriginalDocument(Object originalDocument) {
        this.originalDocument = originalDocument;
    }

    
}
