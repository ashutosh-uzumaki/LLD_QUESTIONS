package DigitalWallet;

import DigitalWallet.models.*;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoV2 {
    public static void main(String[] args) throws InterruptedException {
        // 1. Create Users
        User alice = new User("U1", "Alice");
        alice.createWallet("W1"); // Wallet ID pass karni hogi

        User bob = new User("U2", "Bob");
        bob.createWallet("W2");

        // 2. Add initial money
        alice.getWallet().addMoney(new Money(new BigDecimal("10000"), "INR"));

        System.out.println("Initial Alice Balance: " + alice.getWallet().getBalance().getAmount());

        // 3. Create Thread Pool (5 threads)
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 4. Submit 5 transfer tasks (Each transfers ₹100)
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                boolean success = alice.getWallet().transfer(
                        bob.getWallet(),
                        new Money(new BigDecimal("100"), "INR")
                );
                System.out.println("Transfer Success: " + success);
            });
        }

        // 5. Shutdown and wait
        executor.shutdown();
        executor.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);

        // 6. Final Verification
        System.out.println("Final Alice Balance: " + alice.getWallet().getBalance().getAmount());
        System.out.println("Final Bob Balance: " + bob.getWallet().getBalance().getAmount());
        System.out.println("Alice History Size: " + alice.getWallet().getHistory().size());

        // Expected: Alice = 9500, Bob = 500, History = 5
    }
}