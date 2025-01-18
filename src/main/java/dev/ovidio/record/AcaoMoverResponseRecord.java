package dev.ovidio.record;

import dev.ovidio.entity.Armadura;
import dev.ovidio.entity.SlotInventario;

import java.util.List;

public record AcaoMoverResponseRecord(
        List<SlotInventario> slots,
        Armadura armadura
) {
}
