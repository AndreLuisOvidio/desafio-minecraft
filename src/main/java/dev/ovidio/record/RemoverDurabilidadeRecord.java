package dev.ovidio.record;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RemoverDurabilidadeRecord(
        @NotNull
        @NotBlank
        String nomeSlot,

        @Min(1)
        int quantidadeRemover) {
}
