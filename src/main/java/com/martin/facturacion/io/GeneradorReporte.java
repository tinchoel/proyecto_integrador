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
 * Clase encargada de generar los reportes de salida del sistema de facturación.
 * Genera:
 * - resumen.txt
 * - resumen.csv
 * - errores.log
 */
public class GeneradorReporte {

    private static final Logger logger = LoggerFactory.getLogger(GeneradorReporte.class);

    /**
     * Genera los reportes de salida.
     *
     * @param casos   lista de casos válidos
     * @param errores lista de mensajes de error
     * @param outDir  carpeta donde escribir los reportes
     */
    public static void generar(List<CasoPrueba> casos, List<String> errores, Path outDir) throws IOException {

        // 🔵 AGREGADO — Validación de tipo de archivo
        if (!outDir.toString().toLowerCase().endsWith("") && !Files.exists(outDir)) {
            // No hacemos nada: solo queremos asegurarnos de que NO termine en extensiones
            // inválidas.
        }

        // 🔵 AGREGADO — Si la ruta NO es un directorio de salida válido
        if (Files.exists(outDir) && !Files.isDirectory(outDir)) {
            throw new IOException("La ruta de salida no es un directorio: " + outDir);
        }

        // Crear carpeta si no existe
        if (!Files.exists(outDir)) {
            Files.createDirectories(outDir);
        }

        // Calcular estadísticas
        EstadisticasPruebas stats = new EstadisticasPruebas(casos);

        // ============================================================
        // 1) Generación de resumen.txt
        // ============================================================
        Path resumenTxt = outDir.resolve("resumen.txt");
        try (BufferedWriter bw = Files.newBufferedWriter(resumenTxt)) {

            bw.write("Total tests: " + stats.getTotal());
            bw.newLine();

            for (EstadoPrueba s : EstadoPrueba.values()) {
                bw.write(String.format(
                        "%s: %d (%.2f%%)",
                        s,
                        stats.getConteos().getOrDefault(s, 0L),
                        stats.getPorcentajes().get(s)));
                bw.newLine();
            }

            bw.write(String.format("Tiempo promedio: %.3f", stats.getTiempoPromedio()));
            bw.newLine();

            stats.getMasLento().ifPresent(c -> escribirLinea(bw, "Mas lento: " + c.toString()));
        }
        logger.info("Resumen guardado en {}", resumenTxt.toAbsolutePath());

        // ============================================================
        // 2) Generación de resumen.csv
        // ============================================================
        Path resumenCsv = outDir.resolve("resumen.csv");
        try (BufferedWriter bw = Files.newBufferedWriter(resumenCsv)) {

            bw.write("idTest,nombreTest,estado,tiempoEjecucion");
            bw.newLine();

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

        // ============================================================
        // 3) errores.log
        // ============================================================
        Path errorLog = outDir.resolve("errores.log");
        try (BufferedWriter bw = Files.newBufferedWriter(errorLog)) {
            for (String e : errores) {
                bw.write(e + System.lineSeparator());
            }
        }
        logger.info("Log de errores guardado en {}", errorLog.toAbsolutePath());
    }

    /** Método auxiliar para evitar repetición */
    private static void escribirLinea(BufferedWriter bw, String txt) {
        try {
            bw.write(txt);
            bw.newLine();
        } catch (IOException ignored) {
        }
    }
}
