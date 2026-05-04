package mayorista.usuario.Service;

import mayorista.usuario.Model.UsuarioModel;
import mayorista.usuario.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioModel> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<UsuarioModel> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

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
        usuario.setNombre(datos.getNombre());
        usuario.setCorreo(datos.getCorreo());
        usuario.setContrasena(datos.getContrasena());
        usuario.setTelefono(datos.getTelefono());
        usuario.setDireccion(datos.getDireccion());
        usuario.setRol(datos.getRol());
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}