package es.scafftrix.notificacionesms.api.mapper;

import es.scafftrix.notificacionesms.api.model.Notificacion;
import es.scafftrix.notificacionesms.api.model.Notificacion.EstadoEnum;
import es.scafftrix.notificacionesms.domain.model.EstadoNotificacion;
import org.mapstruct.Mapper;

/**
 * Traduce entre el modelo generado por OpenAPI y el modelo de dominio.
 *
 * <p>Vive en la capa api a proposito: es la frontera la que conoce ambos mundos, ni el
 * dominio conoce OpenAPI ni el contrato conoce el dominio.
 *
 * <p>El estado es un enum en ambos lados pero de distinto tipo ({@code EstadoEnum} lo genera
 * OpenAPI a partir del contrato, {@code EstadoNotificacion} es el del dominio): los dos
 * metodos de abajo le dicen a MapStruct como cruzarlos en cada sentido.
 */
@Mapper(componentModel = "spring")
public interface NotificacionApiMapper {

    Notificacion toApi(es.scafftrix.notificacionesms.domain.model.Notificacion notificacion);

    es.scafftrix.notificacionesms.domain.model.Notificacion toDomain(Notificacion notificacion);

    EstadoEnum toEstadoApi(EstadoNotificacion estado);

    EstadoNotificacion toEstadoDomain(EstadoEnum estado);
}
