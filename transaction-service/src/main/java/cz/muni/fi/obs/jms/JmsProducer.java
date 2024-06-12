package cz.muni.fi.obs.jms;

import org.springframework.jms.core.JmsTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JmsProducer {

    private final JmsTemplate jmsTemplate;
    public final static String TRANSACTION_QUEUE_NAME = "transactions";

    public JmsProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(String transactionId) {
        log.info("Attempting to send message: " + transactionId);
        jmsTemplate.convertAndSend(TRANSACTION_QUEUE_NAME, transactionId);
    }
}
