package mayorista.usuario.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data                   // genera getters y setters automáticamente
@NoArgsConstructor      // constructor vacío
@AllArgsConstructor     // constructor con todos los campos
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @Email
    @NotBlank
    @Column(unique = true)
    private String correo;

    @NotBlank
    private String contrasena;

    private String telefono;
    private String direccion;
    private String rol;
}