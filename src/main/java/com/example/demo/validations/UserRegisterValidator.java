package com.example.demo.validations;

import com.example.demo.domain.models.binding.UserRegister;
import com.example.demo.repositories.UserRepository;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@com.example.demo.validations.Validator
public class UserRegisterValidator implements Validator {
    private UserRepository userRepository;

    public UserRegisterValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegister.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegister userRegister = (UserRegister) target;

        if (this.userRepository.findByUsername(userRegister.getUsername()).isPresent()) {
            errors.rejectValue(
                    "username",
                    "Username exists",
                    "Username exists"
            );
        }

    }
}
