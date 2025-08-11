package mx.edu.utez.vehicleManager.module.vehicle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.edu.utez.vehicleManager.utils.Utilities;
import mx.edu.utez.vehicleManager.module.brand.BrandModel;
import mx.edu.utez.vehicleManager.module.brand.IBrandRepository;
import mx.edu.utez.vehicleManager.module.service.IServiceRepository;
import mx.edu.utez.vehicleManager.module.service.ServiceModel;

@Service
@Primary
@Transactional
public class VehicleService {

    private final IVehicleRepository vehicleRepository;
    private final IBrandRepository brandRepository;
    private final IServiceRepository serviceRepository;

    public VehicleService(IVehicleRepository vehicleRepository, IBrandRepository brandRepository,
            IServiceRepository serviceRepository) {
        this.vehicleRepository = vehicleRepository;
        this.brandRepository = brandRepository;
        this.serviceRepository = serviceRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getAll() {
        List<VehicleModel> vehicles = this.vehicleRepository.findAll();
        return Utilities.generateResponse(HttpStatus.OK, "Consulta exitosa", vehicles);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> getById(Long id) {
        return vehicleRepository.findById(id)
                .map(vehicle -> Utilities.generateResponse(HttpStatus.OK, "Consulta exitosa", vehicle))
                .orElseGet(() -> Utilities.generateResponse(HttpStatus.NOT_FOUND, "Empleado no encontrado", null));
    }

    @Transactional
    public ResponseEntity<Object> save(VehicleDto dto) {
        try {
            // Create new VehicleModel from DTO
            VehicleModel vehicle = new VehicleModel();
            vehicle.setModel(dto.getModel());
            vehicle.setColor(dto.getColor());
            vehicle.setPrice(dto.getPrice());
            vehicle.setRegistration_date(LocalDate.now());
            
            // Set brand if provided
            if (dto.getBrandId() != null) {
                Optional<BrandModel> brandOpt = brandRepository.findById(dto.getBrandId());
                if (!brandOpt.isPresent()) {
                    return Utilities.generateResponse(HttpStatus.NOT_FOUND, "No se encontró la marca", null);
                }
                vehicle.setBrand(brandOpt.get());
            } else {
                return Utilities.generateResponse(HttpStatus.BAD_REQUEST, "La marca es obligatoria", null);
            }
            
            // Map service IDs to managed entities
            if (dto.getServiceIds() != null && !dto.getServiceIds().isEmpty()) {
                List<ServiceModel> managedServices = dto.getServiceIds().stream()
                        .map(serviceRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
                vehicle.setServices(managedServices);
            } else {
                vehicle.setServices(new ArrayList<>());
            }
            
            VehicleModel saved = vehicleRepository.save(vehicle);
            return Utilities.generateResponse(HttpStatus.CREATED, "Vehículo registrado correctamente", saved);
        } catch (Exception e) {
            return Utilities.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al guardar el registro", null);
        }
    }

    @Transactional
    public ResponseEntity<Object> update(Long id, VehicleDto dto) {
        try {
            return vehicleRepository.findById(id)
                    .map(existingVehicle -> {
                        if (dto.getModel() != null) {
                            existingVehicle.setModel(dto.getModel());
                        }
                        if (dto.getColor() != null) {
                            existingVehicle.setColor(dto.getColor());
                        }
                        if (dto.getPrice() != null) {
                            existingVehicle.setPrice(dto.getPrice());
                        }
                        // Update services if provided
                        if (dto.getServiceIds() != null) {
                            if (!dto.getServiceIds().isEmpty()) {
                                List<ServiceModel> managedServices = dto.getServiceIds().stream()
                                        .map(serviceRepository::findById)
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .collect(Collectors.toList());
                                existingVehicle.setServices(managedServices);
                            } else {
                                existingVehicle.setServices(new ArrayList<>());
                            }
                        }
                        VehicleModel updated = vehicleRepository.save(existingVehicle);
                        return Utilities.generateResponse(HttpStatus.OK, "Vehículo actualizado exitosamente", updated);
                    })
                    .orElseGet(() -> Utilities.generateResponse(HttpStatus.NOT_FOUND, "Vehículo no encontrado", null));
        } catch (Exception e) {
            return Utilities.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar el vehículo",
                    null);
        }
    }

    @Transactional
    public ResponseEntity<Object> delete(Long id) {
        try {
            return vehicleRepository.findById(id).map(vehicle -> {
                if (vehicle.getSale_date() != null) {
                    return Utilities.generateResponse(HttpStatus.CONFLICT, "No se puede eliminar un vehículo vendido",
                            null);
                }
                this.vehicleRepository.delete(vehicle);
                return Utilities.generateResponse(HttpStatus.OK, "Vehículo eliminado exitosamente", id);
            }).orElseGet(() -> Utilities.generateResponse(HttpStatus.NOT_FOUND, "Vehículo no encontrado", null));
        } catch (Exception e) {
            return Utilities.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrió un error al eliminar el registro",
                    null);
        }
    }

    public ResponseEntity<Object> getSoldVehiclesCount() {
        long count = vehicleRepository.getSoldVehiclesCount();
        return Utilities.generateResponse(HttpStatus.OK, "Cantidad de autos vendidos", count);
    }

    public ResponseEntity<Object> getAvailableVehiclesCount() {
        long count = vehicleRepository.getAvailableVehiclesCount();
        return Utilities.generateResponse(HttpStatus.OK, "Cantidad de autos disponibles", count);
    }
}
