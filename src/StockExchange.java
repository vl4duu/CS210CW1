import java.util.ArrayList;
import java.util.HashMap;

public class StockExchange {
    HashMap<Company, Float> Companies;
    ArrayList<Client> clients;

    StockExchange() {
    }

    public boolean registerCompany(Company company, float numberOfShares) {
        Companies.put(company, numberOfShares);
        return true;
    }

    public boolean deregisterCompany(Company company, float numberOfShares) {
        return Companies.remove(company, numberOfShares);
    }

    public boolean addClient(Client client) {
        return clients.add(client);
    }

    public boolean removeClient(Client client) {
        return clients.remove(client);
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public HashMap<Company, Float> getCompanies() {
        return Companies;
    }

    public void setPrice(Company company, float amount){

    }

    public void setPriceBy(Company company, float amount){}

}
