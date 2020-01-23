package royal.ondemandservices;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import im.delight.android.location.SimpleLocation;
import royal.ondemandservices.Model.UserLocation;
import royal.ondemandservices.ServiceProviderFragment.AllJobFragment;
import royal.ondemandservices.ServiceProviderFragment.ProfileFragment;
import royal.ondemandservices.ServiceProviderFragment.ServiceCreateFragment;
import royal.ondemandservices.sellandbuy.BuyJobFragment;
import royal.ondemandservices.sellandbuy.SellServiceFragment;
import royal.ondemandservices.utils.GPSTracker;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;


    //Fragment..

    private AllJobFragment allJobFragment;
    private ServiceCreateFragment serviceCreateFragment;
    private ProfileFragment profileFragment;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar=findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("OnDemand Home Service");

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                    finish();
                }
            }
        });


        allJobFragment=new AllJobFragment();
        serviceCreateFragment=new ServiceCreateFragment();
        profileFragment = new ProfileFragment();

        setFragment(allJobFragment);

        bottomNavigationView = findViewById(R.id.bottombar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.allJob:
                        setFragment(allJobFragment);
                        return true;

                    case R.id.service:
                        setFragment(serviceCreateFragment);
                        return true;

                    case R.id.profile:
                        setFragment(profileFragment);
                        return true;
                    case R.id.sellservice:
                        setFragment(new SellServiceFragment());
                        return true;
                    case R.id.buyjobs:
                        setFragment(new BuyJobFragment());
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

        GPSTracker gpsTracker = new GPSTracker(this);
        String userId, latitude, longitude, country, city, postalCode, addressLine;
        userId = FirebaseConfig.getUserId();

        if (!FirebaseConfig.getUserId().isEmpty() && checkLocationPermission()){
            if (gpsTracker.getIsGPSTrackingEnabled()) {
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

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
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
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission")
                        .setMessage("It's need for this app")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}
