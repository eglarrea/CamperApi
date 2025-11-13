package hemen.go.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Clase de configuración para la internacionalización (i18n) en la aplicación Hemengo.
 *
 * Objetivo:
 *  - Permitir que los mensajes de validación y otros textos se gestionen
 *    desde archivos de propiedades internacionalizados (messages.properties).
 *  - Soportar múltiples idiomas (ej. español, inglés, euskera).
 *  - Garantizar que se usen caracteres especiales y acentos correctamente
 *    gracias a la codificación UTF-8.
 */
@Configuration
public class InternationalizationConfig {

    /**
     * Bean encargado de cargar los mensajes internacionalizados.
     *
     * - Usa ReloadableResourceBundleMessageSource para leer los archivos de mensajes.
     * - Basename "classpath:messages" significa que buscará:
     *      messages.properties (idioma por defecto)
     *      messages_en.properties (inglés)
     *      messages_eu.properties (euskera)
     * - Se configura UTF-8 para evitar problemas con acentos y caracteres especiales.
     *
     * @return instancia de MessageSource configurada para i18n.
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Bean que conecta el sistema de validación con los mensajes internacionalizados.
     *
     * - LocalValidatorFactoryBean es el puente entre Hibernate Validator y Spring.
     * - Se le indica que use el MessageSource definido arriba.
     * - Así, cuando se usan anotaciones como:
     *      @NotBlank(message = "{user.name.required}")
     *   Spring resolverá el mensaje correcto según el idioma del cliente.
     *
     * @return instancia de LocalValidatorFactoryBean con soporte de i18n.
     */
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
