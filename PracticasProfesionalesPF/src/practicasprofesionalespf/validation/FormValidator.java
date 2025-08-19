package practicasprofesionalespf.validation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import practicasprofesionalespf.utils.ValidationUtils;

public class FormValidator {

    public static Set<String> checkEmptyFields(Map<String, String> fields) {
        Set<String> emptyFields = new HashSet<>();

        for (Map.Entry<String, String> field : fields.entrySet()) {
            if (ValidationUtils.isEmpty(field.getValue().trim())) {
                emptyFields.add(field.getKey());
            }
        }

        return emptyFields;
    }

    public static Set<String> checkInvalidFieldsLinkedOrganization(Map<String, String> fields) {
        Set<String> invalidFields = new HashSet<>();

        String name = fields.get("tfName");
        String street = fields.get("tfStreet");
        String neighborhood = fields.get("tfNeighborhood");
        String city = fields.get("tfCity");
        String state = fields.get("tfState");
        String postalCode = fields.get("tfPostalCode");
        String phone = fields.get("tfPhone");

        if (!isLengthValid(name, 45)) {
            invalidFields.add("tfName");
        }
        
        if (!isLengthValid(street, 100)) {
            invalidFields.add("tfStreet");
        }
        if (!isLengthValid(neighborhood, 100)) {
            invalidFields.add("tfNeighborhood");
        }
        if (!isLengthValid(city, 100) || !ValidationUtils.isOnlyLetters(city)) {
            invalidFields.add("tfCity");
        }
        if (!isLengthValid(state, 100) || !ValidationUtils.isOnlyLetters(state)) {
            invalidFields.add("tfState");
        }
        if (!ValidationUtils.isPostalCodeValid(postalCode)) {
            invalidFields.add("tfPostalCode");
        }
        if (!ValidationUtils.isPhoneValid(phone)) {
            invalidFields.add("tfPhone");
        }
        
        return invalidFields;
    }

    public static Set<String> checkInvalidFieldsProjectManager(Map<String, String> fields) {
        Set<String> invalidFields = new HashSet<>();

        String managerName = fields.get("tfName");
        String managerLastNameFather = fields.get("tfLastNameFather");
        String managerLastNameMother = fields.get("tfLastNameMother");
        String managerPosition = fields.get("tfPosition");
        String managerEmail = fields.get("tfEmail");
        String managerPhone = fields.get("tfPhone");

        if (!isLengthValid(managerName, 60) || !ValidationUtils.isOnlyLetters(managerName)) {
            invalidFields.add("tfName");
        }

        if (!isLengthValid(managerLastNameFather, 60) || !ValidationUtils.isOnlyLetters(managerLastNameFather)) {
            invalidFields.add("tfLastNameFather");
        }

        if (!isLengthValid(managerLastNameMother, 60) || !ValidationUtils.isOnlyLetters(managerLastNameMother)) {
            invalidFields.add("tfLastNameMother");
        }

        if (!isLengthValid(managerPosition, 60) || !ValidationUtils.isOnlyLetters(managerPosition)) {
            invalidFields.add("tfPosition");
        }

        if (!isLengthValid(managerEmail, 100) || !ValidationUtils.isValidEmail(managerEmail)) {
            invalidFields.add("tfEmail");
        }

        if (!isLengthValid(managerPhone, 10) || !ValidationUtils.isPhoneValid(managerPhone)) {
            invalidFields.add("tfPhone");
        }

        return invalidFields;
    }

    public static Set<String> checkInvalidFieldsProject(Map<String, String> fields) {
        Set<String> invalidFields = new HashSet<>();

        String projectName = fields.get("tfName");
        String department = fields.get("tfDepartment");
        String description = fields.get("taDescription");
        String methodology = fields.get("tfMethodology");
        String availability = fields.get("tfAvailability");

        if (!isLengthValid(projectName, 50) || !ValidationUtils.isOnlyLetters(projectName)) {
            invalidFields.add("tfName");
        }

        if (!isLengthValid(department, 30) || !ValidationUtils.isOnlyLetters(department)) {
            invalidFields.add("tfDepartment");
        }

        if (!isLengthValid(description, 200) || !ValidationUtils.isOnlyLetters(description)) {
            invalidFields.add("taDescription");
        }

        if (!isLengthValid(methodology, 45) || !ValidationUtils.isOnlyLetters(methodology)) {
            invalidFields.add("tfMethodology");
        }

        if (!ValidationUtils.isOnlyNumber(availability)) {
            invalidFields.add("tfAvailability");
        } else {
            int availabilityNumber = Integer.parseInt(availability);
            if (availabilityNumber > 5 || availabilityNumber <= 0) {
                invalidFields.add("tfAvailability");
            }
        }

        return invalidFields;
    }

    public static boolean isLengthValid(String value, int maxLength) {
        return value != null && value.length() <= maxLength;
    }

    public static boolean isTextAreaEmpty(String longText) {
        return longText != null && longText.trim().isEmpty();
    }

    public static boolean isTextFieldEmpty(String text) {
        return text != null && text.trim().isEmpty();
    }

}