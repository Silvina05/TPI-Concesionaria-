package ar.edu.utn.frre.concesionaria.excepciones;

/**
 * Excepción lanzada cuando los datos suministrados no superan las reglas de validación de negocio
 * (por ejemplo, precio negativo, año fuera de rango, kilometraje negativo en un auto usado, etc.).
 */
public class DatoInvalidoException extends ConcesionariaException {

    private static final long serialVersionUID = 1L;

    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
