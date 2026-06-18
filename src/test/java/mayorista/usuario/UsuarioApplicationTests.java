package mayorista.usuario;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// se usa el profile "test" para que use una base de datos en memoria en vez de la real
@SpringBootTest
@ActiveProfiles("test")
class UsuarioApplicationTests {

	@Test
	void contextLoads() {
	}

}