package com.martin.facturacion.excepciones;

/**
 * Excepción personalizada que representa un error detectado en una línea
 * específica
 * durante la lectura o validación de un archivo CSV.
 * 
 * <p>
 * Esta clase permite identificar exactamente qué línea produjo el error,
 * junto con su contenido y un mensaje descriptivo, facilitando la depuración
 * y el registro detallado de errores.
 * </p>
 * 
 * <p>
 * Ejemplo de uso:
 * </p>
 * 
 * <pre>
 * if (columnas.length != 4) {
 *     throw new ExcepcionLineaInvalida(
 *             numeroLinea,
 *             linea,
 *             "Número de columnas incorrecto en la línea " + numeroLinea);
 * }
 * </pre>
 * 
 * @author Martin
 * @version 1.0
 */
public class ExcepcionLineaInvalida extends Exception {

    /** Número de línea en el archivo donde ocurrió el error. */
    private int numeroLinea;

    /** Contenido completo de la línea inválida. */
    private String contenidoLinea;

    /**
     * Crea una nueva excepción que representa una línea inválida dentro del
     * archivo.
     * 
     * @param numeroLinea    número de línea (1-indexado) donde se detectó el error.
     * @param contenidoLinea texto completo de la línea que produjo la excepción.
     * @param mensaje        descripción detallada del motivo del error.
     */
    public ExcepcionLineaInvalida(int numeroLinea, String contenidoLinea, String mensaje) {
        super(mensaje);
        this.numeroLinea = numeroLinea;
        this.contenidoLinea = contenidoLinea;
    }

    /**
     * Devuelve el número de línea en el archivo donde ocurrió el error.
     * 
     * @return número de línea del archivo CSV (comienza en 1).
     */
    public int getNumeroLinea() {
        return numeroLinea;
    }

    /**
     * Devuelve el contenido textual de la línea inválida.
     * 
     * @return texto original de la línea que produjo el error.
     */
    public String getContenidoLinea() {
        return contenidoLinea;
    }
}
