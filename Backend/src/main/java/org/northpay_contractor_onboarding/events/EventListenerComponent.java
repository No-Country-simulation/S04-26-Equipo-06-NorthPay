package org.northpay_contractor_onboarding.events;

import org.northpay_contractor_onboarding.events.events.MetricsEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventListenerComponent {
  private final SimpMessagingTemplate messagingTemplate;

  @TransactionalEventListener(
    phase = TransactionPhase.AFTER_COMMIT
  )
  public void handleMetricsChange(MetricsEvent event) {
    messagingTemplate.convertAndSend(TopicRoutes.METRICS, event.data());
  }
}
