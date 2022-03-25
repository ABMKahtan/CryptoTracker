package com.coinmonitor.app.service;

import com.coinmonitor.app.model.NotificationType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;

    public void sendEmailNotification(
            Integer price,
            String coinId,
            String currencyId,
            String emailToNotify,
            NotificationType notificationType
    ) {
        LOGGER.info("Sending notification to " + emailToNotify);
        String coin = coinId.toUpperCase();
        String currency = currencyId.toUpperCase();

        String subject = notificationType.equals(NotificationType.HIGHER_THAN_MAX) ?
                "HIGH PRICE ALERT FOR: " + coin : "LOW PRICE ALERT FOR " + coin;

        String text = "Current price for " + coin + " is " + price + " " + currency;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cryptopricenotify@gmail.com");
        message.setTo(emailToNotify);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
