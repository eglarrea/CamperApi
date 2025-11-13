package hemen.go.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(org.postgresql.util.PSQLException.class)
    public ResponseEntity<String> handlePSQLException(org.postgresql.util.PSQLException ex) {
        String mensaje = messageSource.getMessage(
                "error.database",                // clave definida en messages.properties
                new Object[]{ex.getMessage()},   // parámetros opcionales
                LocaleContextHolder.getLocale()  // idioma del cliente
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensaje);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrity(DataIntegrityViolationException ex) {
        String mensaje = messageSource.getMessage(
                "error.integrity",               // clave definida en messages.properties
                new Object[]{ex.getMessage()},   // parámetros opcionales
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(mensaje);
    }
}