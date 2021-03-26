import java.util.concurrent.Semaphore;

public class Company {
    String name;
    float totalNumberOfShares;
    float availableNumberOfShares;
    float price;
    public Semaphore semaphore = new Semaphore(1, true);

    Company() {
    }

    /**
     * Constructor made for testing purposes
     * @param name String
     * @param totalNumberOfShares float
     * @param availableNumberOfShares float
     * @param price float
     */
    Company(String name, float totalNumberOfShares, float availableNumberOfShares, float price) {
        this.name = name;
        this.totalNumberOfShares = totalNumberOfShares;
        this.availableNumberOfShares = availableNumberOfShares;
        this.price = price;
    }
    /**
     * acquires a lock on the company
     * @throws InterruptedException while the thread is waiting
     */
    public void acquire() throws InterruptedException {
        semaphore.acquire();

    }

    /**
     * release the company
     */
    public void release() {
        semaphore.release();
    }


    /**
     * sets the name of the company
     * @param name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns company's name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * setting the total number of shares to a given amount
     * @param totalNumberOfShares given number of shares
     */
    public void setTotalShares(float totalNumberOfShares) {
        this.totalNumberOfShares = totalNumberOfShares;
    }

    /**
     * returns the total amount of shares
     * @return float
     */
    public float getTotalShares() {
        return totalNumberOfShares;
    }

    /**
     * sets the available number of shares
     * @param availableNumberOfShares to be set to
     */
    public void setAvailableShares(float availableNumberOfShares) {
        this.availableNumberOfShares = availableNumberOfShares;
    }

    /**
     * returns the available number of shares
     * @return float
     */
    public float getAvailableShares() {
        return availableNumberOfShares;
    }

    /**
     * sets the company price
     * @param price to be set to
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * returns the company's price
     * @return float
     */
    public float getPrice() {
        return price;
    }


}
