package com.teamsync.api.common.event.event;

import java.time.LocalDateTime;

public abstract class BaseDomainEvent {

  private final LocalDateTime occurredAt = LocalDateTime.now();

  public LocalDateTime getOccurredAt() {
    return occurredAt;
  }

}
