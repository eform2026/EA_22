package com.example.eform_mobile.utils;

public class InputValidator {

    public static boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidDecimal(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            double number = Double.parseDouble(value);
            return number >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidInteger(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            int number = Integer.parseInt(value);
            return number >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
