package com.example.demo.domain.models.binding;


import com.example.demo.validations.PasswordValueMatch;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords do not match!"
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegister {
    @NotNull
    @Size(min = 3,max = 20)
    private String username;
    @NotNull
    @Size(min = 3, max = 30)
    private String firstName;
    @NotNull
    @Size(min = 3, max = 30)
    private String lastName;
    @NotNull
    @Min(value = 18)
    private Integer age;
    @NotNull
    @NotBlank
    @Size(min = 3,max = 20)
    private String password;
    @NotNull
    @NotBlank
    private String confirmPassword;
}
