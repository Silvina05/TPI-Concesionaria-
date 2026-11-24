package ar.edu.utn.frre.concesionaria.excepciones;

/**
 * Excepción lanzada cuando ocurre un error en la capa de persistencia (archivos I/O).
 * Encapsula las excepciones de bajo nivel de java.io (como FileNotFoundException o IOException)
 * evitando acoplar la capa de presentación con detalles de infraestructura.
 */
public class PersistenciaException extends ConcesionariaException {

    private static final long serialVersionUID = 1L;

    public PersistenciaException(String mensaje) {
        super(mensaje);
    }

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
