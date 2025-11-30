package com.martin.facturacion;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class AplicacionPrincipalTest {

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @BeforeEach
    public void setup() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restore() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void errorCuandoArchivoNoExiste() {
        String archivoInexistente = "no_existe_123.csv";
        String outDir = "salida_test";

        AplicacionPrincipal.main(new String[] { archivoInexistente, outDir });

        String error = errContent.toString();
        assertTrue(error.contains("ERROR: El archivo CSV no existe"));
    }

    @Test
    public void errorExtensionInvalida() throws Exception {
        Path tmpFile = Files.createTempFile("archivo", ".txt");
        Path outDir = Files.createTempDirectory("salida");

        AplicacionPrincipal.main(new String[] { tmpFile.toString(), outDir.toString() });

        String error = errContent.toString();
        assertTrue(error.contains("ERROR: Formato de archivo inválido"));
    }

    @Test
    public void ejecutaCorrectamenteConCsvValido() throws Exception {
        Path csv = Files.createTempFile("pruebas", ".csv");
        Path outDir = Files.createTempDirectory("salida");

        Files.writeString(csv,
                "idTest,nombreTest,estado,tiempoEjecucion\n" +
                        "T1,Login,PASSED,1.5\n");

        AplicacionPrincipal.main(new String[] {
                csv.toString(),
                outDir.toString(),
                "--ignorar-cabecera"
        });

        String salida = outContent.toString();

        assertTrue(salida.contains("Reporte generado en"));
        assertTrue(Files.exists(outDir.resolve("resumen.txt")));
        assertTrue(Files.exists(outDir.resolve("resumen.csv")));
        assertTrue(Files.exists(outDir.resolve("errores.log")));
    }

    @Test
    public void ignoraCabeceraCuandoCorresponde() throws Exception {
        Path csv = Files.createTempFile("pruebas", ".csv");
        Path outDir = Files.createTempDirectory("salida");

        Files.writeString(csv,
                "cabecera1,cabecera2,cabecera3,cabecera4\n" +
                        "T1,Login,PASSED,1.0\n");

        AplicacionPrincipal.main(new String[] {
                csv.toString(),
                outDir.toString(),
                "--ignorar-cabecera"
        });

        // Si la cabecera fue ignorada, debe haber 1 solo caso procesado
        String resumen = Files.readString(outDir.resolve("resumen.csv"));
        long lineas = resumen.lines().count();

        assertEquals(2, lineas); // 1 cabecera + 1 caso válido
    }

    @Test
    public void errorSiOutEsArchivoEnLugarDeDirectorio() throws Exception {
        Path csv = Files.createTempFile("pruebas", ".csv");
        Path archivoSalida = Files.createTempFile("noDirector", ".txt");

        Files.writeString(csv, "T1,Login,PASSED,1.0");

        AplicacionPrincipal.main(new String[] {
                csv.toString(),
                archivoSalida.toString()
        });

        String error = errContent.toString();

        assertTrue(error.contains("ERROR: No se pudo leer el archivo CSV")
                || error.contains("Error de lectura"));
    }
}
