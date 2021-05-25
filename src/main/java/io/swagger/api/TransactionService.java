package io.swagger.api;

import io.swagger.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;


    public void SaveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
