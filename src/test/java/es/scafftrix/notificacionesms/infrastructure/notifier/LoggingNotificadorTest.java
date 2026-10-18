package es.scafftrix.notificacionesms.infrastructure.notifier;

import es.scafftrix.notificacionesms.domain.model.ResultadoEnvio;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba el adaptador de referencia del puerto Notificador.
 */
class LoggingNotificadorTest {

    private final LoggingNotificador notificador = new LoggingNotificador();

    @Test
    void devuelveExitoPorqueElLogNuncaFalla() {
        ResultadoEnvio resultado = notificador.enviar("ana@example.com", "Asunto", "Cuerpo");

        assertThat(resultado.exito()).isTrue();
        assertThat(resultado.detalle()).contains("log");
    }

    @Test
    void noLanzaExcepcionAunqueElCuerpoEsteVacio() {
        ResultadoEnvio resultado = notificador.enviar("ana@example.com", "Asunto", null);

        assertThat(resultado.exito()).isTrue();
    }
}
