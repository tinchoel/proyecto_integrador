package com.martin.facturacion.io;

import com.martin.facturacion.modelo.CasoPrueba;
import com.martin.facturacion.modelo.EstadoPrueba;
import com.martin.facturacion.excepciones.ExcepcionFormatoCsv;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase encargada de la lectura, validación y conversión de los archivos CSV
 * que contienen los resultados de las pruebas automatizadas del sistema.
 *
 * <p>
 * Esta versión incluye validaciones adicionales exigidas en la Etapa 3:
 * </p>
 * <ul>
 * <li>Archivo inexistente.</li>
 * <li>Ruta que no es un archivo regular.</li>
 * <li>Permisos de lectura insuficientes.</li>
 * <li>Extensión incorrecta (.txt, .xls, etc.) → lanza
 * {@link ExcepcionFormatoCsv}.</li>
 * </ul>
 *
 * @author Martin
 * @version 1.2
 */
public class LectorCsv {

    /** Logger utilizado para registrar información y advertencias. */
    private static final Logger logger = LoggerFactory.getLogger(LectorCsv.class);

    /**
     * Lee un archivo CSV, valida su contenido y devuelve una lista de casos de
     * prueba válidos.
     *
     * @param file            archivo CSV a procesar.
     * @param errores         lista donde se agregan mensajes sobre líneas
     *                        inválidas.
     * @param ignorarCabecera si es true, ignora la primera línea del archivo.
     * @return lista de {@link CasoPrueba}.
     *
     * @throws IOException         si el archivo no existe, no es un archivo regular
     *                             o no puede leerse.
     * @throws ExcepcionFormatoCsv si la extensión del archivo NO es ".csv".
     */
    public static List<CasoPrueba> leer(File file, List<String> errores, boolean ignorarCabecera)
            throws IOException, ExcepcionFormatoCsv {

        List<CasoPrueba> resultado = new ArrayList<>();

        // ============================================================
        // Validaciones previas (Etapa 3)
        // ============================================================

        // ⛔ Archivo inexistente
        if (!file.exists()) {
            throw new FileNotFoundException("El archivo no existe: " + file.getAbsolutePath());
        }

        // ⛔ La ruta existe pero NO es un archivo (puede ser una carpeta)
        if (!file.isFile()) {
            throw new IOException("La ruta no es un archivo válido: " + file.getAbsolutePath());
        }

        // ⛔ Extensión incorrecta → se usa la excepción personalizada
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            throw new ExcepcionFormatoCsv("El archivo no tiene extensión .csv: " + file.getName());
        }

        // ⛔ Sin permisos de lectura
        if (!file.canRead()) {
            throw new IOException("No se puede leer el archivo: " + file.getAbsolutePath());
        }

        // ============================================================
        // Lectura y validación de contenido CSV
        // ============================================================

        try (BufferedReader br = Files.newBufferedReader(file.toPath())) {

            String linea;
            int numero = 0;
            boolean primerLineaLeida = false;

            while ((linea = br.readLine()) != null) {
                numero++;

                if (linea.trim().isEmpty())
                    continue;

                if (ignorarCabecera && !primerLineaLeida) {
                    primerLineaLeida = true;
                    logger.info("Cabecera detectada e ignorada: {}", linea);
                    continue;
                }

                String[] partes = linea.split(",", -1);

                if (partes.length != 4) {
                    String msg = numero + ": cantidad de columnas incorrecta -> " + linea;
                    errores.add(msg);
                    logger.warn(msg);
                    continue;
                }

                String id = partes[0].trim();
                String nombre = partes[1].trim();
                String estadoS = partes[2].trim();
                String tiempoS = partes[3].trim();

                EstadoPrueba estado = EstadoPrueba.fromString(estadoS);
                if (estado == null) {
                    String msg = numero + ": estado inválido -> " + linea;
                    errores.add(msg);
                    logger.warn(msg);
                    continue;
                }

                double tiempo;
                try {
                    tiempo = Double.parseDouble(tiempoS);
                } catch (NumberFormatException e) {
                    String msg = numero + ": tiempo inválido -> " + linea;
                    errores.add(msg);
                    logger.warn(msg);
                    continue;
                }

                resultado.add(new CasoPrueba(id, nombre, estado, tiempo));
            }
        }

        return resultado;
    }
}
