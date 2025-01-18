package dev.ovidio.exception;

public class SlotJaPreenchidoException extends BaseException{

    public SlotJaPreenchidoException() {
        super("O slot destino já está preenchido e por isso não pode ser movido");
    }

}
