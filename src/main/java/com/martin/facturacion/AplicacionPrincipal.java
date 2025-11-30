package com.martin.facturacion;

import com.martin.facturacion.io.LectorCsv;
import com.martin.facturacion.io.GeneradorReporte;
import com.martin.facturacion.modelo.CasoPrueba;
import com.martin.facturacion.excepciones.ExcepcionFormatoCsv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal de la aplicación de análisis de casos de prueba.
 */
public class AplicacionPrincipal {

    private static final Logger logger = LoggerFactory.getLogger(AplicacionPrincipal.class);

    public static void main(String[] args) {

        // ---------------------------
        // Validación de argumentos
        // ---------------------------
        if (args.length < 2) {
            System.out.println("Uso: java -jar proyecto_integrador.jar <ruta_csv> <out_dir> [--ignorar-cabecera]");
            return;
        }

        File csv = new File(args[0]);
        File out = new File(args[1]);
        boolean ignorar = args.length >= 3 && "--ignorar-cabecera".equals(args[2]);

        List<String> errores = new ArrayList<>();
        List<CasoPrueba> casos;

        try {
            // 1) Validación adicional: el directorio de salida NO puede ser un archivo
            if (out.exists() && out.isFile()) {
                throw new IOException("El directorio de salida no puede ser un archivo.");
            }

            // 2) Leer CSV
            casos = LectorCsv.leer(csv, errores, ignorar);

            // 3) Generar reportes
            GeneradorReporte.generar(casos, errores, out.toPath());

            System.out.println("Reporte generado en: " + out.getAbsolutePath());
            logger.info("Proceso finalizado: {} casos válidos, {} errores", casos.size(), errores.size());

            // 4) Ejecutar menú solo en modo normal (NO en tests)
            if (System.getProperty("testMode") == null) {
                MenuConsola menu = new MenuConsola(casos, errores, out.toPath());
                menu.iniciar();
            }
        }

        // ---------------------------
        // Manejo de excepciones
        // ---------------------------
        catch (FileNotFoundException e) {
            System.err.println("ERROR: El archivo CSV no existe: " + csv.getAbsolutePath());
            logger.error("Archivo inexistente", e);
        }

        catch (ExcepcionFormatoCsv e) {
            System.err.println("ERROR: Formato de archivo inválido: " + e.getMessage());
            logger.error("Extensión incorrecta", e);
        }

        catch (IOException e) {
            System.err.println("ERROR: No se pudo leer el archivo CSV: " + e.getMessage());
            logger.error("Error de lectura", e);
        }

        catch (Exception e) {
            System.err.println("ERROR inesperado: " + e.getMessage());
            logger.error("Error inesperado", e);
        }
    }
}
