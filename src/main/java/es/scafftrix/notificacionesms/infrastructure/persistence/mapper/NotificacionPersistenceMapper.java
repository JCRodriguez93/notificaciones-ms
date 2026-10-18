package es.scafftrix.notificacionesms.infrastructure.persistence.mapper;

import es.scafftrix.notificacionesms.domain.model.EstadoNotificacion;
import es.scafftrix.notificacionesms.domain.model.Notificacion;
import es.scafftrix.notificacionesms.infrastructure.persistence.entity.NotificacionEntity;
import org.mapstruct.Mapper;

/**
 * Traduce entre la entidad JPA y el modelo de dominio. MapStruct genera la implementacion.
 *
 * <p>{@code EstadoNotificacion} (enum) y {@code estado} (cadena) se convierten solos:
 * MapStruct usa {@code name()} al guardar y {@code valueOf()} al leer.
 */
@Mapper(componentModel = "spring")
public interface NotificacionPersistenceMapper {

    Notificacion toDomain(NotificacionEntity entity);

    NotificacionEntity toEntity(Notificacion notificacion);

    String toEstadoString(EstadoNotificacion estado);

    EstadoNotificacion toEstadoEnum(String estado);
}
