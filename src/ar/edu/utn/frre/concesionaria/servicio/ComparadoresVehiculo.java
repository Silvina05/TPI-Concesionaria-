package ar.edu.utn.frre.concesionaria.servicio;

import ar.edu.utn.frre.concesionaria.modelo.Vehiculo;
import java.util.Comparator;

/**
 * Colección de Comparadores personalizados para la entidad {@link Vehiculo}.
 * <p>
 * Demuestra la utilización de la interfaz estándar {@link java.util.Comparator}
 * en conjunto con {@link java.util.Collections#sort(java.util.List, java.util.Comparator)}.
 * Esto permite ordenar los vehículos bajo múltiples criterios complementarios
 * al orden natural provisto por {@link java.lang.Comparable}.
 * </p>
 * 
 * Cumple con el Eje Técnico N° 10: "Colecciones & Sobreescritura de Métodos"
 * y el material de la cátedra (java 5: "La interface Comparator").
 */
public final class ComparadoresVehiculo {

    // Constructor privado para clase utilitaria
    private ComparadoresVehiculo() { }

    /**
     * Ordena alfabéticamente por Marca y luego por Modelo.
     */
    public static class PorMarcaYModelo implements Comparator<Vehiculo> {
        @Override
        public int compare(Vehiculo v1, Vehiculo v2) {
            if (v1 == null && v2 == null) return 0;
            if (v1 == null) return 1;
            if (v2 == null) return -1;

            int cmpMarca = v1.getMarca().compareToIgnoreCase(v2.getMarca());
            if (cmpMarca != 0) {
                return cmpMarca;
            }
            return v1.getModelo().compareToIgnoreCase(v2.getModelo());
        }
    }

    /**
     * Ordena cronológicamente en forma descendente por Año de fabricación (más recientes primero).
     */
    public static class PorAnioDescendente implements Comparator<Vehiculo> {
        @Override
        public int compare(Vehiculo v1, Vehiculo v2) {
            if (v1 == null || v2 == null) return 0;
            // Descendente: v2 compareTo v1
            return v2.getAnio().compareTo(v1.getAnio());
        }
    }

    /**
     * Ordena ascendentemente por Kilometraje acumulado (menor rodaje primero).
     */
    public static class PorKilometrajeAscendente implements Comparator<Vehiculo> {
        @Override
        public int compare(Vehiculo v1, Vehiculo v2) {
            if (v1 == null || v2 == null) return 0;
            return v1.getKilometraje().compareTo(v2.getKilometraje());
        }
    }

    /* =========================================================================
     * >>> ZONA DE MODIFICACIÓN EN VIVO (LIVE CODING - COLOQUIO) <<<
     * Si el docente te solicita: "Agregue un nuevo comparador (por ejemplo:
     * ordenar por Color alfabéticamente, o por Precio Final descendente)",
     * impleméntalo aquí como una clase estática adicional:
     * 
     * public static class PorColor implements Comparator<Vehiculo> {
     *     @Override
     *     public int compare(Vehiculo v1, Vehiculo v2) {
     *         return v1.getColor().compareToIgnoreCase(v2.getColor());
     *     }
     * }
     * ========================================================================= */
}
