package hemen.go;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Clase principal de la aplicación Spring Boot "Hemengo".
 *
 * - Se encarga de inicializar el contexto de Spring Boot.
 * - Carga variables de entorno desde un archivo `.env` durante el desarrollo.
 * - Configura propiedades del sistema para la conexión a la base de datos.
 */
@SpringBootApplication
public class HemengoApplication {

    /**
     * Método main: punto de entrada de la aplicación.
     *
     * Pasos principales:
     * 1. Carga el archivo `.env` usando la librería dotenv-java.
     *    - Dotenv.load() busca un archivo `.env` en el directorio raíz del proyecto.
     *    - Extrae las variables definidas (ej. DB_URL, DB_USERNAME, DB_PASSWORD).
     *
     * 2. Asigna estas variables como propiedades del sistema:
     *    - System.setProperty("DB_URL", dotenv.get("DB_URL"));
     *    - System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
     *    - System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
     *
     *    Esto permite que Spring Boot y otros componentes (como JPA o JDBC)
     *    puedan acceder a la configuración de la base de datos sin exponer
     *    credenciales directamente en `application.properties`.
     *
     * 3. Lanza la aplicación Spring Boot:
     *    - SpringApplication.run(HemengoApplication.class, args);
     *    - Inicializa el contexto de Spring, escanea componentes y arranca el servidor embebido (Tomcat por defecto).
     *
     * @param args argumentos de línea de comandos (opcional).
     */
    public static void main(String[] args) {
        // Para desarrollo: carga variables desde .env
        Dotenv dotenv = Dotenv.load();

        // Configuración de propiedades del sistema para la base de datos
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

        // Arranca la aplicación Spring Boot
        SpringApplication.run(HemengoApplication.class, args);
    }
}
