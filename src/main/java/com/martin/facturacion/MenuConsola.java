package com.martin.facturacion;

import com.martin.facturacion.modelo.CasoPrueba;
import com.martin.facturacion.servicio.EstadisticasPruebas;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * Menú interactivo en consola para consultar estadísticas
 * y visualizar datos generados por la aplicación.
 *
 * Se ejecuta solo cuando NO se está corriendo en modo test.
 *
 * Opciones disponibles:
 * 1) Cantidad total de casos
 * 2) Cantidad por estado (PASSED / FAILED / SKIPPED)
 * 3) Tiempo total de ejecución
 * 4) Mostrar errores detectados en el CSV
 * 5) Abrir carpeta de reportes
 * 0) Salir
 */
public class MenuConsola {

    private final List<CasoPrueba> casos;
    private final List<String> errores;
    private final Path outDir;
    private final Scanner scanner;

    public MenuConsola(List<CasoPrueba> casos, List<String> errores, Path outDir) {
        this.casos = casos;
        this.errores = errores;
        this.outDir = outDir;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Inicia el menú interactivo.
     */
    public void iniciar() {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> mostrarCantidadTotal();
                case 2 -> mostrarCantidadPorEstado();
                case 3 -> mostrarTiempoTotal();
                case 4 -> mostrarErrores();
                case 5 -> abrirCarpetaReportes();
                case 0 -> System.out.println("Saliendo del menú...");
                default -> System.out.println("Opción inválida, intente nuevamente.");
            }

            System.out.println();

        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println("========================================");
        System.out.println("        MENÚ DE ESTADÍSTICAS");
        System.out.println("========================================");
        System.out.println("1) Mostrar cantidad total de casos");
        System.out.println("2) Mostrar cantidad por estado");
        System.out.println("3) Mostrar tiempo total de ejecución");
        System.out.println("4) Mostrar errores detectados");
        System.out.println("5) Abrir carpeta de reportes");
        System.out.println("0) Salir");
        System.out.println("========================================");
    }

    private int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.println("Debe ingresar un número válido.");
            scanner.next(); // descarta la entrada inválida
            System.out.print(mensaje);
        }
        return scanner.nextInt();
    }

    // =======================================
    // OPCIONES DEL MENÚ
    // =======================================

    private void mostrarCantidadTotal() {
        System.out.println("Cantidad total de casos: " + casos.size());
    }

    private void mostrarCantidadPorEstado() {
        var estadisticas = new EstadisticasPruebas(casos);

        System.out.println("PASSED : " + estadisticas.contarPassed());
        System.out.println("FAILED : " + estadisticas.contarFailed());
        System.out.println("SKIPPED: " + estadisticas.contarSkipped());
    }

    private void mostrarTiempoTotal() {
        var estadisticas = new EstadisticasPruebas(casos);
        double total = estadisticas.tiempoTotal();

        System.out.printf("Tiempo total de ejecución: %.2f segundos\n", total);
    }

    private void mostrarErrores() {
        if (errores.isEmpty()) {
            System.out.println("No se detectaron errores en el CSV.");
            return;
        }

        System.out.println("Errores detectados:");
        errores.forEach(e -> System.out.println(" - " + e));
    }

    private void abrirCarpetaReportes() {
        System.out.println("Carpeta de reportes: " + outDir.toAbsolutePath());

        try {
            // Abre explorador de archivos automáticamente
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("explorer.exe", outDir.toAbsolutePath().toString()).start();
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                new ProcessBuilder("open", outDir.toAbsolutePath().toString()).start();
            } else {
                new ProcessBuilder("xdg-open", outDir.toAbsolutePath().toString()).start();
            }

        } catch (IOException e) {
            System.out.println("No se pudo abrir la carpeta automáticamente.");
        }
    }
}
