package ar.edu.utn.frre.concesionaria.modelo;

/**
 * Entidad Concreta: Camioneta (Utilitario / Pick-up)
 * <p>
 * Hereda de {@link Vehiculo}. Incorpora atributos específicos del segmento
 * utilitario: capacidad de carga en kilogramos y si dispone de tracción 4x4.
 * </p>
 * 
 * Cumple con el Eje Técnico N° 4: "Herencia y Polimorfismo".
 */
public class Camioneta extends Vehiculo {

    private static final long serialVersionUID = 1L;

    // Atributos específicos
    private Double capacidadCargaKg;
    private Boolean es4x4;

    /* =========================================================================
     * >>> ZONA DE MODIFICACIÓN EN VIVO (LIVE CODING - COLOQUIO) <<<
     * Si el docente te solicita: "Agregue un atributo específico a Camioneta
     * (por ejemplo: tipoCabina (Simple/Doble), tieneGanchoRemolque, o potenciaHp)",
     * hazlo en este bloque:
     * 
     * private Boolean tieneGanchoRemolque;
     * ========================================================================= */

    /**
     * Constructor por defecto sin argumentos (JavaBean).
     */
    public Camioneta() {
        super();
        this.setTipo(TipoVehiculo.CAMIONETA);
        this.capacidadCargaKg = 1000.0;
        this.es4x4 = false;
    }

    /**
     * Constructor completo parametrizado.
     */
    public Camioneta(String patente, String marca, String modelo, Integer anio,
                     String color, Double precioBase, CondicionVehiculo condicion,
                     Integer kilometraje, Double capacidadCargaKg, Boolean es4x4) {
        super(patente, marca, modelo, anio, color, precioBase, condicion, kilometraje, TipoVehiculo.CAMIONETA);
        this.capacidadCargaKg = capacidadCargaKg;
        this.es4x4 = es4x4;
    }

    // =========================================================================
    // SOBREESCRITURA POLIMÓRFICA (EJE 4)
    // =========================================================================

    @Override
    public double calcularImpuestoCategoria() {
        // Las camionetas tienen un arancel reducido del 8% por considerarse vehículo productivo
        double arancel = getPrecioBase() * 0.08;
        // Si cuenta con tracción integral 4x4, se añade un plus de certificación mecánica
        if (Boolean.TRUE.equals(this.es4x4)) {
            arancel += 50000.0;
        }
        return arancel;
    }

    @Override
    public String mostrarFichaTecnica() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("        FICHA TÉCNICA - CAMIONETA       \n");
        sb.append("========================================\n");
        sb.append("Patente:         ").append(getPatente()).append("\n");
        sb.append("Marca y Modelo:  ").append(getMarca()).append(" ").append(getModelo()).append("\n");
        sb.append("Año:             ").append(getAnio()).append("\n");
        sb.append("Color:           ").append(getColor()).append("\n");
        sb.append("Condición:       ").append(getCondicion().getEtiqueta()).append("\n");
        sb.append("Kilometraje:     ").append(getKilometraje()).append(" km\n");
        sb.append("Cap. Carga:      ").append(capacidadCargaKg != null ? capacidadCargaKg : 0.0).append(" kg\n");
        sb.append("Tracción:        ").append(Boolean.TRUE.equals(es4x4) ? "Tracción 4x4 Integral" : "Tracción Simple 4x2").append("\n");
        sb.append("Precio Base:     $").append(getPrecioBase()).append("\n");
        sb.append("Arancel Cat.:    $").append(calcularImpuestoCategoria()).append("\n");
        sb.append("PRECIO FINAL:    $").append(calcularPrecioFinal()).append("\n");
        sb.append("========================================");
        return sb.toString();
    }

    @Override
    public String toString() {
        return super.toString() + " | Carga: " + capacidadCargaKg + " kg | 4x4: " +
               (Boolean.TRUE.equals(es4x4) ? "SÍ" : "NO");
    }

    // =========================================================================
    // GETTERS Y SETTERS
    // =========================================================================

    public Double getCapacidadCargaKg() {
        return capacidadCargaKg;
    }

    public void setCapacidadCargaKg(Double capacidadCargaKg) {
        this.capacidadCargaKg = capacidadCargaKg;
    }

    public Boolean getEs4x4() {
        return es4x4;
    }

    public void setEs4x4(Boolean es4x4) {
        this.es4x4 = es4x4;
    }
}
