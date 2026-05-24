package org.northpay_contractor_onboarding.controller;

import org.northpay_contractor_onboarding.dto.MetricsResponseDTO;
import org.northpay_contractor_onboarding.service.MetricsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {
  private final MetricsService metricsService;

  @GetMapping()
  @Operation(
    summary = "Get metrics without synchronization",
    description = "This method should be used when the frontend has to render these metrics. To synchronize connect to websocket `/topic/metrics`"
  )
  public ResponseEntity<MetricsResponseDTO> getAll() {
    return new ResponseEntity<>(metricsService.getMetrics(), HttpStatus.OK);
  }
}
