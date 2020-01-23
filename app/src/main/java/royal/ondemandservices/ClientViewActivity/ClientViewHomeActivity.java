package royal.ondemandservices.ClientViewActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import royal.ondemandservices.ClientViewFragment.AllServicesFragment;
import royal.ondemandservices.ClientViewFragment.YourAllJobPostFragment;
import royal.ondemandservices.ClientViewFragment.YourPostFragment;
import royal.ondemandservices.FirebaseConfig;
import royal.ondemandservices.MainActivity;
import royal.ondemandservices.Model.UserLocation;
import royal.ondemandservices.R;
import royal.ondemandservices.sellandbuy.BuyServiceFragment;
import royal.ondemandservices.sellandbuy.SellJobFragment;
import royal.ondemandservices.utils.GPSTracker;

public class ClientViewHomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    private AllServicesFragment allServicesFragment;
    private YourPostFragment yourPostFragment;
    private YourAllJobPostFragment yourAllJobPostFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_view_home);

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(ClientViewHomeActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

        toolbar=findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("OnDemand Service App");


        bottomNavigationView=findViewById(R.id.bottombar);

        allServicesFragment = new AllServicesFragment();
        yourPostFragment = new YourPostFragment();
        yourAllJobPostFragment = new YourAllJobPostFragment();


        setFragment(yourAllJobPostFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.allJob:
                        setFragment(yourAllJobPostFragment);
                        return true;

                    case R.id.postjob:
                        setFragment(yourPostFragment);
                        return true;
                    case R.id.servicesclient:
                        setFragment(allServicesFragment);
                        return true;

                    case R.id.selljob:
                        setFragment(new SellJobFragment());
                        return true;
                    case R.id.buyservice:
                        setFragment(new BuyServiceFragment());
                        return true;

                    default:
                        return true;


                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        setLocation();
    }

    private void setLocation(){
        GPSTracker gpsTracker = new GPSTracker(this);


        if (!FirebaseConfig.getUserId().isEmpty()){
            if (gpsTracker.getIsGPSTrackingEnabled()) {
                String userId, latitude, longitude, country, city, postalCode, addressLine;
                userId = FirebaseConfig.getUserId();
                latitude = String.valueOf(gpsTracker.getLatitude());
                longitude = String.valueOf(gpsTracker.getLongitude());
                country = gpsTracker.getCountryName(this);
                city = gpsTracker.getLocality(this);
                postalCode = gpsTracker.getPostalCode(this);
                addressLine = gpsTracker.getAddressLine(this);
                UserLocation location = new UserLocation(userId, latitude, longitude, country, city, postalCode, addressLine);
                FirebaseConfig.userLocation().child(userId).setValue(location);
            }else {
                gpsTracker.showSettingsAlert();
            }
        }
    }
    //Set Fragment method..

    public void setFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainframe,fragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_option_menu_1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ClientViewHomeActivity.this, MainActivity.class));
                finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
