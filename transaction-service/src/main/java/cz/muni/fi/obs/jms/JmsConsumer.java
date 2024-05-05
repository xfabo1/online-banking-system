package cz.muni.fi.obs.jms;

import cz.muni.fi.obs.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;

import static cz.muni.fi.obs.jms.JmsProducer.TRANSACTION_QUEUE_NAME;

@Slf4j
public class JmsConsumer {

    private final TransactionService transactionService;

    public JmsConsumer(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @JmsListener(destination = TRANSACTION_QUEUE_NAME, concurrency = "1-4")
    public void listenTransactions(String transactionId) {
        log.info("Executing transaction with id: " + transactionId);
        transactionService.executeTransaction(transactionId);
    }
}
