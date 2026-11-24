package ar.edu.utn.frre.concesionaria.modelo;

/**
 * Entidad Concreta: Auto (Automóvil)
 * <p>
 * Hereda de {@link Vehiculo}. Incorpora atributos específicos para automóviles
 * como la cantidad de puertas y el tipo de carrocería (Sedán, Hatchback, etc.).
 * </p>
 * 
 * Cumple con el Eje Técnico N° 4: "Herencia y Polimorfismo".
 */
public class Auto extends Vehiculo {

    private static final long serialVersionUID = 1L;

    // Atributos específicos
    private Integer cantidadPuertas;
    private TipoCarroceria tipoCarroceria;

    /* =========================================================================
     * >>> ZONA DE MODIFICACIÓN EN VIVO (LIVE CODING - COLOQUIO) <<<
     * Si el docente te solicita: "Agregue un atributo específico al Auto
     * (por ejemplo: tieneAireAcondicionado, transmisionAutomatica, o capacidadBaulLitros)",
     * hazlo en este bloque:
     * 
     * private Boolean tieneAireAcondicionado;
     * 
     * Recuerda agregarlo a los constructores, getters/setters y mostrarFichaTecnica().
     * ========================================================================= */

    /**
     * Constructor por defecto sin argumentos (JavaBean).
     */
    public Auto() {
        super();
        this.setTipo(TipoVehiculo.AUTO);
        this.cantidadPuertas = 5;
        this.tipoCarroceria = TipoCarroceria.SEDAN;
    }

    /**
     * Constructor completo parametrizado.
     */
    public Auto(String patente, String marca, String modelo, Integer anio,
                String color, Double precioBase, CondicionVehiculo condicion,
                Integer kilometraje, Integer cantidadPuertas, TipoCarroceria tipoCarroceria) {
        super(patente, marca, modelo, anio, color, precioBase, condicion, kilometraje, TipoVehiculo.AUTO);
        this.cantidadPuertas = cantidadPuertas;
        this.tipoCarroceria = tipoCarroceria;
    }

    // =========================================================================
    // SOBREESCRITURA POLIMÓRFICA (EJE 4)
    // =========================================================================

    @Override
    public double calcularImpuestoCategoria() {
        // Los automóviles abonan un 12% de alícuota sobre el valor base
        return getPrecioBase() * 0.12;
    }

    @Override
    public String mostrarFichaTecnica() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("         FICHA TÉCNICA - AUTOMÓVIL      \n");
        sb.append("========================================\n");
        sb.append("Patente:         ").append(getPatente()).append("\n");
        sb.append("Marca y Modelo:  ").append(getMarca()).append(" ").append(getModelo()).append("\n");
        sb.append("Año:             ").append(getAnio()).append("\n");
        sb.append("Color:           ").append(getColor()).append("\n");
        sb.append("Condición:       ").append(getCondicion().getEtiqueta()).append("\n");
        sb.append("Kilometraje:     ").append(getKilometraje()).append(" km\n");
        sb.append("Carrocería:      ").append(tipoCarroceria != null ? tipoCarroceria.getNombre() : "N/D").append("\n");
        sb.append("Cant. Puertas:   ").append(cantidadPuertas).append("\n");
        sb.append("Precio Base:     $").append(getPrecioBase()).append("\n");
        sb.append("Arancel Cat.:    $").append(calcularImpuestoCategoria()).append("\n");
        sb.append("PRECIO FINAL:    $").append(calcularPrecioFinal()).append("\n");
        sb.append("========================================");
        return sb.toString();
    }

    @Override
    public String toString() {
        return super.toString() + " | Carrocería: " +
               (tipoCarroceria != null ? tipoCarroceria.getNombre() : "N/D") +
               " | Puertas: " + cantidadPuertas;
    }

    // =========================================================================
    // GETTERS Y SETTERS
    // =========================================================================

    public Integer getCantidadPuertas() {
        return cantidadPuertas;
    }

    public void setCantidadPuertas(Integer cantidadPuertas) {
        this.cantidadPuertas = cantidadPuertas;
    }

    public TipoCarroceria getTipoCarroceria() {
        return tipoCarroceria;
    }

    public void setTipoCarroceria(TipoCarroceria tipoCarroceria) {
        this.tipoCarroceria = tipoCarroceria;
    }
}
