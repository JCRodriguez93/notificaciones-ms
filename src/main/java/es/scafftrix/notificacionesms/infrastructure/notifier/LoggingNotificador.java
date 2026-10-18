package es.scafftrix.notificacionesms.infrastructure.notifier;

import es.scafftrix.notificacionesms.application.port.out.Notificador;
import es.scafftrix.notificacionesms.domain.model.ResultadoEnvio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Adaptador de referencia del puerto {@link Notificador}: en lugar de llamar a un proveedor,
 * deja constancia del envio en el log.
 *
 * <p>Existe para que el servicio arranque y sea utilizable sin ninguna infraestructura externa
 * (ni SMTP ni broker). Sustituirlo por un adaptador real no toca una sola linea del dominio ni
 * del caso de uso: es exactamente lo que compra el puerto.
 */
@Component
public class LoggingNotificador implements Notificador {

    private static final Logger log = LoggerFactory.getLogger(LoggingNotificador.class);

    @Override
    public ResultadoEnvio enviar(String destinatario, String asunto, String cuerpo) {
        log.info("Enviando notificacion a={} asunto='{}' ({} caracteres)",
                destinatario, asunto, cuerpo == null ? 0 : cuerpo.length());
        return ResultadoEnvio.ok("Entregado al adaptador de log; sin proveedor SMTP configurado");
    }
}
