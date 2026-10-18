package es.scafftrix.notificacionesms.infrastructure.persistence.mapper;

import es.scafftrix.notificacionesms.domain.model.EstadoNotificacion;
import es.scafftrix.notificacionesms.domain.model.Notificacion;
import es.scafftrix.notificacionesms.infrastructure.persistence.entity.NotificacionEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class NotificacionPersistenceMapperTest {

    private final NotificacionPersistenceMapper mapper = Mappers.getMapper(NotificacionPersistenceMapper.class);

    @Test
    void mapsEntityToDomain() {
        NotificacionEntity entity = new NotificacionEntity();
        entity.setId(3L);
        entity.setDestinatario("ana@example.com");
        entity.setAsunto("Asunto");
        entity.setCuerpo("Cuerpo");
        entity.setEstado("ENVIADO");
        entity.setMotivoFallo(null);

        Notificacion domain = mapper.toDomain(entity);

        assertThat(domain.id()).isEqualTo(3L);
        assertThat(domain.destinatario()).isEqualTo("ana@example.com");
        assertThat(domain.estado()).isEqualTo(EstadoNotificacion.ENVIADO);
        assertThat(domain.motivoFallo()).isNull();
    }

    @Test
    void mapsDomainToEntity() {
        NotificacionEntity entity = mapper.toEntity(new Notificacion(3L,
                "ana@example.com", "Asunto", "Cuerpo", EstadoNotificacion.FALLIDO, "Motivo"));

        assertThat(entity.getId()).isEqualTo(3L);
        assertThat(entity.getDestinatario()).isEqualTo("ana@example.com");
        assertThat(entity.getEstado()).isEqualTo("FALLIDO");
        assertThat(entity.getMotivoFallo()).isEqualTo("Motivo");
    }
}
