package royal.ondemandservices.sellactivity.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import royal.ondemandservices.FirebaseConfig;
import royal.ondemandservices.MainActivity;
import royal.ondemandservices.MapsActivity;
import royal.ondemandservices.Model.PostJob;
import royal.ondemandservices.Model.ProfileData;
import royal.ondemandservices.Model.SellJob;
import royal.ondemandservices.Model.UserLocation;
import royal.ondemandservices.R;
import royal.ondemandservices.interfaces.SellJobsInterface;
import royal.ondemandservices.sellactivity.SellJobsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobSellingDetailsFragment extends Fragment implements SellJobsInterface {

    private Context context;
    private SellJob model;

    TextView titleTV, buyernameTV, sellernameTV, priceTV, buyerPhoneTV, buyerAddressLineTV, sellerAddressLineTV;
    private Button buyerLocationBT, sellerLocationBT;

    public JobSellingDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_selling_details, container, false);

        titleTV = view.findViewById(R.id.titleTV);
        buyernameTV = view.findViewById(R.id.buyernameTV);
        sellernameTV = view.findViewById(R.id.sellernameTV);
        priceTV = view.findViewById(R.id.priceTV);
        buyerPhoneTV = view.findViewById(R.id.buyerPhoneTV);

        sellerLocationBT = view.findViewById(R.id.sellerLocationBT);
        buyerLocationBT = view.findViewById(R.id.buyerLocationBT);
        sellerAddressLineTV = view.findViewById(R.id.sellerAddressLineTV);
        buyerAddressLineTV = view.findViewById(R.id.buyerAddressLineTV);

        FirebaseConfig.saveJobPost().child(model.getJobId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    PostJob data = dataSnapshot.getValue(PostJob.class);
                    if (data != null){
                        try {
                            titleTV.setText(data.getTitle());
                            priceTV.setText("Price : " + data.getBudget());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseConfig.saveUsers().child(model.getBuyerId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    ProfileData data = dataSnapshot.getValue(ProfileData.class);
                    if (data != null){
                        try {
                           buyernameTV.setText("Buyer : " +data.getName());
                           buyerPhoneTV.setText("Buyer Phone : " + data.getPhone());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseConfig.saveUsers().child(model.getSellerId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    ProfileData data = dataSnapshot.getValue(ProfileData.class);
                    if (data != null){
                        try {
                            sellernameTV.setText("Seller : " + data.getName());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buyerLocationBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("id", model.getBuyerId());
                startActivity(intent);
            }
        });

        sellerLocationBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("id", model.getSellerId());
                startActivity(intent);
            }
        });

        setRecentLocation();

        return view;
    }

    private void setRecentLocation(){

        FirebaseConfig.userLocation().child(model.getBuyerId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    UserLocation location = dataSnapshot.getValue(UserLocation.class);
                    try {
                        buyerAddressLineTV.setText(location.getAddressLine());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseConfig.userLocation().child(model.getSellerId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    UserLocation location = dataSnapshot.getValue(UserLocation.class);
                    try {
                        sellerAddressLineTV.setText(location.getAddressLine());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        ((SellJobsActivity) context).setSellJobsInterface(this);
    }

    @Override
    public void sellJobs(SellJob sellJob) {
        this.model = sellJob;
    }
}
