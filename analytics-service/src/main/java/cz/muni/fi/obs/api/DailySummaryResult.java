package cz.muni.fi.obs.api;

import java.time.LocalDate;
import java.util.List;

public record DailySummaryResult(LocalDate reportDate,
                                 List<DailySummary> summaries) {
}
