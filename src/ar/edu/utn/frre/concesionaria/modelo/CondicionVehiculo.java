package ar.edu.utn.frre.concesionaria.modelo;

/**
 * Enumerativo que define el estado comercial de una unidad: Nuevo (0 Km) o Usado.
 * Cumple con el Eje Técnico N° 7: "Tipos Enumerativos (Enums)".
 */
public enum CondicionVehiculo {
    NUEVO("Nuevo (0 Km)"),
    USADO("Usado (con rodaje)");

    private final String etiqueta;

    CondicionVehiculo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
