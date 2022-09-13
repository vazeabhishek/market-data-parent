package com.invicto.cnp.service;

import com.invicto.cnp.model.NotificationRecord;
import com.invicto.mdp.entity.Notification;
import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.repository.NotificationRepository;
import com.invicto.mdp.repository.SymbolRepository;
import lombok.extern.slf4j.Slf4j;
import net.rationalminds.LocalDateModel;
import net.rationalminds.Parser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CorporateNotificationLoader {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

    private final NotificationRepository notificationRepository;
    private final SymbolRepository symbolRepository;

    public CorporateNotificationLoader(NotificationRepository notificationRepository, SymbolRepository symbolRepository) {
        this.notificationRepository = notificationRepository;
        this.symbolRepository = symbolRepository;
    }

    public void processFile(File file) {
        try {
            Reader in = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().withAllowMissingColumnNames().parse(in);
            for (CSVRecord record : records) {
                NotificationRecord notificationRecord = new NotificationRecord();
                notificationRecord.setSymbol(record.get(0));
                notificationRecord.setComapanyName(record.get("COMPANY NAME"));
                notificationRecord.setSubject(record.get("SUBJECT"));
                notificationRecord.setDetails(record.get("DETAILS"));
                notificationRecord.setBroadcaseDateTime(record.get("BROADCAST DATE/TIME"));
                notificationRecord.setReciept(record.get("RECEIPT"));
                notificationRecord.setDissemination(record.get("DISSEMINATION"));
                notificationRecord.setDifference(record.get("DIFFERENCE"));
                try {
                    process(notificationRecord);
                }
                catch (Exception ex){
                    log.error(ex.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process(NotificationRecord record) {
        Optional<Symbol> symbolOptional = symbolRepository.findByTicker(record.getSymbol());
        if (symbolOptional.isPresent()) {

            Symbol symbol = symbolOptional.get();
            Notification notification = createNotificationFromRecord(symbol, record);
            notificationRepository.save(notification);
            List<LocalDate> identifiedDates = identifyDate(notification.getDetails());
            identifiedDates.stream().forEach(date -> {
                Notification addedNotification = notification.getCopy();
                addedNotification.setBroadcastDate(date);
                addedNotification.setBroadcastTime(null);
                notificationRepository.save(addedNotification);
            });
        } else
            log.info("Ignoring notification for {} due to ticker not found", record.getComapanyName());

    }

    private Notification createNotificationFromRecord(Symbol symbol, NotificationRecord record) {
        Notification notification = new Notification();
        notification.setSymbol(symbol);
        notification.setDetails(record.getDetails());
        notification.setBroadcastDate(fetchDateTime(record.getBroadcaseDateTime()).toLocalDate());
        notification.setBroadcastTime(fetchDateTime(record.getBroadcaseDateTime()).toLocalTime());
        notification.setComapanyName(record.getComapanyName());
        notification.setDifference(record.getDifference());
        notification.setReciept(record.getReciept());
        notification.setSubject(record.getSubject());
        return notification;

    }

    private static LocalDateTime fetchDateTime(String recordDateTime) {
        try {
            return LocalDateTime.parse(recordDateTime, dateTimeFormatter);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return LocalDateTime.now();
        }
    }

    private List<LocalDate> identifyDate(String s) {
        Parser parser = new Parser();
        List<LocalDateModel> dates = parser.parse(s);
        List<LocalDate> localDates = dates.stream().map(date -> {
            try {
                LocalDate localDate = LocalDate.parse(date.getDateTimeString(), DateTimeFormatter.ofPattern(date.getConDateFormat()));
                return localDate;
            } catch (Exception e) {
                log.warn(e.getMessage());
                return null;
            }

        }).filter(Objects::nonNull).collect(Collectors.toList());
        return localDates;
    }
}
