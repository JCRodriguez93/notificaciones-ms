package es.scafftrix.notificacionesms.application.port.out;

import es.scafftrix.notificacionesms.domain.model.Notificacion;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida de persistencia de notificaciones.
 *
 * <p>Habla en terminos del modelo de dominio, no de entidades JPA. La implementacion real
 * vive en {@code infrastructure.persistence.adapter}.
 */
public interface NotificacionRepository {

    List<Notificacion> findAll();

    Optional<Notificacion> findById(Long id);

    Notificacion save(Notificacion notificacion);
}
