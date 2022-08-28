package com.invicto.fbl.service;

import com.invicto.fbl.model.EquityDerivativeCsvRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

@Component
public class BhavCopyReader {

    private DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("dd-MMM-yyyy")
            .toFormatter(Locale.ENGLISH);



    @Transactional
    public void loadBhavCopy(File file, BhavCopyRecordProcessor processor) {
        try {
            Reader in = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().withAllowMissingColumnNames().parse(in);
            for (CSVRecord record : records) {
                EquityDerivativeCsvRecord edcsvRecord = new EquityDerivativeCsvRecord();
                edcsvRecord.setInstrument(record.get("INSTRUMENT"));
                edcsvRecord.setSymbol(record.get("SYMBOL"));
                edcsvRecord.setExpiryDt(record.get("EXPIRY_DT"), formatter);
                edcsvRecord.setStrikePrice(record.get("STRIKE_PR"));
                edcsvRecord.setOptionTYpe(record.get("OPTION_TYP"));
                edcsvRecord.setOpen(record.get("OPEN"));
                edcsvRecord.setHigh(record.get("HIGH"));
                edcsvRecord.setLow(record.get("LOW"));
                edcsvRecord.setClose(record.get("CLOSE"));
                edcsvRecord.setSettle(record.get("SETTLE_PR"));
                edcsvRecord.setContracts(record.get("CONTRACTS"));
                edcsvRecord.setValInLakh(record.get("VAL_INLAKH"));
                edcsvRecord.setOi(record.get("OPEN_INT"));
                edcsvRecord.setCoi(record.get("CHG_IN_OI"));
                edcsvRecord.setTimestamp(record.get("TIMESTAMP"), formatter);
                if(edcsvRecord.getInstrument().contentEquals("FUTSTK"))
                    processor.process(edcsvRecord);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
