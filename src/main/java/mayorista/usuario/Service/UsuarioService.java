package mayorista.usuario.Service;

import mayorista.usuario.Model.UsuarioModel;
import mayorista.usuario.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service //marca esta clase como componente de lógica de negocio
public class UsuarioService {
    //inyectamos el repository para poder acceder a la base de datos
    private final UsuarioRepository usuarioRepository;
    // constructor — Spring inyecta el repository automáticamente
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
     //retorna todos los usuarios de la base de datos
    public List<UsuarioModel> getAllUsuarios() {
        return usuarioRepository.findAll();
    }
    //busca un usuario por su id, retorna Optional porque puede no existir
    public Optional<UsuarioModel> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    //busca un usuario por su correo, retorna Optional porque puede no existir
    public Optional<UsuarioModel> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public UsuarioModel crearUsuario(UsuarioModel usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("El correo ya existe");
        }
        return usuarioRepository.save(usuario);
    }

    public UsuarioModel actualizarUsuario(Long id, UsuarioModel datos) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        //reemplaza los datos viejos con los nuevos
        usuario.setNombre(datos.getNombre());
        usuario.setCorreo(datos.getCorreo());
        usuario.setContrasena(datos.getContrasena());
        usuario.setTelefono(datos.getTelefono());
        usuario.setDireccion(datos.getDireccion());
        usuario.setRol(datos.getRol());
        // guarda el usuario actualizado en la base de datos
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}