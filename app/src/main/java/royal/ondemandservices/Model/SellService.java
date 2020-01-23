package royal.ondemandservices.Model;

import java.io.Serializable;

public class SellService implements Serializable {

    private String id, serviceId, sellerId, buyerId;

    public SellService() {
    }

    public SellService(String id, String serviceId, String sellerId, String buyerId) {
        this.id = id;
        this.serviceId = serviceId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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
