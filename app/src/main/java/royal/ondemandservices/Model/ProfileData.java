package royal.ondemandservices.Model;

/**
 * Created by Yazdani on 10/18/2018.
 */

public class ProfileData {

    private String name;
    private String phone;
    private String district;
    private String address;
    private String id;
    private boolean isAdmin;

    public ProfileData(){

    }

    public ProfileData(String name, String phone, String district, String address, String id, boolean isAdmin) {
        this.name = name;
        this.phone = phone;
        this.district = district;
        this.address = address;
        this.id = id;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
