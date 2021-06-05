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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    public boolean UnderDayLimit(BigDecimal amount, TransferType transferType, User performerUser) {
        List<Transaction> transactions;
        List<Deposit> deposits;
        List<Withdrawal> withdrawals;

        //make an empty account to access the time method
        Account account = new Account();
        String now = convertNowToString();

        BigDecimal daySpent;
        BigDecimal dayLimit;
        switch (transferType) {
            case TYPE_TRANSACTION:
                transactions = transactionService.getTransactionsByUser(performerUser.getId());
                if (transactions.size() == 0)
                    return false;

                //get the day spent and the day limit of the user
                daySpent = transactions.get(0).getUserPerforming().getDaySpent();
                dayLimit = transactions.get(0).getUserPerforming().getDaySpent();

                //calculate total day spent of the transactions on a specific day
                for (Transaction transaction : transactions) {
                    if (transaction.getDate().compareTo(now) == 0) {
                        daySpent = daySpent.add(transaction.getAmount());
                    }
                }
                break;

            case TYPE_DEPOSIT:
                deposits = depositService.getDepositsByUser(performerUser.getId());
                if (deposits.size() == 0)
                    return false;

                //get the day spent and the day limit of the user
                daySpent = deposits.get(0).getUserPerforming().getDaySpent();
                dayLimit = deposits.get(0).getUserPerforming().getDaySpent();

                //calculate total day spent of the transactions on a specific day
                for (Deposit deposit : deposits) {
                    if (deposit.getDate().compareTo(now) == 0) {
                        daySpent = daySpent.add(deposit.getAmount());
                    }
                }
                break;

            case TYPE_WITHDRAW:
                withdrawals = withdrawalService.getWithdrawalsByUser(performerUser.getId());
                if (withdrawals.size() == 0)
                    return false;

                //get the day spent and the day limit of the user
                daySpent = withdrawals.get(0).getUserPerforming().getDaySpent();
                dayLimit = withdrawals.get(0).getUserPerforming().getDaySpent();

                //calculate total day spent of the transactions on a specific day
                for (Withdrawal withdrawal : withdrawals) {
                    if (withdrawal.getDate().compareTo(now) == 0) {
                        daySpent = daySpent.add(withdrawal.getAmount());
                    }
                }
                break;

            default:
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Something went wrong while processing the transfer");
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

        //update day spent of performer
        BigDecimal daySpent = getDaySpent(performerAccount.getUser().getId());
        BigDecimal newDaySpent = daySpent.add(amount);
        userService.updateDaySpent(performerAccount.getUser().getId(), newDaySpent);

        //update balance of receiver
        accountRepository.UpdateBalance(newReceiverBalance, receiverAccount.getIban(), receiverAccount.getType());
    }

    public BigDecimal getDaySpent(Integer userId) {
        return userService.getDaySpent(userId);
    }

    public BigDecimal getBalanceByIban(String iban, boolean accountType) {
        return accountRepository.getBalanceByIban(iban, accountType);
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

        //check if the iban is the same TODO weer terug veranderen
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

    //method to check if the account is a current or savings
    public boolean CheckAccount(Account account) {
        //false = current account, true = savings account
        return account.getType();
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

        if (UnderDayLimit(amount, TransferType.TYPE_TRANSACTION, performerUser)) {
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
            CheckDayLimitAndTransactionLimit(amount, performerUser);
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

        if (UnderDayLimit(amount, TransferType.TYPE_DEPOSIT, performerUser)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You went over your day limit, so this deposit cannot be made");
        }

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

        if (UnderDayLimit(amount, TransferType.TYPE_WITHDRAW, performerUser)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You went over your day limit, so this withdrawal cannot be made");
        }

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
        if (!createUpdateAccount.getType()) {
            updatedAccount = accountRepository.getCurrentAccountByUsername(createUpdateAccount.getUsername());
        } else if (createUpdateAccount.getType()) {
            updatedAccount = accountRepository.getSavingsAccountByUsername(createUpdateAccount.getUsername());
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
    }

    public Account add(CreateUpdateAccountDTO createUpdateAccount) {
        if ((accountRepository.getCurrentAccountByUsername(createUpdateAccount.getUsername()) == null) || (accountRepository.getSavingsAccountByUsername(createUpdateAccount.getUsername()) == null)) {
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
        String timeOfWithdrawel = convertNowToString();
        withdrawal.setDate(timeOfWithdrawel);
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
        String timeOfTransaction = df.format(now);
        return timeOfTransaction;
    }
}
