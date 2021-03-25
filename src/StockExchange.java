import java.util.*;
import java.util.concurrent.Semaphore;

public class StockExchange {
    private HashMap<Company, Float> companies;
    private ArrayList<Client> clients;

    StockExchange() {
    }

    StockExchange(HashMap<Company, Float> companies, ArrayList<Client> clients) {
        this.companies = companies;
        this.clients = clients;
        for (Client cl : clients) {
            cl.setStockExchange(this);
        }
    }

    public boolean registerCompany(Company company, float numberOfShares) {
        return companies.putIfAbsent(company, numberOfShares) == null;
    }

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

    public boolean addClient(Client client) {
        client.setStockExchange(this);
        return clients.add(client);
    }

    public void updateCompanyAvailableShares(Company company) {
        this.companies.replace(company, company.getAvailableShares());
    }

    public boolean removeClient(Client client) {
        client.setStockExchange(null);
        return clients.remove(client);
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public HashMap<Company, Float> getCompanies() {
        return companies;
    }

    public void setPrice(Company company, float amount) throws InterruptedException {
        company.acquire();
        company.setPrice(amount);
        company.release();
    }

    public void changePriceBy(Company company, float amount) throws InterruptedException {
        company.acquire();
        company.setPrice(company.getPrice() + amount);
        company.release();
    }

    /***
     * Ca sa vindem shareurile trebuie sa:
     *
     *  1. le scoatem din portofoliul clientului
     *  2. dam clientului banii pe tranzactie
     *  3. le adaugam inapoi in stockexchange (si in companie)
     */
    public boolean buy(Client client, Company company, float numberOfShares) throws InterruptedException {
        boolean response;
        company.acquire();
        response = buyTransaction(client,company,numberOfShares);
        company.release();
        return response;
    }

    /***
     * Ca sa vindem shareurile trebuie sa:
     *
     *  1. le scoatem din portofoliul clientului
     *  2. dam clientului banii pe tranzactie
     *  3. le adaugam inapoi in stockexchange (si in companie)
     */
    public boolean sell(Client client, Company company, float numberOfShares) throws InterruptedException {
        company.acquire();
        boolean response = sellTransaction(client, company, numberOfShares);
        company.release();
        return response;
    }

    public boolean buyLow(Client client, Company company, float numberOfShares, float limit) throws InterruptedException {
        boolean response;
        company.acquire();
        if (company.getPrice() >= limit) {
            response = false;
        } else {
            response = buyTransaction(client, company, numberOfShares);
        }
        return response;
    }

    public boolean sellHigh(Client client, Company company, float numberOfShares, float limit) throws InterruptedException {
        boolean response;
        company.acquire();
        if(company.getPrice() < limit){
            response = false;
        }else {
            response = sellTransaction(client, company, numberOfShares);
        }
        company.release();
        return response;
    }

    public boolean buyTransaction(Client client, Company company, float numberOfShares){
        boolean response;
        float transactionPrice = numberOfShares * company.getPrice();

        if (company.getAvailableShares() >= numberOfShares && client.balance >= transactionPrice) {
            client.modifyBalanceBy(-transactionPrice);
            company.setAvailableShares(company.getAvailableShares() - numberOfShares);
            this.updateCompanyAvailableShares(company);
            client.modifyCompanyStocksBy(company, numberOfShares);
            response = true;
        } else {
            System.out.println("Requirements not met");
            response = false;
        }
        return response;
    }

    public boolean sellTransaction(Client client, Company company, float numberOfShares){
        boolean response;
        float transactionPrice = numberOfShares * company.price;
        if(client.getStocks().get(company) >= numberOfShares){
            client.modifyCompanyStocksBy(company, -numberOfShares);
            client.modifyBalanceBy(+transactionPrice);
            company.setAvailableShares(company.getAvailableShares() + numberOfShares);
            this.updateCompanyAvailableShares(company);
            response = true;
        }else {
            response = false;
        }
        return response;
    }

}

