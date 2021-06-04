package io.swagger.service;

import io.swagger.model.*;
import io.swagger.model.DTO.CreateUpdateAccountDTO;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    DepositService depositService;

    @Autowired
    WithdrawalService withdrawalService;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    //get account by userid
    public Account getUserAccountById(Long userId) {
        return accountRepository.getAccountByUserId(userId);
    }

    //get saving account by userid
    public Account getSavingAccountByUserId(Long userId) {
        return accountRepository.getCorrectAccountByUserId(userId, true);
    }

    //get current account by userid
    public Account getCurrentAccountByUserId(Long userId) {
        return accountRepository.getCorrectAccountByUserId(userId, false);
    }

    //get account by iban
    public Account getUserAccountByIban(String iban) {
        return accountRepository.getAccountByIban(iban);
    }

    //method to get savingsaccount
    public Account getSavingsAccountByIban(String iban) {
        return accountRepository.getCorrectAccountByIban(iban, true);
    }

    //method to get currentaccount
    public Account getCurrentAccountByIban(String iban) {
        return accountRepository.getCorrectAccountByIban(iban, false);
    }

    public void updateBalance(Account performerAccount, Account receiverAccount, BigDecimal amount) {
        try {
            //get balance from performer account
            BigDecimal performerBalance = getBalanceByIban(performerAccount.getIban());

            //get balance from receiver account
            BigDecimal receiverBalance = getBalanceByIban(receiverAccount.getIban());

            //take amount from performer and add to receiver
            BigDecimal newPerformerBalance = performerBalance.subtract(amount);
            BigDecimal newReceiverBalance = receiverBalance.add(amount);

            //update balance of performer
            accountRepository.UpdateBalance(performerAccount.getIban(), newPerformerBalance);

            //update day spent of performer
            BigDecimal daySpent = getDaySpent(performerAccount.getUser().getId());
            BigDecimal newDaySpent = daySpent.add(amount);
            updateDaySpent(performerAccount.getUser().getId(), newDaySpent);

            //update balance of receiver
            accountRepository.UpdateBalance(receiverAccount.getIban(), newReceiverBalance);
        } catch (Exception e) {
            //TODO exception handling
        }
    }

    public BigDecimal getDaySpent(Integer userId) {
        return userService.getDaySpent(userId);
    }

    public void updateDaySpent(Integer userId, BigDecimal newDaySpent) {
        userService.updateDaySpent(userId, newDaySpent);
    }

    public BigDecimal getBalanceByIban(String iban) {
        return accountRepository.getBalanceByIban(iban);
    }

    //method to check day limit en transaction limit of the user
    void CheckDayLimitAndTransactionLimit(BigDecimal amount, User performerUser) throws Exception {
        //determine if the user spent below the day limit
        if (performerUser.getDaySpent().compareTo(performerUser.getDayLimit()) > 0) {
            throw new Exception("Cannot make transaction, because you already spent more than your day limit");
        }

        //determine if the amount is higher than the transaction limit
        if (amount.compareTo(performerUser.getTransactionLimit()) > 0) {
            throw new Exception("Cannot make transaction, because the amount to be transferred is higher than the transaction limit");
        }
    }

    //method to get the performer account and receiver
    public Account[] GetPerformerAndReceiver(Long performerUserId, String receiverIban, TransferType transferType) throws Exception {
        Account[] performerAndReceiver = new Account[2];
        switch (transferType) {
            //current to current
            case TYPE_TRANSACTION:
                performerAndReceiver[0] = getCurrentAccountByUserId(performerUserId);
                performerAndReceiver[1] =  getCurrentAccountByIban(receiverIban);

                //check if the transaction is valid
                if (!ValidTransaction(performerAndReceiver))
                    throw new Exception("To make a transaction, the receiver account must have another iban");
                break;
            case TYPE_DEPOSIT:
                //current to savings
                performerAndReceiver[0] = getCurrentAccountByUserId(performerUserId);
                performerAndReceiver[1] =  getSavingsAccountByIban(receiverIban);

                //check if the deposit is valid
                if (!ValidDeposit(performerAndReceiver))
                    throw new Exception("You cannot transfer money directly from your current account to another account's savings account");

                break;
            case TYPE_WITHDRAW:
                //savings to current
                performerAndReceiver[0] = getSavingAccountByUserId(performerUserId);
                performerAndReceiver[1] = getCurrentAccountByIban(receiverIban);

                //check if the withdrawal is valid
                if (!ValidWithdrawal(performerAndReceiver))
                    throw new Exception("You cannot transfer money directly from your savings account to another account's current account");
                break;
        }
        return performerAndReceiver;
    }

    //method to determine validity of the transaction
    private boolean ValidTransaction(Account[] performerAndReceiver) {
        int valid = 0;

        //check if the iban is the same
        if (!performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            valid++;
        }

        //check if the performer is a current and the receiver a savings
        if (!performerAndReceiver[0].getType() && !performerAndReceiver[1].getType()) {
            valid++;
        }

        //if both true then it is a valid withdrawal
        return valid == 2;
    }

    //method to determine validity of the deposit
    private boolean ValidDeposit(Account[] performerAndReceiver) {
        int valid = 0;

        //check if the iban is the same
        if (performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            valid++;
        }

        //check if the performer is a current and the receiver a savings
        if (!performerAndReceiver[0].getType() && performerAndReceiver[1].getType()) {
            valid++;
        }

        //if both true then it is a valid withdrawal
        return valid == 2;
    }

    //method to determine validity of the withdrawal
    private boolean ValidWithdrawal(Account[] performerAndReceiver) {
        int valid = 0;

        //check if the iban is the same
        if (performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            valid++;
        }

        //check if the performer is a savings and the receiver a current
        if (performerAndReceiver[0].getType() && !performerAndReceiver[1].getType()) {
            valid++;
        }

        //if both true then it is a valid withdrawal
        return valid == 2;
    }

    //method to check if the account is a current or savings
    public boolean CheckAccount(Account account) {
        //false = current account, true = savings account
        return account.getType();
    }

    //TODO manier vinden om dayspent te resetten
    public Transaction PerformTransaction(Long performerUserId, BigDecimal amount, String receiverIban) throws Exception {
        //check first if the iban exists
        if (accountRepository.getIban(receiverIban).equals("")) {
            throw new Exception("Target Iban was not found, maybe the account you are looking for does not exist");
        }
        //get the performer user (maybe user will be the parameter instead of userid in the future)
        User performerUser = userService.getUserById(performerUserId);

        Account[] performerAndReceiver = GetPerformerAndReceiver(performerUserId, receiverIban, TransferType.TYPE_TRANSACTION);

        //determine if the performers balance is not below the absolute limit
        if (performerAndReceiver[0].getBalance().subtract(amount).compareTo(performerAndReceiver[0].getAbsoluteLimit()) < 0) {
            throw new Exception("The requested amount to be transferred is below the absolute limit, and thus not possible");
        }

        //check day limit and transaction limit of the user if it is a transaction
        if (!performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            CheckDayLimitAndTransactionLimit(amount, performerUser);
        }

        //Make the transaction
        Transaction transaction = performerAndReceiver[0].MakeTransaction(amount, performerAndReceiver[1], performerAndReceiver[0]);

        //save the transaction
        transactionService.SaveTransaction(transaction);

        //execute transaction
        updateBalance(performerAndReceiver[0], performerAndReceiver[1], amount);
        return transaction;
    }

    //method to perform deposit
    public Deposit PerformDeposit(Long performerUserId, BigDecimal amount, String receiverIban) throws Exception {
        //check first if the iban exists
        if (accountRepository.getIban(receiverIban).equals("")) {
            throw new Exception("Target Iban was not found, maybe the account you are looking for does not exist");
        }
        //get the performer user (maybe user will be the parameter instead of userid in the future)
        User performerUser = userService.getUserById(performerUserId);

        Account[] performerAndReceiver = GetPerformerAndReceiver(performerUserId, receiverIban, TransferType.TYPE_DEPOSIT);

        //determine if the performers balance is not below the absolute limit
        if (performerAndReceiver[0].getBalance().subtract(amount).compareTo(performerAndReceiver[0].getAbsoluteLimit()) < 0) {
            throw new Exception("The requested amount to be transferred is below the absolute limit, and thus not possible");
        }

        //check day limit and transaction limit of the user if it is a transaction
        if (!performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            CheckDayLimitAndTransactionLimit(amount, performerUser);
        }

        //make the deposit
        Deposit deposit = performerAndReceiver[0].MakeDeposit(amount, performerAndReceiver[1], performerAndReceiver[0]);

        //save the deposit
        depositService.SaveDeposit(deposit);

        //execute deposit
        updateBalance(performerAndReceiver[0], performerAndReceiver[1], amount);
        return deposit;
    }

    //method to perform deposit
    public Withdrawal PerformWithdrawal(Long performerUserId, BigDecimal amount, String receiverIban) throws Exception {
        //check first if the iban exists
        if (accountRepository.getIban(receiverIban).equals("")) {
            throw new Exception("Target Iban was not found, maybe the account you are looking for does not exist");
        }
        //get the performer user (maybe user will be the parameter instead of userid in the future)
        User performerUser = userService.getUserById(performerUserId);

        Account[] performerAndReceiver = GetPerformerAndReceiver(performerUserId, receiverIban, TransferType.TYPE_WITHDRAW);

        //determine if the performers balance is not below the absolute limit
        if (performerAndReceiver[0].getBalance().subtract(amount).compareTo(performerAndReceiver[0].getAbsoluteLimit()) < 0) {
            throw new Exception("The requested amount to be transferred is below the absolute limit, and thus not possible");
        }

        //check day limit and transaction limit of the user if it is a transaction
        if (!performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            CheckDayLimitAndTransactionLimit(amount, performerUser);
        }

        //make the deposit
        Withdrawal withdrawal = performerAndReceiver[0].MakeWithdrawal(amount, performerAndReceiver[1], performerAndReceiver[0]);

        //save the deposit
        withdrawalService.SaveWithdrawal(withdrawal);

        //execute deposit
        updateBalance(performerAndReceiver[0], performerAndReceiver[1], amount);
        return withdrawal;
    }

    public Account updateAccount(CreateUpdateAccountDTO createUpdateAccount) {
        Account updatedAccount;
        if (!createUpdateAccount.getType()){
            updatedAccount = accountRepository.getCurrentAccountByUserId(userRepository.getUserIdByUsername(createUpdateAccount.getUsername()));
        }
        else if (createUpdateAccount.getType()){
            updatedAccount = accountRepository.getSavingsAccountByUserId(userRepository.getUserIdByUsername(createUpdateAccount.getUsername()));
        }
        else {throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Can't find users correct account.");}
        if (createUpdateAccount.getAbsoluteLimit().compareTo(BigDecimal.ZERO) >= 0){
            updatedAccount.setAbsoluteLimit(createUpdateAccount.getAbsoluteLimit());
        }
        else {throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Absolute limit can't be lower than 0");}
        if (userRepository.findByUsername(createUpdateAccount.getUsername()) != null){
            updatedAccount.setUser(userRepository.findByUsername(createUpdateAccount.getUsername()));
        }
        else {throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Can't find user with given username.");}
        if (createUpdateAccount.getBalance().compareTo(updatedAccount.getAbsoluteLimit()) >= 0) {
            updatedAccount.setBalance(createUpdateAccount.getBalance());
        }
        else {throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "New balance is below absolute limit");}
        updatedAccount.setIsActive(createUpdateAccount.getIsActive());
        accountRepository.save(updatedAccount);
        return updatedAccount;
    }

    public Account add(CreateUpdateAccountDTO createUpdateAccount) {
        if ((accountRepository.getCurrentAccountByUserId(userRepository.getUserIdByUsername(createUpdateAccount.getUsername())) == null) || (accountRepository.getSavingsAccountByUserId(userRepository.getUserIdByUsername(createUpdateAccount.getUsername())) == null)){
            Account newAccount = new Account();
            newAccount.setIban(generateIban());
            newAccount.setType(createUpdateAccount.getType());
            if (createUpdateAccount.getAbsoluteLimit().compareTo(BigDecimal.ZERO) >= 0){
                newAccount.setAbsoluteLimit(createUpdateAccount.getAbsoluteLimit());
            }
            else {throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Absolute limit can't be lower than 0");}
            if (userRepository.findByUsername(createUpdateAccount.getUsername()) != null){
                newAccount.setUser(userRepository.findByUsername(createUpdateAccount.getUsername()));
            }
            else {throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Can't find user with given username.");}
            if (createUpdateAccount.getBalance().compareTo(createUpdateAccount.getAbsoluteLimit()) >= 0) {
                newAccount.setBalance(createUpdateAccount.getBalance());
            }
            else {throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "New balance is below absolute limit");}
            newAccount.setIsActive(createUpdateAccount.getIsActive());
            accountRepository.save(newAccount);
            return newAccount;
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already has both account types.");
        }
    }

    public String generateIban(){
        Iban iban = new Iban.Builder().countryCode(CountryCode.NL).bankCode("INHO").buildRandom();
        while (accountRepository.getIban(iban.toString()) != null){
            iban = new Iban.Builder().countryCode(CountryCode.NL).bankCode("INHO").buildRandom();
        }
        return iban.toString();
    }

    public void createCurrentAccount(String username){
        Account newAccount = new Account();
        newAccount.setIban(generateIban());
        newAccount.setType(false);
        newAccount.setUser(userRepository.findByUsername(username));
        newAccount.setAbsoluteLimit(BigDecimal.ZERO);
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setIsActive(true);
        accountRepository.save(newAccount);
    }

    public void createSavingsAccount(String username){
        Account newAccount = new Account();
        newAccount.setIban(generateIban());
        newAccount.setType(true);
        newAccount.setUser(userRepository.findByUsername(username));
        newAccount.setAbsoluteLimit(BigDecimal.ZERO);
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setIsActive(true);
        accountRepository.save(newAccount);
    }
}
