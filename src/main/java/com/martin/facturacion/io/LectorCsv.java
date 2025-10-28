package com.martin.facturacion.io;

import com.martin.facturacion.modelo.CasoPrueba;
import com.martin.facturacion.modelo.EstadoPrueba;
import com.martin.facturacion.excepciones.ExcepcionLineaInvalida;

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
 * Cada línea del archivo debe tener el siguiente formato:
 * </p>
 * 
 * <pre>
 * idTest,nombreTest,estado,tiempoEjecucion
 * </pre>
 * 
 * <p>
 * Por ejemplo:
 * </p>
 * 
 * <pre>
 * TC_001,Prueba login,PASSED,0.342
 * TC_002,Validar facturación,FAILED,1.120
 * </pre>
 *
 * <p>
 * La clase realiza las siguientes validaciones:
 * </p>
 * <ul>
 * <li>Verifica que la cantidad de columnas sea exactamente 4.</li>
 * <li>Valida que el campo "estado" corresponda a uno de los valores del enum
 * {@link EstadoPrueba}.</li>
 * <li>Comprueba que el campo "tiempoEjecucion" sea numérico.</li>
 * <li>Omite líneas vacías o cabeceras, si se indica.</li>
 * <li>Registra todos los errores detectados en una lista de strings.</li>
 * </ul>
 *
 * <p>
 * Las líneas válidas se convierten en objetos {@link CasoPrueba} y se
 * agregan a la lista de resultados.
 * </p>
 *
 * @author Martin
 * @version 1.0
 */
public class LectorCsv {

    /**
     * Logger utilizado para registrar información y advertencias durante la
     * lectura.
     */
    private static final Logger logger = LoggerFactory.getLogger(LectorCsv.class);

    /**
     * Lee un archivo CSV, valida su contenido y devuelve una lista de casos de
     * prueba válidos.
     *
     * @param file            archivo CSV a procesar.
     * @param errores         lista donde se agregan los mensajes de error por línea
     *                        inválida.
     * @param ignorarCabecera si es {@code true}, la primera línea del archivo se
     *                        ignora (se asume que es cabecera).
     * @return una lista de objetos {@link CasoPrueba} válidos y listos para ser
     *         procesados.
     * @throws IOException si ocurre un problema de acceso al archivo.
     */
    public static List<CasoPrueba> leer(File file, List<String> errores, boolean ignorarCabecera) throws IOException {
        List<CasoPrueba> resultado = new ArrayList<>();

        // Se utiliza try-with-resources para asegurar el cierre automático del archivo.
        try (BufferedReader br = Files.newBufferedReader(file.toPath())) {
            String linea;
            int numero = 0;
            boolean primerLineaLeida = false;

            // Se lee línea por línea hasta llegar al final del archivo.
            while ((linea = br.readLine()) != null) {
                numero++;

                // Se ignoran líneas vacías.
                if (linea.trim().isEmpty())
                    continue;

                // Si se indicó ignorar cabecera, se salta la primera línea.
                if (ignorarCabecera && !primerLineaLeida) {
                    primerLineaLeida = true;
                    logger.info("Cabecera detectada e ignorada: {}", linea);
                    continue;
                }

                // Se separan los campos por coma (manteniendo columnas vacías si las hay).
                String[] partes = linea.split(",", -1);

                // Validación de cantidad de columnas (deben ser 4).
                if (partes.length != 4) {
                    String msg = numero + ": cantidad de columnas incorrecta -> " + linea;
                    errores.add(msg);
                    logger.warn(msg);
                    continue;
                }

                // Extracción de valores con trim (elimina espacios extra).
                String id = partes[0].trim();
                String nombre = partes[1].trim();
                String estadoS = partes[2].trim();
                String tiempoS = partes[3].trim();

                // Conversión del estado (texto a enum).
                EstadoPrueba estado = EstadoPrueba.fromString(estadoS);
                if (estado == null) {
                    String msg = numero + ": estado inválido -> " + linea;
                    errores.add(msg);
                    logger.warn(msg);
                    continue;
                }

                // Conversión del tiempo a número decimal.
                double tiempo;
                try {
                    tiempo = Double.parseDouble(tiempoS);
                } catch (NumberFormatException e) {
                    String msg = numero + ": tiempo inválido -> " + linea;
                    errores.add(msg);
                    logger.warn(msg);
                    continue;
                }

                // Si todo es válido, se crea un nuevo CasoPrueba.
                resultado.add(new CasoPrueba(id, nombre, estado, tiempo));
            }
        }
        return resultado;
    }
}
