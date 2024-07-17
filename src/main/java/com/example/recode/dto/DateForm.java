package com.example.recode.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DateForm {

    private String currentYear;
    private String currentMonthAndDay;
    private String startYear;
    private String startMonth;
    private String startDay;
    private String endYear;
    private String endMonth;
    private String endDay;

    @Builder
    public DateForm(String currentYear, String currentMonthAndDay, String startYear, String startMonth, String startDay, String endYear, String endMonth, String endDay) {
        this.currentYear = currentYear;
        this.currentMonthAndDay = currentMonthAndDay;
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.endYear = endYear;
        this.endMonth = endMonth;
        this.endDay = endDay;
    }
}
