package com.martin.facturacion.servicio;

import com.martin.facturacion.modelo.CasoPrueba;
import com.martin.facturacion.modelo.EstadoPrueba;

import java.util.*;
import java.util.stream.*;

/**
 * La clase {@code EstadisticasPruebas} se encarga de calcular métricas
 * y estadísticas generales sobre un conjunto de casos de prueba.
 * <p>
 * A partir de una lista de objetos {@link CasoPrueba}, la clase calcula:
 * <ul>
 * <li>Cantidad total de pruebas.</li>
 * <li>Cantidad y porcentaje de pruebas por estado ({@link EstadoPrueba}).</li>
 * <li>Tiempo promedio de ejecución.</li>
 * <li>El caso de prueba más lento.</li>
 * </ul>
 * <p>
 * Esta clase es inmutable: todos los valores se calculan al construir la
 * instancia.
 *
 * @author Martin
 * @version 1.0
 * @since 2025-10
 */
public class EstadisticasPruebas {

    /** Cantidad total de casos de prueba procesados. */
    private long total;

    /** Mapa que contiene el conteo de casos agrupados por estado. */
    private Map<EstadoPrueba, Long> conteos;

    /**
     * Mapa con el porcentaje que representa cada estado sobre el total de casos.
     */
    private Map<EstadoPrueba, Double> porcentajes;

    /** Tiempo promedio de ejecución de todos los casos de prueba. */
    private double tiempoPromedio;

    /** Caso de prueba con el mayor tiempo de ejecución, si existe. */
    private Optional<CasoPrueba> masLento;

    /**
     * Constructor principal.
     * <p>
     * Inicializa las estadísticas a partir de la lista de casos de prueba.
     * Si la lista es {@code null}, se utiliza una lista vacía.
     *
     * @param casos lista de {@link CasoPrueba} a analizar.
     */
    public EstadisticasPruebas(List<CasoPrueba> casos) {
        if (casos == null)
            casos = Collections.emptyList();
        this.total = casos.size();

        // Agrupa los casos por estado y cuenta cuántos hay en cada grupo
        this.conteos = casos.stream()
                .collect(Collectors.groupingBy(CasoPrueba::getEstado, Collectors.counting()));

        // Calcula los porcentajes de cada estado
        this.porcentajes = new EnumMap<>(EstadoPrueba.class);
        for (EstadoPrueba s : EstadoPrueba.values()) {
            long c = conteos.getOrDefault(s, 0L);
            double pct = total == 0 ? 0.0 : (c * 100.0) / total;
            porcentajes.put(s, pct);
        }

        // Calcula el tiempo promedio de ejecución
        this.tiempoPromedio = casos.stream()
                .mapToDouble(CasoPrueba::getTiempoEjecucion)
                .average()
                .orElse(0.0);

        // Identifica el caso más lento
        this.masLento = casos.stream()
                .max(Comparator.comparingDouble(CasoPrueba::getTiempoEjecucion));
    }

    /**
     * Obtiene la cantidad total de casos de prueba analizados.
     *
     * @return número total de casos.
     */
    public long getTotal() {
        return total;
    }

    /**
     * Devuelve un mapa con el conteo de casos agrupados por su estado.
     *
     * @return mapa de {@link EstadoPrueba} a cantidad de ocurrencias.
     */
    public Map<EstadoPrueba, Long> getConteos() {
        return conteos;
    }

    /**
     * Devuelve un mapa con los porcentajes de distribución por estado.
     * <p>
     * El valor está expresado en porcentaje (0 a 100).
     *
     * @return mapa de {@link EstadoPrueba} a porcentaje.
     */
    public Map<EstadoPrueba, Double> getPorcentajes() {
        return porcentajes;
    }

    /**
     * Obtiene el tiempo promedio de ejecución de los casos de prueba.
     *
     * @return tiempo promedio en segundos (o la unidad utilizada en
     *         {@link CasoPrueba}).
     */
    public double getTiempoPromedio() {
        return tiempoPromedio;
    }

    /**
     * Obtiene el caso de prueba más lento, si existe.
     *
     * @return un {@link Optional} con el {@link CasoPrueba} más lento o vacío si la
     *         lista estaba vacía.
     */
    public Optional<CasoPrueba> getMasLento() {
        return masLento;
    }
}
