package hemen.go.controller.publicapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hemen.go.model.QrRequest;


@RestController
@RequestMapping("/api/public")
public class AreaAccessController {

    @PostMapping("/acceder")
    
    public ResponseEntity<String> procesarQr(@RequestBody QrRequest request) {
        String contenido = request.getQrData();

        // Aquí puedes procesar el contenido del QR
        System.out.println("Contenido del QR recibido: " + contenido);

        // Simulación de respuesta
        return ResponseEntity.ok("QR procesado correctamente: " + contenido);
    }
    
    
 // Nuevo método GET
    @GetMapping("/estado")
    public ResponseEntity<String> obtenerEstado() {
        // Simulación de estado
        String estado = "Última lectura: exitosa";
        return ResponseEntity.ok(estado);
    }
}
