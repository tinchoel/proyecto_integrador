package com.martin.facturacion.modelo;

/**
 * Representa un caso de prueba individual leído desde el archivo CSV.
 * 
 * Cada objeto de esta clase modela una línea válida del archivo de resultados
 * de pruebas.
 * Contiene el identificador del test, su nombre, el estado (PASSED, FAILED,
 * SKIPPED)
 * y el tiempo de ejecución en segundos.
 * 
 * <p>
 * Ejemplo de línea CSV representada:
 * 
 * <pre>
 * TC001,LoginTest,PASSED,0.45
 * </pre>
 * 
 * Esta clase aplica principios de encapsulación:
 * - Los atributos son privados y solo accesibles mediante getters.
 * - Se inicializan a través del constructor.
 * - No existen setters para mantener inmutabilidad parcial.
 * 
 * @author Martin
 * @version 1.0
 */
public class CasoPrueba {

    /** Identificador único del test, por ejemplo "TC001". */
    private String idTest;

    /**
     * Nombre descriptivo del test, por ejemplo
     * "ValidarLoginConCredencialesCorrectas".
     */
    private String nombreTest;

    /** Estado del test (PASSED, FAILED o SKIPPED). */
    private EstadoPrueba estado;

    /** Tiempo total de ejecución del test en segundos. */
    private double tiempoEjecucion;

    /**
     * Constructor que inicializa todos los campos de un caso de prueba.
     * 
     * @param idTest          identificador único del test
     * @param nombreTest      nombre descriptivo del test
     * @param estado          estado del test (debe ser un valor válido del enum
     *                        EstadoPrueba)
     * @param tiempoEjecucion tiempo de ejecución en segundos (debe ser >= 0)
     * @throws IllegalArgumentException si alguno de los parámetros es inválido
     */
    public CasoPrueba(String idTest, String nombreTest, EstadoPrueba estado, double tiempoEjecucion) {
        // Validaciones básicas para evitar datos nulos o incorrectos
        if (idTest == null || idTest.isBlank()) {
            throw new IllegalArgumentException("El idTest no puede ser nulo ni vacío.");
        }
        if (nombreTest == null || nombreTest.isBlank()) {
            throw new IllegalArgumentException("El nombreTest no puede ser nulo ni vacío.");
        }
        if (estado == null) {
            throw new IllegalArgumentException("El estado del test no puede ser nulo.");
        }
        if (tiempoEjecucion < 0) {
            throw new IllegalArgumentException("El tiempo de ejecución no puede ser negativo.");
        }

        // Asignación de valores una vez validados
        this.idTest = idTest;
        this.nombreTest = nombreTest;
        this.estado = estado;
        this.tiempoEjecucion = tiempoEjecucion;
    }

    /**
     * * Devuelve el identificador único del test.
     * 
     * @return el identificador del test
     */
    public String getIdTest() {
        return idTest;
    }

    /**
     * * Devuelve el nombre descriptivo del test.
     * 
     * @return el nombre del test
     */
    public String getNombreTest() {
        return nombreTest;
    }

    /**
     * * Devuelve el estado actual del test.
     * 
     * @return el estado del test (PASSED, FAILED o SKIPPED)
     */
    public EstadoPrueba getEstado() {
        return estado;
    }

    /**
     * * Devuelve el tiempo que tardó el test en ejecutarse, en segundos.
     * 
     * @return el tiempo de ejecución del test en segundos
     */
    public double getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    /**
     * Devuelve una representación textual del caso de prueba, en formato CSV.
     * 
     * @return cadena con los valores separados por coma, por ejemplo:
     * 
     *         <pre>
     *         "TC001,LoginTest,PASSED,0.45"
     *         </pre>
     */
    @Override
    public String toString() {
        return idTest + "," + nombreTest + "," + estado + "," + String.format("%.3f", tiempoEjecucion);
    }
}
