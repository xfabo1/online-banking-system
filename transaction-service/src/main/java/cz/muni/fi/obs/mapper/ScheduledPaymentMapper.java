package cz.muni.fi.obs.mapper;

import cz.muni.fi.obs.api.ScheduledPaymentDto;
import cz.muni.fi.obs.data.dbo.ScheduledPayment;
import org.mapstruct.Mapper;

@Mapper
public interface ScheduledPaymentMapper {

    ScheduledPaymentDto toDto(ScheduledPayment scheduledPayment);
}
