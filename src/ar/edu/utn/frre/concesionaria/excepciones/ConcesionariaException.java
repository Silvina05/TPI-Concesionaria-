package ar.edu.utn.frre.concesionaria.excepciones;

/**
 * Excepción Base Comprobada (Checked Exception) del Sistema de Concesionaria.
 * <p>
 * Todas las excepciones propias del negocio heredan de esta clase.
 * Al ser comprobada (hereda de {@link Exception} y no de RuntimeException),
 * el compilador fuerza a que la capa de presentación capture o declare la excepción,
 * garantizando que el programa nunca sufra caídas intempestivas frente a errores del usuario.
 * </p>
 * 
 * Cumple con el Eje Técnico N° 6: "Manejo de Excepciones".
 */
public class ConcesionariaException extends Exception {

    private static final long serialVersionUID = 1L;

    public ConcesionariaException(String mensaje) {
        super(mensaje);
    }

    public ConcesionariaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
