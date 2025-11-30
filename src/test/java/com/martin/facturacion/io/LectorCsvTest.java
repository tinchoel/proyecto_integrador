package com.martin.facturacion.io;

import com.martin.facturacion.modelo.CasoPrueba;
import com.martin.facturacion.excepciones.ExcepcionFormatoCsv;

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
                "3,Test C,SKIPPED,0.0");
        Files.write(tmp, lines);

        List<String> errores = new ArrayList<>();
        List<CasoPrueba> casos = LectorCsv.leer(tmp.toFile(), errores, false);

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
                "3,Test C,SKIPPED,notanumber");
        Files.write(tmp, lines);

        List<String> errores = new ArrayList<>();
        List<CasoPrueba> casos = LectorCsv.leer(tmp.toFile(), errores, false);

        assertEquals(1, casos.size());
        assertEquals(3, errores.size());

        Files.deleteIfExists(tmp);
    }

    // -------------------------------------------------------------------------
    // 🔵 NUEVAS PRUEBAS – ETAPA 3
    // -------------------------------------------------------------------------

    /** 🔵 Archivo inexistente → debe lanzar IOException */
    @Test
    public void archivoInexistenteLanzaIOException() {
        File file = new File("no-existe-12345.csv");
        List<String> errores = new ArrayList<>();

        assertThrows(IOException.class, () -> {
            LectorCsv.leer(file, errores, false);
        });
    }

    /** 🔵 Extensión incorrecta (.txt) → debe lanzar ExcepcionFormatoCsv */
    @Test
    public void extensionIncorrectaDebeFallar() throws Exception {
        Path tmp = Files.createTempFile("prueba", ".txt");
        List<String> errores = new ArrayList<>();

        assertThrows(ExcepcionFormatoCsv.class, () -> {
            LectorCsv.leer(tmp.toFile(), errores, false);
        });

        Files.deleteIfExists(tmp);
    }

    /** 🔵 Archivo con separador incorrecto (;) → genera errores */
    @Test
    public void separadorIncorrectoGeneraErrores() throws Exception {
        Path tmp = Files.createTempFile("tests", ".csv");
        List<String> lines = Arrays.asList(
                "1;Test A;PASSED;0.5",
                "2;Test B;FAILED;1.1");
        Files.write(tmp, lines);

        List<String> errores = new ArrayList<>();
        List<CasoPrueba> casos = LectorCsv.leer(tmp.toFile(), errores, false);

        assertEquals(0, casos.size());
        assertEquals(2, errores.size());

        Files.deleteIfExists(tmp);
    }

    /** 🔵 Archivo con solo errores → no debe producir casos válidos */
    @Test
    public void archivoSinCasosValidos() throws Exception {
        Path tmp = Files.createTempFile("tests", ".csv");
        List<String> lines = Arrays.asList(
                "bad,column",
                "1,Test,UNKNOWN,1.2",
                "2,Otro,FAILED,notnumber");
        Files.write(tmp, lines);

        List<String> errores = new ArrayList<>();
        List<CasoPrueba> casos = LectorCsv.leer(tmp.toFile(), errores, false);

        assertEquals(0, casos.size());
        assertEquals(3, errores.size());

        Files.deleteIfExists(tmp);
    }
}
