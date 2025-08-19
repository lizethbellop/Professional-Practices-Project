package practicasprofesionalespf.model.enums;

public enum DeliveryType {
    INITIAL_DOCUMENT("INITIAL DOCUMENT"),
    FINAL_DOCUMENT("FINAL DOCUMENT"),
    REPORT("REPORT");

    private final String databaseValue;

    DeliveryType(String databaseValue) {
        this.databaseValue = databaseValue;
    }


    @Override
    public String toString() {
        return this.databaseValue;
    }
}