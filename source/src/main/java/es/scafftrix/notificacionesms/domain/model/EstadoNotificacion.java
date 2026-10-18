package es.scafftrix.notificacionesms.domain.model;

/**
 * Estado de una notificacion.
 *
 * <p>En el contrato OpenAPI viaja como cadena, pero en el dominio es un enum: asi es el
 * codigo, y no una cadena libre, el que impide valores inventados.
 */
public enum EstadoNotificacion {

    /** Registrada en base de datos pero aun no entregada al canal de envio. */
    PENDIENTE,

    /** El canal de envio acepto la notificacion. */
    ENVIADO,

    /** El canal de envio rechazo o fallo; el motivo queda en {@code motivoFallo}. */
    FALLIDO
}
