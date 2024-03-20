package com.dxc.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.dxc.dto.PasswordResetDto;

@Component
public class PasswordResetDtoValidator implements Validator {
    public boolean supports(Class<?> clazz) {
        return PasswordResetDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "token", "token.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "newPassword.required");
       
    }
}