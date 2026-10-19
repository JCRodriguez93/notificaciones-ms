package es.scafftrix.notificacionesms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificacionesMsApplication {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionesMsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(NotificacionesMsApplication.class, args);
        logger.info("notificaciones-ms iniciada correctamente");
    }
}
