package es.scafftrix.notificacionesms.domain.model;

/**
 * Modelo de dominio de una notificacion.
 *
 * <p>No depende de OpenAPI ni de JPA: la API y la persistencia se adaptan a este modelo,
 * nunca al reves. Es un record para que sea inmutable y para que MapStruct pueda construirlo
 * a partir de sus componentes.
 *
 * <p>Una notificacion no se edita ni se borra: es un registro de envio. Si hay que repetirla,
 * se crea otra.
 */
public record Notificacion(Long id, String destinatario, String asunto, String cuerpo,
                           EstadoNotificacion estado, String motivoFallo) {
}
