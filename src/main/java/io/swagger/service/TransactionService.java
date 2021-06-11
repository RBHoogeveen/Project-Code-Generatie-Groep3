package io.swagger.service;

import io.swagger.repository.TransactionRepository;
import io.swagger.model.Transaction;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    private Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    public void SaveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.getOne(transactionId);
    }

    public List<Transaction> getTransactionsByUser(Integer userId) {
        return transactionRepository.getAllByUserPerforming_Id(userId);
    }

    public List<Transaction> getTransactionHistory() {
        List<Transaction> transactions = transactionRepository.getAllByUserPerforming_Id(userRepository.getUserIdByUsername(authentication.getName()));
        if (!transactions.isEmpty()){
            return transactions;
        }
        else { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No transactions found"); }
    }
}
