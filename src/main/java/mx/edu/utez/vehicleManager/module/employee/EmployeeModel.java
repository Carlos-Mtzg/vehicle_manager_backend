package mx.edu.utez.vehicleManager.module.employee;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import mx.edu.utez.vehicleManager.module.customer.CustomerModel;
import mx.edu.utez.vehicleManager.module.user.UserModel;
import io.swagger.v3.oas.annotations.media.Schema; 

@Schema(
    description = "Modelo que representa a un empleado de la agencia automotriz",
    example = """
    {
      "id": 1,
      "fullName": "Ana López",
      "phone": "5559876543",
      "email": "ana.lopez@email.com"
    }
    """
)
@Entity
@Table(name = "employee")
public class EmployeeModel {

    public static final String NOT_BLANK_MESSAGE = "Este campo no puede estar vacío";
    public static final String NO_ANGLE_BRACKETS_MESSAGE = "No se permiten los caracteres < o >";
    public static final String NOT_NULL_MESSAGE = "Este campo es obligatorio";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @OneToOne(mappedBy = "employee")
    @JsonIgnore
    private UserModel user;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<CustomerModel> customers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String fullName;
        private String phone;
        private String email;
        private UserModel user;

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder user(UserModel user) {
            this.user = user;
            return this;
        }

        public EmployeeModel build() {
            EmployeeModel employee = new EmployeeModel();
            employee.setFullName(fullName);
            employee.setPhone(phone);
            employee.setEmail(email);
            employee.setUser(user);
            return employee;
        }
    }
}
