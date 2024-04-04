package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.api.DailySummaryRequest;
import cz.muni.fi.obs.api.DailySummaryResult;
import cz.muni.fi.obs.api.MonthlySummaryRequest;
import cz.muni.fi.obs.api.MonthlySummaryResult;
import cz.muni.fi.obs.facade.AnalyticsFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/{accountNumber}")
public class AnalyticsController {
    private final AnalyticsFacade analyticsFacade;
    @Autowired
    public AnalyticsController(AnalyticsFacade analyticsFacade) {
        this.analyticsFacade = analyticsFacade;
    }

    @PostMapping("/daily-summary")
    public ResponseEntity<DailySummaryResult> getDailySummary(@PathVariable String accountNumber, @RequestBody DailySummaryRequest request) {
        log.info("Received request for daily summary for account number: {}, year: {}, month: {}", accountNumber, request.year(), request.month());
        DailySummaryResult result = analyticsFacade.getDailySummary(accountNumber, request.year(), request.month());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/monthly-summary")
    public ResponseEntity<MonthlySummaryResult> getMonthlySummary(@PathVariable String accountNumber, @RequestBody MonthlySummaryRequest request) {
        log.info("Received request for monthly summary for account number: {}, year: {}", accountNumber, request.year());
        MonthlySummaryResult result = analyticsFacade.getMonthlySummary(accountNumber, request.year(), request.month());
        return ResponseEntity.ok(result);
    }
}
