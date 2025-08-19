package practicasprofesionalespf.model.pojo;

public class OperationResult {
    private boolean error;
    private String message;

    public OperationResult() {
    }

    public OperationResult(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
