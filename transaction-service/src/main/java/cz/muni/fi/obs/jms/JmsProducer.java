package cz.muni.fi.obs.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JmsProducer {

    private final JmsTemplate jmsTemplate;
    public final static String TRANSACTION_QUEUE_NAME = "transactions";


    @Autowired
    public JmsProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(String transactionId) {
        log.info("Attempting to send message: " + transactionId);
        jmsTemplate.convertAndSend(TRANSACTION_QUEUE_NAME, transactionId);
    }
}
