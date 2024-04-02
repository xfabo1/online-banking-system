package cz.muni.fi.obs.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvConfig {

    @Bean
    public CSVParser nbsCsvParser() {
        return new CSVParserBuilder().withSeparator(';').withQuoteChar('"').withIgnoreQuotations(false).build();
    }
}
