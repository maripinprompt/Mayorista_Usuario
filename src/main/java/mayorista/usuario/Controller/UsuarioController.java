package mayorista.usuario.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import mayorista.usuario.Service.UsuarioService;
import mayorista.usuario.Model.UsuarioModel;

import java.util.List;
import java.util.Map;

@RestController  //indica que esta clase maneja peticiones HTTP y retorna JSON
@RequestMapping("/usuarios")
public class UsuarioController {
    // inyectamos el service para usar la lógica de negocio
    private final UsuarioService usuarioService;
    // constructor — Spring inyecta el service automáticamente
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
     // GET /usuarios — retorna todos los usuarios
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioModel.class)))
    @GetMapping
    public ResponseEntity<List<UsuarioModel>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios()); // retorna 200 con la lista
    }
    // GET /usuarios/{id}  busca un usuario por su id
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve un usuario según su ID")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioModel> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok) //si existe retorna 200 con el usuario
                .orElseGet(() -> ResponseEntity.notFound().build()); // si no existe retorna 404
    }

    //GET /usuarios/buscar  busca un usuario por correo
    @Operation(summary = "Buscar usuario por correo electrónico", description = "Devuelve un usuario que coincide con el correo electrónico proporcionado")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/buscar")
    public ResponseEntity<UsuarioModel> buscarPorCorreo(@RequestParam String correo) {
        return usuarioService.buscarPorCorreo(correo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe en la base de datos"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody UsuarioModel usuario) {
          // @Valid activa las validaciones del Model (@NotBlank, @Email, etc.)
        // @RequestBody convierte el JSON que llega en un objeto UsuarioModel
        try {
            UsuarioModel usuarioCreado = usuarioService.crearUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (IllegalArgumentException e) {
            if ("El correo ya existe".equals(e.getMessage())) {
                 // si el correo ya existe retorna 409 Conflict
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("mensaje", e.getMessage()));
            }
              //cualquier otro error retorna 400 Bad Request
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario existente")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioModel usuario) {
        try {
            return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario existente del sistema")
    @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}