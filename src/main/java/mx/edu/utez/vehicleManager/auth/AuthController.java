package mx.edu.utez.vehicleManager.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para autenticación y registro de usuarios")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Inicio de sesión de usuario", description = "Autentica a un usuario y retorna un token JWT si las credenciales son correctas.")
    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/recover-password")
    @Operation(summary = "Recuperación de contraseña", description = "Permite solicitar la recuperación de contraseña enviando el correo registrado. Si el correo existe, se genera una nueva contraseña temporal y se envía al usuario por correo electrónico.")
    @ApiResponse(responseCode = "200", description = "Recuperación exitosa")
    @ApiResponse(responseCode = "404", description = "No se encontró el usuario")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<Object> recoverPassword(@RequestBody @Valid RecoverPassword request) {
        return authService.recoverPassword(request);
    }

}
