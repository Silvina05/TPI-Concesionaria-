package ar.edu.utn.frre.concesionaria.modelo;

/**
 * Entidad Concreta: Moto (Motocicleta)
 * <p>
 * Hereda de {@link Vehiculo}. Incorpora atributos específicos del segmento
 * de dos ruedas: cilindrada en centímetros cúbicos (cc) y el estilo de moto
 * (Deportiva, Scooter, Custom, Enduro, Naked).
 * </p>
 * 
 * Cumple con el Eje Técnico N° 4: "Herencia y Polimorfismo".
 */
public class Moto extends Vehiculo {

    private static final long serialVersionUID = 1L;

    // Atributos específicos
    private Integer cilindradaCc;
    private EstiloMoto estiloMoto;

    /* =========================================================================
     * >>> ZONA DE MODIFICACIÓN EN VIVO (LIVE CODING - COLOQUIO) <<<
     * Si el docente te solicita: "Agregue un atributo específico a Moto
     * (por ejemplo: incluyeCasco, tipoArranque (Eléctrico/Pedal), o capacidadTanqueLitros)",
     * hazlo en este bloque:
     * 
     * private Boolean incluyeCasco;
     * ========================================================================= */

    /**
     * Constructor por defecto sin argumentos (JavaBean).
     */
    public Moto() {
        super();
        this.setTipo(TipoVehiculo.MOTO);
        this.cilindradaCc = 150;
        this.estiloMoto = EstiloMoto.NAKED;
    }

    /**
     * Constructor completo parametrizado.
     */
    public Moto(String patente, String marca, String modelo, Integer anio,
                String color, Double precioBase, CondicionVehiculo condicion,
                Integer kilometraje, Integer cilindradaCc, EstiloMoto estiloMoto) {
        super(patente, marca, modelo, anio, color, precioBase, condicion, kilometraje, TipoVehiculo.MOTO);
        this.cilindradaCc = cilindradaCc;
        this.estiloMoto = estiloMoto;
    }

    // =========================================================================
    // SOBREESCRITURA POLIMÓRFICA (EJE 4)
    // =========================================================================

    @Override
    public double calcularImpuestoCategoria() {
        // Alícuota escalonada según cilindrada:
        // Hasta 250cc abona un arancel promocional del 5%, superiores abonan 9%
        if (cilindradaCc != null && cilindradaCc > 250) {
            return getPrecioBase() * 0.09;
        }
        return getPrecioBase() * 0.05;
    }

    @Override
    public String mostrarFichaTecnica() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("       FICHA TÉCNICA - MOTOCICLETA      \n");
        sb.append("========================================\n");
        sb.append("Patente:         ").append(getPatente()).append("\n");
        sb.append("Marca y Modelo:  ").append(getMarca()).append(" ").append(getModelo()).append("\n");
        sb.append("Año:             ").append(getAnio()).append("\n");
        sb.append("Color:           ").append(getColor()).append("\n");
        sb.append("Condición:       ").append(getCondicion().getEtiqueta()).append("\n");
        sb.append("Kilometraje:     ").append(getKilometraje()).append(" km\n");
        sb.append("Cilindrada:      ").append(cilindradaCc).append(" cc\n");
        sb.append("Estilo:          ").append(estiloMoto != null ? estiloMoto.getEstilo() : "N/D").append("\n");
        sb.append("Precio Base:     $").append(getPrecioBase()).append("\n");
        sb.append("Arancel Cat.:    $").append(calcularImpuestoCategoria()).append("\n");
        sb.append("PRECIO FINAL:    $").append(calcularPrecioFinal()).append("\n");
        sb.append("========================================");
        return sb.toString();
    }

    @Override
    public String toString() {
        return super.toString() + " | Cilindrada: " + cilindradaCc + " cc | Estilo: " +
               (estiloMoto != null ? estiloMoto.getEstilo() : "N/D");
    }

    // =========================================================================
    // GETTERS Y SETTERS
    // =========================================================================

    public Integer getCilindradaCc() {
        return cilindradaCc;
    }

    public void setCilindradaCc(Integer cilindradaCc) {
        this.cilindradaCc = cilindradaCc;
    }

    public EstiloMoto getEstiloMoto() {
        return estiloMoto;
    }

    public void setEstiloMoto(EstiloMoto estiloMoto) {
        this.estiloMoto = estiloMoto;
    }
}
