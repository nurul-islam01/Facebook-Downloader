package royal.ondemandservices.ServiceCreateFragment;


import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import royal.ondemandservices.FirebaseConfig;
import royal.ondemandservices.Model.Category;
import royal.ondemandservices.Model.Rating;
import royal.ondemandservices.Model.Services;
import royal.ondemandservices.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourAllServiceFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference homeServiceOWN, softwareServiceReference;

    private DatabaseReference tutionservice;
    private String userId;

    //Recycler view

    private RecyclerView mRecyclerHomeService, mRecyclerSoftwareService;
    private RecyclerView mRecyclerTution;
    private List<Services> services, softwareServices;
    private List<Services> tusionServices;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myview=inflater.inflate(R.layout.fragment_your_all, container, false);


        mRecyclerHomeService=myview.findViewById(R.id.recycler_home_serview);
        mRecyclerTution=myview.findViewById(R.id.recycler_tution_service);
        mRecyclerSoftwareService=myview.findViewById(R.id.recycler_software_service);
        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        final String uid=mUser.getUid();
        userId = uid;

        homeServiceOWN= FirebaseConfig.saveService();
        tutionservice= FirebaseConfig.saveService();
        softwareServiceReference = FirebaseConfig.saveService();


        //Recycler Home Service

        LinearLayoutManager layoutManagerHomeService=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerHomeService.setReverseLayout(true);
        layoutManagerHomeService.setStackFromEnd(true);

        mRecyclerHomeService.setHasFixedSize(true);
        mRecyclerHomeService.setLayoutManager(layoutManagerHomeService);

        //Recycler Tution Service..

        LinearLayoutManager layoutManagerTution=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerTution.setReverseLayout(true);
        layoutManagerTution.setStackFromEnd(true);

        LinearLayoutManager softwareLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        softwareLayoutManager.setReverseLayout(true);
        softwareLayoutManager.setStackFromEnd(true);
        mRecyclerSoftwareService.setHasFixedSize(true);
        mRecyclerSoftwareService.setLayoutManager(softwareLayoutManager);

        mRecyclerTution.setHasFixedSize(true);
        mRecyclerTution.setLayoutManager(layoutManagerTution);

        services = new ArrayList<>();
        homeServiceOWN.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    services = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        Services service = data.getValue(Services.class);
                        try {
                            if (service.getUserId().equals(userId) && service.getCategory().equals(Category.HOME_SERVICE)){
                                services.add(service);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if (services.size() > 0){
                        HomeServiceAdapter homeServiceAdapter = new HomeServiceAdapter(services);
                        mRecyclerHomeService.setAdapter(homeServiceAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });

        softwareServices = new ArrayList<>();
        softwareServiceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    softwareServices = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        Services service = data.getValue(Services.class);
                        try {
                            if (service.getUserId().equals(userId) && service.getCategory().equals(Category.IT_SERVICE)) {
                                softwareServices.add(service);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                    if (softwareServices.size() > 0){
                        SoftwareServiceAdapter softwareAdapter = new SoftwareServiceAdapter(softwareServices);
                        mRecyclerSoftwareService.setAdapter(softwareAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });

        tusionServices = new ArrayList<>();
        tutionservice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    tusionServices = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        Services service = data.getValue(Services.class);
                        try {
                            if (service.getUserId().equals(userId) && service.getCategory().equals(Category.TUTION_SERVICE)) {
                                tusionServices.add(service);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if (tusionServices.size() > 0){
                        TusionServiceAdapter tutionAdapter = new TusionServiceAdapter(tusionServices);
                        mRecyclerTution.setAdapter(tutionAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });


        return myview;
    }

    private class HomeServiceAdapter extends RecyclerView.Adapter<HomeServiceAdapter.ViewHolder>{

        private List<Services> services;

        public HomeServiceAdapter(List<Services> services) {
            this.services = services;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.all_service_data_own_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Services model = services.get(position);
            holder.mtitle.setText(model.getTitle());
            holder.mDescription.setText(model.getDescription());
            holder.mDate.setText(model.getDate());
            holder.mBudget.setText(model.getBudget());

            FirebaseConfig.allRatings().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        float allRatings = 0;
                        int count = 0;
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            Rating rating = data.getValue(Rating.class);
                            if (rating.getProductId().equals(model.getId())){
                                allRatings = allRatings + rating.getRating();
                                count++;
                            }
                        }
                        float rating = allRatings / count;
                        holder.ratingbar.setRating(rating);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            holder.buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    final RatingBar ratingBar = new RatingBar(holder.itemView.getContext());
                    ratingBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    builder.setView(ratingBar);
                    builder.setTitle("Please Rating Before Buying");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            float rat = ratingBar.getRating();
                            String pushKey = FirebaseConfig.allRatings().push().getKey();
                            Rating rating = new Rating(pushKey, model.getId(), rat);
                            FirebaseConfig.allRatings().child(pushKey).setValue(rating);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return services.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mtitle, mBudget, mDescription, mDate, buyNow;
            RatingBar ratingbar;
            Button buynowbtn;

            public ViewHolder(View itemView) {
                super(itemView);
                ratingbar =itemView.findViewById(R.id.ratingbar);
                buyNow = itemView.findViewById(R.id.bynowTV);
                mtitle = itemView.findViewById(R.id.job_title);
                mBudget = itemView.findViewById(R.id.job_budget);
                mDescription = itemView.findViewById(R.id.job_description);
                mDate = itemView.findViewById(R.id.job_post_Date);
                buynowbtn = itemView.findViewById(R.id.buynowbtn);
                buynowbtn.setVisibility(View.GONE);
            }
        }
    }

    private class SoftwareServiceAdapter extends RecyclerView.Adapter<SoftwareServiceAdapter.ViewHolder>{

        private List<Services> services;

        public SoftwareServiceAdapter(List<Services> services) {
            this.services = services;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.all_service_data_own_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Services model = services.get(position);
            holder.mtitle.setText(model.getTitle());
            FirebaseConfig.allRatings().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        float allRatings = 0;
                        int count = 0;
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            Rating rating = data.getValue(Rating.class);
                            if (rating.getProductId().equals(model.getId())){
                                allRatings = allRatings + rating.getRating();
                                count++;
                            }
                        }
                        float rating = allRatings / count;
                        holder.ratingbar.setRating(rating);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            holder.buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    final RatingBar ratingBar = new RatingBar(holder.itemView.getContext());
                    ratingBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    ratingBar.setMax(5);
                    ratingBar.setNumStars(5);
                    builder.setView(ratingBar);
                    builder.setTitle("Please Rate this");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            float rat = ratingBar.getRating();
                            String pushKey = FirebaseConfig.allRatings().push().getKey();
                            Rating rating = new Rating(pushKey, model.getId(), rat);
                            FirebaseConfig.allRatings().child(pushKey).setValue(rating);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.show();
                }
            });
            holder.mDescription.setText(model.getDescription());
            holder.mDate.setText(model.getDate());
            holder.mBudget.setText(model.getBudget());

        }

        @Override
        public int getItemCount() {
            return services.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mtitle, mBudget, mDescription, mDate, buyNow;
            RatingBar ratingbar;
            Button buynowbtn;

            public ViewHolder(View itemView) {
                super(itemView);
                ratingbar =itemView.findViewById(R.id.ratingbar);
                buyNow = itemView.findViewById(R.id.bynowTV);
                mtitle = itemView.findViewById(R.id.job_title);
                mBudget = itemView.findViewById(R.id.job_budget);
                mDescription = itemView.findViewById(R.id.job_description);
                mDate = itemView.findViewById(R.id.job_post_Date);
                buynowbtn = itemView.findViewById(R.id.buynowbtn);
                buynowbtn.setVisibility(View.GONE);
            }
        }
    }

    private class TusionServiceAdapter extends RecyclerView.Adapter<TusionServiceAdapter.ViewHolder>{

        private List<Services> services;

        public TusionServiceAdapter(List<Services> services) {
            this.services = services;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.all_service_data_own_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Services model = services.get(position);
            holder.mtitle.setText(model.getTitle());
            holder.mDescription.setText(model.getDescription());
            holder.mDate.setText(model.getDate());
            holder.mBudget.setText(model.getBudget());

            FirebaseConfig.allRatings().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        float allRatings = 0;
                        int count = 0;
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            Rating rating = data.getValue(Rating.class);
                            if (rating.getProductId().equals(model.getId())){
                                allRatings = allRatings + rating.getRating();
                                count++;
                            }
                        }
                        float rating = allRatings / count;
                        holder.ratingbar.setRating(rating);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            holder.buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    final RatingBar ratingBar = new RatingBar(holder.itemView.getContext());
                    ratingBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    ratingBar.setMax(5);
                    ratingBar.setNumStars(5);
                    builder.setView(ratingBar);
                    builder.setTitle("Please Rate this");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            float rat = ratingBar.getRating();
                            String pushKey = FirebaseConfig.allRatings().push().getKey();
                            Rating rating = new Rating(pushKey, model.getId(), rat);
                            FirebaseConfig.allRatings().child(pushKey).setValue(rating);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return services.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mtitle, mBudget, mDescription, mDate, buyNow;
            RatingBar ratingbar;
            public ViewHolder(View itemView) {
                super(itemView);
                ratingbar =itemView.findViewById(R.id.ratingbar);
                buyNow = itemView.findViewById(R.id.bynowTV);
                mtitle = itemView.findViewById(R.id.job_title);
                mBudget = itemView.findViewById(R.id.job_budget);
                mDescription = itemView.findViewById(R.id.job_description);
                mDate = itemView.findViewById(R.id.job_post_Date);
            }
        }
    }

}




