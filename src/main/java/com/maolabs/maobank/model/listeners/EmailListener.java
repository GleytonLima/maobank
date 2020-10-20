package com.maolabs.maobank.model.listeners;

import com.maolabs.maobank.model.events.EmailEvent;
import com.maolabs.maobank.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmailListener {

    private final EmailService emailService;

    public EmailListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void onApplicationEvent(EmailEvent event) {
        this.emailService.sendSimpleMessage(event.getMensagem());
    }
}
