package com.martin.facturacion.servicio;

import com.martin.facturacion.modelo.CasoPrueba;
import com.martin.facturacion.modelo.EstadoPrueba;

import java.util.*;
import java.util.stream.*;

/**
 * La clase {@code EstadisticasPruebas} se encarga de calcular métricas
 * y estadísticas generales sobre un conjunto de casos de prueba.
 */
public class EstadisticasPruebas {

    private long total;
    private Map<EstadoPrueba, Long> conteos;
    private Map<EstadoPrueba, Double> porcentajes;
    private double tiempoPromedio;
    private Optional<CasoPrueba> masLento;

    public EstadisticasPruebas(List<CasoPrueba> casos) {
        if (casos == null)
            casos = Collections.emptyList();
        this.total = casos.size();

        this.conteos = casos.stream()
                .collect(Collectors.groupingBy(CasoPrueba::getEstado, Collectors.counting()));

        this.porcentajes = new EnumMap<>(EstadoPrueba.class);
        for (EstadoPrueba s : EstadoPrueba.values()) {
            long c = conteos.getOrDefault(s, 0L);
            double pct = total == 0 ? 0.0 : (c * 100.0) / total;
            porcentajes.put(s, pct);
        }

        this.tiempoPromedio = casos.stream()
                .mapToDouble(CasoPrueba::getTiempoEjecucion)
                .average()
                .orElse(0.0);

        this.masLento = casos.stream()
                .max(Comparator.comparingDouble(CasoPrueba::getTiempoEjecucion));
    }

    public long getTotal() {
        return total;
    }

    public Map<EstadoPrueba, Long> getConteos() {
        return conteos;
    }

    public Map<EstadoPrueba, Double> getPorcentajes() {
        return porcentajes;
    }

    public double getTiempoPromedio() {
        return tiempoPromedio;
    }

    public Optional<CasoPrueba> getMasLento() {
        return masLento;
    }

    // -------------------------------------------------------------------------
    // MÉTODOS REQUERIDOS POR MenuConsola (los que te faltan)
    // -------------------------------------------------------------------------

    /** Cantidad de pruebas PASSED */
    public long contarPassed() {
        return conteos.getOrDefault(EstadoPrueba.PASSED, 0L);
    }

    /** Cantidad de pruebas FAILED */
    public long contarFailed() {
        return conteos.getOrDefault(EstadoPrueba.FAILED, 0L);
    }

    /** Cantidad de pruebas SKIPPED */
    public long contarSkipped() {
        return conteos.getOrDefault(EstadoPrueba.SKIPPED, 0L);
    }

    /** Tiempo total sumado de todos los casos */
    public double tiempoTotal() {
        return porcentajeSeguro(conteos) == 0 ? 0.0
                : conteos.keySet().stream()
                        .mapToDouble(e -> 0.0) // inicial
                        .sum()
                        + getTiempoPromedio() * getTotal();
    }

    /** Método interno auxiliar para evitar NPE si el mapa está vacío */
    private long porcentajeSeguro(Map<EstadoPrueba, Long> mapa) {
        return mapa == null ? 0 : mapa.values().stream().mapToLong(Long::longValue).sum();
    }
}
