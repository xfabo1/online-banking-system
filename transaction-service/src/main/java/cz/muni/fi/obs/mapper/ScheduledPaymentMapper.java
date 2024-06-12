package cz.muni.fi.obs.mapper;

import org.mapstruct.Mapper;

import cz.muni.fi.obs.api.ScheduledPaymentDto;
import cz.muni.fi.obs.data.dbo.ScheduledPayment;

@Mapper
public interface ScheduledPaymentMapper {

    ScheduledPaymentDto toDto(ScheduledPayment scheduledPayment);
}
