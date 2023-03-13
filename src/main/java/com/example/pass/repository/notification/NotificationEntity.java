package com.example.pass.repository.notification;

import com.example.pass.repository.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@ToString
@Entity
@Table(name = "notification")
public class NotificationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationSeq;
    private String uuid;

    @Enumerated(EnumType.STRING)
    private NotificationEvent notificationEvent;
    private String text;
    private boolean sent;
    private LocalDateTime sentAt;
}
