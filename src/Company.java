import java.util.concurrent.Semaphore;

public class Company {
    String name;
    float totalNumberOfShares;
    float availableNumberOfShares;
    float price;
    public Semaphore semaphore = new Semaphore(1, true);

    Company() {
    }

    public void acquire() throws InterruptedException {
        semaphore.acquire();

    }

    public void release() {
        semaphore.release();
    }

    Company(String name, float totalNumberOfShares, float availableNumberOfShares, float price) {
        this.name = name;
        this.totalNumberOfShares = totalNumberOfShares;
        this.availableNumberOfShares = availableNumberOfShares;
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTotalShares(float totalNumberOfShares) {
        this.totalNumberOfShares = totalNumberOfShares;
    }

    public float getTotalShares() {
        return totalNumberOfShares;
    }

    public void setAvailableShares(float availableNumberOfShares) {
        this.availableNumberOfShares = availableNumberOfShares;
    }


    public float getAvailableShares() {
        return availableNumberOfShares;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }


}
