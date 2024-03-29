package fr.teama.transactionservice.components;

import fr.teama.transactionservice.interfaces.ITransactionSaver;
import fr.teama.transactionservice.models.transaction.Transaction;
import fr.teama.transactionservice.repository.transaction.TransactionRepository;
import fr.teama.transactionservice.services.RabbitMQProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionSaver implements ITransactionSaver {
    @Autowired
    RabbitMQProducerService rabbitMQProducerService;
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void debitAndSaveTransaction(Transaction transaction) {

        Transaction savedTransaction = transactionRepository.save(transaction);
        rabbitMQProducerService.sendBalanceMessage(transaction.getBankAccountId(), -1 * (transaction.getAmount()),savedTransaction.getId(),false);
        try{
            rabbitMQProducerService.sendTransactionMessage(savedTransaction,false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
