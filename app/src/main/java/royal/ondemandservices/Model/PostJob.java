package royal.ondemandservices.Model;

/**
 * Created by Yazdani on 11/21/2018.
 */

public class PostJob {

    private String title;
    private String budget;
    private String address;
    private String phone;
    private String description;
    private String startDate;
    private String endDate;
    private String date;
    private String id;
    private String userId;
    private String postCategory;
    private String category;
    private boolean jobGranted;

    public PostJob(){

    }

    public PostJob(String title, String budget, String address, String phone, String description, String startDate, String endDate, String date, String id, String userId, String category, String postCategory, boolean jobGranted) {
        this.title = title;
        this.budget = budget;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.date = date;
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.postCategory = postCategory;
        this.jobGranted = jobGranted;
    }

    public boolean isJobGranted() {
        return jobGranted;
    }

    public void setJobGranted(boolean jobGranted) {
        this.jobGranted = jobGranted;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
