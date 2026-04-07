package DigitalWallet.models;

public class User {
    private final String id;
    private final String name;
    private Wallet wallet;

    public User(String id, String name){
        this.id = id;
        this.name = name;
        this.wallet = null;
    }

    public Wallet createWallet(String id){
        if(this.wallet == null){
            this.wallet = new Wallet(id);
        }
        return wallet;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
