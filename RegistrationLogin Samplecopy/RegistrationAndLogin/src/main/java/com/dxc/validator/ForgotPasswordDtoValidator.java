package com.dxc.validator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.dxc.dto.ForgotPasswordDto;

@Component
public class ForgotPasswordDtoValidator implements Validator {
    public boolean supports(Class<?> clazz) {
        return ForgotPasswordDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required");
    
    }
}