package es.scafftrix.notificacionesms.api.mapper;

import es.scafftrix.notificacionesms.api.model.Notificacion;
import es.scafftrix.notificacionesms.api.model.Notificacion.EstadoEnum;
import es.scafftrix.notificacionesms.domain.model.EstadoNotificacion;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class NotificacionApiMapperTest {

    private final NotificacionApiMapper mapper = Mappers.getMapper(NotificacionApiMapper.class);

    @Test
    void mapsDomainToApiModel() {
        Notificacion api = mapper.toApi(
                new es.scafftrix.notificacionesms.domain.model.Notificacion(
                        1L, "ana@example.com", "Asunto", "Cuerpo", EstadoNotificacion.ENVIADO, null));

        assertThat(api.getId()).isEqualTo(1L);
        assertThat(api.getDestinatario()).isEqualTo("ana@example.com");
        assertThat(api.getAsunto()).isEqualTo("Asunto");
        assertThat(api.getCuerpo()).isEqualTo("Cuerpo");
        assertThat(api.getEstado()).isEqualTo(EstadoEnum.ENVIADO);
        assertThat(api.getMotivoFallo()).isNull();
    }

    @Test
    void mapsApiModelToDomain() {
        Notificacion api = new Notificacion("ana@example.com", "Asunto", "Cuerpo")
                .id(7L)
                .estado(EstadoEnum.FALLIDO)
                .motivoFallo("Buzon inexistente");

        es.scafftrix.notificacionesms.domain.model.Notificacion domain = mapper.toDomain(api);

        assertThat(domain.id()).isEqualTo(7L);
        assertThat(domain.destinatario()).isEqualTo("ana@example.com");
        assertThat(domain.estado()).isEqualTo(EstadoNotificacion.FALLIDO);
        assertThat(domain.motivoFallo()).isEqualTo("Buzon inexistente");
    }
}
