package co.edu.unbosque.modelo.excepciones;

public class OperacionNoPermitidaException extends AerolineaException {
    public OperacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}