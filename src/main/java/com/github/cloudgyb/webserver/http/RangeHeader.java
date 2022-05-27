package com.github.cloudgyb.webserver.http;

import java.util.ArrayList;
import java.util.List;

public class RangeHeader {
    private String unit;
    private final List<Long[]> rangeStartEnds;

    public RangeHeader() {
        this.rangeStartEnds = new ArrayList<>();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Long[]> getRangeStartEnds() {
        return rangeStartEnds;
    }


    @SuppressWarnings("all")
    public List<Long[]> addRangeStartEnd(Long[] startEnd) {
        this.rangeStartEnds.add(startEnd);
        return this.rangeStartEnds;
    }
}
