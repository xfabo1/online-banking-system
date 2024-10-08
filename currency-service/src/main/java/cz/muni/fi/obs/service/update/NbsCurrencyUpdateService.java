package cz.muni.fi.obs.service.update;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import cz.muni.fi.obs.data.dbo.Currency;
import cz.muni.fi.obs.data.dbo.ExchangeRate;
import cz.muni.fi.obs.data.repository.CurrencyRepository;
import cz.muni.fi.obs.data.repository.ExchangeRateRepository;
import cz.muni.fi.obs.exception.CsvFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@ConditionalOnProperty(prefix = "currency.auto-update.services.nbs", name = "url")
@Slf4j
public class NbsCurrencyUpdateService implements CurrencyUpdateService {

    private final ExchangeRateRepository exchangeRateRepository;

    private final CurrencyRepository currencyRepository;

    private final CSVParser nbsCsvParser;

    private final URI nbsCsvURI;

    @Autowired
    public NbsCurrencyUpdateService(CurrencyRepository currencyRepository,
                                    ExchangeRateRepository exchangeRateRepository,
                                    CSVParser nbsCsvParser,
                                    @Value("${currency.auto-update.services.nbs.url}") String currencyFileUrl) throws URISyntaxException {
        this.currencyRepository = currencyRepository;
        this.nbsCsvParser = nbsCsvParser;
        this.nbsCsvURI = new URI(currencyFileUrl);
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Scheduled(cron = "* * 1 * * *")
    @Transactional
    public void updateCurrencies() {
        log.info("Reading csv from nbs.sk url: " + nbsCsvURI);
        try (Reader reader = new InputStreamReader(nbsCsvURI.toURL().openStream())) {
            try (CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(nbsCsvParser).withSkipLines(0).build()) {
                List<String> header = Arrays.asList(csvReader.readNext());
                List<String> exchangeRates = Arrays.asList(csvReader.readNext());
                List<Currency> currencies = header.stream().skip(1).map(name -> new Currency(name, name)).toList();
                List<Double> exchangeRateValues = exchangeRates.stream().skip(1).map(rate -> Double.parseDouble(sanitizeNumberFormat(rate))).toList();
                log.info("Read: " + currencies.size() + " currencies. ");

                Currency euro = currencyRepository.findByCode("EUR").orElseGet(() -> {
                    Currency currency = new Currency("EUR", "euro");
                    return currencyRepository.save(currency);
                });
                for (int i = 0; i < exchangeRateValues.size(); i++) {
                    final int index = i;
                    Currency currency = currencyRepository.findByCode(currencies.get(index).getCode())
                            .orElseGet(() -> currencies.get(index));

                    currency = currencyRepository.save(currency);

                    Optional<ExchangeRate> currentExchangeRate = exchangeRateRepository
                            .findCurrentExchangeRate(euro.getId(), currency.getId(), Instant.now());

                    LocalDateTime now = LocalDateTime.now();
                    Instant validStart;
                    Instant validEnd = now.toLocalDate().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

                    if (currentExchangeRate.isPresent()) {
                        ExchangeRate exchangeRate = currentExchangeRate.get();
                        validStart = now.toInstant(ZoneOffset.UTC);
                        exchangeRate.setValidUntil(validStart);
                        exchangeRateRepository.save(exchangeRate);
                    } else {
                        validStart = now.toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC);
                    }

                    ExchangeRate exchangeRate = ExchangeRate.builder()
                            .from(euro)
                            .to(currency)
                            .conversionRate(exchangeRateValues.get(index))
                            .createdAt(validStart)
                            .validUntil(validEnd)
                            .build();

                    euro.getExchangeRatesFrom().add(exchangeRate);
                    currency.getExchangeRatesTo().add(exchangeRate);
                    exchangeRateRepository.save(exchangeRate);
                }
                currencyRepository.save(euro);
                log.info("Successfully updated: " + currencies.size() + " currencies.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV", e);
        } catch (CsvValidationException csvValidationException) {
            throw new CsvFormatException(csvValidationException);
        }
    }

    private String sanitizeNumberFormat(String number) {
        return number.replace(",", ".").replace(" ", "");
    }
}
