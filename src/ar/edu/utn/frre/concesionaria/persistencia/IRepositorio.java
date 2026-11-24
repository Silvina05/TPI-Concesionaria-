package ar.edu.utn.frre.concesionaria.persistencia;

import ar.edu.utn.frre.concesionaria.excepciones.PersistenciaException;
import java.util.List;

/**
 * Interfaz Genérica: IRepositorio<T>
 * <p>
 * Define las operaciones abstractas de almacenamiento permanente para cualquier
 * entidad del sistema mediante tipado parametrizado genérico.
 * </p>
 * 
 * Cumple con el Eje Técnico N° 12: "Uso de Genéricos (Generics)".
 * 
 * @param <T> Tipo de entidad administrada por el repositorio.
 */
public interface IRepositorio<T> {

    /**
     * Guarda una entidad individual en el soporte de persistencia.
     * 
     * @param entidad Objeto a almacenar.
     * @throws PersistenciaException Si ocurre una falla en el medio de almacenamiento.
     */
    void guardar(T entidad) throws PersistenciaException;

    /**
     * Elimina una entidad identificada por su clave unívoca.
     * 
     * @param id Clave identificatoria de la entidad.
     * @throws PersistenciaException Si ocurre una falla en el medio de almacenamiento.
     */
    void eliminar(String id) throws PersistenciaException;

    /**
     * Sobrescribe y guarda la colección completa de entidades.
     * 
     * @param lista Colección de entidades a persistir.
     * @throws PersistenciaException Si ocurre un error de I/O.
     */
    void guardarTodos(List<T> lista) throws PersistenciaException;

    /**
     * Recupera y carga todas las entidades almacenadas en el medio de persistencia.
     * 
     * @return Lista con los elementos cargados.
     * @throws PersistenciaException Si el archivo no existe o no se puede interpretar.
     */
    List<T> cargarTodos() throws PersistenciaException;
}
