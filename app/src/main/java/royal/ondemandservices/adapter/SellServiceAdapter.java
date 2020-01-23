package royal.ondemandservices.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import royal.ondemandservices.FirebaseConfig;
import royal.ondemandservices.Model.ProfileData;
import royal.ondemandservices.Model.SellService;
import royal.ondemandservices.Model.Services;
import royal.ondemandservices.R;
import royal.ondemandservices.sellactivity.SellServiceActivity;

public class SellServiceAdapter extends RecyclerView.Adapter<SellServiceAdapter.ViewHolder> {

    private Context context;
    private List<SellService> models;

    public SellServiceAdapter(Context context, List<SellService> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.sellrowview, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final SellService model = models.get(i);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SellServiceActivity.class);

                intent.putExtra("model", model);
                context.startActivity(intent);
            }
        });

        FirebaseConfig.saveService().child(model.getServiceId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Services data = dataSnapshot.getValue(Services.class);
                    if (data != null){
                        try {
                            viewHolder.titleTV.setText(data.getTitle());
                            viewHolder.priceTV.setText("Price : " + data.getBudget());
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
                            viewHolder.buyernameTV.setText("Buyer : " + data.getName());
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
                            viewHolder.sellernameTV.setText("Seller : " + data.getName());
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


    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTV, buyernameTV, sellernameTV, priceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.titleTV);
            buyernameTV = itemView.findViewById(R.id.buyernameTV);
            sellernameTV = itemView.findViewById(R.id.sellernameTV);
            priceTV = itemView.findViewById(R.id.priceTV);
        }
    }
}
