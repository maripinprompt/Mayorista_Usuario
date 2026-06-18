package mayorista.usuario;

import mayorista.usuario.Model.UsuarioModel;
import mayorista.usuario.Repository.UsuarioRepository;
import mayorista.usuario.Service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // activa Mockito para simular el repository sin usar la base de datos real
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository; // simulamos el repository

    @InjectMocks
    private UsuarioService usuarioService; // inyectamos el mock en el service

    private UsuarioModel usuario;

    @BeforeEach // se ejecuta antes de cada prueba para preparar los datos
    void setUp() {
        usuario = new UsuarioModel();
        usuario.setNombre("Juan Perez");
        usuario.setCorreo("juan@correo.com");
        usuario.setContrasena("1234");
        usuario.setTelefono("912345678");
        usuario.setDireccion("Valparaiso");
        usuario.setRol("cliente");
    }

    @Test // prueba que getAllUsuarios retorna la lista correctamente
    void getAllUsuarios_retornaLista() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        List<UsuarioModel> resultado = usuarioService.getAllUsuarios();

        assertEquals(1, resultado.size()); // verifica que retorna 1 usuario
        verify(usuarioRepository, times(1)).findAll(); // verifica que se llamo al repository
    }

    @Test // prueba que obtenerPorId retorna el usuario cuando existe
    void obtenerPorId_cuandoExiste_retornaUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<UsuarioModel> resultado = usuarioService.obtenerPorId(1L);

        assertTrue(resultado.isPresent()); // verifica que el usuario existe
        assertEquals("Juan Perez", resultado.get().getNombre());
    }

    @Test // prueba que obtenerPorId retorna vacio cuando no existe
    void obtenerPorId_cuandoNoExiste_retornaVacio() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UsuarioModel> resultado = usuarioService.obtenerPorId(99L);

        assertFalse(resultado.isPresent()); // verifica que no existe el usuario
    }

    @Test // prueba que buscarPorCorreo retorna el usuario cuando existe
    void buscarPorCorreo_cuandoExiste_retornaUsuario() {
        when(usuarioRepository.findByCorreo("juan@correo.com")).thenReturn(Optional.of(usuario));

        Optional<UsuarioModel> resultado = usuarioService.buscarPorCorreo("juan@correo.com");

        assertTrue(resultado.isPresent());
        assertEquals("juan@correo.com", resultado.get().getCorreo());
    }

    @Test // prueba que crearUsuario lanza excepcion cuando el correo ya existe
    void crearUsuario_correoYaExiste_lanzaExcepcion() {
        when(usuarioRepository.existsByCorreo("juan@correo.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearUsuario(usuario);
        }); // verifica que lanza excepcion si el correo ya esta registrado
    }

    @Test // prueba que crearUsuario funciona correctamente cuando el correo no existe
    void crearUsuario_correoNoExiste_creaCorrectamente() {
        when(usuarioRepository.existsByCorreo("juan@correo.com")).thenReturn(false);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        UsuarioModel resultado = usuarioService.crearUsuario(usuario);

        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombre());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test // prueba que actualizarUsuario lanza excepcion cuando no existe
    void actualizarUsuario_cuandoNoExiste_lanzaExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.actualizarUsuario(99L, usuario);
        }); // verifica que lanza excepcion cuando el usuario no existe
    }

    @Test // prueba que eliminarUsuario lanza excepcion cuando no existe
    void eliminarUsuario_cuandoNoExiste_lanzaExcepcion() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.eliminarUsuario(99L);
        }); // verifica que lanza excepcion cuando el usuario no existe
    }

    @Test // prueba que eliminarUsuario funciona correctamente cuando existe
    void eliminarUsuario_cuandoExiste_eliminaCorrectamente() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        assertDoesNotThrow(() -> {
            usuarioService.eliminarUsuario(1L);
        });

        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}