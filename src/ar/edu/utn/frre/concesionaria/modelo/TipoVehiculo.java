package ar.edu.utn.frre.concesionaria.modelo;

/**
 * Enumerativo que representa los tipos de vehículos gestionados por la concesionaria.
 * Cumple con el Eje Técnico N° 7: "Tipos Enumerativos (Enums)".
 */
public enum TipoVehiculo {
    AUTO("Automóvil"),
    CAMIONETA("Camioneta / Utilitario"),
    MOTO("Motocicleta");

    private final String descripcion;

    TipoVehiculo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
