package ar.edu.utn.frre.concesionaria.modelo;

/**
 * Interfaz de Comportamiento: Vendible
 * <p>
 * Define el protocolo común que todo bien comercializable dentro de la concesionaria
 * debe implementar. Permite calcular el valor de venta final aplicando impuestos
 * o recargos según el tipo de bien, y gestionar descuentos promocionales.
 * </p>
 * 
 * Cumple con el Eje Técnico N° 5 de la cátedra: "Interfaces de Comportamiento".
 */
public interface Vendible {

    /**
     * Calcula el precio final de venta al público teniendo en cuenta
     * la categoría del vehículo, su condición (nuevo/usado) y los recargos o aranceles aplicables.
     * 
     * @return Precio final de venta en moneda de curso legal.
     */
    double calcularPrecioFinal();

    /**
     * Aplica un porcentaje de descuento comercial directo sobre el precio base.
     * 
     * @param porcentaje Porcentaje entre 0 y 100 a deducir del precio base.
     */
    void aplicarDescuento(double porcentaje);

    /**
     * Obtiene el valor base de lista del bien.
     * 
     * @return Precio base numérico.
     */
    double getPrecioBase();
}
