package cz.muni.fi.obs.etl.step.read.accounts;

import cz.muni.fi.obs.etl.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * TODO: read all accounts from transaction service, similar logic to TempAccountReader but use client here to fetch them
 * see {@link cz.muni.fi.obs.etl.step.create.facts.TempAccountReader}
 */
@Component
@StepScope
@Slf4j
public class AccountReader implements ItemReader<AccountDto> {

    private Instant instant = Instant.now();

    @Override
    public AccountDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }
}
