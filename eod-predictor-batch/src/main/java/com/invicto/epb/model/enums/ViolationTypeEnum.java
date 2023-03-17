package com.invicto.epb.model.enums;

import lombok.Getter;

@Getter
public enum ViolationTypeEnum {
    OI_CHECK("OI_CHECK"),
    PRICE_CHECK("PRICE_CHECK"),
    LONG_BUILD_UP_MISSING("LONG_BUILDUP_MISSING"),
    SHORT_BUILD_UP_MISSING("SHORT_BUILDUP_MISSING"),
    LONG_BUILD_UP_EXIST("LONG_BUILDUP_EXITS"),
    SHORT_BUILD_UP_EXITS("SHORT_BUILDUP_EXITS"),
    FUTURE_IS_AT_DISCOUNT("FUTURE IS AT DISCOUNTED PRICE"),
    FUTURE_IS_AT_PREMIUM("FUTURE IS AT PREMIUM PRICE"),
    UNKNOWN("UNKNOWN");
    private String violationType;

    ViolationTypeEnum(String violationType) {
        this.violationType = violationType;
    }
}
