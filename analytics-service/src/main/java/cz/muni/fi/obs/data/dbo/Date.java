package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "date_dim")
public class Date extends Dbo {

    @Column(nullable = false)
    int yearNumber;

    @Column(nullable = false)
    int monthNumber;

    @Column(nullable = false)
    int dayNumber;

    @Column(nullable = false)
    LocalDate fullDate;

    public Date(int year, int month, int day) {
        this.yearNumber = year;
        this.monthNumber = month;
        this.dayNumber = day;
        this.fullDate = LocalDate.of(year, month, day);
    }
}
