package cz.muni.fi.obs.api;

import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Range;

public record DailySummaryRequest(
        @Min(value = 1, message = "Year must be positive") int year,
        @Range(min = 1, max = 12) int month){
}
