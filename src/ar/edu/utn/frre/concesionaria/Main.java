package ar.edu.utn.frre.concesionaria;

import ar.edu.utn.frre.concesionaria.excepciones.PersistenciaException;
import ar.edu.utn.frre.concesionaria.modelo.Vehiculo;
import ar.edu.utn.frre.concesionaria.persistencia.IRepositorio;
import ar.edu.utn.frre.concesionaria.persistencia.RepositorioVehiculosArchivo;
import ar.edu.utn.frre.concesionaria.presentacion.MenuConsola;
import ar.edu.utn.frre.concesionaria.servicio.ConcesionariaServicio;

/**
 * Punto de entrada principal (Bootstrap) del Sistema de Concesionaria Multimarcas.
 * <p>
 * Se encarga de instanciar las dependencias del sistema bajo la arquitectura en capas:
 * 1. Instancia el Repositorio de Persistencia (Capa de Datos).
 * 2. Inyecta el Repositorio en el Servicio (Capa de Negocio).
 * 3. Inyecta el Servicio en el Menú de Consola (Capa de Presentación / Diseño).
 * 4. Inicia la ejecución interactiva.
 * </p>
 * 
 * Cumple con el Eje Técnico N° 2: "Arquitectura en Capas: Diseño vs. Negocio".
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando Sistema de Gestión de Concesionaria Multimarcas...");

        try {
            // 1. Capa de Persistencia: Creación del repositorio genérico
            IRepositorio<Vehiculo> repositorio = new RepositorioVehiculosArchivo();

            // 2. Capa de Negocio: Inyección de persistencia en el servicio
            ConcesionariaServicio servicio = new ConcesionariaServicio(repositorio);

            // 3. Capa de Presentación: Menú de consola interactivo
            MenuConsola menu = new MenuConsola(servicio);

            // 4. Iniciar ejecución
            menu.iniciar();

        } catch (PersistenciaException e) {
            System.err.println("\n[ERROR FATAL DE INICIALIZACIÓN] No se pudo cargar el repositorio de datos.");
            System.err.println("Detalle: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("\n[ERROR FATAL INESPERADO] " + e.getMessage());
            e.printStackTrace();
        }
    }
}
