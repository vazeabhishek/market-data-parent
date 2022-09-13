package com.invicto.cnp.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRecord {
    private String symbol;
    private String comapanyName;
    private String subject;
    private String details;
    private String broadcaseDateTime;
    private String reciept;
    private String dissemination;
    private String difference;
}
