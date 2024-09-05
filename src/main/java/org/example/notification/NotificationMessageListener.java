package org.example.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationMessageListener {

    @RabbitListener(queues = "notificationQueue")
    public void receiveMessage(NotificationMessage message) {
        // Здесь можно интегрировать с почтовым сервисом для отправки email-уведомлений
        System.out.println("Отправка уведомления на " + message.getEmail() + ": " + message.getMessage());
    }
}
