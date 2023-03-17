package com.invicto.epb.model.enums;

import lombok.Getter;

@Getter
public enum SignalEnum {
    BUY("BUY"),
    SELL("SELL"),
    NEUTRAL("NEUTRAL");
    private String signal;

    SignalEnum(String signal) {
        this.signal = signal;
    }
}
