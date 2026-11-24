package ar.edu.utn.frre.concesionaria.excepciones;

/**
 * Excepción lanzada cuando se intenta registrar un vehículo cuya patente ya existe en el stock.
 */
public class VehiculoDuplicadoException extends ConcesionariaException {

    private static final long serialVersionUID = 1L;

    public VehiculoDuplicadoException(String patente) {
        super("Ya existe un vehículo registrado con la patente / identificador: '" + patente + "'.");
    }
}
