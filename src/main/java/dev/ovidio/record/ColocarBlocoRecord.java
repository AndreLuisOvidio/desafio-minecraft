package dev.ovidio.record;

import dev.ovidio.entity.Coordenadas;
import dev.ovidio.type.CodigoSlot;
import io.smallrye.common.constraint.NotNull;

public record ColocarBlocoRecord(
        @NotNull
        Coordenadas coordenadas,

        CodigoSlot slotItem
) {
}
