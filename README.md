# 📊 Proyecto Integrador: Sistema de Reporte QA

Herramienta de procesamiento en Java diseñada para leer y validar resultados de casos de prueba desde un archivo CSV, generando un reporte final en formato de texto y CSV, además de un log de errores.

---

## 🚀 Requisitos y Tecnologías

El proyecto fue desarrollado utilizando el ecosistema Java y Maven.

| Tecnología | Versión | Propósito |
| :--- | :--- | :--- |
| **Java** | 18 | Lenguaje de programación principal. |
| **Maven** | 3+ | Herramienta de gestión de dependencias y *build* (compilación). |
| **SLF4J / Logback** | 2.0.9 / 1.4.11 | API y librería para el registro de eventos y logs. |
| **iText** | 5.5.13.3 | Generación de reportes en formato PDF (si aplica). |
| **JUnit** | 5.10.0 | Pruebas unitarias. |

---

## 🛠️ Instalación y Compilación

Para compilar el proyecto y generar el archivo JAR ejecutable (*fat jar*), clona el repositorio y ejecuta el comando de *build* de Maven:

1. Clona el repositorio:
   ```bash
   git clone [https://github.com/tinchoel/proyecto_integrador.git](https://github.com/tinchoel/proyecto_integrador.git)
   cd proyecto_integrador
   
2. Compila el proyecto (esto generará el archivo proyecto_integrador-1.0-SNAPSHOT.jar dentro de la carpeta target/): mvn clean install   



📋 Uso del Programa
El programa se ejecuta a través de la línea de comandos, requiriendo la ruta del archivo de entrada y el directorio de salida.

Formato de Ejecución
Asegúrate de ejecutar el comando desde la raíz del proyecto (proyecto_integrador).

java -jar target/proyecto_integrador-1.0-SNAPSHOT.jar <ruta_csv> <out_dir> [bandera]

Argumentos	

Argumento		      		Descripción									                          					Ejemplo
<ruta_csv>		      	Ruta del archivo CSV de entrada con los casos de prueba.		    tests.csv
<out_dir>				      Ruta del directorio donde se guardarán los reportes generados.	out/
[--ignorar-cabecera]	(Opcional) Bandera para omitir la primera línea del CSV.      	--ignorar-cabecera

Ejemplo Completo

java -jar target/proyecto_integrador-1.0-SNAPSHOT.jar tests.csv out/ --ignorar-cabecera


💾 Estructura del CSV de Entrada
El archivo de entrada (tests.csv) debe seguir estrictamente la siguiente estructura de 4 columnas, separadas por comas (,).

Columna			  	  Tipo de Dato		Valores Válidos				  Descripción
idTest				    String			  	Alfanumérico				    Identificador único del caso de prueba.
nombreTest		    String				  Texto						        Descripción del caso de prueba.
estado				    String				  PASSED, FAILED, SKIPPED	Resultado final de la ejecución.
tiempoEjecucion		Double				  Numérico					      Tiempo de ejecución en segundos (ej. 1.25).


Ejemplo de Contenido

idTest,nombreTest,estado,tiempoEjecucion
TC_001,Validar login,PASSED,0.342
TC_002,Facturacion masiva,FAILED,2.15
TC_003,Chequeo de logs,PASSED,0.01


📝 Archivos de Salida Generados
El programa genera los siguientes archivos dentro del directorio de salida (out/):

resumen.txt: Reporte legible con estadísticas de pruebas (total de casos, aprobados, fallidos, etc.).

resumen.csv: Versión CSV del reporte final para procesamiento.

errores.log: Registro de las líneas inválidas encontradas durante la lectura del archivo de entrada.
