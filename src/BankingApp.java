import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


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

    public Optional<String> deposit(double amount) {
        balance += amount;
        System.out.println("Deposited $" + amount + " into account " + accountId);
        return Optional.empty(); // Operation succeeded, return empty Optional
    }

    public Optional<String> withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawn $" + amount + " from account " + accountId);
            return Optional.empty(); // Operation succeeded, return empty Optional
        } else {
            return Optional.of("Insufficient funds in account " + accountId);
        }
    }
}

// Main class to manage accounts and transactions
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

    public Optional<String> transfer(String fromAccountId, String toAccountId, double amount) {
        Optional<Account> optionalFromAccount = Optional.ofNullable(accounts.get(fromAccountId));
        Optional<Account> optionalToAccount = Optional.ofNullable(accounts.get(toAccountId));

        if (optionalFromAccount.isPresent() && optionalToAccount.isPresent()) {
            Account fromAccount = optionalFromAccount.get();
            Account toAccount = optionalToAccount.get();

            Optional<String> withdrawalResult = fromAccount.withdraw(amount);
            if (withdrawalResult.isEmpty()) {
                Optional<String> depositResult = toAccount.deposit(amount);
                if (depositResult.isEmpty()) {
                    System.out.println("Transferred $" + amount + " from account " + fromAccountId + " to account " + toAccountId);
                    return Optional.empty(); // Operation succeeded, return empty Optional
                } else {
                    return depositResult; // Return error message from deposit operation
                }
            } else {
                return withdrawalResult; // Return error message from withdraw operation
            }
        } else {
            return Optional.of("One or both accounts do not exist.");
        }
    }

    public Optional<String> deposit(String accountId, double amount) {
        Optional<Account> optionalAccount = Optional.ofNullable(accounts.get(accountId));

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            return account.deposit(amount);
        } else {
            return Optional.of("Account " + accountId + " does not exist.");
        }
    }

    // Main method to test the bank operations
    public static void main(String[] args) {
        BankingApp bank = new BankingApp();

        // Create accounts
        bank.createAccount("John Doe", "@johndoe", 1000);
        bank.createAccount("Jane Doe", "@janedoe", 500);

        // Deposit money
        Optional<String> depositResult = bank.deposit("@johndoe", 200);
        depositResult.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Deposited $200 into account @johndoe")
        );

        // Transfer money
        Optional<String> transferResult = bank.transfer("@johndoe", "@janedoe", 300);
        transferResult.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Transferred $300 from account @johndoe to account @janedoe")
        );

        // Attempt transfer with insufficient funds
        Optional<String> failedTransferResult = bank.transfer("@janedoe", "@johndoe", 1000);
        failedTransferResult.ifPresent(System.out::println); // This should print an error message

        // Print final balances
        System.out.println("Final balances:");
        System.out.println("Account @johndoe balance: $" + bank.accounts.get("@johndoe").getBalance());
        System.out.println("Account @janedoe balance: $" + bank.accounts.get("@janedoe").getBalance());
    }
}
