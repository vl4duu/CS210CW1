import java.util.HashMap;

public class Client {
    HashMap<Company, Float> shares;
    float balance;

    Client() {
    }

    public HashMap<Company, Float> getStocks() {
        return shares;
    }

    public void setStocks(HashMap<Company, Float> shares) {
        this.shares = shares;
    }

    public boolean buy(Company company, float numberOfShares) {
        return true;
    }

    public boolean sell(Company company, float numberOfShares) {
        return true;
    }

    public boolean buyLow(Company company, float numberOfShares, float limit) {
        return true;
    }

    public boolean sellHigh(Company company, float numberOfShares, float limit) {
        return true;
    }

    public boolean deposit(float amount) {
        this.balance += amount;
        return true;
    }

    public boolean withdraw(float amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            return true;
        } else return false;
    }



}
