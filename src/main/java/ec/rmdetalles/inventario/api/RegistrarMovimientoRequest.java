package ec.rmdetalles.inventario.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record RegistrarMovimientoRequest(
        @NotBlank @Size(max=20) String tipoCodigo,
        @Positive int cantidad,
        @Size(max=240) String motivo,
        @Size(max=30) String referenciaTipo,
        UUID referenciaId
) { }
