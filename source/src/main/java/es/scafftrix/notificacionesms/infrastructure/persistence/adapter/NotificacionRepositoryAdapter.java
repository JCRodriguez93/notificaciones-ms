package es.scafftrix.notificacionesms.infrastructure.persistence.adapter;

import es.scafftrix.notificacionesms.application.port.out.NotificacionRepository;
import es.scafftrix.notificacionesms.domain.model.Notificacion;
import es.scafftrix.notificacionesms.infrastructure.persistence.entity.NotificacionEntity;
import es.scafftrix.notificacionesms.infrastructure.persistence.mapper.NotificacionPersistenceMapper;
import es.scafftrix.notificacionesms.infrastructure.persistence.repository.JpaNotificacionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa el puerto {@link NotificacionRepository} con Spring Data + JPA.
 *
 * <p>Es el unico punto donde se cruzan dominio y persistencia: traduce con MapStruct y
 * delega en el repositorio JPA.
 */
@Component
public class NotificacionRepositoryAdapter implements NotificacionRepository {

    private final JpaNotificacionRepository jpaNotificacionRepository;
    private final NotificacionPersistenceMapper mapper;

    public NotificacionRepositoryAdapter(JpaNotificacionRepository jpaNotificacionRepository,
                                         NotificacionPersistenceMapper mapper) {
        this.jpaNotificacionRepository = jpaNotificacionRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Notificacion> findAll() {
        return jpaNotificacionRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Notificacion> findById(Long id) {
        return jpaNotificacionRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Notificacion save(Notificacion notificacion) {
        NotificacionEntity saved = jpaNotificacionRepository.save(mapper.toEntity(notificacion));
        return mapper.toDomain(saved);
    }
}
