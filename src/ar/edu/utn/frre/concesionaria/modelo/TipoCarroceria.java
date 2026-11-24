package ar.edu.utn.frre.concesionaria.modelo;

/**
 * Enumerativo para los tipos de carrocería de automóviles y camionetas.
 */
public enum TipoCarroceria {
    SEDAN("Sedán"),
    HATCHBACK("Hatchback"),
    SUV("SUV / Crossover"),
    PICKUP("Pick-up"),
    COUPE("Coupé");

    private final String nombre;

    TipoCarroceria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
