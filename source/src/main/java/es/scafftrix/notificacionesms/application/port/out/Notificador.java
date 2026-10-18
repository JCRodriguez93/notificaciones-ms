package es.scafftrix.notificacionesms.application.port.out;

import es.scafftrix.notificacionesms.domain.model.ResultadoEnvio;

/**
 * Puerto de salida del canal de envio.
 *
 * <p>Es la pieza que desacopla el servicio de notificaciones del proveedor concreto: el caso
 * de uso solo sabe que existe algo capaz de entregar un mensaje, no si eso es un log, un SMTP
 * o, mas adelante, una cola. El adaptador real vive en {@code infrastructure.notifier}.
 *
 * <p>Devuelve un {@link ResultadoEnvio} en lugar de lanzar excepciones: un fallo de envio es
 * un resultado esperado que hay que registrar, no un error que deba tumbar el proceso.
 */
public interface Notificador {

    ResultadoEnvio enviar(String destinatario, String asunto, String cuerpo);
}
