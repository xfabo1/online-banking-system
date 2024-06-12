package cz.muni.fi.obs.controller;

import static cz.muni.fi.obs.controller.ScheduledPaymentController.SCHEDULED_PAYMENT_PATH;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cz.muni.fi.obs.api.ScheduledPaymentCreateDto;
import cz.muni.fi.obs.api.ScheduledPaymentDto;
import cz.muni.fi.obs.facade.ScheduledPaymentFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "ScheduledPayment", description = "Scheduled payment creation and disabling")
@RestController
@RequestMapping(SCHEDULED_PAYMENT_PATH)
public class ScheduledPaymentController {

    public static final String SCHEDULED_PAYMENT_PATH = "/v1/scheduled-payments";

    private final ScheduledPaymentFacade facade;

    @Autowired
    public ScheduledPaymentController(ScheduledPaymentFacade facade) {
        this.facade = facade;
    }

    @PostMapping("/")
    public ScheduledPaymentDto create(@Valid @RequestBody ScheduledPaymentCreateDto createDto) {
        return facade.createPayment(createDto);
    }

    @PutMapping("/{id}/disable")
    public void disable(@PathVariable String id, @RequestParam(required = false) Instant disableTime) {
        facade.disablePayment(id, disableTime == null ? Instant.now() : disableTime);
    }

    @PutMapping("/{id}/enable")
    public void enable(@PathVariable String id) {
        facade.enablePayment(id);
    }
}
