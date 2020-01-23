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
import royal.ondemandservices.Model.PostJob;
import royal.ondemandservices.Model.SellJob;
import royal.ondemandservices.Model.SellService;
import royal.ondemandservices.R;
import royal.ondemandservices.adapter.SellJobsAdapter;
import royal.ondemandservices.adapter.SellServiceAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyJobFragment extends Fragment {


    private RecyclerView buyJobsRV;
    private List<SellJob> models;
    private String userId;

    public BuyJobFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy_job, container, false);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        buyJobsRV = view.findViewById(R.id.buyJobsRV);


        FirebaseConfig.sellJobs().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    models = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        SellJob service = data.getValue(SellJob.class);
                        if (service.getBuyerId().equals(userId)){
                            models.add(service);
                        }
                    }
                    if (models.size() > 0){
                        SellJobsAdapter adapter = new SellJobsAdapter(getActivity(), models);
                        buyJobsRV.setAdapter(adapter);
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
