package org.northpay_contractor_onboarding.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {

    registry.addEndpoint("/ws")
      .setAllowedOriginPatterns("*")
      .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // por convención general se usa topic para referirse a los canales a los que los receptores, ej frontend, estarán conectados
    registry.enableSimpleBroker("/topic");

    // este sería el prefijo para mandar datos al back
    registry.setApplicationDestinationPrefixes("/api/v1/ws");
  }
}
