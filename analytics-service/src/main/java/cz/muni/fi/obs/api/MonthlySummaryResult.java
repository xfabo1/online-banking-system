package cz.muni.fi.obs.api;

import java.time.LocalDate;

public record MonthlySummaryResult(LocalDate reportDate,
                                   MonthlySummary summary) { }
