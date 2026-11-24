package ar.edu.utn.frre.concesionaria.modelo;

/**
 * Enumerativo para los estilos de motocicletas comercializadas.
 */
public enum EstiloMoto {
    DEPORTIVA("Deportiva / Sport"),
    CUSTOM("Custom / Chopper"),
    SCOOTER("Scooter"),
    ENDURO("Enduro / Cross"),
    NAKED("Naked / Urbana");

    private final String estilo;

    EstiloMoto(String estilo) {
        this.estilo = estilo;
    }

    public String getEstilo() {
        return estilo;
    }

    @Override
    public String toString() {
        return estilo;
    }
}
