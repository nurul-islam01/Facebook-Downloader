package royal.ondemandservices;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfig extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static FirebaseDatabase getDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database;
    }

    public static String getUserId(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }else {
            return " ";
        }
    }

    public static DatabaseReference saveUsers(){
        return getDatabase().getReference().child("ALL_USERS");
    }
    public static DatabaseReference saveService(){
        return getDatabase().getReference().child("SERVICE");
    }


    public static DatabaseReference saveJobPost(){
        return getDatabase().getReference().child("JOB_POST");
    }

    public static DatabaseReference allRatings(){
        return getDatabase().getReference().child("RATINGS");
    }
    public static DatabaseReference sellJobs(){
        return getDatabase().getReference().child("SELL_JOBS");
    }
    public static DatabaseReference sellService(){
        return getDatabase().getReference().child("SELL_SERVICE");
    }
    public static DatabaseReference sellChats(){
        return getDatabase().getReference().child("CHATS");
    }

    public static DatabaseReference userLocation(){
        return getDatabase().getReference().child("LOCATIONS");
    }



}
