package dev.ovidio.record;

public record ExceptionResponseRecord(String tipo, String mensagem) {

    public ExceptionResponseRecord(Exception e) {
        this(e.getClass().getName(), e.getMessage());
    }

}
