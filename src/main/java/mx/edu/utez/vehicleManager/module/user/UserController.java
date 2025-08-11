package mx.edu.utez.vehicleManager.module.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import mx.edu.utez.vehicleManager.auth.AuthService;
import mx.edu.utez.vehicleManager.auth.RegisterRequest;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("")
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados.")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa")
    public ResponseEntity<Object> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene un usuario específico por su ID.")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("")
    @Operation(summary = "Registro de usuario", description = "Registra un nuevo usuario y retorna un token JWT si el registro es exitoso.")
    @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o el usuario ya existe")
    @ApiResponse(responseCode = "404", description = "Rol de usuario no encontrado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "409", description = "El nombre de usuario o correo ya está en uso")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @PutMapping("/change-password/{id}")
    @Operation(summary = "Cambiar contraseña", description = "Permite cambiar la contraseña de un usuario.")
    @ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente")
    @ApiResponse(responseCode = "400", description = "La contraseña no puede estar vacía")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<?> changePassword(@PathVariable("id") Long id,
            @RequestBody @Valid UpdateUserRequest request) {
        return userService.changeUserPassword(id, request);
    }

    @PutMapping("/disabled/{id}")
    @Operation(summary = "Deshabilitar usuario", description = "Deshabilita un usuario para que no pueda acceder al sistema.")
    @ApiResponse(responseCode = "200", description = "Usuario deshabilitado correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "409", description = "Este usuario ya se encuentra deshabilitado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<?> disableUser(@PathVariable("id") Long id) {
        return userService.disableUser(id);
    }

    @PutMapping("/enabled/{id}")
    @Operation(summary = "Habilitar usuario", description = "Habilita un usuario para que pueda acceder al sistema.")
    @ApiResponse(responseCode = "200", description = "Usuario habilitado correctamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "409", description = "Este usuario ya se encuentra habilitado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<?> enableUser(@PathVariable("id") Long id) {
        return userService.enableUser(id);
    }
}
