import java.util.ArrayList;
import java.util.HashMap;

public class Application {
    public static void main(String[] args)  {

        //Companies
        Company tesla = new Company("Tesla", 100, 50, 100);

//        Client Threads
        ArrayList<Client> clients = new ArrayList<>();
        Client client1 = new Client("Vlad", tesla,1);
        Client client2 = new Client("Radu", tesla,1);
        Client client3 = new Client("Jax", tesla,0);
        Client client4 = new Client("Maya", tesla,2);
        Client client5 = new Client("Scooby", tesla,2);
        client1.deposit(10000);
        client2.deposit(10000);
        client3.deposit(10000);
        client4.deposit(10000);
        client5.deposit(10000);


//        Stock Exchange
        StockExchange stockExchange = new StockExchange();
        stockExchange.registerCompany(tesla, tesla.getAvailableShares());
        stockExchange.addClient(client1);
        stockExchange.addClient(client2);
        stockExchange.addClient(client3);
        stockExchange.addClient(client4);
        stockExchange.addClient(client5);



        Thread c1 = new Thread(client1);
        Thread c2 = new Thread(client2);
        Thread c3 = new Thread(client3);
        Thread c4 = new Thread(client4);
        Thread c5 = new Thread(client5);
        c1.start();
        c2.start();
        c3.start();
        c4.start();
        c5.start();





    }
}

