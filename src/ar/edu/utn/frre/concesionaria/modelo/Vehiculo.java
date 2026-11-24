package ar.edu.utn.frre.concesionaria.modelo;

import java.io.Serializable;

/**
 * Clase Abstracta Base: Vehiculo
 * <p>
 * Representa la abstracción de mayor nivel de cualquier unidad comercializada
 * por la concesionaria. Cumple con el estándar JavaBean / POJO:
 * - Atributos estrictamente privados (encapsulamiento).
 * - Constructor por defecto sin argumentos y constructor parametrizado.
 * - Métodos getters y setters públicos.
 * - Implementa {@link Serializable}.
 * </p>
 * <p>
 * Además, implementa la interfaz de comportamiento {@link Vendible} y
 * {@link Comparable} para habilitar el ordenamiento natural en colecciones.
 * Sobrescribe de manera impecable {@code equals()}, {@code hashCode()} y {@code toString()}.
 * </p>
 * 
 * Cumple con los Ejes Técnicos N° 1, 3, 4, 5, 10 y 11 del TFI.
 */
public abstract class Vehiculo implements Vendible, Comparable<Vehiculo>, Serializable {

    private static final long serialVersionUID = 1L;

    // =========================================================================
    // ATRIBUTOS PRIVADOS DE LA ENTIDAD (ENCAPSULAMIENTO POJO / JAVABEAN)
    // =========================================================================
    private String patente;
    private String marca;
    private String modelo;
    private Integer anio;
    private String color;
    private Double precioBase;
    private CondicionVehiculo condicion;
    private Integer kilometraje;
    private TipoVehiculo tipo;

    /* =========================================================================
     * >>> ZONA DE MODIFICACIÓN EN VIVO (LIVE CODING - COLOQUIO) <<<
     * Si el docente te solicita: "Agregue un nuevo atributo general al vehículo
     * (por ejemplo: numeroChasis, cantidadDuenios, o tipoCombustible)", hazlo aquí:
     * 
     * 1. Declara el atributo privado:
     *    // private String numeroChasis;
     * 
     * 2. Agrega el parámetro al constructor y sus respectivos getter y setter.
     * 3. Inclúyelo en el método toString() y en la ficha técnica.
     * ========================================================================= */

    /**
     * Constructor por defecto sin parámetros (Requisito mandatorio JavaBean).
     */
    public Vehiculo() {
        this.patente = "";
        this.marca = "";
        this.modelo = "";
        this.anio = 2024;
        this.color = "Blanco";
        this.precioBase = 0.0;
        this.condicion = CondicionVehiculo.NUEVO;
        this.kilometraje = 0;
    }

    /**
     * Constructor parametrizado con los datos fundamentales de cualquier vehículo.
     * 
     * @param patente Identificador unívoco del vehículo (ej: "AA123BB").
     * @param marca Fabricante o marca automotriz.
     * @param modelo Denominación comercial del modelo.
     * @param anio Año de fabricación.
     * @param color Color de la carrocería.
     * @param precioBase Valor comercial de lista base.
     * @param condicion Condición de la unidad (NUEVO o USADO).
     * @param kilometraje Kilómetros recorridos (0 para nuevos).
     * @param tipo Tipo de vehículo (AUTO, CAMIONETA, MOTO).
     */
    public Vehiculo(String patente, String marca, String modelo, Integer anio,
                    String color, Double precioBase, CondicionVehiculo condicion,
                    Integer kilometraje, TipoVehiculo tipo) {
        this.patente = (patente != null) ? patente.trim().toUpperCase() : "";
        this.marca = (marca != null) ? marca.trim() : "";
        this.modelo = (modelo != null) ? modelo.trim() : "";
        this.anio = anio;
        this.color = (color != null) ? color.trim() : "";
        this.precioBase = precioBase;
        this.condicion = condicion;
        this.kilometraje = (condicion == CondicionVehiculo.NUEVO) ? 0 : kilometraje;
        this.tipo = tipo;
    }

    // =========================================================================
    // MÉTODOS ABSTRACTOS POLIMÓRFICOS (EJE 4)
    // =========================================================================

    /**
     * Genera una descripción detallada multilínea con las características
     * técnicas específicas de cada subtipo de vehículo (sobrescrito polimórficamente).
     * 
     * @return Cadena formateada para ser mostrada en consola.
     */
    public abstract String mostrarFichaTecnica();

    /**
     * Calcula el impuesto o costo de flete/transferencia según la categoría del vehículo.
     * Cada subclase define su alícuota específica (Polimorfismo).
     * 
     * @return Monto adicional en pesos.
     */
    public abstract double calcularImpuestoCategoria();

    // =========================================================================
    // IMPLEMENTACIÓN DE LA INTERFAZ VENDIBLE (EJE 5)
    // =========================================================================

    @Override
    public double calcularPrecioFinal() {
        double subtotal = this.precioBase;

        // Si es usado, se aplica una depreciación leve según su kilometraje
        if (this.condicion == CondicionVehiculo.USADO && this.kilometraje != null && this.kilometraje > 0) {
            double factorDepreciacion = Math.min(0.30, (this.kilometraje / 10000.0) * 0.02);
            subtotal = subtotal * (1.0 - factorDepreciacion);
        }

        // Se le adiciona el impuesto o arancel de la categoría específica
        double arancel = calcularImpuestoCategoria();
        return subtotal + arancel;
    }

    @Override
    public void aplicarDescuento(double porcentaje) {
        if (porcentaje > 0 && porcentaje <= 100) {
            double rebaja = this.precioBase * (porcentaje / 100.0);
            this.precioBase = this.precioBase - rebaja;
        }
    }

    @Override
    public double getPrecioBase() {
        return (this.precioBase != null) ? this.precioBase : 0.0;
    }

    // =========================================================================
    // ORDENAMIENTO NATURAL CON COMPARABLE (EJE 10)
    // Ordena de menor a mayor por Precio Base de lista.
    // =========================================================================
    @Override
    public int compareTo(Vehiculo otro) {
        if (otro == null) {
            return 1;
        }
        // Comparación natural según java 5 (página 33): negativo si menor, positivo si mayor, cero si igual
        if (this.precioBase < otro.precioBase) {
            return -1;
        } else if (this.precioBase > otro.precioBase) {
            return 1;
        } else {
            return 0;
        }
    }

    // =========================================================================
    // CONTRATO EQUALS Y HASHCODE (EJE 10 - java 5, páginas 6 y 13)
    // Dos vehículos son idénticos si y sólo si comparten la misma patente.
    // =========================================================================
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Vehiculo) {
            Vehiculo otro = (Vehiculo) obj;
            if (this.patente != null && otro.patente != null) {
                return this.patente.equalsIgnoreCase(otro.patente);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (patente != null) ? patente.toUpperCase().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "[" + (tipo != null ? tipo.name() : "VEHICULO") + "] " +
               marca + " " + modelo + " (" + anio + ") - Color: " + color +
               " | Condición: " + (condicion != null ? condicion.getEtiqueta() : "N/D") +
               " | Km: " + kilometraje + " | Base: $" + precioBase +
               " | Final: $" + calcularPrecioFinal() + " | Patente: " + patente;
    }

    // =========================================================================
    // GETTERS Y SETTERS (ENCAPSULAMIENTO JAVABEAN)
    // =========================================================================

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = (patente != null) ? patente.trim().toUpperCase() : "";
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public CondicionVehiculo getCondicion() {
        return condicion;
    }

    public void setCondicion(CondicionVehiculo condicion) {
        this.condicion = condicion;
        if (condicion == CondicionVehiculo.NUEVO) {
            this.kilometraje = 0;
        }
    }

    public Integer getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(Integer kilometraje) {
        this.kilometraje = kilometraje;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }
}
