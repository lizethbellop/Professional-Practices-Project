package practicasprofesionalespf.model.pojo;

public class LinkedOrganization {
   private int idLinkedOrganization;
   private String name;
   private boolean isActive;
   private String street;
   private String neighborhood;
   private String city;
   private String state;
   private String postalCode;
   private String phone;

    public LinkedOrganization() {
    }

    public LinkedOrganization(int idLinkedOrganization, String name, boolean isActive, String street, String neighborhood, String city, String state, String postalCode, String phone) {
        this.idLinkedOrganization = idLinkedOrganization;
        this.name = name;
        this.isActive = isActive;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.phone = phone;
    }

    public int getIdLinkedOrganization() {
        return idLinkedOrganization;
    }

    public void setIdLinkedOrganization(int idLinkedOrganization) {
        this.idLinkedOrganization = idLinkedOrganization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
   
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (street != null && !street.isEmpty()) {
            sb.append(street);
        }
        if (neighborhood != null && !neighborhood.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(neighborhood);
        }
        if (city != null && !city.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(city);
        }
        if (state != null && !state.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(state);
        }
        if (postalCode != null && !postalCode.isEmpty()) {
            if (sb.length() > 0) sb.append(", C.P. ");
            sb.append(postalCode);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return name;
    }
}