package dev.ovidio.exception;

public class BlocoPreenchidoException extends BaseException{
    public BlocoPreenchidoException() {
        super("Ação não pode ser realizada pois o bloco está preenchido");
    }
}
