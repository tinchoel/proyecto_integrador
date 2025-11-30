package com.martin.facturacion.servicio;

import com.martin.facturacion.modelo.CasoPrueba;
import com.martin.facturacion.modelo.EstadoPrueba;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EstadisticasPruebasTest {

    // -------------------------------------------------------------------------
    // ✔ TEST ORIGINAL (SE MANTIENE)
    // -------------------------------------------------------------------------
    @Test
    public void estadisticasCalculadasCorrectamente() {
        List<CasoPrueba> lista = Arrays.asList(
                new CasoPrueba("1", "t1", EstadoPrueba.PASSED, 1.0),
                new CasoPrueba("2", "t2", EstadoPrueba.FAILED, 3.0),
                new CasoPrueba("3", "t3", EstadoPrueba.PASSED, 2.0),
                new CasoPrueba("4", "t4", EstadoPrueba.SKIPPED, 0.5));

        EstadisticasPruebas stats = new EstadisticasPruebas(lista);

        assertEquals(4, stats.getTotal());
        assertEquals(2L, stats.getConteos().get(EstadoPrueba.PASSED));
        assertEquals(1L, stats.getConteos().get(EstadoPrueba.FAILED));
        assertEquals(1L, stats.getConteos().get(EstadoPrueba.SKIPPED));
        assertEquals(1.625, stats.getTiempoPromedio(), 1e-6);

        assertTrue(stats.getMasLento().isPresent());
        assertEquals("2", stats.getMasLento().get().getIdTest());
    }

    // -------------------------------------------------------------------------
    // 🔵 NUEVAS PRUEBAS — ETAPA 3
    // -------------------------------------------------------------------------

    /** 🔵 Lista vacía → totales en cero, porcentajes en cero, sin más lento */
    @Test
    public void listaVaciaProduceEstadisticasCero() {
        List<CasoPrueba> lista = Collections.emptyList();
        EstadisticasPruebas stats = new EstadisticasPruebas(lista);

        assertEquals(0, stats.getTotal());
        assertEquals(0.0, stats.getTiempoPromedio());
        assertFalse(stats.getMasLento().isPresent());

        // todos los porcentajes deben ser 0
        assertEquals(0.0, stats.getPorcentajes().get(EstadoPrueba.PASSED));
        assertEquals(0.0, stats.getPorcentajes().get(EstadoPrueba.FAILED));
        assertEquals(0.0, stats.getPorcentajes().get(EstadoPrueba.SKIPPED));
    }

    /** 🔵 Un solo caso → ese es el más lento, los porcentajes deben ser 100% */
    @Test
    public void unSoloElementoCalculaCorrecto() {
        CasoPrueba c = new CasoPrueba("1", "t1", EstadoPrueba.PASSED, 2.5);
        EstadisticasPruebas stats = new EstadisticasPruebas(List.of(c));

        assertEquals(1, stats.getTotal());
        assertEquals(2.5, stats.getTiempoPromedio());
        assertTrue(stats.getMasLento().isPresent());
        assertEquals("1", stats.getMasLento().get().getIdTest());

        assertEquals(100.0, stats.getPorcentajes().get(EstadoPrueba.PASSED));
        assertEquals(0.0, stats.getPorcentajes().get(EstadoPrueba.FAILED));
        assertEquals(0.0, stats.getPorcentajes().get(EstadoPrueba.SKIPPED));
    }

    /** 🔵 Todos fallados → porcentaje FAILED = 100%, resto = 0% */
    @Test
    public void todosFallados() {
        List<CasoPrueba> lista = Arrays.asList(
                new CasoPrueba("1", "t1", EstadoPrueba.FAILED, 1),
                new CasoPrueba("2", "t2", EstadoPrueba.FAILED, 2),
                new CasoPrueba("3", "t3", EstadoPrueba.FAILED, 3));

        EstadisticasPruebas stats = new EstadisticasPruebas(lista);

        assertEquals(3, stats.getTotal());
        assertEquals(3L, stats.getConteos().get(EstadoPrueba.FAILED));
        assertEquals(100.0, stats.getPorcentajes().get(EstadoPrueba.FAILED));
        assertEquals(0.0, stats.getPorcentajes().get(EstadoPrueba.PASSED));
        assertEquals(0.0, stats.getPorcentajes().get(EstadoPrueba.SKIPPED));

        assertEquals(2.0, stats.getTiempoPromedio());
        assertTrue(stats.getMasLento().isPresent());
        assertEquals("3", stats.getMasLento().get().getIdTest());
    }

    /** 🔵 Estadísticas deben incluir los estados aunque no aparezcan en la lista */
    @Test
    public void porcentajesIncluyenTodosLosEstados() {
        List<CasoPrueba> lista = Arrays.asList(
                new CasoPrueba("1", "t1", EstadoPrueba.PASSED, 1),
                new CasoPrueba("2", "t2", EstadoPrueba.PASSED, 1));

        EstadisticasPruebas stats = new EstadisticasPruebas(lista);

        assertEquals(2, stats.getTotal());
        assertEquals(100.0, stats.getPorcentajes().get(EstadoPrueba.PASSED));
        assertEquals(0.0, stats.getPorcentajes().get(EstadoPrueba.FAILED));
        assertEquals(0.0, stats.getPorcentajes().get(EstadoPrueba.SKIPPED));
    }

    /** 🔵 Más lento debe ser correcto para tiempos mezclados */
    @Test
    public void calculaMasLentoCorrectamente() {
        List<CasoPrueba> lista = Arrays.asList(
                new CasoPrueba("10", "t1", EstadoPrueba.PASSED, 10.0),
                new CasoPrueba("20", "t2", EstadoPrueba.PASSED, 5.0),
                new CasoPrueba("30", "t3", EstadoPrueba.PASSED, 20.0));

        EstadisticasPruebas stats = new EstadisticasPruebas(lista);

        assertTrue(stats.getMasLento().isPresent());
        assertEquals("30", stats.getMasLento().get().getIdTest());
    }
}
