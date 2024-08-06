import java.util.HashMap;
import java.util.Map;

// Account class representing a bank account
class Account {

    private String accountName;
    private String accountId;
    private double balance;

    public Account(String accountName, String accountId, double initialBalance) {
        this.accountName = accountName;
        this.accountId = accountId;
        this.balance = initialBalance;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited $" + amount + " into account " + accountId);
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawn $" + amount + " from account " + accountId);
        } else {
            System.out.println("Insufficient funds in account " + accountId);
        }
    }
}

// Main class // to manage accounts and transactions
public class BankingApp {
    private Map<String, Account> accounts;

    public BankingApp() {
        this.accounts = new HashMap<>();
    }

    public void createAccount(String accountName, String accountId, double initialBalance) {
        if (!accounts.containsKey(accountId)) {
            Account newAccount = new Account(accountName, accountId, initialBalance);
            accounts.put(accountId, newAccount);
            System.out.println("Created account " + accountId + " with initial balance $" + initialBalance);
        } else {
            System.out.println("Account " + accountId + " already exists.");
        }
    }

    public void transfer(String fromAccountId, String toAccountId, double amount) {
        if (accounts.containsKey(fromAccountId) && accounts.containsKey(toAccountId)) {
            Account fromAccount = accounts.get(fromAccountId);
            Account toAccount = accounts.get(toAccountId);
            
            if (fromAccount.getBalance() >= amount) {
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);
                System.out.println("Transferred $" + amount + " from account " + fromAccountId + " to account " + toAccountId);
            } else {
                System.out.println("Insufficient funds in account " + fromAccountId + " for transfer.");
            }
        } else {
            System.out.println("One or both accounts do not exist.");
        }
    }

    public void deposit(String accountId, double amount) {
        if (accounts.containsKey(accountId)) {
            Account account = accounts.get(accountId);
            account.deposit(amount);
        } else {
            System.out.println("Account " + accountId + " does not exist.");
        }
    }



    // Main method to test the bank operations
    public static void main(String[] args) {
        BankingApp bank = new BankingApp();

        // Create accounts
        bank.createAccount("John Doe", "@johndoe", 1000);
        bank.createAccount("Jane Doe", "@janedoe", 500);

        // Deposit money
        bank.deposit("@johndoe", 200);

        // Transfer money
        bank.transfer("@johndoe", "@janedoe", 300);

        // Attempt transfer with insufficient funds
        bank.transfer("@janedoe", "@johndoe", 1000); // This should fail

        // Print final balances
        System.out.println("Final balances:");
        System.out.println("Account @johndoe balance: $" + bank.accounts.get("@johndoe").getBalance());
        System.out.println("Account @janedoe balance: $" + bank.accounts.get("@janedoe").getBalance());
    }
}

