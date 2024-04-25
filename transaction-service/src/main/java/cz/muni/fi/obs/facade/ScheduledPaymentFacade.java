package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.api.ScheduledPaymentCreateDto;
import cz.muni.fi.obs.api.ScheduledPaymentDto;

public interface ScheduledPaymentFacade {

    ScheduledPaymentDto createPayment(ScheduledPaymentCreateDto createDto);

    void disablePayment(String id);

    void enablePayment(String id);
}
