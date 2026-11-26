package ar.edu.utn.frre.concesionaria.persistencia;

import ar.edu.utn.frre.concesionaria.excepciones.PersistenciaException;
import ar.edu.utn.frre.concesionaria.modelo.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de Persistencia mediante Serialización de Objetos.
 * <p>
 * Se basa estrictamente en el material oficial de la cátedra (Unidad de Entrada/Salida:
 * diapositivas "java 7.pdf", páginas 14 a 19):
 * - {@link File}: Para verificar existencia del archivo.
 * - {@link FileOutputStream} y {@link ObjectOutputStream}: Para escribir el estado de los objetos (writeObject).
 * - {@link FileInputStream} y {@link ObjectInputStream}: Para recuperar los objetos del archivo (readObject).
 * - Manejo en bloques {@code try - catch - finally} con cierre explícito de flujos.
 * </p>
 * <p>
 * Ventaja para el Coloquio: Al usar serialización nativa de Java, cualquier nuevo atributo
 * agregado en una instancia de Live Coding se persiste automáticamente sin necesidad de
 * modificar algoritmos complejos de parseo de cadenas.
 * </p>
 * 
 * Cumple con el Eje Técnico N° 11: "Persistencia de Datos (Archivos / BD SQL)".
 */
public class RepositorioVehiculosArchivo implements IRepositorio<Vehiculo> {

    /* =========================================================================
     * >>> ZONA DE MODIFICACIÓN EN VIVO (LIVE CODING - COLOQUIO) <<<
     * Si el docente te solicita: "Cambie el nombre o ruta del archivo de persistencia",
     * sólo debes modificar esta constante String:
     * ========================================================================= */
    public static final String RUTA_ARCHIVO_DEFECTO = "data/stock_vehiculos.dat";

    private final String rutaArchivo;

    /**
     * Constructor por defecto: utiliza la ruta estándar "data/stock_vehiculos.dat".
     */
    public RepositorioVehiculosArchivo() {
        this(RUTA_ARCHIVO_DEFECTO);
    }

    /**
     * Constructor con ruta personalizada.
     * 
     * @param rutaArchivo Ruta del archivo de datos.
     */
    public RepositorioVehiculosArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        verificarOInicializarArchivo();
    }

    /**
     * Verifica si el archivo existe; si no existe, crea los datos iniciales de prueba.
     */
    private void verificarOInicializarArchivo() {
        File archivo = new File(this.rutaArchivo);
        if (!archivo.exists()) {
            File carpeta = archivo.getParentFile();
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }
            // Sembrar datos de ejemplo la primera vez que corre el sistema
            sembrarDatosIniciales();
        }
    }

    /**
     * Inicializa el archivo con un stock demostrativo variado.
     */
    private void sembrarDatosIniciales() {
        List<Vehiculo> semilla = new ArrayList<Vehiculo>();
        semilla.add(new Auto("AF123AA", "Toyota", "Corolla", 2024, "Blanco Perlado", 28000000.0, CondicionVehiculo.NUEVO, 0, 4, TipoCarroceria.SEDAN));
        semilla.add(new Auto("AB456CD", "Volkswagen", "Golf TSI", 2020, "Gris Plata", 19500000.0, CondicionVehiculo.USADO, 48000, 5, TipoCarroceria.HATCHBACK));
        semilla.add(new Camioneta("AG789BB", "Toyota", "Hilux 4x4 SRV", 2025, "Rojo", 45000000.0, CondicionVehiculo.NUEVO, 0, 1000.0, true));
        semilla.add(new Camioneta("AD321CC", "Ford", "Ranger XLT", 2021, "Azul", 32000000.0, CondicionVehiculo.USADO, 72000, 1100.0, false));
        semilla.add(new Moto("A012DEF", "Honda", "CB 300F Twister", 2024, "Negro", 6200000.0, CondicionVehiculo.NUEVO, 0, 293, EstiloMoto.NAKED));
        semilla.add(new Moto("A034GHI", "Yamaha", "MT-07", 2022, "Gris Mate", 14800000.0, CondicionVehiculo.USADO, 15000, 689, EstiloMoto.DEPORTIVA));

        try {
            guardarTodos(semilla);
        } catch (PersistenciaException e) {
            System.err.println("Aviso al sembrar datos iniciales: " + e.getMessage());
        }
    }

    // =========================================================================
    // IMPLEMENTACIÓN DE IRepositorio<Vehiculo>
    // Siguiendo el código de ejemplo de java 7.pdf (página 19)
    // =========================================================================

    @Override
    public void guardar(Vehiculo vehiculo) throws PersistenciaException {
        if (vehiculo == null) {
            throw new PersistenciaException("No se puede guardar un vehículo nulo.");
        }
        List<Vehiculo> lista = cargarTodos();
        int indice = lista.indexOf(vehiculo);
        if (indice >= 0) {
            lista.set(indice, vehiculo);
        } else {
            lista.add(vehiculo);
        }
        guardarTodos(lista);
    }

    @Override
    public void eliminar(String patente) throws PersistenciaException {
        if (patente == null) {
            return;
        }
        List<Vehiculo> lista = cargarTodos();
        boolean encontrado = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getPatente().equalsIgnoreCase(patente.trim())) {
                lista.remove(i);
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            guardarTodos(lista);
        }
    }

    @Override
    public void guardarTodos(List<Vehiculo> lista) throws PersistenciaException {
        ObjectOutputStream out = null;
        try {
            File f = new File(this.rutaArchivo);
            out = new ObjectOutputStream(new FileOutputStream(f));
            // Escribe la lista completa serializada (java 7, página 19)
            out.writeObject(lista);
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar en el archivo de persistencia: " + e.getMessage(), e);
        } finally {
            // Cierre seguro del flujo en finally
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar ObjectOutputStream: " + e.getMessage());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vehiculo> cargarTodos() throws PersistenciaException {
        List<Vehiculo> lista = new ArrayList<Vehiculo>();
        File f = new File(this.rutaArchivo);

        if (!f.exists() || f.length() == 0) {
            return lista;
        }

        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(f));
            // Recupera la lista mediante readObject (java 7, página 19)
            lista = (List<Vehiculo>) in.readObject();// en Java no podés asignar directamente un Object a una variable List<Vehiculo> sin hacer un casting explícito
        } catch (IOException e) {
            throw new PersistenciaException("Error de lectura en archivo de persistencia: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new PersistenciaException("Clase no encontrada al deserializar: " + e.getMessage(), e);
        } finally {
            // Cierre seguro del flujo en finally
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar ObjectInputStream: " + e.getMessage());
                }
            }
        }

        return lista;
    }
}
