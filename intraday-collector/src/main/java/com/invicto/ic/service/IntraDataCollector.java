package com.invicto.ic.service;

import com.invicto.ic.bridge.NseBridge;
import com.invicto.ic.model.EquitySnapVo;
import com.invicto.ic.model.SnapType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class IntraDataCollector implements Runnable{

    private final NseBridge nseBridge;
    private final IntradayDataPersister dataPersister;

    private final SnapType snapType;



    @Autowired
    public IntraDataCollector(NseBridge nseBridge, IntradayDataPersister dataPersister,SnapType snapType) {
        this.nseBridge = nseBridge;
        this.dataPersister = dataPersister;
        this.snapType = snapType;
    }

    @Override
    public void run() {
        try {
            EquitySnapVo equitySnapVo = nseBridge.fetchIndexAndStockFutures();
            equitySnapVo.setType(snapType);
            dataPersister.saveSnapshot(equitySnapVo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
