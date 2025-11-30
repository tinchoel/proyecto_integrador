# 📊 Proyecto Integrador – Análisis de Casos de Prueba (CSV)

Este proyecto implementa una aplicación en **Java 18 + Maven** que permite analizar un archivo CSV con resultados de casos de prueba (test cases), generar reportes automáticos y ofrecer un **menú interactivo en consola** para explorar las estadísticas.

Incluye:
- ✔ Lectura y validación de archivos CSV  
- ✔ Manejo de errores detallado  
- ✔ Generación automática de reportes  
- ✔ Estadísticas completas de ejecución  
- ✔ Menú interactivo de análisis  
- ✔ Test unitarios (JUnit 5)  
- ✔ Arquitectura orientada a servicios (SRP / Clean-ish)  

---

## 🏗️ Tecnologías utilizadas

- **Java 18**
- **Maven**
- **JUnit 5**
- **SLF4J + Logback**
- **Paradigma: POO + separación de capas**

---

## 📁 Estructura del proyecto

```
/src
 ├── main/java/com/martin/facturacion
 │     ├── AplicacionPrincipal.java
 │     ├── MenuConsola.java
 │     ├── io/
 │     │     ├── LectorCsv.java
 │     │     └── GeneradorReporte.java
 │     ├── modelo/
 │     │     ├── CasoPrueba.java
 │     │     └── EstadoPrueba.java
 │     └── servicio/
 │           └── EstadisticasPruebas.java
 └── test/java/com/martin/facturacion
       └── AplicacionPrincipalTest.java
```

---

## ▶️ Ejecución del programa

Una vez compilado:

```bash
mvn clean package
```

Ejecutar la aplicación:

```bash
java -jar target/proyecto_integrador-1.0-SNAPSHOT.jar datos.csv salida --ignorar-cabecera
```

Parámetros:
- `<ruta_csv>` → archivo CSV de entrada  
- `<out_dir>` → carpeta donde se generarán los reportes  
- `--ignorar-cabecera` opcional → omite la primera línea del CSV  

---

## 📄 Formato del CSV

```
idTest,nombreTest,estado,tiempoEjecucion
T1,Login,PASSED,1.5
T2,LoginInvalido,FAILED,2.0
T3,Home,SKIPPED,0.5
```

---

## 🧪 Test Unitarios (JUnit 5)

El proyecto incluye pruebas unitarias para validar:

- Archivo inexistente
- Extensión inválida
- CSV válido (generación de reportes)
- Manejo del flag `--ignorar-cabecera`
- Error cuando el output es un archivo
- Validación de contenido generado

Para ejecutarlos:

```bash
mvn test
```

---

## 📊 Funcionalidades del menú interactivo

Luego de generar los reportes, aparece un menú con opciones:

**1. Ver estadísticas generales**
- Total de casos
- Conteo por estado
- Promedios
- Caso más lento

**2. Buscar caso por ID**

**3. Filtrar casos por estado**

**4. Exportar resultados a un nuevo archivo**

**0. Salir**

---

## 🧱 Arquitectura del proyecto

La aplicación sigue **responsabilidades separadas**:

- **AplicacionPrincipal** → entrada y validación inicial  
- **LectorCsv** → parsing, validación y carga  
- **GeneradorReporte** → creación de archivos (txt, csv, log)  
- **EstadisticasPruebas** → cálculos y métricas  
- **MenuConsola** → interacción con el usuario  
- **Modelo** → representación limpia de datos  

---

## 🚀 Mejoras futuras (TODO)

- Exportación JSON de estadísticas  
- Manejo de múltiples archivos CSV  
- Soporte para colores ANSI en el menú  
- Integración con base de datos  
- Web UI con Spring Boot  

---

## 🧑‍💻 Autor

**Martín Aguirre**  
Proyecto integrador – Curso Alkemy 2025  

---

