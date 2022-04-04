package com.halalhomemade.backend.services.events;

import com.halalhomemade.backend.models.Chef;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ChefRegistrationCompleteEvent extends ApplicationEvent {

  private Chef chef;

  public ChefRegistrationCompleteEvent(Chef chef) {
    super(chef);
    this.chef = chef;
  }
}
