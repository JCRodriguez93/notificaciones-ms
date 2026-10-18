package es.scafftrix.notificacionesms.application.service;

import es.scafftrix.notificacionesms.application.port.in.NotificacionUseCase;
import es.scafftrix.notificacionesms.application.port.out.NotificacionRepository;
import es.scafftrix.notificacionesms.application.port.out.Notificador;
import es.scafftrix.notificacionesms.domain.exception.NotificacionNotFoundException;
import es.scafftrix.notificacionesms.domain.model.EstadoNotificacion;
import es.scafftrix.notificacionesms.domain.model.Notificacion;
import es.scafftrix.notificacionesms.domain.model.ResultadoEnvio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementacion del caso de uso de notificaciones.
 *
 * <p>Orquesta dos puertos de salida: la persistencia y el envio. No conoce HTTP, JPA ni el
 * proveedor de mensajes: todo eso lo aporta la infraestructura tras las interfaces.
 */
@Service
@Transactional
public class NotificacionService implements NotificacionUseCase {

    private final NotificacionRepository repository;
    private final Notificador notificador;

    public NotificacionService(NotificacionRepository repository, Notificador notificador) {
        this.repository = repository;
        this.notificador = notificador;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Notificacion findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotificacionNotFoundException(id));
    }

    /**
     * Registra primero la notificacion como PENDIENTE y solo despues intenta entregarla.
     *
     * <p>El orden importa: si el envio fallara de forma abrupta, el registro ya existe en base
     * de datos y se puede auditar o reintentar. El id lo asigna la base de datos.
     */
    @Override
    public Notificacion create(Notificacion notificacion) {
        Notificacion pendiente = repository.save(new Notificacion(null,
                notificacion.destinatario(), notificacion.asunto(), notificacion.cuerpo(),
                EstadoNotificacion.PENDIENTE, null));

        ResultadoEnvio resultado = notificador.enviar(
                pendiente.destinatario(), pendiente.asunto(), pendiente.cuerpo());

        EstadoNotificacion estado = resultado.exito()
                ? EstadoNotificacion.ENVIADO
                : EstadoNotificacion.FALLIDO;
        String motivoFallo = resultado.exito() ? null : resultado.detalle();

        return repository.save(new Notificacion(pendiente.id(), pendiente.destinatario(),
                pendiente.asunto(), pendiente.cuerpo(), estado, motivoFallo));
    }
}
