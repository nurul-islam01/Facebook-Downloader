package royal.ondemandservices.Model;

/**
 * Created by Yazdani on 11/22/2018.
 */

public class Services {

    private String title;
    private String budget;
    private String skill;
    private String phone;
    private String description;
    private String date;
    private String id;
    private String userId;
    private String serviceCategory;
    private String category;
    private boolean serviceGranted;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Services(){

    }

    public Services(String title, String budget, String skill, String phone, String description, String date, String id, String userId, String serviceCategory, String category, boolean serviceGranted) {
        this.title = title;
        this.budget = budget;
        this.skill = skill;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.id = id;
        this.userId = userId;
        this.serviceCategory = serviceCategory;
        this.category = category;
        this.serviceGranted = serviceGranted;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public boolean isServiceGranted() {
        return serviceGranted;
    }

    public void setServiceGranted(boolean serviceGranted) {
        this.serviceGranted = serviceGranted;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
