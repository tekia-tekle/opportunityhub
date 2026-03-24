package com.opportunityhub.service;

import com.opportunityhub.model.Opportunity;
import com.opportunityhub.model.Subscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    // Inject email/SMS sender here (JavaMailSender or Twilio client)
    // private final JavaMailSender mailSender;

    public void notify(Subscriber subscriber, Opportunity opportunity) {
        if (subscriber.getEmail() != null) {
            // Send email
            System.out.println("Sending email to " + subscriber.getEmail() +
                    " about new opportunity: " + opportunity.getTitle());
            // Implement actual email sending here
        }

        if (subscriber.getPhone() != null) {
            // Send SMS or WhatsApp
            System.out.println("Sending SMS to " + subscriber.getPhone() +
                    " about new opportunity: " + opportunity.getTitle());
            // Implement actual SMS sending here
        }
    }
}