package mx.edu.utez.vehicleManager.module.customer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import mx.edu.utez.vehicleManager.module.employee.EmployeeModel;
import mx.edu.utez.vehicleManager.module.vehicle.VehicleModel;
import io.swagger.v3.oas.annotations.media.Schema; 

@Schema(
    description = "Modelo que representa a un cliente de la agencia automotriz",
    example = """
    {
      "id": 1,
      "full_name": "Juan Pérez",
      "phone": "5551234567",
      "email": "juan.perez@email.com",
      "employee": {
        "id": 2,
        "full_name": "Ana López"
      }
    }
    """
)
@Entity
@Table(name = "customer")
public class CustomerModel {

    public static final String NOT_BLANK_MESSAGE = "Este campo no puede estar vacío";
    public static final String NO_ANGLE_BRACKETS_MESSAGE = "No se permiten los caracteres < o >";
    public static final String NOT_NULL_MESSAGE = "Este campo es obligatorio";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    @Pattern(regexp = "^[^<>]*$", message = NO_ANGLE_BRACKETS_MESSAGE)
    @Size(max = 50, message = "Este campo no puede tener más de 50 caracteres")
    private String full_name;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    @Pattern(regexp = "^[^<>]*$", message = NO_ANGLE_BRACKETS_MESSAGE)
    @Size(max = 10, message = "Este campo no puede tener más de 10 caracteres")
    private String phone;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    @Pattern(regexp = "^[^<>]*$", message = NO_ANGLE_BRACKETS_MESSAGE)
    @Email(message = "Este correo electrónico no es válido")
    private String email;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<VehicleModel> vehicles;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeModel employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<VehicleModel> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleModel> vehicles) {
        this.vehicles = vehicles;
    }

    public EmployeeModel getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeModel employee) {
        this.employee = employee;
    }

}
