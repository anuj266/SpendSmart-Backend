package com.spendsmart.notification.service;

import com.spendsmart.notification.entity.Notification;
import com.spendsmart.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotifServiceImpl implements NotifService {

    private final NotificationRepository repository;

    @Override
    public void send(Notification notification) {
        repository.save(notification);
    }

    @Override
    public void sendBudgetAlert(Long recipientId, String message, double amount) {
        Notification notification = new Notification();
        notification.setRecipientId(recipientId);
        notification.setType("BUDGET_EXCEEDED");
        notification.setSeverity(amount > 1000 ? "CRITICAL" : "WARNING");
        notification.setTitle("Budget Alert");
        notification.setMessage(message);

        send(notification);
    }

    @Override
    public void sendBulk(List<Long> recipients, String title, String message) {
        for (Long id : recipients) {
            Notification n = new Notification();
            n.setRecipientId(id);
            n.setTitle(title);
            n.setMessage(message);
            n.setSeverity("INFO");
            send(n);
        }
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification n = repository.findById(notificationId).orElseThrow();
        n.setRead(true);
        repository.save(n);
    }

    @Override
    public void markAllRead(Long recipientId) {
        List<Notification> list = repository.findByRecipientId(recipientId);
        list.forEach(n -> n.setRead(true));
        repository.saveAll(list);
    }

    @Override
    public void acknowledge(Long notificationId) {
        Notification n = repository.findById(notificationId).orElseThrow();
        n.setAcknowledged(true);
        repository.save(n);
    }

    @Override
    public List<Notification> getByRecipient(Long recipientId) {
        return repository.findByRecipientId(recipientId);
    }

    @Override
    public int getUnreadCount(Long recipientId) {
        return repository.countByRecipientIdAndIsRead(recipientId, false);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        repository.deleteById(notificationId);
    }

    @Override
    public List<Notification> getAll() {
        return repository.findAll();
    }
}