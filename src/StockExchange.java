import java.util.*;
import java.util.concurrent.Semaphore;

public class StockExchange {
    private HashMap<Company, Float> companies = new HashMap<>();
    private ArrayList<Client> clients = new ArrayList<>();

    StockExchange() {
    }

    /**
     * adds a company to the company list.
     * @param company to be added
     * @param numberOfShares company's available shares
     * @return true if there is not already a company else returns false
     */
    public boolean registerCompany(Company company, float numberOfShares) {
        return companies.putIfAbsent(company, numberOfShares) == null;
    }

    /**
     * removes a company from the company list and also dissolves all the shares possessed by the clients
     * @param company to be removed
     * @return true
     * @throws InterruptedException when acquiring
     */
    public boolean deregisterCompany(Company company) throws InterruptedException {
        if (companies.get(company) != null) {
            company.acquire();
            for (Client c : clients) {
                c.getStocks().remove(company);
            }
            companies.remove(company);
            company.release();
            return true;
        } else {
            for (Client c : clients) {
                c.getStocks().remove(company);
            }
            return false;
        }

    }

    /**
     * adds a given client to the client Array and sets its Stock exchange to this
     * @param client to be added
     * @return true
     */
    public boolean addClient(Client client) {
        client.setStockExchange(this);
        return clients.add(client);
    }

    /**
     * updates StockExchanges number of available shares to the company's
     * @param company to be tracked
     */
    public void updateCompanyAvailableShares(Company company) {
        this.companies.replace(company, company.getAvailableShares());
    }

    /**
     * sets the clients Stock Exchange to none and removes the client from the list
     * @param client to be removed
     * @return true if this list contained the specified element
     */
    public boolean removeClient(Client client) {
        client.setStockExchange(null);
        client.setStockExchange(null);
        return clients.remove(client);
    }

    /**
     * returns the clients Array
     * @return ArrayList
     */
    public ArrayList<Client> getClients() {
        return clients;
    }

    /**
     * returns the HashMap that tracks the company's available shares
     * @return HashMap
     */
    public HashMap<Company, Float> getCompanies() {
        return companies;
    }

    /**
     * sets the company's price to the given amount
     * @param company to be modified
     * @param amount to be set
     * @throws InterruptedException while acquiring
     */
    public void setPrice(Company company, float amount) throws InterruptedException {
        company.acquire();
        company.setPrice(amount);
        company.release();
    }

    /**
     * Change the company's price by
     * @param company to be modified
     * @param amount to be changed
     * @throws InterruptedException while acquiring
     */
    public void changePriceBy(Company company, float amount) throws InterruptedException {
        company.acquire();
        company.setPrice(company.getPrice() + amount);
        company.release();
    }

    public boolean buy(Client client, Company company, float numberOfShares) throws InterruptedException {
        boolean response;
        System.out.println("-" +client.name + " is waiting for a permit to " + company.getName());
        company.acquire();
        System.out.println("----------------\n" + client.name + " has acquired " + company.getName() + "\n----------------");
        response = buyTransaction(client, company, numberOfShares);
        System.out.println(client.name + " is releasing " + company.getName());
        company.release();
        return response;
    }

    /**
     * waits until company's Semaphore grants permission to lock
     * and initiates the sellTransaction method
     * @param client that sells
     * @param company to be sold
     * @param numberOfShares to be sold
     * @return true is the transaction succeeded else false
     * @throws InterruptedException while acquiring
     */
    public boolean sell(Client client, Company company, float numberOfShares) throws InterruptedException {
        System.out.println("-" + client.name + " is trying to acquire " + company.getName() );
        company.acquire();
        System.out.println("----------------\n" + client.name + " has acquired " + company.getName() + "\n----------------");
        boolean response = sellTransaction(client, company, numberOfShares);
        System.out.println(client.name + " is releasing " + company.getName());
        company.release();
        return response;
    }

    /**
     * attempts to acquire the company
     * checks if the price is under the limit
     * calls method buyTransaction if the price is under the limit else releases the company
     * @param client that buys
     * @param company company to be bought from
     * @param numberOfShares amount of share to be bought
     * @param limit the company's price has to be under
     * @return true if transaction succeeds else returns false
     * @throws InterruptedException while acquiring
     */
    public boolean buyLow(Client client, Company company, float numberOfShares, float limit) throws InterruptedException {
        boolean response;
        System.out.println(client.name + " is waiting to acquire " + company.getName());
        company.acquire();
        System.out.println("----------------\n" + client.name + " has acquired " + company.getName() + "\n----------------");
        if (company.getPrice() > +limit) {
            response = false;
            System.out.println("Price is too high");
        } else {
            System.out.println(client.name + " is starting the transaction");
            response = buyTransaction(client, company, numberOfShares);
        }
        System.out.println(client.name + " is releasing " + company.getName());
        company.release();
        return response;
    }
    /**
     * attempts to acquire the company
     * checks if the price is over the limit
     * calls method sellTransaction if the price is under the limit else releases the company
     * @param client that sells
     * @param company company to be sold
     * @param numberOfShares amount of shares to be sold
     * @param limit the company's price has to be over
     * @return true if transaction succeeds else returns false
     * @throws InterruptedException while acquiring
     */
    public boolean sellHigh(Client client, Company company, float numberOfShares, float limit) throws InterruptedException {
        boolean response;
        System.out.println(client.name + " is waiting to acquire " + company.getName());
        company.acquire();
        System.out.println("----------------\n" + client.name + " has acquired " + company.getName() + "\n----------------");
        if (company.getPrice() < limit) {
            System.out.println("Price has gotten too low while acquiring " + company.getName());
            response = false;
        } else {
            response = sellTransaction(client, company, numberOfShares);
        }
        company.release();
        return response;
    }

    /**
     * Checks if there are enough shares to be sold and if the client has enough funds
     * if both conditions are satisfied then the transaction is being made, else nothing happens
     * @param client buying
     * @param company to be bought from
     * @param numberOfShares to be bought
     * @return true if transaction succeeds else returns false
     */
    public boolean buyTransaction(Client client, Company company, float numberOfShares) {
        boolean response;
        float transactionPrice = numberOfShares * company.getPrice();

        if (company.getAvailableShares() >= numberOfShares && client.balance >= transactionPrice) {
            System.out.println(client.name + " has passed the requirements");
            client.modifyBalanceBy(-transactionPrice);
            company.setAvailableShares(company.getAvailableShares() - numberOfShares);
            this.updateCompanyAvailableShares(company);
            if (client.getStocks().get(company) == null) {
                client.getStocks().putIfAbsent(company, numberOfShares);
                System.out.println(client.name + " has bought " + numberOfShares + " from " + company.getName() + " for the first time for " + company.getPrice());
            } else {
                client.modifyCompanyStocksBy(company, numberOfShares);
                System.out.println(client.name + " has bought " + numberOfShares + " from " + company.getPrice());
            }

            //Artificial price increase.
            company.setPrice(company.getPrice() + 10);
            System.out.println("Transaction complete!");
            response = true;
        } else {
            System.out.println("Requirements not met");
            response = false;
        }
        return response;
    }
    /**
     * Checks if the client has enough shares to sell
     * @param client selling
     * @param company to be sold
     * @param numberOfShares to be sold
     * @return true if transaction succeeds else returns false
     */
    public boolean sellTransaction(Client client, Company company, float numberOfShares) {
        boolean response;
        float transactionPrice = numberOfShares * company.price;
        float clientStocks = client.getStocks().get(company);
        System.out.println(client.name + " Client has " + clientStocks + " shares and wants to sell " + numberOfShares + " shares");
        if ( clientStocks >= numberOfShares) {
            System.out.println(client.name + " has passed the requirements");
            client.modifyCompanyStocksBy(company, -numberOfShares);
            client.modifyBalanceBy(+transactionPrice);
            company.setAvailableShares(company.getAvailableShares() + numberOfShares);
            this.updateCompanyAvailableShares(company);
            System.out.println(client.name + " has sold " + numberOfShares + " to " + company.getName() + " for " + company.getPrice());
            //Artificial price decrease
            company.setPrice(company.getPrice() - 10);
            System.out.println("Transaction complete!");
            response = true;
        } else {
            System.out.println("Requirements not met");
            response = false;
        }
        return response;
    }

}

