package io.swagger.service;

import io.swagger.model.User;
import io.swagger.repository.TransactionRepository;
import io.swagger.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public void SaveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.getOne(transactionId);
    }

    public List<Transaction> getTransactionsByUser(User user) {
        return transactionRepository.getAllByUserPerforming(user);
    }
}
