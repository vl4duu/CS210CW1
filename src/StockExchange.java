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
        for(Client cl : clients){
            cl.setStockExchange(this);
        }
    }

    public boolean registerCompany(Company company, float numberOfShares) {
        return companies.putIfAbsent(company, numberOfShares) == null;
    }

    public boolean deregisterCompany(Company company, float numberOfShares) {
        return companies.remove(company, numberOfShares);
    }

    public boolean addClient(Client client) {
        return clients.add(client);
    }

    public void updateCompanyAvailableShares(Company company) {
        this.companies.replace(company, company.getAvailableShares());
    }

    public boolean removeClient(Client client) {
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
        float transactionPrice = numberOfShares * company.getPrice();

        if (company.getAvailableShares() >= numberOfShares && client.balance >= transactionPrice) {
            client.modifyBalanceBy(-transactionPrice);
            // first update the company object's internal number of shares
            // then broadcast these changes to the stockexchange
            company.setAvailableShares(company.getAvailableShares() - numberOfShares);
            this.updateCompanyAvailableShares(company);
            client.modifyCompanyStocksBy(company, numberOfShares);
            response = true;
        } else {
            System.out.println("Requirements not met");
            response = false;
        }


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

        client.modifyBalanceBy(-numberOfShares);
        client.modifyCompanyStocksBy(company, numberOfShares * company.getPrice());
        company.setAvailableShares(company.getAvailableShares() + numberOfShares);
        this.updateCompanyAvailableShares(company);
        company.release();
        return true;
    }

}

