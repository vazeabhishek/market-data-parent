package com.invicto.ic.service;

import com.invicto.ic.bridge.NseBridge;
import com.invicto.ic.model.EquitySnapVo;
import com.invicto.ic.model.SnapType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
public class IntraDataCollector implements Runnable{

    private final NseBridge nseBridge;
    private final IntradayDataPersister dataPersister;

    private final SnapType snapType;




    public IntraDataCollector(NseBridge nseBridge, IntradayDataPersister dataPersister,SnapType snapType) {
        this.nseBridge = nseBridge;
        this.dataPersister = dataPersister;
        this.snapType = snapType;
    }

    @Override
    public void run() {
        try {
            log.info("Executing snap for {}",snapType);
            EquitySnapVo equitySnapVo = nseBridge.fetchIndexAndStockFutures();
            equitySnapVo.setType(snapType);
            dataPersister.saveSnapshot(equitySnapVo);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }

    }
}
