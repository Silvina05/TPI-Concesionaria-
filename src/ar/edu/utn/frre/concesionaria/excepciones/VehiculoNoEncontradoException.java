package ar.edu.utn.frre.concesionaria.excepciones;

/**
 * Excepción lanzada cuando una patente solicitada no se encuentra registrada en el inventario.
 */
public class VehiculoNoEncontradoException extends ConcesionariaException {

    private static final long serialVersionUID = 1L;

    public VehiculoNoEncontradoException(String patente) {
        super("No se encontró ningún vehículo con la patente / identificador: '" + patente + "'.");
    }
}
