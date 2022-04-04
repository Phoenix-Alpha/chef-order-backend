package com.halalhomemade.backend.services.listeners;

import com.halalhomemade.backend.constants.IApplicationConstants;
import com.halalhomemade.backend.models.User;
import com.halalhomemade.backend.services.AwsService;
import com.halalhomemade.backend.services.events.PhoneNumberUpdatedEvent;

import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PhoneNumberUpdatedListener implements ApplicationListener<PhoneNumberUpdatedEvent> {
	
	@Autowired AwsService awsService;
	
	@Override
	public void onApplicationEvent(PhoneNumberUpdatedEvent event) {
		this.sendPhoneVerificationCodeViaSms(event.getUser().getPhoneCode(), event.getUser().getPhoneNumber(), event.getUser().getPhoneVerificationCode());
	}

	private void sendPhoneVerificationCodeViaSms(String phoneCode, String phoneNumber, String code) {
		String rawPhoneNumber = phoneCode + phoneNumber;
		String smsMessage = String.format("Your phone verification code for Halal Homemade is %s.", code);
		awsService.pubTextSMS(rawPhoneNumber, smsMessage);
	}
}
