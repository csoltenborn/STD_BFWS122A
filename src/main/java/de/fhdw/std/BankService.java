package de.fhdw.std;

import java.util.List;

public class BankService {
    private final BankDatabase database;

    public BankService(BankDatabase database) {
        if (database == null) {
            throw new IllegalArgumentException("database is null");
        }
        this.database = database;
    }

    public void deposit(String accountId, double amount) {
        BankAccount account = database.getAccount(accountId);
        account.deposit(amount);
        database.updateAccount(account);
        database.addTransaction(accountId, new Transaction("Deposit", amount, account.getBalance()));
    }

    public void withdraw(String accountId, double amount) {
        BankAccount account = database.getAccount(accountId);
        account.withdraw(amount);
        database.updateAccount(account);
        database.addTransaction(accountId, new Transaction("Withdraw", amount, account.getBalance()));
    }

    public void transfer(String fromAccountId, String toAccountId, double amount) {
        BankAccount fromAccount = database.getAccount(fromAccountId);
        BankAccount toAccount = database.getAccount(toAccountId);

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        database.updateAccount(fromAccount);
        database.updateAccount(toAccount);

        database.addTransaction(fromAccountId, new Transaction("Transfer Out", amount, fromAccount.getBalance()));
        database.addTransaction(toAccountId, new Transaction("Transfer In", amount, toAccount.getBalance()));
    }

    public void addInterest(String accountId) {
        BankAccount account = database.getAccount(accountId);
        if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.addInterest();
            database.updateAccount(savingsAccount);
            database.addTransaction(accountId, new Transaction("Interest", savingsAccount.getBalance() * savingsAccount.getInterestRate(), savingsAccount.getBalance()));
        }
    }

    public List<Transaction> generateStatement(String accountId) {
        return database.getTransactions(accountId);
    }
}