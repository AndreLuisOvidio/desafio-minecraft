package dev.ovidio.exception;

public class BaseException extends Exception{

    private final String mensagem;

    public BaseException(String mensagem) {
        super(mensagem);
        this.mensagem = mensagem;
    }

    @Override
    public String getMessage() {
        return mensagem;
    }
}
