package hemen.go;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.validation.Validator;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Clase principal de la aplicación Spring Boot "Hemengo".
 *
 * - Se encarga de inicializar el contexto de Spring Boot.
 * - Carga variables de entorno desde un archivo `.env` durante el desarrollo.
 * - Configura propiedades del sistema para la conexión a la base de datos.
 */
@SpringBootApplication
public class HemengoApplication implements WebMvcConfigurer {
	/*private final LocalValidatorFactoryBean validator;
	
	public HemengoApplication(LocalValidatorFactoryBean validator) {
        this.validator = validator;
    }*/
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
    	// Cargar dotenv en local, ignorar si no existe (producción)
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // Para cada variable, primero intenta leer de .env, si no existe usa System.getenv
        String dbUrl = dotenv.get("DB_URL", System.getenv("DB_URL"));
        String dbUsername = dotenv.get("DB_USERNAME", System.getenv("DB_USERNAME"));
        String dbPassword = dotenv.get("DB_PASSWORD", System.getenv("DB_PASSWORD"));

        // Configuración de propiedades del sistema para la base de datos
        System.setProperty("DB_URL", dbUrl);
        System.setProperty("DB_USERNAME", dbUsername);
        System.setProperty("DB_PASSWORD", dbPassword);

        // Arranca la aplicación Spring Boot
        SpringApplication.run(HemengoApplication.class, args);
    }
    
/*    @Override
    public Validator getValidator() {
        return validator;
    }*/
}
