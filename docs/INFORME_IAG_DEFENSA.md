# UNIVERSIDAD TECNOLÓGICA NACIONAL
## Facultad Regional Resistencia — Tecnicatura Universitaria en Programación
### Cátedra: Programación II — Año 2026 — Comisiones 2 y 3

---

# INFORME TÉCNICO OBLIGATORIO: USO DE INTELIGENCIA ARTIFICIAL GENERATIVA (IAG) Y GUÍA DE DEFENSA EN COLOQUIO
**Trabajo Final Integrador — Concesionaria Multimarcas en Java**

- **Alumno/a:** ________________________________________
- **Legajo N°:** ____________________
- **Comisión:** [ ] Comisión 2 &nbsp;&nbsp;&nbsp;&nbsp; [ ] Comisión 3
- **IAG Seleccionada:** Google Gemini (Gemini 2.5 Pro / Flash)

---

## 1. Declaración y Fundamentación de la IAG Seleccionada

### a) Utilización Mandatoria de una IAG Gratuita tipo LLM
Para el desarrollo asistido del presente Trabajo Final Integrador se utilizó la plataforma de Inteligencia Artificial Generativa **Google Gemini**, en sus versiones de libre acceso universitario.

### b) Justificación Técnica de la Elección
Se seleccionó Google Gemini frente a otras alternativas disponibles en el mercado debido a los siguientes factores técnicos determinantes:
1. **Adherencia a estándares estrictos de Java SE:** Capacidad para ceñirse a las versiones y conceptos de referencia del programa de la cátedra sin forzar librerías externas o frameworks no vistos en clase.
2. **Capacidad de modularización arquitectónica:** Precisión al estructurar la aplicación bajo la regla de separación de responsabilidades (*Diseño vs. Negocio*), evitando mezclar operaciones de entrada/salida de consola con el modelo de dominio.
3. **Manejo fidedigno de la API estándar de Colecciones y Serialización:** Correcta aplicación de las interfaces `Comparable` y `Comparator`, la sincronización entre `List` y `Map`, y el mecanismo de serialización nativa de objetos enseñado en la unidad de Entrada/Salida.

### c) Ámbito de Aplicación de la IAG en el Proyecto
La herramienta fue empleada en:
- **Diseño del esquema de clases y jerarquías POO:** Modelado de la clase abstracta `Vehiculo` y sus derivadas especializadas (`Auto`, `Camioneta`, `Moto`).
- **Implementación de persistencia con Serialización (java.io):** Asistencia en la construcción de los flujos `ObjectOutputStream` y `ObjectInputStream` con bloques `try-catch-finally` y cierre seguro de recursos.
- **Estructuración de Colecciones y Genéricos:** Diseño de la interfaz genérica `IRepositorio<T>` y combinación de `ArrayList<Vehiculo>` con `HashMap<String, Vehiculo>` para búsquedas en tiempo constante $O(1)$ por patente.
- **Manejo de Excepciones del Dominio:** Creación de la jerarquía de excepciones comprobadas (`ConcesionariaException` y derivadas).
- **Documentación técnica y Live Coding:** Etiquetado de zonas críticas para modificación en vivo durante el coloquio individual.

---

## 2. Documentación de Inexactitudes y Reflexión Crítica

### d) Registro de Inconsistencias y Errores Detectados (Casos Reales Auditados)

Durante el proceso de auditoría y revisión del código generado por la IAG, el alumno detectó y corrigió las siguientes inconsistencias reales frente al material didáctico de la cátedra:

#### Caso 1: Incompatibilidad en la Persistencia y Uso de Elementos Fuera del Programa (Auditado por el Alumno)
- **Inconsistencia generada por la IAG:** Inicialmente, la IAG propuso una persistencia basada en cadenas de texto plano utilizando métodos como `linea.split(";")`, configuraciones de formato con `java.util.Locale.US`, `String.format()` y `Boolean.parseBoolean()`.
- **Diagnóstico y Auditoría del Alumno:** Al cotejar el código propuesto contra las diapositivas oficiales de la materia, el alumno observó que ni `java.util.Locale` ni el método `split()` forman parte del material brindado por la cátedra. Además, ese enfoque con múltiples índices numéricos de tokens resultaba frágil y muy difícil de defender y modificar en un examen oral de Live Coding.
- **Corrección aplicada por el Alumno:** Se instruyó a la IAG a descartar ese enfoque y basar la persistencia **estrictamente en el mecanismo de Serialización de Objetos de Java**, explicado en las diapositivas **"java 7.pdf" (páginas 14 a 19)** mediante las clases `File`, `FileInputStream`, `FileOutputStream`, `ObjectInputStream` y `ObjectOutputStream` con `writeObject()` y `readObject()`. Este enfoque redujo el código a un tercio, utiliza sólo herramientas del programa y permite que cualquier nuevo atributo agregado en vivo se guarde automáticamente.

#### Caso 2: Error Sintáctico en Validación de Tipos Primitivos vs. Wrappers
- **Inconsistencia generada por la IAG:** En la capa de servicio (`ConcesionariaServicio.java`), al validar el precio base, la IAG generó la siguiente condición:
  ```java
  if (v.getPrecioBase() == null || v.getPrecioBase() <= 0) { ... }
  ```
- **Diagnóstico del Alumno:** El método `getPrecioBase()` retorna el tipo primitivo `double` (definido en la interfaz `Vendible`). En Java, una variable primitiva se aloja en el *Stack* y contiene un valor directo; **nunca puede ser `null`**. Esto provocó el error de compilación: `bad operand types for binary operator '==' (first type: double, second type: <null>)`.
- **Corrección aplicada:** El alumno ajustó la verificación a `if (v.getPrecioBase() <= 0)`, reservando la comparación con `null` exclusivamente para referencias a objetos.

#### Caso 3: Sesgo Conceptual en la Sobreescritura de `equals()` y `hashCode()`
- **Inconsistencia generada por la IAG:** La IAG generó una comparación que incluía marca, modelo, color y precio para determinar si dos vehículos eran iguales.
- **Diagnóstico del Alumno:** En el negocio de una concesionaria automotriz, dos autos son la misma unidad física si y sólo si comparten la misma **Patente / Dominio**. Incluir atributos mutables (como el color o el precio) rompería el contrato de colecciones si el valor del vehículo se actualiza.
- **Corrección aplicada:** Se sobrescribió `equals()` y `hashCode()` comparando unívocamente la `patente.toUpperCase()`, respetando el contrato de la clase `Object` visto en *java 5*.

### e) Valoración Crítica del Alumno: Pros y Contras de la IAG

#### Pros (Ventajas):
- **Aceleración en tareas repetitivas:** Alta velocidad para generar esqueletos de clases POJO, constructores sobrecargados, métodos getters/setters y opciones de menú interactivo.
- **Refuerzo en la comprensión de contratos:** Ayuda a visualizar cómo interactúan las interfaces `Comparable` y `Comparator` en el ordenamiento de listas.

#### Contras (Riesgos y Limitaciones):
- **Riesgo de introducir APIs no dictadas en clase:** La IAG suele recurrir a métodos o clases utilitarias (`Locale`, `split`, Streams, Lambdas) que no corresponden al nivel del curso.
- **Obligación de dominio técnico:** La IAG no reemplaza el conocimiento del alumno. Si el estudiante no entiende qué hace cada línea o cómo funciona el ciclo de vida de los flujos de I/O, no podrá superar las modificaciones en vivo solicitadas por el tribunal docente.

---

## 3. Guía Maestra para Live Coding en el Coloquio Individual

Ante la exigencia ineludible de la cátedra de **demostrar capacidad de modificación en vivo**, se detallan los 4 casos típicos de examen:

### Escenario 1: "Incorpore un nuevo atributo al modelo" (ej: `numeroChasis` o `tieneAireAcondicionado`)
1. Abrir `Vehiculo.java` (si es general) o `Auto.java` (si es específico del auto).
2. Localizar la sección comentada `>>> ZONA DE MODIFICACIÓN EN VIVO <<<`.
3. Declarar el campo privado: `private String numeroChasis;`
4. Añadirlo al constructor con parámetros y asignar: `this.numeroChasis = numeroChasis;`
5. Generar su Getter y Setter público (`getNumeroChasis()`, `setNumeroChasis()`).
6. En `mostrarFichaTecnica()` añadir la línea de impresión.
7. **Nota clave para el coloquio:** Gracias a que la persistencia utiliza **Serialización de Objetos**, ¡no hace falta modificar ninguna línea de archivos para que el nuevo atributo se guarde y recupere en disco!

### Escenario 2: "Modifique o agregue un criterio de filtrado de colecciones"
1. Abrir `ConcesionariaServicio.java`.
2. Ubicar la sección `FILTRADOS DE COLECCIONES`.
3. Crear el método recorriendo la lista con un bucle `for` tradicional (java 5):
   ```java
   public List<Vehiculo> filtrarPorColor(String colorBuscado) {
       List<Vehiculo> filtrados = new ArrayList<Vehiculo>();
       for (Vehiculo v : this.listaStock) {
           if (v.getColor().equalsIgnoreCase(colorBuscado.trim())) {
               filtrados.add(v);
           }
       }
       return filtrados;
   }
   ```
4. Llamarlo desde `MenuConsola.java` en la opción de filtrados.

### Escenario 3: "Agregue una validación personalizada de datos de entrada"
1. Abrir `ConcesionariaServicio.java`.
2. Ubicar el método `validarDatosVehiculo(Vehiculo v)`.
3. Agregar la regla lanzando la excepción propia `DatoInvalidoException` (java 3):
   ```java
   // Ejemplo: No permitir unidades anteriores al año 2005
   if (v.getAnio() < 2005) {
       throw new DatoInvalidoException("La concesionaria no admite unidades anteriores al año 2005.");
   }
   ```

### Escenario 4: "Altere la persistencia o cambie la ruta del archivo"
1. Abrir `RepositorioVehiculosArchivo.java`.
2. Para cambiar el nombre del archivo de persistencia, modificar la constante pública:
   ```java
   public static final String RUTA_ARCHIVO_DEFECTO = "data/mi_nuevo_stock.dat";
   ```
3. Explicar al profesor cómo `ObjectOutputStream.writeObject(lista)` serializa el grafo de objetos y `ObjectInputStream.readObject()` lo reconstruye en memoria, cerrando ambos flujos en el bloque `finally` (java 7, pág. 19).

---

## 4. Matriz de Cumplimiento de los 14 Ejes Técnicos del TFI

| N° | Eje Técnico | Implementación Concreta | Diapositiva de Referencia |
|:---|:---|:---|:---|
| **1** | **Dominio & Abstracción** | Paquete `modelo` (`Vehiculo`, `Auto`, `Camioneta`, `Moto`) | Intro Conceptos POO |
| **2** | **Arquitectura en Capas** | Paquetes `presentacion`, `servicio`, `persistencia`, `modelo` | java 3 (Paquetes y Cohesión) |
| **3** | **Modelado POJO / JavaBean** | Atributos privados, constructor vacío, getters/setters, `Serializable` | java 3 (página 25) |
| **4** | **Herencia y Polimorfismo** | Clase abstracta `Vehiculo` y sobreescritura en `Auto`, `Camioneta`, `Moto` | java 3 (páginas 6, 16, 22) |
| **5** | **Interfaces de Comportamiento** | `Vendible` (`calcularPrecioFinal()`, `aplicarDescuento()`) | java 3 (páginas 39-42) |
| **6** | **Manejo de Excepciones** | Jerarquía `ConcesionariaException` y derivadas comprobadas | java 3 (páginas 43-48) |
| **7** | **Tipos Enumerativos (Enums)** | `TipoVehiculo`, `CondicionVehiculo`, `TipoCarroceria`, `EstiloMoto` | java 3 (páginas 31-32) |
| **8** | **Interfaz de Consola y Data-Entry**| Menú interactivo continuo en `MenuConsola` con lectura segura | java 2 (while, switch, try-catch) |
| **9** | **Wrappers y Conversiones** | Parseo seguro con `Integer.parseInt()` y `Double.parseDouble()` | java 4 (página 6) |
| **10** | **Colecciones & Sobreescritura**| `ArrayList` + `HashMap`, `equals()`, `hashCode()`, `toString()` | java 5 (páginas 6, 13, 29, 44) |
| **11** | **Persistencia de Datos (Archivos)**| `ObjectOutputStream` y `ObjectInputStream` con `File` | java 7 (páginas 14-19) |
| **12** | **Uso de Genéricos (Generics)**| `IRepositorio<T>`, tipado estricto en colecciones | java 5 (páginas 46-54) |
| **13** | **Diagrama de Clases UML** | Diagrama de arquitectura POO en HTML y Markdown | java 3 y TFI Oficial |
| **14** | **Documentación del Código** | JavaDoc completo en métodos, clases y parámetros | java 2 (página 4) |
