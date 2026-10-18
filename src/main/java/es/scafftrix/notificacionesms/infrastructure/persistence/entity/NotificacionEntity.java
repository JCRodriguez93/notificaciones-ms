package es.scafftrix.notificacionesms.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA de notificacion. Es un detalle de infraestructura: el dominio nunca la ve.
 *
 * <p>El estado viaja como cadena en la tabla (lo mismo que en la API): la traduccion al enum
 * de dominio la hace el mapper de persistencia.
 */
@Entity
@Table(name = "notificacion")
public class NotificacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String destinatario;

    @Column(nullable = false, length = 200)
    private String asunto;

    @Column(nullable = false, length = 2000)
    private String cuerpo;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "motivo_fallo", length = 500)
    private String motivoFallo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivoFallo() {
        return motivoFallo;
    }

    public void setMotivoFallo(String motivoFallo) {
        this.motivoFallo = motivoFallo;
    }
}
