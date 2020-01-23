package royal.ondemandservices.Model;

/**
 * Created by Yazdani on 10/18/2018.
 */

public class User {

    private String id;
    private String email;
    private String pass;
    private String date;

    public User(){

    }

    public User(String id, String email, String pass, String date) {
        this.id = id;
        this.email = email;
        this.pass = pass;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
