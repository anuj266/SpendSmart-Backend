package com.spendsmart.web.client;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8088/api/notifications";

    public List<Object> getNotifications(int recipientId) {
        try {
            return restTemplate.getForObject(BASE_URL + "/" + recipientId, List.class);
        } catch (RestClientException e) {
            log.error("Error fetching notifications for recipientId: {}", recipientId, e);
            return Collections.emptyList();
        }
    }

    public List<Object> getAllNotifications() {
        try {
            return restTemplate.getForObject(BASE_URL + "/all", List.class);
        } catch (RestClientException e) {
            log.error("Error fetching all notifications", e);
            return Collections.emptyList();
        }
    }

    public int getUnreadCount(int recipientId) {
        try {
            Integer count = restTemplate.getForObject(BASE_URL + "/unread-count/" + recipientId, Integer.class);
            return count != null ? count : 0;
        } catch (RestClientException e) {
            log.error("Error fetching unread count for recipientId: {}", recipientId, e);
            return 0;
        }
    }

    public void sendBulkNotification(List<Integer> userIds, String title, String message) {
        try {
            String url = BASE_URL + "/bulk?title=" + title + "&message=" + message;
            restTemplate.postForObject(url, userIds, Object.class);
        } catch (RestClientException e) {
            log.error("Error sending bulk notification", e);
            throw new RuntimeException("Failed to send bulk notification: " + e.getMessage());
        }
    }

    public void markAsRead(int id) {
        try {
            restTemplate.put(BASE_URL + "/read/" + id, null);
        } catch (RestClientException e) {
            log.error("Error marking notification as read: {}", id, e);
            throw new RuntimeException("Failed to mark notification as read: " + e.getMessage());
        }
    }

    public void markAllRead(int recipientId) {
        try {
            restTemplate.put(BASE_URL + "/read/all/" + recipientId, null);
        } catch (RestClientException e) {
            log.error("Error marking all notifications as read for recipientId: {}", recipientId, e);
            throw new RuntimeException("Failed to mark all as read: " + e.getMessage());
        }
    }

    public void acknowledge(int id) {
        try {
            restTemplate.put(BASE_URL + "/ack/" + id, null);
        } catch (RestClientException e) {
            log.error("Error acknowledging notification: {}", id, e);
            throw new RuntimeException("Failed to acknowledge notification: " + e.getMessage());
        }
    }

    public void deleteNotification(int id) {
        try {
            restTemplate.delete(BASE_URL + "/" + id);
        } catch (RestClientException e) {
            log.error("Error deleting notification with id: {}", id, e);
            throw new RuntimeException("Failed to delete notification: " + e.getMessage());
        }
    }
}