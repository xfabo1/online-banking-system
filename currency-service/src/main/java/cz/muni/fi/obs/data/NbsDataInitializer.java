package cz.muni.fi.obs.data;

import cz.muni.fi.obs.service.update.NbsCurrencyUpdateService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${currency.auto-update.services.nbs.enabled:false} and ${data.initialize:false}")
@Slf4j
public class NbsDataInitializer {

    private final NbsCurrencyUpdateService nbsCurrencyUpdateService;

    @Autowired
    public NbsDataInitializer(NbsCurrencyUpdateService nbsCurrencyUpdateService) {
        this.nbsCurrencyUpdateService = nbsCurrencyUpdateService;
    }

    @PostConstruct
    public void initializeData() {
        log.info("Initializing currency data.");
        nbsCurrencyUpdateService.updateCurrencies();
        log.info("Initialized currency data.");
    }
}
