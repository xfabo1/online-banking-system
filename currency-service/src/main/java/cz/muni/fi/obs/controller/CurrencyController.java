package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.dto.CurrencyDto;
import cz.muni.fi.obs.dto.CurrencyExchangeRequest;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.facade.CurrencyFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Currency API")
@RestController
@RequestMapping("/v1/currencies")
public class CurrencyController {

    private final CurrencyFacade currencyFacade;

    @Autowired
    public CurrencyController(CurrencyFacade currencyFacade) {
        this.currencyFacade = currencyFacade;
    }

    @Operation(summary = "Exchanges sum between two specified currencies.")
    @PostMapping("/exchange")
    public ResponseEntity<CurrencyExchangeResult> exchange(@RequestBody CurrencyExchangeRequest request) {
        return ResponseEntity.ok(currencyFacade.exchange(request.from(), request.to(), request.amount()));
    }

    @Operation(summary = "Lists a page of available currencies to use")
    @GetMapping("/list")
    public ResponseEntity<Page<CurrencyDto>> currencies(@RequestParam(defaultValue = "0") Integer page,
                                                        @RequestParam(defaultValue = "50") Integer pageSize) {
        return ResponseEntity.ok(currencyFacade.listPage(Pageable.ofSize(pageSize).withPage(page)));
    }
}
