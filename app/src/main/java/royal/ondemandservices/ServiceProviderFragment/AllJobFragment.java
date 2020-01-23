package royal.ondemandservices.ServiceProviderFragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import royal.ondemandservices.CatActivityForJobSeeker.CatHomeServiceActivity;
import royal.ondemandservices.CatActivityForJobSeeker.CatItandSoftwareJobActivity;
import royal.ondemandservices.CatActivityForJobSeeker.CatTutionServiceActivity;
import royal.ondemandservices.FirebaseConfig;
import royal.ondemandservices.JobDetailsActivity;
import royal.ondemandservices.Model.Category;
import royal.ondemandservices.Model.PostJob;
import royal.ondemandservices.Model.Rating;
import royal.ondemandservices.Model.SellJob;
import royal.ondemandservices.R;
import royal.ondemandservices.RegistrationActivity;
import royal.ondemandservices.TutionJobDetailsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllJobFragment extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference mHomeServiewDb;

    private DatabaseReference mTutionService;

    //Recycler view

    private RecyclerView mRecyclerHomeService;
    private RecyclerView mRecyclerTution;

    //Category Button
    private Button btnHomeService;
    private Button btnTuitionService;
    private Button btnItandSoftwareService;
    private String userId;
    private List<PostJob> tutionList, homeList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseConfig.saveJobPost().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    homeList = new ArrayList<>();
                    tutionList = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        PostJob model = data.getValue(PostJob.class);
                        if (model.getCategory().equals(Category.HOME_JOBS)){
                            homeList.add(model);
                        }else if (model.getCategory().equals(Category.TUTION_JOBS)){
                            tutionList.add(model);
                        }
                    }
                    if (homeList.size() > 0) {
                        TutionJobsAdapter adapter = new TutionJobsAdapter(homeList);
                        mRecyclerHomeService.setAdapter(adapter);
                    }

                    if (tutionList.size() > 0){
                        TutionJobsAdapter adapter = new TutionJobsAdapter(tutionList);
                        mRecyclerTution.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myview = inflater.inflate(R.layout.fragment_all_job, container, false);
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(getActivity(), RegistrationActivity.class));
                    getActivity().finish();
                }else {
                    userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                }
            }
        });


        mRecyclerHomeService =myview.findViewById(R.id.recycler_home_serview);
        mRecyclerTution=myview.findViewById(R.id.recycler_tution_service);

        //Button..
        btnHomeService = myview.findViewById(R.id.home_Service_cat);

        btnTuitionService = myview.findViewById(R.id.tution_service);

        btnItandSoftwareService = myview.findViewById(R.id.it_software_service);

        //onlick listener..
        btnHomeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CatHomeServiceActivity.class));
                getActivity().finish();
            }
        });

        btnTuitionService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CatTutionServiceActivity.class));
                getActivity().finish();
            }
        });

        btnItandSoftwareService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CatItandSoftwareJobActivity.class));
                getActivity().finish();
            }
        });

        //Firebase..

        //Home Service DB
        mHomeServiewDb= FirebaseConfig.saveJobPost();

        //Tution Service DB
        mTutionService= FirebaseConfig.saveJobPost();

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

        mRecyclerTution.setHasFixedSize(true);
        mRecyclerTution.setLayoutManager(layoutManagerTution);

        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();

//        FirebaseRecyclerAdapter<PostJob,HomeService>adapterHomeService=new FirebaseRecyclerAdapter<PostJob, HomeService>
//                (
//                        PostJob.class,
//                        R.layout.job_post_item_design,
//                        HomeService.class,
//                        mHomeServiewDb
//                ) {
//            @Override
//            protected void populateViewHolder(final HomeService viewHolder, final PostJob model, final int position) {
//                viewHolder.setTitle(model.getTitle());
//                viewHolder.setDescripton(model.getDescription());
//                viewHolder.setDate(model.getDate());
//                viewHolder.setBudget(model.getBudget());
//                viewHolder.setJobStartDate(model.getStartDate());
//                viewHolder.setJobEndDate(model.getEndDate());
//
//                TextView buyNow = viewHolder.itemView.findViewById(R.id.bynowTV);
//
//                buyNow.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
//                        final RatingBar ratingBar = new RatingBar(viewHolder.itemView.getContext());
//                        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//                        builder.setView(ratingBar);
//                        builder.setTitle("Please Rate this");
//                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                float rat = ratingBar.getRating();
//                                String pushKey = FirebaseConfig.allRatings().push().getKey();
//                                Rating rating = new Rating(pushKey, model.getId(), rat);
//                                FirebaseConfig.allRatings().child(pushKey).setValue(rating);
//                            }
//                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Toast.makeText(viewHolder.itemView.getContext(), "Cancel", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                        builder.show();
//                    }
//                });
//
//                FirebaseConfig.allRatings().addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()){
//                            float allRatings = 0;
//                            int count = 0;
//                            for (DataSnapshot data: dataSnapshot.getChildren()){
//                                Rating rating = data.getValue(Rating.class);
//                                if (rating.getProductId().equals(model.getId())){
//                                    allRatings = allRatings + rating.getRating();
//                                    count++;
//                                }
//                            }
//                            float rating = allRatings / count;
//                            viewHolder.setRating(rating);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//
//                viewHolder.mHomeServiceView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Intent intent=new Intent(getActivity(),JobDetailsActivity.class);
//
//                        intent.putExtra("id",model.getId());
//                        intent.putExtra("budget",model.getBudget());
//                        intent.putExtra("phone",model.getPhone());
//                        intent.putExtra("title",model.getTitle());
//                        intent.putExtra("address",model.getAddress());
//                        intent.putExtra("startDate",model.getStartDate());
//                        intent.putExtra("endData",model.getEndDate());
//                        intent.putExtra("description",model.getDescription());
//                        intent.putExtra("productuserId",model.getSenderId());
//
//                        startActivity(intent);
//
//                    }
//                });
//                Button buyButton = viewHolder.itemView.findViewById(R.id.buynowbtn);
//                if (model.getSenderId().equals(userId)){
//                    buyButton.setVisibility(View.GONE);
//                }
//
//                buyButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                        builder.setTitle("Are You Sure ?");
//                        builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                String pushKey = FirebaseConfig.sellJobs().push().getKey();
//                                SellJob sellService = new SellJob(pushKey,model.getId(), model.getSenderId(), userId);
//                                FirebaseConfig.sellJobs().child(pushKey).setValue(sellService)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()){
//                                                    Toast.makeText(getActivity(), "Buying Complete", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(getActivity(), "Buying Failed", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        });
//                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        builder.show();
//                    }
//                });
//
//
//            }
//        };
//        mRecyclerHomeService.setAdapter(adapterHomeService);
//
//        //Firebase RecyclerAdapter tution data..
//        FirebaseRecyclerAdapter<PostJob,TutionService>adapterTutionService=new FirebaseRecyclerAdapter<PostJob, TutionService>
//                (
//                        PostJob.class,
//                        R.layout.job_post_item_design,
//                        TutionService.class,
//                        mTutionService
//                ) {
//            @Override
//            protected void populateViewHolder(final TutionService viewHolder, final PostJob model, int position) {
//                viewHolder.setTitle(model.getTitle());
//                viewHolder.setDescripton(model.getDescription());
//                viewHolder.setDate(model.getDate());
//                viewHolder.setBudget(model.getBudget());
//                viewHolder.setJobStartDate(model.getStartDate());
//                viewHolder.setJobEndDate(model.getEndDate());
//
//                TextView buyNow = viewHolder.itemView.findViewById(R.id.bynowTV);
//
//                buyNow.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
//                        final RatingBar ratingBar = new RatingBar(viewHolder.itemView.getContext());
//                        ratingBar.setMax(5);
//                        ratingBar.setNumStars(5);
//                        builder.setView(ratingBar);
//                        builder.setTitle("Please Rate this");
//                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                float rat = ratingBar.getRating();
//                                String pushKey = FirebaseConfig.allRatings().push().getKey();
//                                Rating rating = new Rating(pushKey, model.getId(), rat);
//                                FirebaseConfig.allRatings().child(pushKey).setValue(rating);
//                            }
//                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//
//                        builder.show();
//                    }
//                });
//
//                FirebaseConfig.allRatings().addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()){
//                            float allRatings = 0;
//                            int count = 0;
//                            for (DataSnapshot data: dataSnapshot.getChildren()){
//                                Rating rating = data.getValue(Rating.class);
//                                if (rating.getProductId().equals(model.getId())){
//                                    allRatings = allRatings + rating.getRating();
//                                    count++;
//                                }
//                            }
//                            float rating = allRatings / count;
//                            viewHolder.setRating(rating);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//                viewHolder.mTutionService.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent=new Intent(getActivity(),TutionJobDetailsActivity.class);
//                        try {
//                            intent.putExtra("id",model.getId());
//                            intent.putExtra("budget",model.getBudget());
//                            intent.putExtra("phone",model.getPhone());
//                            intent.putExtra("title",model.getTitle());
//                            intent.putExtra("address",model.getAddress());
//                            intent.putExtra("startDate",model.getStartDate());
//                            intent.putExtra("endData",model.getEndDate());
//                            intent.putExtra("description",model.getDescription());
//                            intent.putExtra("productuserId",model.getSenderId());
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//
//                        startActivity(intent);
//                    }
//                });
//
//
//                Button buyButton = viewHolder.itemView.findViewById(R.id.buynowbtn);
//                if (model.getSenderId().equals(userId)){
//                    buyButton.setVisibility(View.GONE);
//                }
//                buyButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                        builder.setTitle("Are You Sure ?");
//                        builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                String pushKey = FirebaseConfig.sellJobs().push().getKey();
//                                SellJob sellService = new SellJob(pushKey,model.getId(), model.getSenderId(), userId);
//                                FirebaseConfig.sellJobs().child(pushKey).setValue(sellService)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()){
//                                                    Toast.makeText(getActivity(), "Buying Complete", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(getActivity(), "Buying Failed", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        });
//                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        builder.show();
//                    }
//                });
//
//
//            }
//
//        };
//        mRecyclerTution.setAdapter(adapterTutionService);

    }

    //Home Service class..

//    public static class HomeService extends RecyclerView.ViewHolder{
//        View mHomeServiceView;
//        public HomeService(View itemView) {
//            super(itemView);
//            mHomeServiceView=itemView;
//        }
//
//
//        public void setTitle(String title){
//            TextView mtitle = mHomeServiceView.findViewById(R.id.job_title);
//            try {
//                mtitle.setText(title);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        public void setBudget(String budget){
//            TextView mBudget=mHomeServiceView.findViewById(R.id.job_budget);
//            try {
//                mBudget.setText("TK."+budget);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        public void setDescripton(String descripton){
//            TextView mDescription=mHomeServiceView.findViewById(R.id.job_description);
//            try {
//                mDescription.setText(descripton);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//
//        }
//
//        public void setDate(String date){
//            TextView mDate=mHomeServiceView.findViewById(R.id.job_post_Date);
//            try {
//                mDate.setText(date);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }
//
//        public void setJobStartDate(String startDate){
//
//        }
//
//        public void setJobEndDate(String endDate){
//
//        }
//
//        public void setRating(float rating){
//            RatingBar  ratingbar =mHomeServiceView.findViewById(R.id.ratingbar);
//            ratingbar.setRating(rating);
//        }
//
//
//    }



    //Tution Service Class..


//    public static class TutionService extends RecyclerView.ViewHolder{
//        View mTutionService;
//        public TutionService(View itemView) {
//            super(itemView);
//            mTutionService=itemView;
//        }
//
//        public void setTitle(String title){
//            TextView mtitle=mTutionService.findViewById(R.id.job_title);
//            mtitle.setText(title);
//        }
//
//        public void setBudget(String budget){
//            TextView mBudget=mTutionService.findViewById(R.id.job_budget);
//            mBudget.setText("TK."+budget);
//        }
//
//        public void setDescripton(String descripton){
//            TextView mDescription=mTutionService.findViewById(R.id.job_description);
//            mDescription.setText(descripton);
//        }
//
//        public void setDate(String date){
//            TextView mDate=mTutionService.findViewById(R.id.job_post_Date);
//            mDate.setText(date);
//        }
//
//        public void setJobStartDate(String startDate){
//
//        }
//
//        public void setJobEndDate(String endDate){
//
//        }
//
//        public void setRating(float rating){
//            RatingBar  ratingbar = mTutionService.findViewById(R.id.ratingbar);
//            ratingbar.setRating(rating);
//        }
//
//    }

    private class TutionJobsAdapter extends RecyclerView.Adapter<TutionJobsAdapter.ViewHolder>{

        private List<PostJob> models;

        public TutionJobsAdapter(List<PostJob> models) {
            this.models = models;
        }

        @NonNull
        @Override
        public TutionJobsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.job_post_item_design, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final TutionJobsAdapter.ViewHolder viewHolder, int i) {
            final PostJob model = models.get(i);
            viewHolder.mtitle.setText(model.getTitle());
            viewHolder.mDescription.setText(model.getDescription());
            viewHolder.mDate.setText(model.getDate());
            viewHolder.mBudget.setText(model.getBudget());

            TextView buyNow = viewHolder.itemView.findViewById(R.id.bynowTV);

            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                    final RatingBar ratingBar = new RatingBar(viewHolder.itemView.getContext());
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
                        viewHolder.ratingbar.setRating(rating);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (model.getCategory().equals(Category.HOME_JOBS)){

                        Intent intent=new Intent(getActivity(),JobDetailsActivity.class);

                        intent.putExtra("id",model.getId());
                        intent.putExtra("budget",model.getBudget());
                        intent.putExtra("phone",model.getPhone());
                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("address",model.getAddress());
                        intent.putExtra("startDate",model.getStartDate());
                        intent.putExtra("endData",model.getEndDate());
                        intent.putExtra("description",model.getDescription());
                        intent.putExtra("productuserId",model.getUserId());

                        startActivity(intent);

                    }else if (model.getCategory().equals(Category.TUTION_JOBS)){

                        Intent intent=new Intent(getActivity(),TutionJobDetailsActivity.class);
                        try {
                            intent.putExtra("id",model.getId());
                            intent.putExtra("budget",model.getBudget());
                            intent.putExtra("phone",model.getPhone());
                            intent.putExtra("title",model.getTitle());
                            intent.putExtra("address",model.getAddress());
                            intent.putExtra("startDate",model.getStartDate());
                            intent.putExtra("endData",model.getEndDate());
                            intent.putExtra("description",model.getDescription());
                            intent.putExtra("productuserId",model.getUserId());
                            startActivity(intent);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            });



            if (model.getUserId().equals(userId)){
                viewHolder.buyButton.setVisibility(View.GONE);
            }
            viewHolder.buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Are You Sure ?");
                    builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String pushKey = FirebaseConfig.sellJobs().push().getKey();
                            SellJob sellService = new SellJob(pushKey,model.getId(), model.getUserId(), userId);
                            FirebaseConfig.sellJobs().child(pushKey).setValue(sellService)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getActivity(), "Buying Complete", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Buying Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView mtitle, mBudget, mDescription, mDate;
            private RatingBar ratingbar;
            Button buyButton;
            public ViewHolder(@NonNull View mTutionService) {
                super(mTutionService);
                 mtitle = mTutionService.findViewById(R.id.job_title);
                 mBudget=mTutionService.findViewById(R.id.job_budget);
                 mDescription=mTutionService.findViewById(R.id.job_description);
                 mDate=mTutionService.findViewById(R.id.job_post_Date);
                ratingbar=mTutionService.findViewById(R.id.ratingbar);
                 buyButton = mTutionService.findViewById(R.id.buynowbtn);
            }
        }
    }

}
