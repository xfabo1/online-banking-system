package cz.muni.fi.obs.facade;

import java.time.Instant;

import cz.muni.fi.obs.api.ScheduledPaymentCreateDto;
import cz.muni.fi.obs.api.ScheduledPaymentDto;

public interface ScheduledPaymentFacade {

    ScheduledPaymentDto createPayment(ScheduledPaymentCreateDto createDto);

    void disablePayment(String id, Instant disableTime);

    void enablePayment(String id);
}
