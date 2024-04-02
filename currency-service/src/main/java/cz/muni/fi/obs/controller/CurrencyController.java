package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.dto.CurrencyDto;
import cz.muni.fi.obs.dto.CurrencyExchangeRequest;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.facade.CurrencyFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/currencies")
public class CurrencyController {

    private final CurrencyFacade currencyFacade;

    @Autowired
    public CurrencyController(CurrencyFacade currencyFacade) {
        this.currencyFacade = currencyFacade;
    }

    @PostMapping("/exchange")
    public ResponseEntity<CurrencyExchangeResult> exchange(@RequestBody CurrencyExchangeRequest request) {
        return ResponseEntity.ok(currencyFacade.exchange(request.from(), request.to(), request.amount()));
    }

    @GetMapping("/")
    public ResponseEntity<Page<CurrencyDto>> currencies(@ModelAttribute Pageable pageRequest) {
        return ResponseEntity.ok(currencyFacade.listPaged(pageRequest));
    }
}
