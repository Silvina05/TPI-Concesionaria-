package ar.edu.utn.frre.concesionaria.servicio;

import ar.edu.utn.frre.concesionaria.excepciones.*;
import ar.edu.utn.frre.concesionaria.modelo.CondicionVehiculo;
import ar.edu.utn.frre.concesionaria.modelo.TipoVehiculo;
import ar.edu.utn.frre.concesionaria.modelo.Vehiculo;
import ar.edu.utn.frre.concesionaria.persistencia.IRepositorio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Capa de Negocio / Servicio: ConcesionariaServicio
 * <p>
 * Centraliza toda la lógica comercial, reglas de validación y administración del stock.
 * Se encuentra completamente desacoplada de la interfaz de usuario (Consola/Diseño)
 * y de la infraestructura de persistencia mediante la inyección de la interfaz {@link IRepositorio}.
 * </p>
 * <p>
 * Administra internamente dos colecciones complementarias:
 * 1. {@link List}: Un {@link ArrayList} para mantener la lista completa y permitir ordenamientos dinámicos.
 * 2. {@link Map}: Un {@link HashMap} indexado por la clave 'Patente' para búsquedas instantáneas O(1).
 * </p>
 * 
 * Cumple con los Ejes Técnicos N° 2 ("Diseño vs. Negocio"), N° 6 ("Manejo de Excepciones")
 * y N° 10 ("Colecciones & Sobreescritura de Métodos").
 */
public class ConcesionariaServicio {

    private final IRepositorio<Vehiculo> repositorio;
    private final List<Vehiculo> listaStock;
    private final Map<String, Vehiculo> indiceStock;

    /**
     * Constructor con inyección de dependencias del repositorio.
     * Carga el stock inicial desde el medio de persistencia.
     * 
     * @param repositorio Soporte de almacenamiento que implementa {@link IRepositorio}.
     * @throws PersistenciaException Si ocurre un fallo crítico al leer los datos.
     */
    public ConcesionariaServicio(IRepositorio<Vehiculo> repositorio) throws PersistenciaException {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio de persistencia no puede ser nulo.");
        }
        this.repositorio = repositorio;
        this.listaStock = new ArrayList<Vehiculo>();
        this.indiceStock = new HashMap<String, Vehiculo>();

        // Cargar datos persistidos y sincronizar el HashMap
        recargarDesdeRepositorio();
    }

    /**
     * Vuelve a leer el medio de almacenamiento y reconstruye tanto la Lista como el HashMap.
     */
    public void recargarDesdeRepositorio() throws PersistenciaException {
        this.listaStock.clear();
        this.indiceStock.clear();

        List<Vehiculo> cargados = this.repositorio.cargarTodos();
        for (Vehiculo v : cargados) {
            this.listaStock.add(v);
            this.indiceStock.put(v.getPatente().toUpperCase(), v);
        }
    }

    // =========================================================================
    // REGLAS DE NEGOCIO Y VALIDACIONES (EJE 6)
    // =========================================================================

    /* =========================================================================
     * >>> ZONA DE MODIFICACIÓN EN VIVO: VALIDACIONES PERSONALIZADAS <<<
     * Si el docente te solicita: "Agregue una validación personalizada
     * (por ejemplo: validar formato de patente nuevo/viejo, año mínimo 2000,
     * o que el precio base no supere los 100 millones)", impleméntalo aquí:
     * ========================================================================= */
    private void validarDatosVehiculo(Vehiculo v) throws DatoInvalidoException {
        if (v == null) {
            throw new DatoInvalidoException("El vehículo a registrar no puede ser nulo.");
        }
        if (v.getPatente() == null || v.getPatente().trim().length() < 6) {
            throw new DatoInvalidoException("La patente '" + v.getPatente() + "' es inválida (debe contener al menos 6 caracteres alfanuméricos).");
        }
        if (v.getMarca() == null || v.getMarca().trim().isEmpty()) {
            throw new DatoInvalidoException("La marca del vehículo no puede estar vacía.");
        }
        if (v.getModelo() == null || v.getModelo().trim().isEmpty()) {
            throw new DatoInvalidoException("El modelo del vehículo no puede estar vacío.");
        }
        if (v.getAnio() == null || v.getAnio() < 1980 || v.getAnio() > 2027) {
            throw new DatoInvalidoException("El año de fabricación (" + v.getAnio() + ") está fuera del rango permitido (1980 - 2027).");
        }
        if (v.getPrecioBase() <= 0) {
            throw new DatoInvalidoException("El precio base de lista ($" + v.getPrecioBase() + ") debe ser estrictamente positivo.");
        }
        if (v.getCondicion() == null) {
            throw new DatoInvalidoException("Debe especificar la condición del vehículo (NUEVO o USADO).");
        }
        if (v.getCondicion() == CondicionVehiculo.NUEVO && v.getKilometraje() != null && v.getKilometraje() > 0) {
            throw new DatoInvalidoException("Una unidad Nueva (0 Km) no puede tener kilometraje registrado (" + v.getKilometraje() + " km).");
        }
        if (v.getCondicion() == CondicionVehiculo.USADO && (v.getKilometraje() == null || v.getKilometraje() <= 0)) {
            throw new DatoInvalidoException("Una unidad Usada debe tener un kilometraje mayor a 0.");
        }
    }

    /**
     * Registra un nuevo vehículo en el sistema validando unicidad de patente y reglas de dominio.
     * 
     * @param vehiculo Vehículo a incorporar.
     * @throws ConcesionariaException Si la patente ya existe o los datos son inválidos.
     */
    public void registrarVehiculo(Vehiculo vehiculo) throws ConcesionariaException {
        validarDatosVehiculo(vehiculo);

        String clavePatente = vehiculo.getPatente().toUpperCase();
        if (this.indiceStock.containsKey(clavePatente)) {
            throw new VehiculoDuplicadoException(clavePatente);
        }

        // Agregar en ambas estructuras de colección
        this.listaStock.add(vehiculo);
        this.indiceStock.put(clavePatente, vehiculo);

        // Persistir en archivo
        this.repositorio.guardar(vehiculo);
    }

    /**
     * Busca un vehículo por su patente utilizando el índice hash O(1).
     * 
     * @param patente Patente alfanumérica a buscar.
     * @return El vehículo encontrado.
     * @throws VehiculoNoEncontradoException Si no existe ninguna unidad con esa patente.
     */
    public Vehiculo buscarPorPatente(String patente) throws VehiculoNoEncontradoException {
        if (patente == null || patente.trim().isEmpty()) {
            throw new VehiculoNoEncontradoException("VACÍA");
        }
        Vehiculo encontrado = this.indiceStock.get(patente.trim().toUpperCase());
        if (encontrado == null) {
            throw new VehiculoNoEncontradoException(patente.trim().toUpperCase());
        }
        return encontrado;
    }

    /**
     * Elimina un vehículo del inventario tanto en memoria como en el almacenamiento físico.
     * 
     * @param patente Patente del vehículo a dar de baja.
     * @throws ConcesionariaException Si el vehículo no existe o falla la persistencia.
     */
    public void eliminarVehiculo(String patente) throws ConcesionariaException {
        Vehiculo v = buscarPorPatente(patente);
        this.indiceStock.remove(v.getPatente().toUpperCase());
        this.listaStock.remove(v);
        this.repositorio.eliminar(v.getPatente());
    }

    /**
     * Retorna una copia de la lista completa del stock.
     */
    public List<Vehiculo> obtenerTodos() {
        return new ArrayList<Vehiculo>(this.listaStock);
    }

    /**
     * Retorna la cantidad total de unidades en existencia.
     */
    public int getCantidadTotal() {
        return this.listaStock.size();
    }

    // =========================================================================
    // FILTRADOS DE COLECCIONES (EJE 10)
    // =========================================================================

    /* =========================================================================
     * >>> ZONA DE MODIFICACIÓN EN VIVO: MODIFICAR O AGREGAR FILTRADOS <<<
     * Si el docente te solicita: "Modifique o agregue un criterio de filtrado
     * (por ejemplo: filtrar por Color, o filtrar autos con menos de X km,
     * o autos nuevos con precio menor a Y)", puedes adaptarlo o crearlo aquí:
     * ========================================================================= */

    /**
     * Filtra el stock por tipo de vehículo (AUTO, CAMIONETA, MOTO).
     */
    public List<Vehiculo> filtrarPorTipo(TipoVehiculo tipo) {
        List<Vehiculo> filtrados = new ArrayList<Vehiculo>();
        if (tipo == null) return filtrados;

        for (Vehiculo v : this.listaStock) {
            if (v.getTipo() == tipo) {
                filtrados.add(v);
            }
        }
        return filtrados;
    }

    /**
     * Filtra el stock por condición comercial (NUEVO o USADO).
     */
    public List<Vehiculo> filtrarPorCondicion(CondicionVehiculo condicion) {
        List<Vehiculo> filtrados = new ArrayList<Vehiculo>();
        if (condicion == null) return filtrados;

        for (Vehiculo v : this.listaStock) {
            if (v.getCondicion() == condicion) {
                filtrados.add(v);
            }
        }
        return filtrados;
    }

    /**
     * Filtra vehículos cuyo precio final de venta se encuentre dentro de un rango cerrado.
     */
    public List<Vehiculo> filtrarPorRangoPrecio(double precioMinimo, double precioMaximo) {
        List<Vehiculo> filtrados = new ArrayList<Vehiculo>();
        for (Vehiculo v : this.listaStock) {
            double finalVenta = v.calcularPrecioFinal();
            if (finalVenta >= precioMinimo && finalVenta <= precioMaximo) {
                filtrados.add(v);
            }
        }
        return filtrados;
    }

    /**
     * Filtra vehículos por coincidencia de texto en la Marca (no sensitivo a mayúsculas).
     */
    public List<Vehiculo> filtrarPorMarca(String textoMarca) {
        List<Vehiculo> filtrados = new ArrayList<Vehiculo>();
        if (textoMarca == null || textoMarca.trim().isEmpty()) return filtrados;

        String busqueda = textoMarca.trim().toLowerCase();
        for (Vehiculo v : this.listaStock) {
            if (v.getMarca().toLowerCase().contains(busqueda)) {
                filtrados.add(v);
            }
        }
        return filtrados;
    }

    // =========================================================================
    // ORDENAMIENTOS DE COLECCIONES (EJE 10)
    // =========================================================================

    /**
     * Ordena la lista de vehículos según su orden natural implementado en Comparable (Precio Base).
     */
    public void ordenarPorPrecioNatural() {
        Collections.sort(this.listaStock);
    }

    /**
     * Ordena la lista de vehículos aplicando un Comparator específico.
     * 
     * @param comparador Instancia que implementa {@link Comparator}.
     */
    public void ordenarPorCriterio(Comparator<Vehiculo> comparador) {
        if (comparador != null) {
            Collections.sort(this.listaStock, comparador);
        }
    }

    // =========================================================================
    // OPERACIONES COMERCIALES Y ESTADÍSTICAS
    // =========================================================================

    /**
     * Aplica un descuento promocional a un vehículo específico identificado por su patente.
     */
    public void aplicarDescuentoAVehiculo(String patente, double porcentaje) throws ConcesionariaException {
        if (porcentaje <= 0 || porcentaje > 50) {
            throw new DatoInvalidoException("El porcentaje de descuento debe estar entre 1% y 50%.");
        }
        Vehiculo v = buscarPorPatente(patente);
        v.aplicarDescuento(porcentaje);
        this.repositorio.guardar(v);
    }

    /**
     * Calcula la suma total en pesos del valor de venta de todo el stock disponible en la concesionaria.
     */
    public double calcularValorTotalStock() {
        double total = 0.0;
        for (Vehiculo v : this.listaStock) {
            total += v.calcularPrecioFinal();
        }
        return total;
    }

    /**
     * Guarda el estado actual completo de la lista en el soporte persistente.
     */
    public void guardarCambiosEnDisco() throws PersistenciaException {
        this.repositorio.guardarTodos(this.listaStock);
    }
}
