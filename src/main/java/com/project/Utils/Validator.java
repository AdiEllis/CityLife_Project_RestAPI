package com.project.Utils;

import org.apache.commons.validator.routines.EmailValidator;

public interface Validator {
    default public boolean isValidEmail(String email){
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
    default public boolean isValidRange(int x, int y){
        return true;
    }
}
