package royal.ondemandservices.sellactivity;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import royal.ondemandservices.Model.SellJob;
import royal.ondemandservices.R;
import royal.ondemandservices.interfaces.SellJobsInterface;

public class SellJobsActivity extends AppCompatActivity {

    SellJobsInterface sellJobsInterface;
    private  SellJob model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_jobs);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.details, R.id.chats)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Intent intent = getIntent();
        try {
            model = (SellJob) intent.getSerializableExtra("model");
            sellJobsInterface.sellJobs(model);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setSellJobsInterface(SellJobsInterface sellJobsInterface){
        this.sellJobsInterface = sellJobsInterface;
        sellJobsInterface.sellJobs(model);
    }

}
