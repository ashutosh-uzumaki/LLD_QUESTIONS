package DigitalWallet;

import DigitalWallet.models.Money;
import DigitalWallet.models.User;

import java.math.BigDecimal;

public class DemoV1 {
    public static void main(String[] args) {
        // Create users
        User alice = new User("U1", "Alice");
        alice.createWallet("W1");

        User bob = new User("U2", "Bob");
        bob.createWallet("W2");

        // Test 1: Add money
        alice.getWallet().addMoney(new Money(new BigDecimal("1000"), "INR"));
        System.out.println("Alice Balance: " + alice.getWallet().getBalance().getAmount());
        // Expected: 1000

        // Test 2: Transfer
        boolean success = alice.getWallet().transfer(
                bob.getWallet(),
                new Money(new BigDecimal("200"), "INR")
        );
        System.out.println("Transfer Success: " + success);
        // Expected: true

        // Test 3: Verify balances
        System.out.println("Alice: " + alice.getWallet().getBalance().getAmount()); // 800
        System.out.println("Bob: " + bob.getWallet().getBalance().getAmount());     // 200

        // Test 4: View history
        System.out.println("Alice History: " + alice.getWallet().getHistory().size());
        System.out.println(alice.getWallet().getHistory());// 1
    }
}