package es.scafftrix.notificacionesms.application.port.in;

import es.scafftrix.notificacionesms.domain.model.Notificacion;

import java.util.List;

/**
 * Puerto de entrada (caso de uso) de notificaciones.
 *
 * <p>La capa api depende de esta interfaz y no de la implementacion, de modo que el
 * framework HTTP y el dominio no se conocen entre si.
 */
public interface NotificacionUseCase {

    List<Notificacion> findAll();

    Notificacion findById(Long id);

    Notificacion create(Notificacion notificacion);
}
