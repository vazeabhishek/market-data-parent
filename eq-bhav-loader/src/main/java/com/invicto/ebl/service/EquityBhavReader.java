package com.invicto.ebl.service;

import com.invicto.ebl.model.EqCsvRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class EquityBhavReader {

    public void readBhavCopy(File file, EquityBhavProcessor processor) {
        try {
            Reader in = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().withAllowMissingColumnNames().parse(in);
            for (CSVRecord record : records) {
                if (record.get("SERIES").contains("EQ")) {
                    EqCsvRecord csvRecord = new EqCsvRecord();
                    csvRecord.setSymbol(record.get("SYMBOL"));
                    csvRecord.setSeries(record.get("SERIES"));
                    csvRecord.setOpen(record.get("OPEN"));
                    csvRecord.setHigh(record.get("HIGH"));
                    csvRecord.setLow(record.get("LOW"));
                    csvRecord.setClose(record.get("CLOSE"));
                    csvRecord.setLast(record.get("LAST"));
                    csvRecord.setPrevClose(record.get("PREVCLOSE"));
                    csvRecord.setTotalTradedQty(record.get("TOTTRDQTY"));
                    csvRecord.setTotalTradedVol(record.get("TOTTRDVAL"));
                    csvRecord.setTimestamp(record.get("TIMESTAMP"));
                    csvRecord.setTotalTrades(record.get("TOTALTRADES"));
                    csvRecord.setISIN(record.get("ISIN"));
                    processor.process(csvRecord);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
