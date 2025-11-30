package com.martin.facturacion.io;

import com.martin.facturacion.modelo.CasoPrueba;
import com.martin.facturacion.modelo.EstadoPrueba;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.util.*;
import java.io.IOException; // ✅ FALTABA ESTA IMPORTACIÓN

import static org.junit.jupiter.api.Assertions.*;

public class GeneradorReporteTest {

    @Test
    public void generaArchivosCorrectamente() throws Exception {
        Path tmp = Files.createTempDirectory("salida");

        List<CasoPrueba> casos = Arrays.asList(
                new CasoPrueba("1", "Login", EstadoPrueba.PASSED, 1.2),
                new CasoPrueba("2", "Factura", EstadoPrueba.FAILED, 2.5));

        List<String> errores = Arrays.asList(
                "1: estado invalido",
                "3: tiempo invalido");

        GeneradorReporte.generar(casos, errores, tmp);

        assertTrue(Files.exists(tmp.resolve("resumen.txt")));
        assertTrue(Files.exists(tmp.resolve("resumen.csv")));
        assertTrue(Files.exists(tmp.resolve("errores.log")));

        List<String> csv = Files.readAllLines(tmp.resolve("resumen.csv"));
        assertEquals("idTest,nombreTest,estado,tiempoEjecucion", csv.get(0));
        assertEquals(3, csv.size());
    }

    @Test
    public void fallaSiOutDirEsArchivo() throws Exception {
        Path archivo = Files.createTempFile("noDir", ".txt");

        List<CasoPrueba> casos = Collections.emptyList();
        List<String> errores = Collections.emptyList();

        assertThrows(IOException.class,
                () -> GeneradorReporte.generar(casos, errores, archivo));
    }
}
