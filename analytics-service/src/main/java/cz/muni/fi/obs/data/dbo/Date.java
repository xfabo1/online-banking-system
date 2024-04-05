package cz.muni.fi.obs.data.dbo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Date {
    int year;
    int month;
    int day;
    LocalDate date;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.date = LocalDate.of(year, month, day);
    }
}
