package ar.edu.utn.frre.concesionaria.presentacion;

import ar.edu.utn.frre.concesionaria.excepciones.ConcesionariaException;
import ar.edu.utn.frre.concesionaria.modelo.*;
import ar.edu.utn.frre.concesionaria.servicio.ComparadoresVehiculo;
import ar.edu.utn.frre.concesionaria.servicio.ConcesionariaServicio;

import java.util.List;
import java.util.Scanner;

/**
 * Capa de Presentación: Menú Interactivo por Consola
 * <p>
 * Gestiona toda la interacción con el usuario mediante la entrada estándar (System.in)
 * y salida por consola (System.out).
 * Aplica rigurosamente:
 * - Separación de responsabilidades: delega toda la lógica en {@link ConcesionariaServicio}.
 * - Robustez ante entradas inválidas: sanitización y captura de {@link NumberFormatException}.
 * - Uso intensivo de clases Wrapper ({@link Integer}, {@link Double}) para parseo seguro.
 * </p>
 * 
 * Cumple con los Ejes Técnicos N° 2 ("Diseño vs. Negocio"), N° 8 ("Interfaz de Consola y Data-Entry")
 * y N° 9 ("Wrappers y Conversiones").
 */
public class MenuConsola {

    private final ConcesionariaServicio servicio;
    private final Scanner scanner;

    public MenuConsola(ConcesionariaServicio servicio) {
        this.servicio = servicio;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Ciclo principal de ejecución del menú interactivo.
     */
    public void iniciar() {
        int opcion = -1;
        do {
            mostrarCabeceraPrincipal();
            System.out.println(" 1.  Listar catálogo completo de vehículos");
            System.out.println(" 2.  Registrar nuevo vehículo (Auto, Camioneta, Moto)");
            System.out.println(" 3.  Buscar vehículo por patente (Ver Ficha Técnica)");
            System.out.println(" 4.  Filtrar vehículos (por Tipo, Condición, Precio o Marca)");
            System.out.println(" 5.  Ordenar stock (por Precio, Marca/Modelo, Año, Kilometraje)");
            System.out.println(" 6.  Aplicar descuento promocional (Interfaz Vendible)");
            System.out.println(" 7.  Estadísticas y balance financiero del inventario");
            System.out.println(" 8.  Eliminar vehículo del stock");
            System.out.println(" 9.  Forzar guardado en disco (Persistencia)");
            System.out.println(" 0.  Salir del sistema");
            System.out.println("=======================================================================");

            opcion = leerEnteroSeguro("Seleccione una opción: ", 0, 9);
            System.out.println();

            try {
                switch (opcion) {
                    case 1:
                        menuListarTodos();
                        break;
                    case 2:
                        menuRegistrarVehiculo();
                        break;
                    case 3:
                        menuBuscarPorPatente();
                        break;
                    case 4:
                        menuFiltrar();
                        break;
                    case 5:
                        menuOrdenar();
                        break;
                    case 6:
                        menuAplicarDescuento();
                        break;
                    case 7:
                        menuEstadisticas();
                        break;
                    case 8:
                        menuEliminarVehiculo();
                        break;
                    case 9:
                        menuGuardarPersistencia();
                        break;
                    case 0:
                        System.out.println("Guardando cambios en disco antes de salir...");
                        this.servicio.guardarCambiosEnDisco();
                        System.out.println("¡Gracias por utilizar el Sistema de Concesionaria Multimarcas UTN!");
                        break;
                    default:
                        System.out.println("Opción no contemplada.");
                        break;
                }
            } catch (ConcesionariaException e) {
                System.err.println("\n[ERROR DE NEGOCIO] " + e.getMessage());
            } catch (Exception e) {
                System.err.println("\n[ERROR INESPERADO] Ocurrió un problema: " + e.getMessage());
            }

            if (opcion != 0) {
                pausar();
            }

        } while (opcion != 0);
    }

    // =========================================================================
    // SUBMENÚS Y OPERACIONES DE PANTALLA
    // =========================================================================

    private void mostrarCabeceraPrincipal() {
        System.out.println("\n=======================================================================");
        System.out.println("   UTN FRRe - TUP | SISTEMA DE GESTIÓN DE CONCESIONARIA MULTIMARCAS    ");
        System.out.println("   Unidades en stock actual: " + servicio.getCantidadTotal());
        System.out.println("=======================================================================");
    }

    private void menuListarTodos() {
        List<Vehiculo> stock = servicio.obtenerTodos();
        mostrarTablaVehiculos(stock, "CATÁLOGO COMPLETO DE VEHÍCULOS");
    }

    private void menuBuscarPorPatente() throws ConcesionariaException {
        System.out.println("--- BÚSQUEDA DE VEHÍCULO POR PATENTE ---");
        String patente = leerCadenaNoVacia("Ingrese la patente / dominio a consultar: ");
        Vehiculo v = servicio.buscarPorPatente(patente);
        System.out.println("\nVehículo localizado con éxito:");
        System.out.println(v.mostrarFichaTecnica());
    }

    private void menuRegistrarVehiculo() throws ConcesionariaException {
        System.out.println("--- REGISTRO DE NUEVO VEHÍCULO ---");
        System.out.println("Seleccione la categoría del rodado:");
        System.out.println("1. Automóvil");
        System.out.println("2. Camioneta / Utilitario");
        System.out.println("3. Motocicleta");
        int tipoSel = leerEnteroSeguro("Opción (1-3): ", 1, 3);

        String patente = leerCadenaNoVacia("Patente (ej: AA123BB o OLM890): ").toUpperCase();
        String marca = leerCadenaNoVacia("Marca: ");
        String modelo = leerCadenaNoVacia("Modelo: ");
        int anio = leerEnteroSeguro("Año de fabricación (1980 - 2027): ", 1980, 2027);
        String color = leerCadenaNoVacia("Color: ");
        double precioBase = leerDoubleSeguro("Precio base de lista ($): ", 1.0);

        System.out.println("Condición de la unidad:");
        System.out.println("1. Nuevo (0 Km)");
        System.out.println("2. Usado");
        int condSel = leerEnteroSeguro("Opción (1-2): ", 1, 2);
        CondicionVehiculo condicion = (condSel == 1) ? CondicionVehiculo.NUEVO : CondicionVehiculo.USADO;

        int km = 0;
        if (condicion == CondicionVehiculo.USADO) {
            km = leerEnteroSeguro("Kilómetros recorridos: ", 1, 1000000);
        }

        Vehiculo nuevoVehiculo = null;

        switch (tipoSel) {
            case 1: { // AUTO
                int puertas = leerEnteroSeguro("Cantidad de puertas (2 a 5): ", 2, 5);
                System.out.println("Seleccione tipo de carrocería:");
                TipoCarroceria[] valores = TipoCarroceria.values();
                for (int i = 0; i < valores.length; i++) {
                    System.out.println((i + 1) + ". " + valores[i].getNombre());
                }
                int carrSel = leerEnteroSeguro("Opción: ", 1, valores.length);
                TipoCarroceria carroceria = valores[carrSel - 1];

                nuevoVehiculo = new Auto(patente, marca, modelo, anio, color, precioBase, condicion, km, puertas, carroceria);
                break;
            }
            case 2: { // CAMIONETA
                double carga = leerDoubleSeguro("Capacidad de carga útil en Kg: ", 100.0);
                System.out.println("¿Cuenta con tracción 4x4 integral?");
                System.out.println("1. Sí");
                System.out.println("2. No (Tracción simple 4x2)");
                int traccSel = leerEnteroSeguro("Opción: ", 1, 2);
                boolean es4x4 = (traccSel == 1);

                nuevoVehiculo = new Camioneta(patente, marca, modelo, anio, color, precioBase, condicion, km, carga, es4x4);
                break;
            }
            case 3: { // MOTO
                int cilindrada = leerEnteroSeguro("Cilindrada en centímetros cúbicos (cc): ", 50, 2500);
                System.out.println("Seleccione estilo de la motocicleta:");
                EstiloMoto[] estilos = EstiloMoto.values();
                for (int i = 0; i < estilos.length; i++) {
                    System.out.println((i + 1) + ". " + estilos[i].getEstilo());
                }
                int estSel = leerEnteroSeguro("Opción: ", 1, estilos.length);
                EstiloMoto estilo = estilos[estSel - 1];

                nuevoVehiculo = new Moto(patente, marca, modelo, anio, color, precioBase, condicion, km, cilindrada, estilo);
                break;
            }
        }

        servicio.registrarVehiculo(nuevoVehiculo);
        System.out.println("\n✓ ¡Vehículo registrado y persistido exitosamente en el stock!");
        System.out.println(nuevoVehiculo.mostrarFichaTecnica());
    }

    private void menuFiltrar() {
        System.out.println("--- FILTRADO DEL STOCK ---");
        System.out.println("1. Filtrar por Tipo de Vehículo (Auto / Camioneta / Moto)");
        System.out.println("2. Filtrar por Condición (Nuevos 0 Km / Usados)");
        System.out.println("3. Filtrar por Rango de Precio Final de Venta");
        System.out.println("4. Filtrar por coincidencia en Marca");
        int opc = leerEnteroSeguro("Seleccione filtro (1-4): ", 1, 4);

        switch (opc) {
            case 1: {
                System.out.println("1. Autos | 2. Camionetas | 3. Motos");
                int t = leerEnteroSeguro("Opción: ", 1, 3);
                TipoVehiculo tipo = (t == 1) ? TipoVehiculo.AUTO : (t == 2) ? TipoVehiculo.CAMIONETA : TipoVehiculo.MOTO;
                List<Vehiculo> resultado = servicio.filtrarPorTipo(tipo);
                mostrarTablaVehiculos(resultado, "VEHÍCULOS DEL TIPO: " + tipo.getDescripcion());
                break;
            }
            case 2: {
                System.out.println("1. Nuevos (0 Km) | 2. Usados");
                int c = leerEnteroSeguro("Opción: ", 1, 2);
                CondicionVehiculo condicion = (c == 1) ? CondicionVehiculo.NUEVO : CondicionVehiculo.USADO;
                List<Vehiculo> resultado = servicio.filtrarPorCondicion(condicion);
                mostrarTablaVehiculos(resultado, "VEHÍCULOS EN CONDICIÓN: " + condicion.getEtiqueta());
                break;
            }
            case 3: {
                double min = leerDoubleSeguro("Ingrese precio final mínimo ($): ", 0.0);
                double max = leerDoubleSeguro("Ingrese precio final máximo ($): ", min);
                List<Vehiculo> resultado = servicio.filtrarPorRangoPrecio(min, max);
                mostrarTablaVehiculos(resultado, String.format("VEHÍCULOS ENTRE $%.2f Y $%.2f", min, max));
                break;
            }
            case 4: {
                String marca = leerCadenaNoVacia("Ingrese texto de la marca a buscar: ");
                List<Vehiculo> resultado = servicio.filtrarPorMarca(marca);
                mostrarTablaVehiculos(resultado, "VEHÍCULOS CON MARCA QUE CONTIENE: '" + marca + "'");
                break;
            }
        }
    }

    private void menuOrdenar() {
        System.out.println("--- ORDENAMIENTO DE STOCK (COLECCIONES) ---");
        System.out.println("1. Orden Natural por Precio Base Ascendente (Usa Comparable<Vehiculo>)");
        System.out.println("2. Ordenar por Marca y Modelo Alfabéticamente (Usa Comparator)");
        System.out.println("3. Ordenar por Año Descendente (Más nuevos primero)");
        System.out.println("4. Ordenar por Kilometraje Ascendente (Menos rodados primero)");
        int opc = leerEnteroSeguro("Seleccione criterio (1-4): ", 1, 4);

        switch (opc) {
            case 1:
                servicio.ordenarPorPrecioNatural();
                System.out.println("✓ Colección ordenada mediante Comparable por Precio Base.");
                break;
            case 2:
                servicio.ordenarPorCriterio(new ComparadoresVehiculo.PorMarcaYModelo());
                System.out.println("✓ Colección ordenada mediante Comparator por Marca y Modelo.");
                break;
            case 3:
                servicio.ordenarPorCriterio(new ComparadoresVehiculo.PorAnioDescendente());
                System.out.println("✓ Colección ordenada mediante Comparator por Año (Descendente).");
                break;
            case 4:
                servicio.ordenarPorCriterio(new ComparadoresVehiculo.PorKilometrajeAscendente());
                System.out.println("✓ Colección ordenada mediante Comparator por Kilometraje (Ascendente).");
                break;
        }

        mostrarTablaVehiculos(servicio.obtenerTodos(), "STOCK TRAS APLICAR ORDENAMIENTO");
    }

    private void menuAplicarDescuento() throws ConcesionariaException {
        System.out.println("--- APLICACIÓN DE DESCUENTO COMERCIAL (INTERFAZ VENDIBLE) ---");
        String patente = leerCadenaNoVacia("Ingrese la patente del vehículo a bonificar: ");
        Vehiculo v = servicio.buscarPorPatente(patente);

        System.out.println("Vehículo: " + v.getMarca() + " " + v.getModelo() +
                           " | Precio base actual: $" + v.getPrecioBase() +
                           " | Precio final actual: $" + v.calcularPrecioFinal());

        double pct = leerDoubleSeguro("Ingrese porcentaje de descuento (1 a 50%): ", 1.0);
        servicio.aplicarDescuentoAVehiculo(patente, pct);

        System.out.println("\n✓ ¡Descuento del " + pct + "% aplicado con éxito!");
        System.out.println("Nuevo precio base: $" + v.getPrecioBase() +
                           " | Nuevo precio de venta final: $" + v.calcularPrecioFinal());
    }

    private void menuEstadisticas() {
        List<Vehiculo> todos = servicio.obtenerTodos();
        int cantAutos = 0;
        int cantCamionetas = 0;
        int cantMotos = 0;
        int cantNuevos = 0;
        int cantUsados = 0;

        for (Vehiculo v : todos) {
            if (v.getTipo() == TipoVehiculo.AUTO) cantAutos++;
            else if (v.getTipo() == TipoVehiculo.CAMIONETA) cantCamionetas++;
            else if (v.getTipo() == TipoVehiculo.MOTO) cantMotos++;

            if (v.getCondicion() == CondicionVehiculo.NUEVO) cantNuevos++;
            else cantUsados++;
        }

        double valorTotal = servicio.calcularValorTotalStock();

        System.out.println("=======================================================================");
        System.out.println("                BALANCE Y ESTADÍSTICAS DEL INVENTARIO                  ");
        System.out.println("=======================================================================");
        System.out.println(" Total de unidades en stock:      " + todos.size() + " vehículos");
        System.out.println(" - Automóviles:                  " + cantAutos);
        System.out.println(" - Camionetas / Utilitarios:     " + cantCamionetas);
        System.out.println(" - Motocicletas:                 " + cantMotos);
        System.out.println("-----------------------------------------------------------------------");
        System.out.println(" - Unidades 0 Km (Nuevas):       " + cantNuevos);
        System.out.println(" - Unidades Usadas con rodaje:   " + cantUsados);
        System.out.println("-----------------------------------------------------------------------");
        System.out.println(" VALOR TOTAL ESTIMADO DEL STOCK:  $" + valorTotal);
        System.out.println("=======================================================================");
    }

    private void menuEliminarVehiculo() throws ConcesionariaException {
        System.out.println("--- BAJA DE VEHÍCULO ---");
        String patente = leerCadenaNoVacia("Ingrese la patente del vehículo a eliminar: ");
        Vehiculo v = servicio.buscarPorPatente(patente);

        System.out.println("Atención: Está a punto de dar de baja la siguiente unidad:");
        System.out.println(v);
        System.out.println("¿Confirma la eliminación permanente? (1 = SÍ / 2 = CANCELAR): ");
        int conf = leerEnteroSeguro("Opción: ", 1, 2);
        if (conf == 1) {
            servicio.eliminarVehiculo(patente);
            System.out.println("✓ Vehículo eliminado del inventario y del archivo persistente.");
        } else {
            System.out.println("Operación cancelada por el usuario.");
        }
    }

    private void menuGuardarPersistencia() throws ConcesionariaException {
        System.out.println("--- GUARDADO DE DATOS (PERSISTENCIA I/O) ---");
        servicio.guardarCambiosEnDisco();
        System.out.println("✓ ¡Todos los datos se han guardado con éxito en el archivo de texto!");
    }

    // =========================================================================
    // UTILIDADES DE PRESENTACIÓN Y DATA-ENTRY SEGURO (EJES 8 Y 9)
    // =========================================================================

    private void mostrarTablaVehiculos(List<Vehiculo> lista, String titulo) {
        System.out.println("\n--------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(" " + titulo + " (Total: " + lista.size() + " unidades)");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------");
        if (lista.isEmpty()) {
            System.out.println(" [ No existen unidades registradas para este criterio ]");
        } else {
            for (Vehiculo v : lista) {
                // Demuestra el uso polimórfico de toString() de Object (Eje 10)
                System.out.println(" • " + v.toString());
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------\n");
    }

    /**
     * Lectura segura de un entero utilizando Integer.parseInt (Eje 9 - java 4).
     * Atrapa NumberFormatException si el usuario ingresa letras o símbolos.
     */
    private int leerEnteroSeguro(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String linea = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(linea); // Uso de Wrapper Integer (java 4, pág 6)
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println(">> Por favor ingrese un número entero comprendido entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println(">> Error de entrada: El valor ingresado no es un número entero válido.");
            }
        }
    }

    /**
     * Lectura segura de un double utilizando Double.parseDouble (Eje 9 - java 4).
     */
    private double leerDoubleSeguro(String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            String linea = scanner.nextLine().trim();
            try {
                double valor = Double.parseDouble(linea); // Uso de Wrapper Double (java 4, pág 6)
                if (valor >= min) {
                    return valor;
                }
                System.out.println(">> El monto debe ser igual o superior a $" + min + ".");
            } catch (NumberFormatException e) {
                System.out.println(">> Error de entrada: Ingrese un valor numérico válido (ej: 15000000).");
            }
        }
    }

    private String leerCadenaNoVacia(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linea = scanner.nextLine().trim();
            if (!linea.isEmpty()) {
                return linea;
            }
            System.out.println(">> Este campo es obligatorio, no puede dejarse en blanco.");
        }
    }

    private void pausar() {
        System.out.print("\nPresione [ENTER] para regresar al menú principal...");
        scanner.nextLine();
    }
}
