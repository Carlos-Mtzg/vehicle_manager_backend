package mx.edu.utez.vehicleManager.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RecoverPassword {
    @NotBlank(message = "Este campo no puede estar vacío")
    @Pattern(regexp = "^[^<>]*$", message = "No se permiten los caracteres < o >")
    @Email(message = "Este correo electrónico no es válido")
    private String email;

    public String getEmail() {
        return email;
    }

}
