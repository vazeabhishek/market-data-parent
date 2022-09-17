package com.invicto.wui.model;

import com.invicto.wui.constants.Query;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BuildUpVo  implements Comparable<BuildUpVo> {
    private long contractId;
    private String symbol;
    private LocalDate analyticsDate;
    private String expiryDate;
    private long buyersWonCount;
    private long sellersWonCount;
    private long higherHighCount;
    private long lowerLowCount;

    public BuildUpVo self() {
        return this;
    }

    @Override
    public int compareTo(BuildUpVo o) {
        BuildUpVo other = (BuildUpVo) o;
        if (other.analyticsDate.isBefore(this.analyticsDate)) {
            return 1;
        }
        if (other.analyticsDate.isAfter(this.analyticsDate)) {
            return -1;
        }
        return 0;
    }

}
