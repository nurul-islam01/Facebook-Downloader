package royal.ondemandservices.sellandbuy;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import royal.ondemandservices.FirebaseConfig;
import royal.ondemandservices.Model.SellJob;
import royal.ondemandservices.R;
import royal.ondemandservices.adapter.SellJobsAdapter;


public class SellJobFragment extends Fragment {


    private List<SellJob> models;
    private String userId;
    private RecyclerView sellJobsRV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sell_job, container, false);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        sellJobsRV = view.findViewById(R.id.sellJobsRV);
        FirebaseConfig.sellJobs().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    models = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        SellJob service = data.getValue(SellJob.class);
                        if (service.getSellerId().equals(userId)){
                            models.add(service);
                        }
                    }
                    if (models.size() > 0){
                        SellJobsAdapter adapter = new SellJobsAdapter(getActivity(), models);
                        sellJobsRV.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
