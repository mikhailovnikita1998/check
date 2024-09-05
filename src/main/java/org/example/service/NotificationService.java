package org.example.service;

import org.example.event.UserProfileUpdatedEvent;
import org.example.event.UserRegisteredEvent;
import org.example.notification.NotificationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendNotification(String email, String message) {
        NotificationMessage notificationMessage = new NotificationMessage(email, message);
        rabbitTemplate.convertAndSend("notificationQueue", notificationMessage);
    }

    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        String message = "Welcome, " + event.getUsername() + "! Your registration was successful.";
        sendNotification(event.getEmail(), message);
    }

    @EventListener
    public void handleUserProfileUpdatedEvent(UserProfileUpdatedEvent event) {
        String message = "Hello, " + event.getUsername() + ". Your profile has been updated.";
        sendNotification(event.getEmail(), message);
    }
}
