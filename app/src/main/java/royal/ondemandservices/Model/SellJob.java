package royal.ondemandservices.Model;

import java.io.Serializable;

public class SellJob implements Serializable {

    private String id, jobId, sellerId, buyerId;

    public SellJob() {
    }

    public SellJob(String id, String jobId, String sellerId, String buyerId) {
        this.id = id;
        this.jobId = jobId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }
}
