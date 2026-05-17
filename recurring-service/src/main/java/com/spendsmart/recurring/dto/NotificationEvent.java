package com.spendsmart.recurring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private Long   recipientId;
    private String type;
    private String severity;
    private String title;
    private String message;
    private Long   relatedId;
    private String relatedType;
}