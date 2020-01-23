package royal.ondemandservices.CatActivityForJobSeeker;

import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

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

import royal.ondemandservices.FirebaseConfig;
import royal.ondemandservices.Model.Category;
import royal.ondemandservices.Model.PostJob;
import royal.ondemandservices.Model.Rating;
import royal.ondemandservices.Model.SellJob;
import royal.ondemandservices.R;

public class CatTutionServiceActivity extends AppCompatActivity {

    private DatabaseReference mTutionService;
    private Toolbar toolbar;
    private String userId;

    //recycler
    private RecyclerView mRecyclerHomeService;
    private List<PostJob> tutionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_tution_service);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        toolbar=findViewById(R.id.cat_home_service);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tuition Service");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mRecyclerHomeService=findViewById(R.id.recycler_cat_home_service);

        mTutionService= FirebaseConfig.saveJobPost();

        //Recycler Home Service

        LinearLayoutManager layoutManagerHomeService=new LinearLayoutManager(getApplicationContext());
        layoutManagerHomeService.setReverseLayout(true);
        layoutManagerHomeService.setStackFromEnd(true);

        mRecyclerHomeService.setHasFixedSize(true);
        mRecyclerHomeService.setLayoutManager(layoutManagerHomeService);

        FirebaseConfig.saveJobPost().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    tutionList = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        PostJob model = data.getValue(PostJob.class);
                        if (model.getCategory().equals(Category.TUTION_JOBS)){
                            tutionList.add(model);
                        }
                    }
                    if (tutionList.size() > 0){
                        TutionJobsAapter adapter = new TutionJobsAapter(tutionList);
                        mRecyclerHomeService.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private class TutionJobsAapter extends RecyclerView.Adapter<TutionJobsAapter.ViewHolder> {

        private List<PostJob> models;

        public TutionJobsAapter(List<PostJob> models) {
            this.models = models;
        }

        @NonNull
        @Override
        public TutionJobsAapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(CatTutionServiceActivity.this).inflate( R.layout.cat_tution_item_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final TutionJobsAapter.ViewHolder viewHolder, int i) {
            final PostJob model = models.get(i);

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


            if (model.getUserId().equals(userId)){
                viewHolder.buyButton.setVisibility(View.GONE);
                viewHolder.buyNow.setVisibility(View.GONE);
            }

            viewHolder.buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                    final RatingBar ratingBar = new RatingBar(viewHolder.itemView.getContext());
                    ratingBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    builder.setView(ratingBar);
                    ratingBar.setMax(5);
                    ratingBar.setNumStars(5);
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


            viewHolder.buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CatTutionServiceActivity.this);
                    builder.setTitle("Are You Sure ?");
                    builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String pushKey = FirebaseConfig.sellService().push().getKey();
                            SellJob sellService = new SellJob(pushKey,model.getId(), model.getUserId(), userId);
                            FirebaseConfig.sellJobs().child(pushKey).setValue(sellService)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(CatTutionServiceActivity.this, "Buying Complete", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CatTutionServiceActivity.this, "Buying Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(CatTutionServiceActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
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
