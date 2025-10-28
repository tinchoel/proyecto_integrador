package com.martin.facturacion;

import com.martin.facturacion.io.LectorCsv;
import com.martin.facturacion.io.GeneradorReporte;
import com.martin.facturacion.modelo.CasoPrueba;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal de la aplicación de facturación.
 * <p>
 * Se encarga de orquestar el proceso completo de lectura de casos de prueba
 * desde un archivo CSV, validación de los mismos y generación de un reporte
 * de salida en el directorio especificado.
 * </p>
 *
 * <p>
 * Uso desde consola:
 * </p>
 * 
 * <pre>{@code
 * java -jar proyecto_integrador.jar <ruta_csv> <out_dir> [--ignorar-cabecera]
 * }</pre>
 *
 * <p>
 * Ejemplo:
 * </p>
 * 
 * <pre>{@code
 * java -jar proyecto_integrador.jar tests.csv output/ --ignorar-cabecera
 * }</pre>
 *
 * <p>
 * Dependencias principales:
 * </p>
 * <ul>
 * <li>{@link com.martin.facturacion.io.LectorCsv} para la lectura de archivos
 * CSV.</li>
 * <li>{@link com.martin.facturacion.io.GeneradorReporte} para la creación del
 * reporte.</li>
 * <li>{@link org.slf4j.Logger} para el registro de eventos.</li>
 * </ul>
 *
 * @author Martin
 * @version 1.0
 */
public class AplicacionPrincipal {

    /**
     * Logger de la aplicación, utilizado para registrar eventos e información del
     * proceso.
     */
    private static final Logger logger = LoggerFactory.getLogger(AplicacionPrincipal.class);

    /**
     * Punto de entrada principal del programa.
     * <p>
     * Este método recibe los parámetros de entrada, valida sus valores,
     * invoca al lector CSV y al generador de reportes, y muestra los resultados
     * por consola y en el log.
     * </p>
     *
     * @param args Argumentos de línea de comandos. Se esperan los siguientes
     *             valores:
     *             <ul>
     *             <li><b>args[0]</b> → Ruta del archivo CSV con los casos de
     *             prueba.</li>
     *             <li><b>args[1]</b> → Directorio de salida donde se generará el
     *             reporte.</li>
     *             <li><b>args[2]</b> (opcional) → Bandera
     *             <code>--ignorar-cabecera</code> para omitir la primera línea del
     *             CSV.</li>
     *             </ul>
     * @throws Exception Si ocurre un error en la lectura del archivo CSV o la
     *                   generación del reporte.
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Uso: java -jar proyecto_integrador.jar <ruta_csv> <out_dir> [--ignorar-cabecera]");
            return;
        }

        File csv = new File(args[0]);
        File out = new File(args[1]);

        boolean ignorar = false;
        if (args.length >= 3 && "--ignorar-cabecera".equals(args[2])) {
            ignorar = true;
        }

        List<String> errores = new ArrayList<>();
        List<CasoPrueba> casos = LectorCsv.leer(csv, errores, ignorar);

        GeneradorReporte.generar(casos, errores, out.toPath());

        System.out.println("Reporte generado en: " + out.getAbsolutePath());
        logger.info("Proceso finalizado, {} casos válidos, {} errores", casos.size(), errores.size());
    }
}
