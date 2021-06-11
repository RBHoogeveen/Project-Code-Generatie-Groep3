package io.swagger.service;

import io.swagger.model.*;
import io.swagger.model.DTO.CreateUpdateAccountDTO;
import io.swagger.repository.*;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private WithdrawalService withdrawalService;

    private Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    @Autowired
    AccountRepository accountService;


    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    //get saving account by userid
    public Account getSavingAccountByUserId(Integer userId) {
        return accountRepository.getCorrectAccountByUserId(userId, true);
    }

    //get current account by userid
    public Account getCurrentAccountByUserId(Integer userId) {
        return accountRepository.getCorrectAccountByUserId(userId, false);
    }

    //get account by iban
    public Account getUserAccountByIban(String iban) {
        return accountRepository.getAccountByIban(iban);
    }

    //method to get savings account
    public Account getSavingsAccountByIban(String iban) {
        return accountRepository.getCorrectAccountByIban(iban, true);
    }

    //method to get current account
    public Account getCurrentAccountByIban(String iban) {
        return accountRepository.getCorrectAccountByIban(iban, false);
    }

    //method to check if the amount will not be over day limit
    public boolean UnderDayLimit(BigDecimal amount, User performerUser) {
        //get all previous transactions
        List<Transaction> transactions = transactionService.getTransactionsByUser(performerUser);

        //if no previous transactions then it is automatically under day limit
        if (transactions.size() == 0)
            return false;

        //get current date
        String currentDate = convertNowToString();

        //set day limit and day spent
        BigDecimal daySpent = new BigDecimal(0);
        BigDecimal dayLimit = performerUser.getDayLimit();

        //calculate total day spent of the transactions on a specific day
        for (Transaction transaction : transactions) {
            if (transaction.getDate().compareTo(currentDate) == 0) {
                daySpent = daySpent.add(transaction.getAmount());
            }
        }

        //check if the day spent stays below the day limit
        if (daySpent.add(amount).compareTo(dayLimit) >= 0)
            return true;
        return false;
    }

    //method to update the balance
    public void updateBalance(Account performerAccount, Account receiverAccount, BigDecimal amount) {
        //get balance from performer account
        BigDecimal performerBalance = getBalanceByIban(performerAccount.getIban(), performerAccount.getType());

        //get balance from receiver account
        BigDecimal receiverBalance = getBalanceByIban(receiverAccount.getIban(), receiverAccount.getType());

        //take amount from performer and add to receiver
        BigDecimal newPerformerBalance = performerBalance.subtract(amount);
        BigDecimal newReceiverBalance = receiverBalance.add(amount);

        //update balance of performer
        accountRepository.UpdateBalance(newPerformerBalance, performerAccount.getIban(), performerAccount.getType());

        //update balance of receiver
        accountRepository.UpdateBalance(newReceiverBalance, receiverAccount.getIban(), receiverAccount.getType());
    }

    public BigDecimal getBalanceByIban(String iban, boolean accountType) {
        return accountRepository.getBalanceByIban(iban, accountType);
    }

    //method to check day limit en transaction limit of the user
    void CheckTransactionLimit(BigDecimal amount, User performerUser) throws Exception {
        //determine if the amount is higher than the transaction limit
        if (amount.compareTo(performerUser.getTransactionLimit()) > 0) {
            throw new Exception("Cannot make transaction, because the amount to be transferred is higher than the transaction limit");
        }
    }

    //method to get the performer account and receiver
    public Account[] GetPerformerAndReceiver(Integer performerUserId, String receiverIban, TransferType transferType) throws Exception {
        Account[] performerAndReceiver = new Account[2];
        switch (transferType) {
            //current to current
            case TYPE_TRANSACTION:
                performerAndReceiver[0] = getCurrentAccountByUserId(performerUserId);
                performerAndReceiver[1] = getCurrentAccountByIban(receiverIban);

                //check if the transaction is valid
                if (!ValidTransaction(performerAndReceiver))
                    throw new Exception("To make a transaction, the receiver account must have another iban");
                break;
            case TYPE_DEPOSIT:
                //current to savings
                performerAndReceiver[0] = getCurrentAccountByUserId(performerUserId);
                performerAndReceiver[1] = getSavingsAccountByIban(receiverIban);

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

        //check if the iban is not the same
        if (!performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            valid++;
        }

        //check if the performer is a current and the receiver a current
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
        if (!performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
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

        //check if the iban is not the same
        if (!performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            valid++;
        }

        //check if the performer is a savings and the receiver a current
        if (performerAndReceiver[0].getType() && !performerAndReceiver[1].getType()) {
            valid++;
        }

        //if both true then it is a valid withdrawal
        return valid == 2;
    }

    public boolean IbanAndAmountCheck(BigDecimal amount, String receiverIban) {
        //check first if the iban exists
        if (accountRepository.getIban(receiverIban).equals(""))
            return true;

        //check if amount is less than zero
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return true;
        return false;
    }

    //method to perform transaction
    public Transaction PerformTransaction(String performerUsername, BigDecimal amount, String receiverIban) throws Exception {
        if (IbanAndAmountCheck(amount, receiverIban)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Amount was below zero or the Iban was not found.");
        }

        //get user by username
        User performerUser = userRepository.findByUsername(performerUsername);

        if (UnderDayLimit(amount, performerUser)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You went over your day limit, so this transaction cannot be made");
        }

        //get the performer user (maybe user will be the parameter instead of userid in the future)
        Account[] performerAndReceiver = GetPerformerAndReceiver(performerUser.getId(), receiverIban, TransferType.TYPE_TRANSACTION);

        //determine if the performers balance is not below the absolute limit
        if (performerAndReceiver[0].getBalance().subtract(amount).compareTo(performerAndReceiver[0].getAbsoluteLimit()) < 0) {
            throw new Exception("The requested amount to be transferred is below the absolute limit, and thus not possible");
        }

        //check day limit and transaction limit of the user if it is a transaction
        if (!performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            CheckTransactionLimit(amount, performerUser);
        }

        //Make the transaction
        Transaction transaction = MakeTransaction(amount, performerAndReceiver[1], performerAndReceiver[0]);

        //save the transaction
        transactionService.SaveTransaction(transaction);

        //execute transaction
        updateBalance(performerAndReceiver[0], performerAndReceiver[1], amount);
        return transaction;
    }

    //method to perform deposit
    public Deposit PerformDeposit(String performerUsername, BigDecimal amount, String receiverIban) throws Exception {
        if (IbanAndAmountCheck(amount, receiverIban)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Amount was below zero or the Iban was not found.");
        }

        //get user by username
        User performerUser = userRepository.findByUsername(performerUsername);

        Account[] performerAndReceiver = GetPerformerAndReceiver(performerUser.getId(), receiverIban, TransferType.TYPE_DEPOSIT);

        //determine if the performers balance is not below the absolute limit
        if (performerAndReceiver[0].getBalance().subtract(amount).compareTo(performerAndReceiver[0].getAbsoluteLimit()) < 0) {
            throw new Exception("The requested amount to be transferred is below the absolute limit, and thus not possible");
        }

        //make the deposit
        Deposit deposit = MakeDeposit(amount, performerAndReceiver[1], performerAndReceiver[0]);

        //save the deposit
        depositService.SaveDeposit(deposit);

        //execute deposit
        updateBalance(performerAndReceiver[0], performerAndReceiver[1], amount);
        return deposit;
    }

    //method to perform deposit
    public Withdrawal PerformWithdrawal(String performerUsername, BigDecimal amount, String receiverIban) throws Exception {
        if (IbanAndAmountCheck(amount, receiverIban)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Amount was below zero or the Iban was not found.");
        }

        //get user by username
        User performerUser = userRepository.findByUsername(performerUsername);

        Account[] performerAndReceiver = GetPerformerAndReceiver(performerUser.getId(), receiverIban, TransferType.TYPE_WITHDRAW);

        //determine if the performers balance is not below the absolute limit
        if (performerAndReceiver[0].getBalance().subtract(amount).compareTo(performerAndReceiver[0].getAbsoluteLimit()) < 0) {
            throw new Exception("The requested amount to be transferred is below the absolute limit, and thus not possible");
        }

        //make the deposit
        Withdrawal withdrawal = MakeWithdrawal(amount, performerAndReceiver[1], performerAndReceiver[0]);

        //save the deposit
        withdrawalService.SaveWithdrawal(withdrawal);

        //execute deposit
        updateBalance(performerAndReceiver[0], performerAndReceiver[1], amount);
        return withdrawal;
    }

    public Account updateAccount(CreateUpdateAccountDTO createUpdateAccount) {
        Account updatedAccount;
        if (userRepository.findByUsername(createUpdateAccount.getUsername()) != null) {
            if (!createUpdateAccount.getType()) {
                updatedAccount = accountRepository.getCurrentAccountByUserId(userRepository.getUserIdByUsername(createUpdateAccount.getUsername()));
            } else if (createUpdateAccount.getType()) {
                updatedAccount = accountRepository.getSavingsAccountByUserId(userRepository.getUserIdByUsername(createUpdateAccount.getUsername()));
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Can't find users correct account.");
            }
            if (createUpdateAccount.getAbsoluteLimit().compareTo(BigDecimal.ZERO) >= 0) {
                updatedAccount.setAbsoluteLimit(createUpdateAccount.getAbsoluteLimit());
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Absolute limit can't be lower than 0");
            }
            if (userRepository.findByUsername(createUpdateAccount.getUsername()) != null) {
                updatedAccount.setUser(userRepository.findByUsername(createUpdateAccount.getUsername()));
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Can't find user with given username.");
            }
            if (createUpdateAccount.getBalance().compareTo(updatedAccount.getAbsoluteLimit()) >= 0) {
                updatedAccount.setBalance(createUpdateAccount.getBalance());
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "New balance is below absolute limit");
            }
            updatedAccount.setIsActive(createUpdateAccount.getIsActive());
            accountRepository.save(updatedAccount);
            return updatedAccount;
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User does not exist.");
        }
    }

    public Account add(CreateUpdateAccountDTO createUpdateAccount) {
        if ((accountRepository.getCurrentAccountByUserId(userRepository.getUserIdByUsername(createUpdateAccount.getUsername())) == null) || (accountRepository.getSavingsAccountByUserId(userRepository.getUserIdByUsername(createUpdateAccount.getUsername())) == null)) {
            Account newAccount = new Account();
            newAccount.setIban(generateIban());
            newAccount.setType(createUpdateAccount.getType());
            if (createUpdateAccount.getAbsoluteLimit().compareTo(BigDecimal.ZERO) >= 0) {
                newAccount.setAbsoluteLimit(createUpdateAccount.getAbsoluteLimit());
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Absolute limit can't be lower than 0");
            }
            if (userRepository.findByUsername(createUpdateAccount.getUsername()) != null) {
                newAccount.setUser(userRepository.findByUsername(createUpdateAccount.getUsername()));
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Can't find user with given username.");
            }
            if (createUpdateAccount.getBalance().compareTo(createUpdateAccount.getAbsoluteLimit()) >= 0) {
                newAccount.setBalance(createUpdateAccount.getBalance());
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "New balance is below absolute limit");
            }
            newAccount.setIsActive(createUpdateAccount.getIsActive());
            accountRepository.save(newAccount);
            return newAccount;
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already has both account types.");
        }
    }

    public String generateIban() {
        Iban iban = new Iban.Builder().countryCode(CountryCode.NL).bankCode("INHO").buildRandom();
        while (accountRepository.getIban(iban.toString()) != null) {
            iban = new Iban.Builder().countryCode(CountryCode.NL).bankCode("INHO").buildRandom();
        }
        return iban.toString();
    }

    public void createCurrentAccount(String username) {
        Account newAccount = new Account();
        newAccount.setIban(generateIban());
        newAccount.setType(false);
        newAccount.setUser(userRepository.findByUsername(username));
        newAccount.setAbsoluteLimit(BigDecimal.ZERO);
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setIsActive(true);
        accountRepository.save(newAccount);
    }

    public void createSavingsAccount(String username) {
        Account newAccount = new Account();
        newAccount.setIban(generateIban());
        newAccount.setType(true);
        newAccount.setUser(userRepository.findByUsername(username));
        newAccount.setAbsoluteLimit(BigDecimal.ZERO);
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setIsActive(true);
        accountRepository.save(newAccount);
    }

    //method to perform a transaction
    public Transaction MakeTransaction(BigDecimal amount, Account receiverAccount, Account performerAccount) {
        //prepare the transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        String timeOfTransaction = convertNowToString();
        transaction.setDate(timeOfTransaction);
        transaction.setUserPerforming(performerAccount.getUser());
        transaction.setFromAccount(performerAccount);
        transaction.setToAccount(receiverAccount);
        transaction.setTransferType(TransferType.TYPE_TRANSACTION);
        return transaction;
    }

    //method to perform a deposit
    public Deposit MakeDeposit(BigDecimal amount, Account receiverAccount, Account performerAccount) {
        //prepare the withdrawal
        Deposit deposit = new Deposit();
        deposit.setAmount(amount);
        String timeOfDeposit = convertNowToString();
        deposit.setDate(timeOfDeposit);
        deposit.setUserPerforming(performerAccount.getUser());
        deposit.setFromAccount(performerAccount);
        deposit.setToAccount(receiverAccount);
        deposit.setTransferType(TransferType.TYPE_DEPOSIT);
        return deposit;
    }

    //method to perform a withdrawal
    public Withdrawal MakeWithdrawal(BigDecimal amount, Account receiverAccount, Account performerAccount) {
        //prepare the withdrawal
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(amount);
        String timeOfWithdrawal = convertNowToString();
        withdrawal.setDate(timeOfWithdrawal);
        withdrawal.setUserPerforming(performerAccount.getUser());
        withdrawal.setFromAccount(performerAccount);
        withdrawal.setToAccount(receiverAccount);
        withdrawal.setTransferType(TransferType.TYPE_WITHDRAW);
        return withdrawal;
    }

    //method to convert now to string
    public String convertNowToString() {
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        Date now = Calendar.getInstance().getTime();
        return df.format(now);
    }

    public List<?> getTransferHistory() {
        List<Object> transfers = new ArrayList<>();
        if (transactionRepository.getTransactionsByUser(userRepository.getUserIdByUsername(authentication.getName())) != null) {
            transfers.addAll(transactionService.getTransactionHistory());
        }
        if (depositRepository.getDepositsByUser(userRepository.getUserIdByUsername(authentication.getName())) != null) {
            transfers.addAll(depositService.getDepositHistory());
        }
        if (withdrawalRepository.getWithdrawalsByUser(userRepository.getUserIdByUsername(authentication.getName())) != null){
            transfers.addAll(withdrawalService.getWithdrawalHistory());
        }
        if (!transfers.isEmpty()){
            return transfers;
        }
        else {throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No transfers found.");}
    }

    public List<Account> getUserAccounts(String username) {
        List<Account> accounts = new ArrayList<>();
        if (accountRepository.getCurrentAccountByUserId(userRepository.getUserIdByUsername(username)) != null){
            accounts.add(accountRepository.getCurrentAccountByUserId(userRepository.getUserIdByUsername(username)));
        }
        if (accountRepository.getSavingsAccountByUserId(userRepository.getUserIdByUsername(username)) != null){
            accounts.add(accountRepository.getSavingsAccountByUserId(userRepository.getUserIdByUsername(username)));
        }
        if(!accounts.isEmpty()){
            return accounts;
        }
        else {throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No accounts found for user: " + username);}
    }
}
