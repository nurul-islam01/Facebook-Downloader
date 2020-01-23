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
public class SellServiceFragment extends Fragment {

    private String userId;
    private List<SellService> models;
    private RecyclerView sellserviceRV;

    public SellServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sell, container, false);
        sellserviceRV = view.findViewById(R.id.sellserviceRV);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseConfig.sellService().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    models = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        SellService service = data.getValue(SellService.class);
                        if (service.getSellerId().equals(userId)){
                            models.add(service);
                        }
                    }
                    if (models.size() > 0){
                        SellServiceAdapter adapter = new SellServiceAdapter(getActivity(), models);
                        sellserviceRV.setAdapter(adapter);
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
