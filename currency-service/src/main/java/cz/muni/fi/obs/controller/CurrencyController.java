package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.dto.CurrencyExchangeRequest;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.facade.CurrencyFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    private final CurrencyFacade currencyFacade;

    @Autowired
    public CurrencyController(CurrencyFacade currencyFacade) {
        this.currencyFacade = currencyFacade;
    }

    @PostMapping("/exchange")
    public CurrencyExchangeResult exchange(@RequestBody CurrencyExchangeRequest request) {
        return currencyFacade.exchange(request.from(), request.to(), request.amount());
    }
}
