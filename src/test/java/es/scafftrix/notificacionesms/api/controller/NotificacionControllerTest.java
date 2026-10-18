package es.scafftrix.notificacionesms.api.controller;

import es.scafftrix.notificacionesms.api.mapper.NotificacionApiMapperImpl;
import es.scafftrix.notificacionesms.api.model.Notificacion.EstadoEnum;
import es.scafftrix.notificacionesms.application.port.in.NotificacionUseCase;
import es.scafftrix.notificacionesms.domain.exception.NotificacionNotFoundException;
import es.scafftrix.notificacionesms.domain.model.EstadoNotificacion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificacionController.class)
@Import(NotificacionApiMapperImpl.class)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificacionUseCase notificacionUseCase;

    @Test
    void listsNotifications() throws Exception {
        when(notificacionUseCase.findAll()).thenReturn(List.of(
                new es.scafftrix.notificacionesms.domain.model.Notificacion(
                        1L, "ana@example.com", "Bienvenida", "Texto", EstadoNotificacion.ENVIADO, null)));

        mockMvc.perform(get("/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].destinatario").value("ana@example.com"))
                .andExpect(jsonPath("$[0].estado").value("ENVIADO"));
    }

    @Test
    void returns404WhenTheNotificationDoesNotExist() throws Exception {
        when(notificacionUseCase.findById(42L)).thenThrow(new NotificacionNotFoundException(42L));

        mockMvc.perform(get("/notificaciones/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.detail").value("Notificacion 42 no encontrada"));
    }

    @Test
    void createsANotificationWith201AndTheServerAssignedStatus() throws Exception {
        when(notificacionUseCase.create(any())).thenReturn(
                new es.scafftrix.notificacionesms.domain.model.Notificacion(
                        5L, "nuevo@example.com", "Asunto", "Cuerpo", EstadoNotificacion.ENVIADO, null));

        mockMvc.perform(post("/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"destinatario\":\"nuevo@example.com\",\"asunto\":\"Asunto\",\"cuerpo\":\"Cuerpo\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.destinatario").value("nuevo@example.com"))
                .andExpect(jsonPath("$.estado").value(EstadoEnum.ENVIADO.name()));
    }

    @Test
    void rejectsANotificationWithoutRequiredFields() throws Exception {
        mockMvc.perform(post("/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"destinatario\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
