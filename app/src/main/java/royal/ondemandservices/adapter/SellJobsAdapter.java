package royal.ondemandservices.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import royal.ondemandservices.FirebaseConfig;
import royal.ondemandservices.Model.PostJob;
import royal.ondemandservices.Model.ProfileData;
import royal.ondemandservices.Model.SellJob;

import royal.ondemandservices.R;
import royal.ondemandservices.sellactivity.SellJobsActivity;

public class SellJobsAdapter extends RecyclerView.Adapter<SellJobsAdapter.ViewHolder> {

    private Context context;
    private List<SellJob> models;

    public SellJobsAdapter(Context context, List<SellJob> models) {
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
        final SellJob model = models.get(i);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SellJobsActivity.class);

                intent.putExtra("model", model);
                context.startActivity(intent);
            }
        });

        FirebaseConfig.saveJobPost().child(model.getJobId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    PostJob data = dataSnapshot.getValue(PostJob.class);
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
                            viewHolder.buyernameTV.setText("Buyer : " +data.getName());
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
