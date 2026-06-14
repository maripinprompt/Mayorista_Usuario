package mayorista.usuario.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore // el id lo genera MySQL automaticamente, no se acepta ni se muestra en el JSON
    private Long id;

    @NotBlank // valida que el nombre no venga vacio
    private String nombre;

    @Email // valida que tenga formato de correo
    @NotBlank // valida que no venga vacio
    @Column(unique = true) // no pueden existir dos usuarios con el mismo correo
    private String correo;

    @NotBlank // valida que la contrasena no venga vacia
    private String contrasena;

    private String telefono;
    private String direccion;
    private String rol;
}