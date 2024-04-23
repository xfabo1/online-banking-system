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
    int year;

    @Column(nullable = false)
    int month;

    @Column(nullable = false)
    int day;

    @Column(nullable = false)
    LocalDate fullDate;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.fullDate = LocalDate.of(year, month, day);
    }
}
