package es.scafftrix.notificacionesms.application.service;

import es.scafftrix.notificacionesms.application.port.out.NotificacionRepository;
import es.scafftrix.notificacionesms.application.port.out.Notificador;
import es.scafftrix.notificacionesms.domain.exception.NotificacionNotFoundException;
import es.scafftrix.notificacionesms.domain.model.EstadoNotificacion;
import es.scafftrix.notificacionesms.domain.model.Notificacion;
import es.scafftrix.notificacionesms.domain.model.ResultadoEnvio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository;

    @Mock
    private Notificador notificador;

    @InjectMocks
    private NotificacionService service;

    /** El repositorio devuelve primero la fila PENDIENTE (id asignado) y despues la final. */
    private void stubSaveAsignandoId() {
        when(repository.save(any(Notificacion.class))).thenAnswer(invocation -> {
            Notificacion given = invocation.getArgument(0);
            Long id = given.id() == null ? 1L : given.id();
            return new Notificacion(id, given.destinatario(), given.asunto(),
                    given.cuerpo(), given.estado(), given.motivoFallo());
        });
    }

    @Test
    void createRegistraPendienteYMarcaEnviadoSiElEnvioVaBien() {
        stubSaveAsignandoId();
        when(notificador.enviar("ana@example.com", "Asunto", "Cuerpo"))
                .thenReturn(ResultadoEnvio.ok("entregado"));

        Notificacion created = service.create(
                new Notificacion(null, "ana@example.com", "Asunto", "Cuerpo", null, null));

        assertThat(created.id()).isEqualTo(1L);
        assertThat(created.estado()).isEqualTo(EstadoNotificacion.ENVIADO);
        assertThat(created.motivoFallo()).isNull();
        verify(notificador).enviar("ana@example.com", "Asunto", "Cuerpo");
    }

    @Test
    void createMarcaFallidoYGuardaElMotivoCuandoElEnvioFalla() {
        stubSaveAsignandoId();
        when(notificador.enviar(anyString(), anyString(), anyString()))
                .thenReturn(ResultadoEnvio.fallo("Buzon inexistente"));

        Notificacion created = service.create(
                new Notificacion(null, "roto@example.com", "Asunto", "Cuerpo", null, null));

        assertThat(created.estado()).isEqualTo(EstadoNotificacion.FALLIDO);
        assertThat(created.motivoFallo()).isEqualTo("Buzon inexistente");
    }

    @Test
    void createPruebaElEnvioConElDestinatarioAsuntoYCuerpoDados() {
        stubSaveAsignandoId();
        when(notificador.enviar("a@example.com", "Hola", "Texto")).thenReturn(ResultadoEnvio.ok("ok"));

        service.create(new Notificacion(null, "a@example.com", "Hola", "Texto", null, null));

        verify(notificador).enviar("a@example.com", "Hola", "Texto");
    }

    @Test
    void findByIdThrowsWhenTheNotificationDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(1L))
                .isInstanceOf(NotificacionNotFoundException.class)
                .hasMessageContaining("1");
    }

    @Test
    void findAllReturnsTheContentOfThePort() {
        when(repository.findAll()).thenReturn(List.of(
                new Notificacion(1L, "a@example.com", "Asunto", "Cuerpo", EstadoNotificacion.ENVIADO, null)));

        assertThat(service.findAll()).hasSize(1);
    }

    @Test
    void findByIdDoesNotTouchTheNotifier() {
        when(repository.findById(4L)).thenReturn(Optional.of(
                new Notificacion(4L, "a@example.com", "Asunto", "Cuerpo", EstadoNotificacion.ENVIADO, null)));

        service.findById(4L);

        verify(notificador, never()).enviar(anyString(), anyString(), anyString());
    }
}
