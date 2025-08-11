package mx.edu.utez.vehicleManager.module.vehicle;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vehicle")
@CrossOrigin(origins = "*")
@Tag(name = "Vehículos", description = "Endpoints para gestión de vehículos")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("")
    @Operation(summary = "Listar vehículos", description = "Obtiene todos los vehículos registrados.")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa")
    public ResponseEntity<Object> getAllVehicles() {
        return vehicleService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener vehículo por ID", description = "Obtiene un vehículo específico por su ID.")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa")
    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    public ResponseEntity<Object> getVehicleById(@PathVariable("id") Long id) {
        return vehicleService.getById(id);
    }

    @PostMapping("")
    @Operation(summary = "Crear vehículo", description = "Crea un nuevo vehículo.")
    @ApiResponse(responseCode = "201", description = "Vehículo registrado correctamente")
    @ApiResponse(responseCode = "404", description = "No se encontró la marca")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<Object> createVehicle(@RequestBody @Valid VehicleDto request) {
        return vehicleService.save(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vehículo", description = "Actualiza los datos de un vehículo existente.")
    @ApiResponse(responseCode = "200", description = "Vehículo actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<Object> updateVehicle(@PathVariable("id") Long id, @RequestBody @Valid VehicleDto request) {
        return vehicleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar vehículo", description = "Elimina un vehículo si no está vendido.")
    @ApiResponse(responseCode = "200", description = "Vehículo eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    @ApiResponse(responseCode = "409", description = "No se puede eliminar un vehículo vendido")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<Object> deleteVehicle(@PathVariable("id") Long id) {
        return vehicleService.delete(id);
    }

    @GetMapping("/sold/count")
    @Operation(summary = "Cantidad de autos vendidos", description = "Obtiene la cantidad de autos vendidos.")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa")
    public ResponseEntity<Object> getSoldVehiclesCount() {
        return vehicleService.getSoldVehiclesCount();
    }

    @GetMapping("/available/count")
    @Operation(summary = "Cantidad de autos disponibles", description = "Obtiene la cantidad de autos disponibles.")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa")
    public ResponseEntity<Object> getAvailableVehiclesCount() {
        return vehicleService.getAvailableVehiclesCount();
    }
}
