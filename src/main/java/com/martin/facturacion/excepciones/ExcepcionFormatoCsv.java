package com.martin.facturacion.excepciones;

/**
 * Excepción personalizada utilizada para indicar errores de formato o lectura
 * en los archivos CSV procesados por la aplicación.
 * 
 * <p>
 * Esta clase extiende {@link Exception} y permite identificar de manera
 * específica los errores relacionados con el formato del archivo de entrada,
 * tales como columnas faltantes, tipos de datos inválidos o líneas mal
 * formadas.
 * </p>
 * 
 * <p>
 * El uso de excepciones personalizadas mejora la claridad del código,
 * separando los errores esperables (como un CSV con una línea inválida) de los
 * errores imprevistos del sistema.
 * </p>
 * 
 * <p>
 * Ejemplo de uso:
 * </p>
 * 
 * <pre>
 * if (columnas.length != 4) {
 *     throw new ExcepcionFormatoCsv("Número de columnas incorrecto en la línea " + numeroLinea);
 * }
 * </pre>
 * 
 * @author Martin
 * @version 1.0
 */
public class ExcepcionFormatoCsv extends Exception {

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     * 
     * @param mensaje Descripción del problema detectado en el formato del archivo
     *                CSV.
     */
    public ExcepcionFormatoCsv(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor que recibe un mensaje y una causa (otra excepción).
     * 
     * <p>
     * Se utiliza cuando el error de formato está relacionado con otra excepción
     * interna, por ejemplo, un {@link NumberFormatException} al intentar convertir
     * un valor numérico del CSV.
     * </p>
     * 
     * @param mensaje Descripción del error detectado.
     * @param causa   Excepción original que provocó este error.
     */
    public ExcepcionFormatoCsv(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
