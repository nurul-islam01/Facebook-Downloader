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
import royal.ondemandservices.MapsActivity;
import royal.ondemandservices.Model.PostJob;
import royal.ondemandservices.Model.ProfileData;
import royal.ondemandservices.Model.SellService;
import royal.ondemandservices.Model.Services;
import royal.ondemandservices.Model.UserLocation;
import royal.ondemandservices.R;
import royal.ondemandservices.interfaces.SellServiceInterface;
import royal.ondemandservices.sellactivity.SellServiceActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceSellingDetailsFragment extends Fragment implements SellServiceInterface {

    private Context context;

    TextView titleTV, buyernameTV, sellernameTV, priceTV, buyerPhoneTV, buyerAddressLineTV, sellerAddressLineTV;
    private SellService model;

    private Button buyerLocationBT, sellerLocationBT;


    public ServiceSellingDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_selling_details, container, false);

        titleTV = view.findViewById(R.id.titleTV);
        buyernameTV = view.findViewById(R.id.buyernameTV);
        sellernameTV = view.findViewById(R.id.sellernameTV);
        priceTV = view.findViewById(R.id.priceTV);
        buyerPhoneTV = view.findViewById(R.id.buyerPhoneTV);

        sellerLocationBT = view.findViewById(R.id.sellerLocationBT);
        buyerLocationBT = view.findViewById(R.id.buyerLocationBT);
        sellerAddressLineTV = view.findViewById(R.id.sellerAddressLineTV);
        buyerAddressLineTV = view.findViewById(R.id.buyerAddressLineTV);

        FirebaseConfig.saveService().child(model.getServiceId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Services data = dataSnapshot.getValue(Services.class);
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
        ((SellServiceActivity) context).setSellServiceInterface(this);
    }

    @Override
    public void setSellService(SellService sellService) {
        this.model = sellService;
    }
}
