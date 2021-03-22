public class Company {
    String name;
    float totalNumberOfShares;
    float availableNumberOfShares;
    float price;

    Company(){}

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
