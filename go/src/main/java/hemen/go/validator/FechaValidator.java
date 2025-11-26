package hemen.go.validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
@Component
public class FechaValidator {

    private final MessageSource messageSource;

    public FechaValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void validarFechas(LocalDate fechaDesde, LocalDate fechaHasta) {
        LocalDate hoy = LocalDate.now();

        // Validación: fechaDesde no puede ser anterior a hoy
        if (fechaDesde != null && fechaDesde.isBefore(hoy)) {
            throw new IllegalArgumentException(
                messageSource.getMessage("error.fecha.anterior.actual", null, LocaleContextHolder.getLocale()));
        }

        // Validación: fechaHasta no puede ser anterior a hoy ni a fechaDesde
        if (fechaHasta != null) {
            if (fechaHasta.isBefore(hoy)) {
                throw new IllegalArgumentException(
                    messageSource.getMessage("error.fecha.anterior.actual", null, LocaleContextHolder.getLocale()));
            }
            if (fechaDesde != null && fechaHasta.isBefore(fechaDesde)) {
                throw new IllegalArgumentException(
                    messageSource.getMessage("error.fecha.fin.anterior.inicio", null, LocaleContextHolder.getLocale()));
            }
        }
    }
    
    public void validarFechas(Date fechaDesde, Date fechaHasta) {
        validarFechas(toLocalDate(fechaDesde), toLocalDate(fechaHasta));
    }

    private LocalDate toLocalDate(Date date) {
        return date == null ? null :
               date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
