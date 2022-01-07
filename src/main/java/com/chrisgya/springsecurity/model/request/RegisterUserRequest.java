package com.chrisgya.springsecurity.model.request;

import com.chrisgya.springsecurity.utils.PhoneUtil;
import com.chrisgya.springsecurity.utils.validations.FieldsValueMatch;
import com.chrisgya.springsecurity.utils.validations.Password;
import com.chrisgya.springsecurity.utils.validations.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldsValueMatch(field = "password", fieldMatch = "confirmPassword", message = "password and confirmPassword do not match")
public class RegisterUserRequest {

    @Schema(description = "Unique username", example = "john123", required = true)
    @NotBlank
    @Size(min=3, max = 50)
    private String username;

    @Schema(description = "User email address", example = "John.doe@yahoo.com", required = true)
    @NotBlank
    @Email
    @Size(max = 75)
    private String email;

    @Schema(description = "User first name", example = "John", required = true)
    @NotBlank
    @Size(min=3, max = 50)
    private String firstName;

    @Schema(description = "User middle name", example = "Kwesi")
    @Size(min=3, max = 50)
    private String middleName;

    @Schema(description = "User last name", example = "Doe", required = true)
    @NotBlank
    @Size(min=3, max = 50)
    private String lastName;

    @Schema(description = "User mobile number", example = "+2347086640922", required = true)
    @NotBlank
    @Phone
    private String mobileNo;

    @Schema(description = "User password", example = "Password@1", required = true)
    @Password
    private String password;

    @Schema(description = "Confirm password should be the same as user password", example = "Password@1", required = true)
    @NotBlank
    private String confirmPassword;

    @Schema(description = "List of role IDs assigned to this user. NB: (-1) means all roles should be assigned to this user", example = "[1, 2, 3]")
    private Set<Long> roleIds;

    public Optional<Set<Long>> getRoleIds() {
        return Optional.ofNullable(roleIds);
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = PhoneUtil.normalize(mobileNo);
    }
}
