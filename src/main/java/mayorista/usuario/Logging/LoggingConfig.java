package mayorista.usuario.Logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect //esta clase "espía" los métodos del Service sin modificarlos
@Component //spring la encuentra y la activa automáticamente
public class LoggingConfig {

    // este objeto es el que escribe los mensajes en la consola
    private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);

    //este método se activa justo ANTES de ejecutar cualquier método del Service
    //por ejemplo, antes de crearUsuario, antes de eliminarUsuario, etc.
    @Before("execution(* mayorista.usuario.Service.*.*(..))")
    public void logAntes(JoinPoint joinPoint) {
        //escribe en consola qué método está a punto de ejecutarse
        //ejemplo: "Ejecutando método: crearUsuario"
        logger.info("Ejecutando método: " + joinPoint.getSignature().getName());
    }

    //este método se activa justo DESPUÉS de que el método del Service terminó bien
    //si el método falla con un error, este NO se ejecuta
    @AfterReturning(pointcut = "execution(* mayorista.usuario.Service.*.*(..))", returning = "resultado")
    public void logDespues(JoinPoint joinPoint, Object resultado) {
        //escribe en consola qué método acaba de terminar
        //ejemplo: "Método finalizado: crearUsuario"
        logger.info("Método finalizado: " + joinPoint.getSignature().getName());
    }
}