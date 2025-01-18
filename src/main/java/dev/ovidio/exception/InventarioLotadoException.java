package dev.ovidio.exception;

public class InventarioLotadoException extends BaseException {

    public InventarioLotadoException() {
        super("Ação não pode ser realizada pois o inventario está lotado");
    }
}
