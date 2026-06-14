package mayorista.usuario.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // intercepta todos los errores de todos los controllers automaticamente
public class GlobalExceptionHandler {

    // captura errores de validacion (@NotBlank, @Email, etc.)
    // se activa cuando llegan datos invalidos como un correo sin @ o un campo vacio
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        // recorre todos los campos con error y los agrega al mapa
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errores.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores); // retorna 400
    }

    // captura errores de logica de negocio
    // se activa cuando el Service lanza un error como "El correo ya existe" o "Usuario no encontrado"
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("mensaje", ex.getMessage())); // retorna 409
    }

    // captura cualquier otro error inesperado y muestra el mensaje real para poder identificarlo
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        ex.printStackTrace(); // imprime el error completo en los logs
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("mensaje", ex.getMessage() != null ? ex.getMessage() : "Error interno del servidor")); // retorna 500
    }
}