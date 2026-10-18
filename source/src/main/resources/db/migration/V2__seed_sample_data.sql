-- Datos de ejemplo para que el microservicio responda algo en el primer arranque.
INSERT INTO notificacion (destinatario, asunto, cuerpo, estado, motivo_fallo)
VALUES ('ana@example.com', 'Bienvenida', 'Bienvenido a la plataforma.', 'ENVIADO', NULL);

INSERT INTO notificacion (destinatario, asunto, cuerpo, estado, motivo_fallo)
VALUES ('luis@example.com', 'Recordatorio', 'Tienes una cita pendiente el viernes.', 'ENVIADO', NULL);

INSERT INTO notificacion (destinatario, asunto, cuerpo, estado, motivo_fallo)
VALUES ('roto@example.com', 'Aviso urgente', 'Este mensaje no pudo entregarse.', 'FALLIDO', 'Dominio del destinatario inexistente');
