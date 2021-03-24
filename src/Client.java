import java.util.HashMap;

public class Client implements Runnable {
    HashMap<Company, Float> shares;
    float balance;
    String name;
    Company target;
    StockExchange stockExchange;

    Client() {
    }

    Client(HashMap<Company, Float> shares, float balance, String name, Company target) {
        this.shares = shares;
        this.balance = balance;
        this.name = name;
        this.target = target;
    }

    public HashMap<Company, Float> getStocks() {
        return shares;
    }

    public void setStocks(Company company, float numberOfShares) {
        shares.replace(company, numberOfShares);
    }

    public void modifyCompanyStocksBy(Company company, float numberOfShares) {
        if (this.getStocks().get(company) != null) {
            this.setStocks(company, this.getStocks().get(company) + numberOfShares);
        } else {
            this.setStocks(company, numberOfShares);
        }
    }

    public boolean buy(Company company, float numberOfShares) throws InterruptedException {
        return stockExchange.buy(this, company, numberOfShares);
    }

    public boolean sell(Company company, float numberOfShares) throws InterruptedException {
        // check if we have any shares for that company and also if we have enough of them
        if (this.getStocks().get(company) != null && this.getStocks().get(company) >= numberOfShares)
            return stockExchange.sell(this, company, numberOfShares);
        //TODO: race condition
        else return false;
    }

    public boolean buyLow(Company company, float numberOfShares, float limit) throws InterruptedException {
        if (this.balance < numberOfShares * limit) return false;

        while (company.getPrice() > limit) {
            wait(1000);
        }
        return buy(company, numberOfShares);
        //TODO: race condition
    }


    public boolean sellHigh(Company company, float numberOfShares, float limit) throws InterruptedException {
        if (this.getStocks().get(company) == null || this.getStocks().get(company) < numberOfShares) return false;

        while (company.getPrice() < limit) {
            wait(1000);
        }
        return buy(company, numberOfShares);
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

    // todo create getter for balance

    public void modifyBalanceBy(float transactionPrice) {
        this.balance -= transactionPrice;
    }

    public void setStockExchange(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    @Override
    public void run() {

        double rand = Math.random();
        long time = (long) (Math.random() * 100);
        System.out.println(name + " will wait for " + time);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " has woken up");
        try {
            System.out.println(name + " is attempting to buy 4 shares from tesla");
            Thread.sleep(1000);
            boolean result = buy(target, 4);
            if (result) {
                System.out.println(name + " has succeded");
            } else System.out.println(name + " has failed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
