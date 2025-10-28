package com.martin.facturacion.servicio;

import com.martin.facturacion.modelo.CasoPrueba;
import com.martin.facturacion.modelo.EstadoPrueba;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class EstadisticasPruebasTest {

    @Test
    public void estadisticasCalculadasCorrectamente() {
        List<CasoPrueba> lista = Arrays.asList(
            new CasoPrueba("1","t1", EstadoPrueba.PASSED, 1.0),
            new CasoPrueba("2","t2", EstadoPrueba.FAILED, 3.0),
            new CasoPrueba("3","t3", EstadoPrueba.PASSED, 2.0),
            new CasoPrueba("4","t4", EstadoPrueba.SKIPPED, 0.5)
        );
        EstadisticasPruebas stats = new EstadisticasPruebas(lista);
        assertEquals(4, stats.getTotal());
        assertEquals(2L, stats.getConteos().get(EstadoPrueba.PASSED));
        assertEquals(1L, stats.getConteos().get(EstadoPrueba.FAILED));
        assertEquals(1L, stats.getConteos().get(EstadoPrueba.SKIPPED));
        assertEquals(1.625, stats.getTiempoPromedio(), 1e-6);
        assertTrue(stats.getMasLento().isPresent());
        assertEquals("2", stats.getMasLento().get().getIdTest());
    }
}
