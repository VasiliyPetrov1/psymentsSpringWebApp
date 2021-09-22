package org.kosiuk.webApp.service;

import org.kosiuk.webApp.entity.Transaction;
import org.kosiuk.webApp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction save(Transaction transaction) {
        transactionRepository.save(transaction);
        return transactionRepository.findById(transaction.getTransactionId()).get();
    }
}
