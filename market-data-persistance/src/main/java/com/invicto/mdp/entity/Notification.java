package com.invicto.mdp.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "NOTIFICATION")
public class Notification {
    @Id
    @GeneratedValue(generator = "notification-sequence-generator")
    @GenericGenerator(
            name = "notification-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "notification_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @ManyToOne(targetEntity = Symbol.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Symbol symbol;
    @Column(name = "COMPANY_NAME", columnDefinition = "TEXT")
    private String comapanyName;
    @Column(name = "SUBJECT", columnDefinition = "TEXT")
    private String subject;
    @Column(name = "DETAILS", columnDefinition = "TEXT")
    private String details;
    @Column(name = "BROADCAST_DATE")
    private LocalDate broadcastDate;
    @Column(name = "BROADCAST_TIME")
    private LocalTime broadcastTime;
    @Column(name = "RECEPIT", columnDefinition = "TEXT")
    private String reciept;
    @Column(name = "DISSEMINATION", columnDefinition = "TEXT")
    private String dissemination;
    @Column(name = "DIFFERENCE", columnDefinition = "TEXT")
    private String difference;

    public Notification getCopy() {
        Notification notification = new Notification();
        notification.setSubject(subject);
        notification.setSymbol(symbol);
        notification.setDetails(details);
        notification.setReciept(reciept);
        notification.setDifference(difference);
        notification.setDissemination(dissemination);
        notification.setComapanyName(comapanyName);
        return notification;
    }

}
