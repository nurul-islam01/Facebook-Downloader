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
import royal.ondemandservices.Model.SellService;
import royal.ondemandservices.R;
import royal.ondemandservices.adapter.SellServiceAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyServiceFragment extends Fragment {

    private List<SellService> models;
    private String userId;
    private RecyclerView sellServiceRV;

    public BuyServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        sellServiceRV = view.findViewById(R.id.sellServiceRV);

        FirebaseConfig.sellService().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    models = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        SellService service = data.getValue(SellService.class);
                        if (service.getBuyerId().equals(userId)){
                            models.add(service);
                        }
                    }
                    if (models.size() > 0){
                        SellServiceAdapter adapter = new SellServiceAdapter(getActivity(), models);
                        sellServiceRV.setAdapter(adapter);
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
