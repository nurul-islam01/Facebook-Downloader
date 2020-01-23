package royal.ondemandservices.ClientViewFragment.ClientViewJobSeekerServicesCatActivity;

import android.content.DialogInterface;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import royal.ondemandservices.FirebaseConfig;
import royal.ondemandservices.Model.Category;
import royal.ondemandservices.Model.PostJob;
import royal.ondemandservices.Model.Rating;
import royal.ondemandservices.Model.SellJob;
import royal.ondemandservices.Model.SellService;
import royal.ondemandservices.Model.Services;
import royal.ondemandservices.R;

public class CatServciesTuitionServiceActivity extends AppCompatActivity {

    //    This file is for services of job seeker..this view can see client from client view
//
//    and when they will select tuition service category then they will see this file.


    private DatabaseReference PublicTuitionServiceCreate;

    private Toolbar toolbar;

    //recycler
    private RecyclerView mRecyclerHomeService;
    private String userId;
    private List<Services> tutionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_servcies_tuition_service);


        toolbar=findViewById(R.id.cat_home_service);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tuition Services Job");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mRecyclerHomeService=findViewById(R.id.recycler_cat_home_service);

        //databse

        PublicTuitionServiceCreate = FirebaseConfig.saveService();


        //Recycler Home Service

        LinearLayoutManager layoutManagerHomeService=new LinearLayoutManager(getApplicationContext());
        layoutManagerHomeService.setReverseLayout(true);
        layoutManagerHomeService.setStackFromEnd(true);

        mRecyclerHomeService.setHasFixedSize(true);
        mRecyclerHomeService.setLayoutManager(layoutManagerHomeService);

        FirebaseConfig.saveService().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    tutionList = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        Services model = data.getValue(Services.class);
                        if (model.getCategory().equals(Category.TUTION_SERVICE)){
                            tutionList.add(model);
                        }
                    }
                    if (tutionList.size() > 0){
                        TutionServiceAapter adapter = new TutionServiceAapter(tutionList);
                        mRecyclerHomeService.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

//        FirebaseRecyclerAdapter<Services,CatServciesTuitionServiceActivity.MyViewHolder> adapter=new FirebaseRecyclerAdapter<Services, CatServciesTuitionServiceActivity.MyViewHolder>
//                (
//                        Services.class,
//                        R.layout.cat_tution_item_layout,
//                        CatServciesTuitionServiceActivity.MyViewHolder.class,
//                        PublicTuitionServiceCreate
//
//                ) {
//            @Override
//            protected void populateViewHolder(final CatServciesTuitionServiceActivity.MyViewHolder viewHolder,final Services model, int position) {
//
//                viewHolder.setDate(model.getDate());
//                viewHolder.setJobTitle(model.getTitle());
//                viewHolder.setJobDescription(model.getDescription());
//                viewHolder.setBudget(model.getBudget());
//
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
//                TextView buyNow = viewHolder.itemView.findViewById(R.id.bynowTV);
//
//                buyNow.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
//                        final RatingBar ratingBar = new RatingBar(viewHolder.itemView.getContext());
//                        builder.setView(ratingBar);
//                        ratingBar.setMax(5);
//                        ratingBar.setNumStars(5);
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
//                Button buyButton = viewHolder.itemView.findViewById(R.id.buynowbtn);
//
//                buyButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(CatServciesTuitionServiceActivity.this);
//                        builder.setTitle("Are You Sure ?");
//                        builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                String pushKey = FirebaseConfig.sellService().push().getKey();
//                                SellService sellService = new SellService(pushKey,model.getId(), model.getSenderId(), userId);
//                                FirebaseConfig.sellService().child(pushKey).setValue(sellService)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()){
//                                                    Toast.makeText(CatServciesTuitionServiceActivity.this, "Buying Complete", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(CatServciesTuitionServiceActivity.this, "Buying Failed", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        });
//                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Toast.makeText(CatServciesTuitionServiceActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        builder.show();
//                    }
//                });
//
//            }
//        };
//
//
//        mRecyclerHomeService.setAdapter(adapter);
    }

//    public static class MyViewHolder extends RecyclerView.ViewHolder{
//        View myview;
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            myview=itemView;
//        }
//
//        public void setDate(String date){
//            TextView mDate=myview.findViewById(R.id.job_date);
//            mDate.setText(date);
//        }
//
//        public void setJobTitle(String title){
//            TextView mTitle=myview.findViewById(R.id.job_title);
//            mTitle.setText(title);
//        }
//
//        public void setJobDescription(String description){
//            TextView mDescription=myview.findViewById(R.id.job_description_cat);
//            mDescription.setText(description);
//        }
//
//        public void setBudget(String budget){
//            TextView mBudget=myview.findViewById(R.id.job_budget);
//            mBudget.setText("TK."+budget);
//        }
//
//        public void setRating(float rating){
//            RatingBar  ratingbar =myview.findViewById(R.id.ratingbar);
//            ratingbar.setRating(rating);
//        }
//    }


    private class TutionServiceAapter extends RecyclerView.Adapter<CatServciesTuitionServiceActivity.TutionServiceAapter.ViewHolder> {

        private List<Services> models;

        public TutionServiceAapter(List<Services> models) {
            this.models = models;
        }

        @NonNull
        @Override
        public TutionServiceAapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(CatServciesTuitionServiceActivity.this).inflate( R.layout.cat_tution_item_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final CatServciesTuitionServiceActivity. TutionServiceAapter.ViewHolder viewHolder, int i) {
            final Services model = models.get(i);

            viewHolder.mDate.setText(model.getDate());
            viewHolder.mTitle.setText(model.getTitle());
            viewHolder.mDescription.setText(model.getDescription());
            viewHolder.mBudget.setText(model.getBudget());

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

            viewHolder.buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CatServciesTuitionServiceActivity.this);
                    builder.setTitle("Are You Sure ?");
                    builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String pushKey = FirebaseConfig.sellService().push().getKey();
                            SellService sellService = new SellService(pushKey, model.getId(), model.getUserId(), userId);
                            FirebaseConfig.sellService().child(pushKey).setValue(sellService)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(CatServciesTuitionServiceActivity.this, "Buying Complete", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CatServciesTuitionServiceActivity.this, "Buying Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(CatServciesTuitionServiceActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
            });

            viewHolder.buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CatServciesTuitionServiceActivity.this);
                    final RatingBar ratingBar = new RatingBar(CatServciesTuitionServiceActivity.this);
                    builder.setView(ratingBar);
                    ratingBar.setMax(5);
                    ratingBar.setNumStars(5);
                    builder.setTitle("Please Rating");
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
                            Toast.makeText(CatServciesTuitionServiceActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
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

            TextView mDate, mBudget, mDescription, mTitle, buyNow;
            Button buyButton;
            RatingBar ratingbar;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mDate=itemView.findViewById(R.id.job_date);
                ratingbar = itemView.findViewById(R.id.ratingbar);
                mBudget=itemView.findViewById(R.id.job_budget);
                mDescription=itemView.findViewById(R.id.job_description_cat);
                mTitle= itemView.findViewById(R.id.job_title);
                buyButton = itemView.findViewById(R.id.buynowbtn);
                buyNow = itemView.findViewById(R.id.buynowTV);
            }
        }
    }
}
