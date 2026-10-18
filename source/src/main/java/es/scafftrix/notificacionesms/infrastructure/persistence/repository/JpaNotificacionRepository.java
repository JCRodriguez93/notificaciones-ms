package es.scafftrix.notificacionesms.infrastructure.persistence.repository;

import es.scafftrix.notificacionesms.infrastructure.persistence.entity.NotificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data. Solo habla de entidades JPA; el adaptador lo usa para
 * implementar el puerto de aplicacion.
 */
public interface JpaNotificacionRepository extends JpaRepository<NotificacionEntity, Long> {
}
