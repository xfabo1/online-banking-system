package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.api.ScheduledPaymentCreateDto;
import cz.muni.fi.obs.api.ScheduledPaymentDto;

import java.time.Instant;

public interface ScheduledPaymentFacade {

    ScheduledPaymentDto createPayment(ScheduledPaymentCreateDto createDto);

    void disablePayment(String id, Instant disableTime);

    void enablePayment(String id);
}
