package mayorista.usuario.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioModel.class)))
    @GetMapping
    public ResponseEntity<List<UsuarioModel>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }
    
    @Operation
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioModel> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    //buscar por correo electrónico
    @Operation(summary = "Buscar usuario por correo electrónico", description = "Devuelve un usuario que coincide con el correo electrónico proporcionado")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")

    @GetMapping("/buscar")
    public ResponseEntity<UsuarioModel> buscarPorCorreo(@RequestParam String correo) {
        return usuarioService.buscarPorCorreo(correo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    //crear usuario
   @Operation(summary = "Crear usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe en la base de datos"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody UsuarioModel usuario) {
        try {
            UsuarioModel usuarioCreado = usuarioService.crearUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (IllegalArgumentException e) {
            if ("El correo ya existe".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("mensaje", e.getMessage()));
            }
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    //actualizar usuario
    @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario existente")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")   

    @PutMapping("/{id}")
    public String putMethodName(@PathVariable Long id, @RequestBody UsuarioModel usuario) {
        //TODO: process PUT request
        usuarioService.actualizarUsuario(id, usuario);
        return "Usuario actualizado exitosamente";
    }
    
    //eliminar usuario
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario existente del sistema")
    @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")   

    @PostMapping("/eliminar/{id}")
    public String deleteMethodName(@PathVariable Long id) {


}