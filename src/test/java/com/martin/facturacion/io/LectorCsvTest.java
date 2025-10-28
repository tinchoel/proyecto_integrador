package com.martin.facturacion.io;

import com.martin.facturacion.modelo.CasoPrueba;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class LectorCsvTest {

    @Test
    public void leeCsvValido() throws Exception {
        Path tmp = Files.createTempFile("tests", ".csv");
        List<String> lines = Arrays.asList(
            "1,Test A,PASSED,0.5",
            "2,Test B,FAILED,1.2",
            "3,Test C,SKIPPED,0.0"
        );
        Files.write(tmp, lines);
        List<String> errores = new ArrayList<>();
        java.util.List<CasoPrueba> casos = LectorCsv.leer(tmp.toFile(), errores, false);
        assertEquals(3, casos.size());
        assertTrue(errores.isEmpty());
        Files.deleteIfExists(tmp);
    }

    @Test
    public void coleccionaErroresPorLineasInvalidas() throws Exception {
        Path tmp = Files.createTempFile("tests", ".csv");
        List<String> lines = Arrays.asList(
            "1,Test A,PASSED,0.5",
            "bad,line,here",
            "2,Test B,UNKNOWN,1.2",
            "3,Test C,SKIPPED,notanumber"
        );
        Files.write(tmp, lines);
        List<String> errores = new ArrayList<>();
        java.util.List<CasoPrueba> casos = LectorCsv.leer(tmp.toFile(), errores, false);
        assertEquals(1, casos.size());
        assertEquals(3, errores.size());
        Files.deleteIfExists(tmp);
    }
}
