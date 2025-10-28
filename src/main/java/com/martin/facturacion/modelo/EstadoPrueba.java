package com.martin.facturacion.modelo;

/**
 * Enumeración que representa los posibles estados de ejecución de un caso de
 * prueba
 * dentro del sistema de facturación.
 *
 * <p>
 * Los valores definidos son:
 * </p>
 * <ul>
 * <li><b>PASSED</b>: El test se ejecutó correctamente y pasó todas las
 * validaciones.</li>
 * <li><b>FAILED</b>: El test falló en alguna validación o produjo un error de
 * ejecución.</li>
 * <li><b>SKIPPED</b>: El test fue omitido, ya sea por dependencia o
 * configuración no ejecutada.</li>
 * </ul>
 *
 * <p>
 * Además, la clase incluye un método utilitario {@link #fromString(String)} que
 * permite
 * convertir cadenas de texto (por ejemplo, leídas desde archivos CSV) a valores
 * de esta
 * enumeración de forma segura, sin generar excepciones en caso de errores de
 * formato.
 * </p>
 *
 * @author Martin
 * @version 1.0
 */
public enum EstadoPrueba {

    /** Test ejecutado exitosamente */
    PASSED,

    /** Test fallido */
    FAILED,

    /** Test omitido (no ejecutado) */
    SKIPPED;

    /**
     * Convierte una cadena de texto en un valor correspondiente del enum
     * {@code EstadoPrueba}.
     *
     * <p>
     * Este método es más seguro que {@link Enum#valueOf(Class, String)} porque:
     * </p>
     * <ul>
     * <li>No lanza excepción si el valor no es reconocido.</li>
     * <li>Ignora mayúsculas/minúsculas y espacios innecesarios.</li>
     * </ul>
     *
     * <p>
     * <b>Ejemplo de uso:</b>
     * </p>
     *
     * <pre>{@code
     * EstadoPrueba estado = EstadoPrueba.fromString("passed");
     * // estado = EstadoPrueba.PASSED
     * }</pre>
     *
     * @param s Cadena de texto a convertir (por ejemplo, "PASSED", "failed" o
     *          "Skipped").
     * @return El valor correspondiente del enum, o {@code null} si el texto no
     *         coincide con ninguno.
     */
    public static EstadoPrueba fromString(String s) {
        if (s == null)
            return null;

        switch (s.trim().toUpperCase()) {
            case "PASSED":
                return PASSED;
            case "FAILED":
                return FAILED;
            case "SKIPPED":
                return SKIPPED;
            default:
                return null;
        }
    }
}
