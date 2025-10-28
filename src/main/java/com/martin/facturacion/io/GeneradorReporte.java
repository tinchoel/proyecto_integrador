package com.martin.facturacion.io;

import com.martin.facturacion.modelo.CasoPrueba;
import com.martin.facturacion.modelo.EstadoPrueba;
import com.martin.facturacion.servicio.EstadisticasPruebas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Clase encargada de generar los reportes de salida del sistema de facturación
 * a partir de los casos de prueba procesados y los errores detectados.
 *
 * <p>
 * Produce tres archivos dentro del directorio de salida indicado:
 * </p>
 * <ul>
 * <li><b>resumen.txt</b>: reporte general con estadísticas de ejecución.</li>
 * <li><b>resumen.csv</b>: detalle línea a línea de todos los casos de prueba
 * (BONUS).</li>
 * <li><b>errores.log</b>: listado textual de los errores ocurridos durante el
 * proceso.</li>
 * </ul>
 *
 * <p>
 * Utiliza la clase {@link EstadisticasPruebas} para calcular totales,
 * porcentajes
 * y tiempos promedio de ejecución.
 * </p>
 *
 * <p>
 * Todos los archivos se generan utilizando {@link java.nio.file.Files} y
 * {@link java.io.BufferedWriter} para garantizar compatibilidad y eficiencia.
 * </p>
 *
 * @author Martin
 * @version 1.0
 */
public class GeneradorReporte {

    /** Logger para registrar eventos e información de depuración. */
    private static final Logger logger = LoggerFactory.getLogger(GeneradorReporte.class);

    /**
     * Genera los reportes de salida a partir de los resultados de los casos de
     * prueba.
     *
     * @param casos   lista de {@link CasoPrueba} correctamente procesados.
     * @param errores lista de mensajes de error generados durante la lectura o
     *                validación del CSV.
     * @param outDir  ruta del directorio donde se guardarán los reportes.
     * @throws IOException si ocurre un error al crear directorios o escribir
     *                     archivos.
     */
    public static void generar(List<CasoPrueba> casos, List<String> errores, Path outDir) throws IOException {

        // Si el directorio no existe, se crea automáticamente.
        if (!Files.exists(outDir))
            Files.createDirectories(outDir);

        // Se calculan las estadísticas globales de ejecución.
        EstadisticasPruebas stats = new EstadisticasPruebas(casos);

        /**
         * ============================================================
         * 1️⃣ Generación del archivo resumen.txt
         * ============================================================
         */
        Path resumenTxt = outDir.resolve("resumen.txt");
        try (BufferedWriter bw = Files.newBufferedWriter(resumenTxt)) {

            // Total de casos procesados.
            bw.write("Total tests: " + stats.getTotal());
            bw.newLine();

            // Conteo y porcentaje por estado (PASSED, FAILED, SKIPPED).
            for (EstadoPrueba s : EstadoPrueba.values()) {
                bw.write(String.format(
                        "%s: %d (%.2f%%)",
                        s,
                        stats.getConteos().getOrDefault(s, 0L),
                        stats.getPorcentajes().get(s)));
                bw.newLine();
            }

            // Promedio de tiempo de ejecución.
            bw.write(String.format("Tiempo promedio: %.3f", stats.getTiempoPromedio()));
            bw.newLine();

            // Caso más lento (si existe).
            if (stats.getMasLento().isPresent()) {
                bw.write("Mas lento: " + stats.getMasLento().get().toString());
                bw.newLine();
            }
        }
        logger.info("Resumen guardado en {}", resumenTxt.toAbsolutePath());

        /**
         * ============================================================
         * 2️⃣ Generación del archivo resumen.csv (BONUS)
         * ============================================================
         */
        Path resumenCsv = outDir.resolve("resumen.csv");
        try (BufferedWriter bw = Files.newBufferedWriter(resumenCsv)) {
            // Cabecera CSV.
            bw.write("idTest,nombreTest,estado,tiempoEjecucion");
            bw.newLine();

            // Se recorre la lista de casos y se escriben sus datos en formato CSV.
            for (CasoPrueba c : casos) {
                bw.write(String.format(
                        "%s,%s,%s,%.3f",
                        c.getIdTest(),
                        c.getNombreTest(),
                        c.getEstado(),
                        c.getTiempoEjecucion()));
                bw.newLine();
            }
        }
        logger.info("Resumen CSV guardado en {}", resumenCsv.toAbsolutePath());

        /**
         * ============================================================
         * 3️⃣ Generación del archivo errores.log
         * ============================================================
         */
        Path errorLog = outDir.resolve("errores.log");
        try (BufferedWriter bw = Files.newBufferedWriter(errorLog)) {
            for (String e : errores) {
                bw.write(e + System.lineSeparator());
            }
        }
        logger.info("Log de errores guardado en {}", errorLog.toAbsolutePath());
    }
}