package mayorista.usuario.Repository;

import mayorista.usuario.Model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository //marca esta interfaz como componente de acceso a datos
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    Optional<UsuarioModel> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}