package com.halalhomemade.backend.services.listeners;

import com.halalhomemade.backend.constants.IApplicationConstants;
import com.halalhomemade.backend.models.User;
import com.halalhomemade.backend.services.events.UserRegistrationCompleteEvent;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserRegistrationListener implements ApplicationListener<UserRegistrationCompleteEvent> {

  @Override
  public void onApplicationEvent(UserRegistrationCompleteEvent event) {
    
  }

  private void confirmRegistration(UserRegistrationCompleteEvent event) {

  }
}
