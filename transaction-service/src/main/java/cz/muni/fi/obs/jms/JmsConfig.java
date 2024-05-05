package cz.muni.fi.obs.jms;

import cz.muni.fi.obs.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
@ConditionalOnProperty(prefix = "jms", name = "enabled", havingValue = "true")
@Slf4j
public class JmsConfig {

    @Bean
    public JmsProducer jmsProducer(JmsTemplate jmsTemplate) {
        log.info("Initializing real JMS producer.");
        return new JmsProducer(jmsTemplate);
    }

    @Bean
    public JmsConsumer jmsConsumer(TransactionService transactionService) {
        log.info("Initializing real JMS consumer.");
        return new JmsConsumer(transactionService);
    }
}
