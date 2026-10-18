package es.scafftrix.notificacionesms.domain.exception;

/**
 * Se lanza cuando se pide una notificacion que no existe. Al vivir en el dominio, la capa de
 * aplicacion puede comunicar el fallo sin conocer el framework web.
 */
public class NotificacionNotFoundException extends RuntimeException {

    public NotificacionNotFoundException(Long id) {
        super("Notificacion " + id + " no encontrada");
    }
}
