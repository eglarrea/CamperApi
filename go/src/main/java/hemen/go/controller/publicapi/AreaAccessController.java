package hemen.go.controller.publicapi;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hemen.go.dto.request.TokenRequest;
import hemen.go.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST público encargado de gestionar el acceso a áreas mediante tokens JWT.
 * 
 * <p>Este controlador expone un endpoint bajo <code>/api/public/acceder</code> que valida
 * un token recibido desde el cliente (por ejemplo, escaneado desde un QR) y determina
 * si se permite la apertura de la puerta de acceso.</p>
 * 
 * <p>Características principales:</p>
 * <ul>
 *   <li>Valida el token JWT utilizando {@link JwtUtil}.</li>
 *   <li>Comprueba que el ID del parking en el token coincide con el enviado en la petición.</li>
 *   <li>Gestiona errores de token caducado o inválido mediante excepciones específicas.</li>
 *   <li>Internacionaliza los mensajes de respuesta usando {@link MessageSource} y el locale actual.</li>
 * </ul>
 * 
 * <p>Respuestas posibles:</p>
 * <ul>
 *   <li><b>200 OK</b>: Puerta abierta correctamente.</li>
 *   <li><b>401 Unauthorized</b>: Token caducado, inválido o parking no coincide.</li>
 * </ul>
 * 
 * @author 
 */
@RestController
@RequestMapping("/api/public")
@Tag(name="Acceso a parking")
public class AreaAccessController {

    /** Utilidad para validar y procesar tokens JWT. */
    private final JwtUtil jwtUtil;
    
    /** Fuente de mensajes para internacionalización (i18n). */
    private final MessageSource messageSource;
    
    /**
     * Constructor del controlador.
     * 
     * @param jwtUtil utilidad para validación de tokens JWT
     * @param messageSource fuente de mensajes para i18n
     */
    public AreaAccessController(JwtUtil jwtUtil, MessageSource messageSource) {
        this.jwtUtil = jwtUtil;
        this.messageSource = messageSource;
    }
    
    /**
     * Endpoint para validar un token de acceso y abrir la puerta si es correcto.
     * 
     * <p>Flujo de validación:</p>
     * <ol>
     *   <li>Se recibe un {@link TokenRequest} con el token y el ID del parking.</li>
     *   <li>Se valida el token con {@link JwtUtil#validarTokenPuerta(String)}.</li>
     *   <li>Se extrae el claim <code>idParking</code> del token.</li>
     *   <li>Si coincide con el ID enviado en la petición, se devuelve <b>200 OK</b> con mensaje de éxito.</li>
     *   <li>Si no coincide, se devuelve <b>401 Unauthorized</b> con mensaje de acceso denegado.</li>
     *   <li>Si el token está caducado, se devuelve <b>401 Unauthorized</b> con mensaje de token caducado.</li>
     *   <li>Si el token es inválido, se devuelve <b>401 Unauthorized</b> con mensaje de token inválido.</li>
     * </ol>
     * 
     * @param request objeto con el token y el ID del parking
     * @return respuesta HTTP con el resultado de la validación
     */
    @PostMapping("/acceder")
    @Operation(
            summary = "Acceso a parking ",
            description = "Cuando le lea el código Qr se llama a este enpoint para abrir la pureba "
                        
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Acceso permitido"),
            @ApiResponse(responseCode = "401", description = "Acceso no autorizado"),
        })
    public ResponseEntity<?> abrirPuerta(@RequestBody TokenRequest request) {
        try {
            Jws<Claims> claims = jwtUtil.validarTokenPuerta(request.getToken());

            Long idParking = claims.getBody().get("idParking", Long.class);
            if (idParking == request.getIdParking()) {
                return ResponseEntity.ok(messageSource.getMessage("message.ok.acceso", null, LocaleContextHolder.getLocale()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(messageSource.getMessage("message.error.acceso.token", null, LocaleContextHolder.getLocale()));
            }
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(messageSource.getMessage("message.error.acceso.token", null, LocaleContextHolder.getLocale()));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(messageSource.getMessage("message.error.acceso.token.invalido", null, LocaleContextHolder.getLocale()));
        }
    }
}
