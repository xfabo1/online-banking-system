package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.AnalyticsManagement;
import cz.muni.fi.obs.api.DailySummaryRequest;
import cz.muni.fi.obs.api.DailySummaryResult;
import cz.muni.fi.obs.api.MonthlySummaryRequest;
import cz.muni.fi.obs.api.MonthlySummaryResult;
import cz.muni.fi.obs.facade.AnalyticsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/{accountNumber}")
public class AnalyticsController {
    private final AnalyticsFacade analyticsFacade;

    @Autowired
    public AnalyticsController(AnalyticsFacade analyticsFacade) {
        this.analyticsFacade = analyticsFacade;
    }

    @Operation(
            summary = "Get daily summary for account",
            security = @SecurityRequirement(name = AnalyticsManagement.SECURITY_SCHEME_BEARER,
                                            scopes = {"SCOPE_test_read"}),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Summary retrieved"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
            }
    )
    @PostMapping("/daily-summary")
    public ResponseEntity<DailySummaryResult> getDailySummary(@PathVariable String accountNumber,
                                                              @Valid @RequestBody DailySummaryRequest request) {
        log.info("Received request for daily summary for account number: {}, year: {}, month: {}",
                 accountNumber,
                 request.year(),
                 request.month()
        );
        DailySummaryResult result = analyticsFacade.getDailySummary(accountNumber, request.year(), request.month());
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Get monthly summary for account",
            security = @SecurityRequirement(name = AnalyticsManagement.SECURITY_SCHEME_BEARER,
                                            scopes = {"SCOPE_test_read"}),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Summary retrieved"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
            }
    )
    @PostMapping("/monthly-summary")
    public ResponseEntity<MonthlySummaryResult> getMonthlySummary(@PathVariable String accountNumber,
                                                                  @Valid @RequestBody MonthlySummaryRequest request) {
        log.info("Received request for monthly summary for account number: {}, year: {}",
                 accountNumber,
                 request.year()
        );
        MonthlySummaryResult result = analyticsFacade.getMonthlySummary(accountNumber, request.year(), request.month());
        return ResponseEntity.ok(result);
    }
}
