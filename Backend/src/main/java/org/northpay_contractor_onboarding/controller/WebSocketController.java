package org.northpay_contractor_onboarding.controller;

import org.springframework.stereotype.Controller;

import io.swagger.v3.oas.annotations.Hidden;

@Controller
@Hidden
public class WebSocketController {
  // En el controller irían las rutas a las que el frontend necesita enviar datos en tiempo real (no solo recibir)
  // EJEMPLO
  /*
  @MessageMapping("/chat") // <- El frontend manda a (prefijo en WSConfig ->)/api/v1/ws/chat el mensaje nuevo que manda un usuario
  public void processMessage(datos) {
    // se llama al Event listener para que publique el evento en el canal correspondiente
  }
  */
}
