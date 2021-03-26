import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class Client implements Runnable {
    HashMap<Company, Float> shares = new HashMap<>();
    float balance;
    String name;
    Company target;
    StockExchange stockExchange;
    int type;

    Client() {
    }

    /**
     * Constructor class used for testing purposes
     * @param name String
     * @param target Company
     * @param type type of client
     */
    Client(String name, Company target, int type) {
        this.name = name;
        this.target = target;
        this.type = type;
    }

    /**
     * return stocks list
     * @return HashMap
     */
    public HashMap<Company, Float> getStocks() {
        return shares;
    }

    /**
     * sets the The value of the given company to the given number
     *
     * @param company        company to be set
     * @param numberOfShares to be set
     */
    public void setStocks(Company company, float numberOfShares) {
        if (this.getStocks().get(company) == null) {
            this.getStocks().putIfAbsent(company, numberOfShares);
        } else {
            this.getStocks().replace(company, numberOfShares);
        }
    }

    /**
     * Modifies the givens company's value by the ammount given
     *
     * @param company to be modiefied
     * @param numberOfShares to modify by
     */
    public void modifyCompanyStocksBy(Company company, float numberOfShares) {
        this.setStocks(company, this.getStocks().get(company) + numberOfShares);
    }

    /**
     * Checks if the input is greater than 0 and initiates the buying sequence.
     *
     * @param company        to be bought from
     * @param numberOfShares the amount of shares to be bought
     * @return true if the transaction succeeded
     * @throws InterruptedException while acquiring
     */
    public boolean buy(Company company, float numberOfShares) throws InterruptedException {

        return numberOfShares > 0 && stockExchange.buy(this, company, numberOfShares);
    }

    /**
     * Checks if the input is greater than 0 and initiates the selling sequence.
     *
     * @param company        to be sold
     * @param numberOfShares the amount of share to be sold
     * @return true if the transaction succeeded
     * @throws InterruptedException while acquiring
     */
    public boolean sell(Company company, float numberOfShares) throws InterruptedException {
        //input check
        if (numberOfShares < 0) {
            return false;
        }
        // check if we have any shares for that company and also if we have enough of them
        if (this.getStocks().get(company) != null) {
            return stockExchange.sell(this, company, numberOfShares);
        } else return false;
    }

    /**
     * Checks if the client has enough funds and then proceeds to wait until
     * the company's prices decreases under the limit. Then it initiates the buying method
     *
     * @param company        to buy from
     * @param numberOfShares client wants to buy
     * @param limit          the maximum price to buy
     * @return true if transaction succeeded, false if it did not
     * @throws InterruptedException while acquiring
     */
    public synchronized boolean buyLow(Company company, float numberOfShares, float limit)
            throws InterruptedException {
        System.out.println(this.name + " is trying to buy under " + limit);
        if (this.balance < numberOfShares * limit) {
            System.out.println(this.name + " does not have enough funds.");
            return false;
        }

        System.out.println(this.name + " is waiting for the price to drop under " + limit);
        while (company.getPrice() > limit) {
            wait(20000);
            if(company.getPrice() > limit){
                System.out.println(this.name + " Gave up on waiting for the price to drop under " + limit);
                return false;
            }
        }
        return stockExchange.buyLow(this, company, numberOfShares, limit);


    }

    /**
     * Checks if the client has enough funds and then proceeds to wait until
     * the company's prices increase over the limit. Then it initiates the buying method
     *
     * @param company        to buy from
     * @param numberOfShares client wants to buy
     * @param limit          the maximum price to buy
     * @return true if transaction succeeded, false if it did not
     * @throws InterruptedException while acquiring
     */
    public synchronized boolean sellHigh(Company company, float numberOfShares, float limit) throws InterruptedException {
        System.out.println(this.name + " is trying to sellHigh and has " + this.getStocks().get(company) + " shares");
        if (!this.getStocks().containsKey(company) || this.getStocks().get(company) < numberOfShares) {
            System.out.println(this.name + "doesn't have enough shares from " + company.getName());
            return false;
        } else {
            System.out.println(this.name + " is waiting for the price to rise over " + limit + ".");
            while (company.getPrice() < limit) {

                wait(8000);
                if (company.getPrice() < limit) {
                    System.out.println(this.name + " has stopped waiting");
                    return false;
                }
            }
            return stockExchange.sellHigh(this, company, numberOfShares, limit);


        }
    }

    /**
     * adds money in the balance
     *
     * @param amount to be added
     * @return true
     */
    public boolean deposit(float amount) {
        this.balance += amount;
        return true;
    }

    /**
     * withdraws money from balance
     *
     * @param amount to be withdrawn
     * @return true if client has enough money
     */
    public boolean withdraw(float amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            return true;
        } else return false;
    }

    /**
     * returns the funds of the client
     *
     * @return total amount of funds
     */
    public float getBalance() {
        return balance;
    }

    /**
     * modifies the amount in the client's balance by the given transaction price
     *
     * @param transactionPrice price of the transaction
     */
    public void modifyBalanceBy(float transactionPrice) {
        this.balance -= transactionPrice;
    }

    /**
     * sets the client's stock exchange
     *
     * @param stockExchange to be set
     */
    public void setStockExchange(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    @Override
    public void run() {
        double random = Math.random();

        switch (type) {
            case 0:
                System.out.println(this.name + " is a repetitive buyer and seller");
                System.out.println(this.name + " was given 2 shares for testing means");
                this.getStocks().putIfAbsent(target, 2F);


                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        buy(target, 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(target.getName() + "'s price is now " + target.getPrice());
                }
                System.out.println(this.name + " will sleep for 3 seconds");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("\n" + this.name + " will start selling his shares back\n");

                for (int i = 0; i < 7; i++) {
                    try {
                        sell(target, 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(target.getName() + "'s price is now " + target.getPrice());
                }

                break;
            case 1:
                System.out.println(this.name + " is a buy and hold");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    buy(target, 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(this.name + " now has " + this.getStocks().get(target) + " shares.");
                try {
                    sellHigh(target, 1, 170);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                System.out.println(this.name + " is buying low");
                try {
                    buyLow(target, 3, 90);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(target.getName() + "'s price is now " + target.getPrice());
                break;
        }
    }
}
