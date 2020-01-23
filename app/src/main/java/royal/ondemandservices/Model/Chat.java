package royal.ondemandservices.Model;

public class Chat {

    private String id, senderId, name, text, time;

    public Chat() {
    }

    public Chat(String id, String senderId, String name, String text, String time) {
        this.id = id;
        this.senderId = senderId;
        this.name = name;
        this.text = text;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
