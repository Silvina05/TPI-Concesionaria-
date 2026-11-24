# Sistema de Gestión de Concesionaria Multimarcas (Java)
## UTN FRRe — Tecnicatura Universitaria en Programación
### Cátedra: Programación II — Comisiones 2 y 3 (Año 2026)

---

### 🚀 Instrucciones Rápidas de Compilación y Ejecución

El proyecto está diseñado en **Java SE estándar puro** (sin dependencias externas ni frameworks), listo para compilarse y ejecutarse desde la terminal o cualquier IDE (Eclipse, IntelliJ, NetBeans, VS Code):

#### Opción 1: Desde la consola / terminal (Scripts incluidos)
1. Abrir la terminal en la carpeta raíz del proyecto:
   ```cmd
   cd C:\Users\Usuario\.gemini\antigravity\scratch\concesionaria-multimarcas-java
   ```
2. Compilar el código:
   ```cmd
   build.bat
   ```
3. Ejecutar la aplicación:
   ```cmd
   run.bat
   ```

#### Opción 2: Compilación manual con comandos estándar de Java
```cmd
javac -encoding UTF-8 -d bin src\ar\edu\utn\frre\concesionaria\*.java src\ar\edu\utn\frre\concesionaria\modelo\*.java src\ar\edu\utn\frre\concesionaria\servicio\*.java src\ar\edu\utn\frre\concesionaria\persistencia\*.java src\ar\edu\utn\frre\concesionaria\excepciones\*.java src\ar\edu\utn\frre\concesionaria\presentacion\*.java
java -cp bin ar.edu.utn.frre.concesionaria.Main
```

---

### 📁 Estructura del Proyecto

```text
concesionaria-multimarcas-java/
├── build.bat                            # Script para compilar todas las fuentes
├── run.bat                              # Script para ejecutar el sistema
├── README.md                            # Documentación general y guía rápida
├── bin/                                 # Bytecodes compilados (.class)
├── data/
│   └── stock_vehiculos.dat              # Archivo de persistencia de objetos (Serialización)
├── docs/
│   ├── INFORME_IAG_DEFENSA.md           # Informe formal exigido por la cátedra para el coloquio
│   └── index.html                       # Copia de la página web interactiva
├── web/
│   └── index.html                       # Página web HTML5 con explorador de código y decisiones
└── src/
    └── ar/edu/utn/frre/concesionaria/
        ├── Main.java                    # Bootstrap y punto de entrada
        ├── modelo/                      # POJOs / JavaBeans, Enums e Interfaces
        │   ├── Vehiculo.java            # Clase abstracta base (POJO, Comparable, Serializable)
        │   ├── Auto.java                # Subclase especializada
        │   ├── Camioneta.java           # Subclase especializada
        │   ├── Moto.java                # Subclase especializada
        │   ├── Vendible.java            # Interfaz de comportamiento comercial
        │   ├── TipoVehiculo.java        # Enum (AUTO, CAMIONETA, MOTO)
        │   ├── CondicionVehiculo.java   # Enum (NUEVO, USADO)
        │   ├── TipoCarroceria.java      # Enum (SEDAN, HATCHBACK, SUV, PICKUP, COUPE)
        │   └── EstiloMoto.java          # Enum (DEPORTIVA, CUSTOM, SCOOTER, ENDURO, NAKED)
        ├── persistencia/                # Capa de Acceso a Datos (I/O)
        │   ├── IRepositorio.java        # Interfaz genérica IRepositorio<T>
        │   └── RepositorioVehiculosArchivo.java # Persistencia con Serialización de Objetos (java 7.pdf)
        ├── servicio/                    # Capa de Lógica de Negocio
        │   ├── ConcesionariaServicio.java # Gestión de stock (ArrayList + HashMap O(1))
        │   └── ComparadoresVehiculo.java # Clases Comparator para ordenamiento
        ├── excepciones/                 # Jerarquía de Excepciones Propias Comprobadas
        │   ├── ConcesionariaException.java # Excepción base checked
        │   ├── VehiculoNoEncontradoException.java
        │   ├── VehiculoDuplicadoException.java
        │   ├── DatoInvalidoException.java
        │   └── PersistenciaException.java
        └── presentacion/                # Capa de Presentación / Diseño
            └── MenuConsola.java         # Menú interactivo con Data-Entry seguro (Wrappers)
```

---

### 🌐 Página Web Interactiva
Para ver el código completo, las decisiones de arquitectura, el diagrama UML y la guía para defender en el coloquio, abre en cualquier navegador:
- `web/index.html` (o `docs/index.html`)
