import java.util.ArrayList;
import java.util.HashMap;

public class Application {
    public static void main(String[] args) {




        /**Companies */

        Company intel = new Company("Intel", 15, 10, 10);
        Company tesla = new Company("Tesla", 15, 10, 10);
        Company microsoft = new Company("Microsoft", 15, 10, 10);



        /**Client Threads */
        ArrayList<Client> clients = new ArrayList<>();
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();



        /**Stock Exchange */
        StockExchange stockExchange = new StockExchange();
        stockExchange.registerCompany(intel, intel.getAvailableShares());
        stockExchange.registerCompany(tesla, tesla.getAvailableShares());
        stockExchange.registerCompany(microsoft, microsoft.getAvailableShares());


        Thread c1 = new Thread(client1);
        Thread c2 = new Thread(client2);
        Thread c3 = new Thread(client3);
        c1.start();
        c2.start();
        c3.start();





    }
}

