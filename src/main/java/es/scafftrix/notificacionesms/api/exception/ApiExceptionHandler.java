package es.scafftrix.notificacionesms.api.exception;

import es.scafftrix.notificacionesms.domain.exception.NotificacionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce las excepciones de dominio a respuestas HTTP.
 *
 * <p>Es la unica capa que conoce {@code ProblemDetail} (RFC 7807): asi el dominio y la
 * aplicacion no se acoplan al protocolo HTTP.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(NotificacionNotFoundException.class)
    public ProblemDetail handleNotFound(RuntimeException exception) {
        logger.warn("HTTP 404: {}", exception.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Recurso no encontrado");
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception) {
        String detail = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((left, right) -> left + "; " + right)
                .orElse("Peticion invalida");
        logger.warn("HTTP 400: {}", detail);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        problem.setTitle("Peticion no valida");
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception exception) {
        logger.error("Error interno no controlado: {}", exception.getMessage(), exception);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        problem.setTitle("Error interno");
        return problem;
    }
}
