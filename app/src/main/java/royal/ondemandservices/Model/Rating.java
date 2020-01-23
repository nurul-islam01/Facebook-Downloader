package royal.ondemandservices.Model;

public class Rating {

    private String id;
    private String productId;
    private float rating;

    public Rating() {
    }

    public Rating(String id, String productId, float rating) {
        this.id = id;
        this.productId = productId;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
