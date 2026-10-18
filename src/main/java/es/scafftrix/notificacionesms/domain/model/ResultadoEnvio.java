package es.scafftrix.notificacionesms.domain.model;

/**
 * Resultado de intentar entregar una notificacion al canal de envio.
 *
 * <p>Lo devuelve el puerto {@code Notificador} en lugar de lanzar una excepcion, de modo que
 * un fallo de envio es un resultado esperado y no un error: la notificacion se registra como
 * FALLIDO con su motivo y el proceso sigue.
 */
public record ResultadoEnvio(boolean exito, String detalle) {

    public static ResultadoEnvio ok(String detalle) {
        return new ResultadoEnvio(true, detalle);
    }

    public static ResultadoEnvio fallo(String motivo) {
        return new ResultadoEnvio(false, motivo);
    }
}
