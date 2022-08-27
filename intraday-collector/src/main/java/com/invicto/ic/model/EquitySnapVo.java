package com.invicto.ic.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class EquitySnapVo {
    private List<EquityVo> data;
    private String timestamp;
    private String currTradingDate;
    private String prevTradingDate;
    private SnapType type;
}