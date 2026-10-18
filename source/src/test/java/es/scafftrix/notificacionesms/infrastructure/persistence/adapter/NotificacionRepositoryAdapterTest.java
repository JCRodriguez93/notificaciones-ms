package es.scafftrix.notificacionesms.infrastructure.persistence.adapter;

import es.scafftrix.notificacionesms.application.port.out.NotificacionRepository;
import es.scafftrix.notificacionesms.domain.model.EstadoNotificacion;
import es.scafftrix.notificacionesms.domain.model.Notificacion;
import es.scafftrix.notificacionesms.infrastructure.persistence.mapper.NotificacionPersistenceMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba el adaptador contra una base de datos real (H2) con las migraciones Flyway
 * aplicadas: es la prueba que demuestra que la persistencia funciona de punta a punta.
 */
@DataJpaTest
@Import({NotificacionRepositoryAdapter.class, NotificacionPersistenceMapperImpl.class})
class NotificacionRepositoryAdapterTest {

    @Autowired
    private NotificacionRepository adapter;

    @Test
    void savesAndReadsBackThroughThePort() {
        Notificacion saved = adapter.save(new Notificacion(null,
                "ana@example.com", "Bienvenida", "Bienvenido a la plataforma.",
                EstadoNotificacion.PENDIENTE, null));

        assertThat(saved.id()).isNotNull();
        Optional<Notificacion> found = adapter.findById(saved.id());
        assertThat(found).isPresent();
        assertThat(found.get().destinatario()).isEqualTo("ana@example.com");
        assertThat(found.get().estado()).isEqualTo(EstadoNotificacion.PENDIENTE);
    }

    @Test
    void storesTheFailureReasonWhenTheStatusIsFallido() {
        Notificacion saved = adapter.save(new Notificacion(null,
                "roto@example.com", "Aviso", "No se pudo entregar.",
                EstadoNotificacion.FALLIDO, "Buzon inexistente"));

        Optional<Notificacion> found = adapter.findById(saved.id());

        assertThat(found).isPresent();
        assertThat(found.get().estado()).isEqualTo(EstadoNotificacion.FALLIDO);
        assertThat(found.get().motivoFallo()).isEqualTo("Buzon inexistente");
    }

    @Test
    void findAllReturnsTheRowsSeededByFlyway() {
        assertThat(adapter.findAll()).hasSize(3);
    }
}
