package com.halalhomemade.backend.services.events;

import com.halalhomemade.backend.models.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PhoneNumberUpdatedEvent extends ApplicationEvent {

  private User user;

  public PhoneNumberUpdatedEvent(User user) {
    super(user);
    this.user = user;
  }
}
