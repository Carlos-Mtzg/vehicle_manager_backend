package mx.edu.utez.vehicleManager.module.vehicle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class VehicleDto {

    public static final String NO_ANGLE_BRACKETS_MESSAGE = "No se permiten los caracteres < o >";

    @Size(max = 50, message = "Este campo no puede tener más de 50 caracteres")
    @Pattern(regexp = "^[^<>]*$", message = NO_ANGLE_BRACKETS_MESSAGE)
    private String model;

    @Size(max = 50, message = "Este campo no puede tener más de 50 caracteres")
    @Pattern(regexp = "^[^<>]*$", message = NO_ANGLE_BRACKETS_MESSAGE)
    private String color;

    @Positive(message = "El precio debe ser mayor a 0")
    @Digits(integer = 7, fraction = 2, message = "El precio debe tener máximo 7 dígitos enteros y 2 decimales")
    private BigDecimal price;

    private LocalDate saleDate;
    private Long customerId;
    private Long brandId;
    private List<Long> serviceIds;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model != null ? model.trim() : null;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color != null ? color.trim() : null;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public List<Long> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Long> serviceIds) {
        this.serviceIds = serviceIds;
    }

}
