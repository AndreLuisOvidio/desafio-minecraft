package dev.ovidio.record;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;

public record MoverItemRecord(
        @NotNull
        @NotBlank
        String slotOrigem,

        @NotNull
        @NotBlank
        String slotDestino,

        Integer quantidade
) {
}
