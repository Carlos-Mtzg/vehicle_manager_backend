package mx.edu.utez.vehicleManager.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    public static final String NOT_BLANK_MESSAGE = "Este campo no puede estar vacío";
    public static final String NO_ANGLE_BRACKETS_MESSAGE = "No se permiten los caracteres < o >";
    public static final String NOT_NULL_MESSAGE = "Este campo es obligatorio";

    @NotBlank(message = NOT_BLANK_MESSAGE)
    @Pattern(regexp = "^[^<>]*$", message = NO_ANGLE_BRACKETS_MESSAGE)
    @Size(max = 50, message = "Este campo no puede tener más de 50 caracteres")
    private String fullName;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    @Pattern(regexp = "^[^<>]*$", message = NO_ANGLE_BRACKETS_MESSAGE)
    @Size(max = 10, message = "Este campo no puede tener más de 10 caracteres")
    private String phone;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    @Pattern(regexp = "^[^<>]*$", message = NO_ANGLE_BRACKETS_MESSAGE)
    @Email(message = "Este correo electrónico no es válido")
    private String email;

    @NotNull(message = NOT_NULL_MESSAGE)
    @NotBlank(message = NOT_BLANK_MESSAGE)
    @Pattern(regexp = "^[^<>]*$", message = NO_ANGLE_BRACKETS_MESSAGE)
    @Size(max = 50, message = "Este campo no puede tener más de 50 caracteres")
    private String username;

    private boolean enabled = true;

    private Long roleId;

    public RegisterRequest() {
    }

    public RegisterRequest(String fullName, String phone, String email, String username,
            boolean enabled, Long roleId) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.enabled = enabled;
        this.roleId = roleId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName != null ? fullName.trim() : null;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone != null ? phone.trim() : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username != null ? username.trim() : null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
