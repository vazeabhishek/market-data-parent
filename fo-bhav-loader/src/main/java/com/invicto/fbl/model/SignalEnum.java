package com.invicto.fbl.model;

public enum SignalEnum {
    OI_BULLISH("OI_BULLISH"),
    OI_BEARISH("OI_BEARISH"),
    NEUTRAL("NEUTRAL"),
    LONG_BUILD_UP("LONG_BUILD_UP"),
    SHORT_BUILD_UP("SHORT_BUILD_UP"),
    PRICE_BULLISH("PRICE_BULLISH"),
    PRICE_BEARISH("PRICE_BEARISH"),
    VOL_BULLISH("VOL_BULLISH"),
    VOL_BEARISH("VOL_BEARISH");


    private String string;

    SignalEnum(String string) {
        this.string = string;
    }
}
