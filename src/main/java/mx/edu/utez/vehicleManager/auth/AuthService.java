package mx.edu.utez.vehicleManager.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.edu.utez.vehicleManager.utils.Utilities;
import mx.edu.utez.vehicleManager.jwt.JwtService;
import mx.edu.utez.vehicleManager.module.employee.EmployeeModel;
import mx.edu.utez.vehicleManager.module.employee.IEmployeeRepository;
import mx.edu.utez.vehicleManager.module.mail.IMailService;
import mx.edu.utez.vehicleManager.module.role.IRoleRepository;
import mx.edu.utez.vehicleManager.module.role.RoleModel;
import mx.edu.utez.vehicleManager.module.user.IUserRepository;
import mx.edu.utez.vehicleManager.module.user.UserModel;

@Service
public class AuthService {

        private final IUserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final IEmployeeRepository employeeRepository;
        private final IRoleRepository roleRepository;
        private final IMailService mailService;

        public AuthService(IUserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager, IEmployeeRepository employeeRepository,
                        IRoleRepository roleRepository, IMailService mailService) {
                this.userRepository = userRepository;
                this.jwtService = jwtService;
                this.passwordEncoder = passwordEncoder;
                this.authenticationManager = authenticationManager;
                this.employeeRepository = employeeRepository;
                this.roleRepository = roleRepository;
                this.mailService = mailService;
        }

        @Transactional
        public ResponseEntity<Object> login(LoginRequest request) {
                try {
                        UserDetails user = userRepository.findByUsername(request.getUsername()).orElse(null);
                        if (user == null) {
                                return Utilities.authResponse(HttpStatus.UNAUTHORIZED,
                                                "Usuario y/o contraseña incorrectos", null);
                        }

                        if (!user.isEnabled()) {
                                return Utilities.authResponse(HttpStatus.UNAUTHORIZED,
                                                "Ocurrió un error al intentar iniciar sesíon", null);
                        }

                        authenticationManager
                                        .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                                                        request.getPassword()));

                        String token = jwtService.getToken(user);
                        return Utilities.authResponse(HttpStatus.OK, "Inicio de sesión correcto", token);

                } catch (org.springframework.security.authentication.BadCredentialsException e) {
                        return Utilities.authResponse(HttpStatus.UNAUTHORIZED,
                                        "Usuario y/o contraseña incorrectos", null);
                } catch (Exception e) {
                        return Utilities.authResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Ocurrió un error inesperado", null);
                }
        }

        @Transactional
        public ResponseEntity<Object> register(RegisterRequest request) {
                try {
                        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                                return Utilities.authResponse(HttpStatus.BAD_REQUEST,
                                                "El nombre de usuario ya está en uso", null);
                        }

                        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
                                return Utilities.authResponse(HttpStatus.BAD_REQUEST,
                                                "El correo de empleado ya está en uso", null);
                        }

                        Optional<RoleModel> roleOpt = roleRepository.findById(request.getRoleId());
                        if (!roleOpt.isPresent()) {
                                return Utilities.authResponse(HttpStatus.NOT_FOUND, "Rol de usuario no encontrado",
                                                null);
                        }
                        RoleModel role = roleOpt.get();

                        EmployeeModel employee = EmployeeModel.builder()
                                        .fullName(request.getFullName())
                                        .phone(request.getPhone())
                                        .email(request.getEmail())
                                        .build();
                        employee = employeeRepository.save(employee);

                        String temporaryPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                        UserModel user = UserModel.builder()
                                        .username(request.getUsername().toUpperCase())
                                        .password(passwordEncoder.encode(temporaryPassword))
                                        .enabled(request.isEnabled())
                                        .employee(employee)
                                        .role(role)
                                        .build();
                        user = userRepository.save(user);

                        String subject = "Registro de Usuario";
                        String message = "¡Bienvenido!\n\n" +
                                        "Tu usuario ha sido registrado exitosamente en VehicleManager.\n\n" +
                                        "Usuario: " + request.getUsername() + "\n" +
                                        "Contraseña temporal: " + temporaryPassword + "\n\n" +
                                        "Por favor, cambia tu contraseña después de iniciar sesión en la opción de tu perfil.\n\n"
                                        +
                                        "Saludos,\nEl equipo de VehicleManager";
                        try {
                                mailService.sendEmail(request.getEmail(), subject, message);
                        } catch (Exception ex) {
                                throw new RuntimeException(
                                                "No se pudo enviar el correo de registro: " + ex.getMessage(), ex);
                        }
                        String token = jwtService.getToken(user);
                        return Utilities.authResponse(HttpStatus.CREATED, "Usuario registrado correctamente",
                                        token);
                } catch (Exception e) {
                        return Utilities.authResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Ocurrió un error inesperado", null);
                }
        }

        @Transactional
        public ResponseEntity<Object> recoverPassword(RecoverPassword request) {
                try {
                        Optional<EmployeeModel> emplyeeOpt = employeeRepository.findByEmail(request.getEmail());
                        if (!emplyeeOpt.isPresent()) {
                                return Utilities.generateResponse(HttpStatus.NOT_FOUND,
                                                "No se encontró correo asignado a este usuario", null);
                        }

                        EmployeeModel employee = emplyeeOpt.get();
                        Optional<UserModel> userOpt = userRepository.findByEmployee(employee);
                        UserModel user = userOpt.get();

                        String temporaryPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                        user.setPassword(passwordEncoder.encode(temporaryPassword));

                        String subject = "Recuperación de contraseña";
                        String message = "¡Tu contraseña se actualizó correctamente!\n\n" +
                                        "Contraseña temporal: " + temporaryPassword + "\n\n" +
                                        "Por favor, cambia tu contraseña después de iniciar sesión en la opción de tu perfil.\n\n"
                                        +
                                        "Saludos,\nEl equipo de VehicleManager";
                        try {
                                mailService.sendEmail(request.getEmail(), subject, message);
                        } catch (Exception ex) {
                                throw new RuntimeException(
                                                "No se pudo enviar el correo de registro: " + ex.getMessage(), ex);
                        }
                        return Utilities.generateResponse(HttpStatus.OK, "La contraseña se actualizó con éxito", null);
                } catch (Exception e) {
                        return Utilities.authResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Ocurrió un error inesperado", null);
                }
        }
}
