package dev.ovidio.record;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.Min;

public record ColetarItemRecord(
        @NotNull
        String nome,

        @Min(value = 1)
        Integer durabilidade,

        @Min(value = 1)
        int quantidade) {
}
