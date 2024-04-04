package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.api.DailySummaryResult;
import cz.muni.fi.obs.api.MonthlySummaryResult;
import cz.muni.fi.obs.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsFacade {
    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsFacade(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    public DailySummaryResult getDailySummary(String accountNumber, int year, int month) {
        return analyticsService.getDailySummary(accountNumber, year, month);
    }

    public MonthlySummaryResult getMonthlySummary(String accountNumber, int year, int month) {
        return analyticsService.getMonthlySummary(accountNumber, year, month);
    }
}
