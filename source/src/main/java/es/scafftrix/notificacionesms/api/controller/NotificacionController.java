package es.scafftrix.notificacionesms.api.controller;

import es.scafftrix.notificacionesms.api.mapper.NotificacionApiMapper;
import es.scafftrix.notificacionesms.api.model.Notificacion;
import es.scafftrix.notificacionesms.api.NotificacionesApi;
import es.scafftrix.notificacionesms.application.port.in.NotificacionUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Implementa la interfaz generada a partir del contrato OpenAPI.
 *
 * <p>El contrato define la superficie HTTP; esta clase solo conecta esa superficie con el
 * caso de uso y traduce con MapStruct. No contiene logica de negocio.
 */
@RestController
public class NotificacionController implements NotificacionesApi {

    private final NotificacionUseCase notificacionUseCase;
    private final NotificacionApiMapper mapper;

    public NotificacionController(NotificacionUseCase notificacionUseCase, NotificacionApiMapper mapper) {
        this.notificacionUseCase = notificacionUseCase;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<List<Notificacion>> getAllNotificaciones() {
        return ResponseEntity.ok(notificacionUseCase.findAll().stream().map(mapper::toApi).toList());
    }

    @Override
    public ResponseEntity<Notificacion> getNotificacion(Long id) {
        return ResponseEntity.ok(mapper.toApi(notificacionUseCase.findById(id)));
    }

    @Override
    public ResponseEntity<Notificacion> createNotificacion(Notificacion notificacion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toApi(notificacionUseCase.create(mapper.toDomain(notificacion))));
    }
}
