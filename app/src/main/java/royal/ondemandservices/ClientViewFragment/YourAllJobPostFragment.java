package royal.ondemandservices.ClientViewFragment;


import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import royal.ondemandservices.Model.PostJob;
import royal.ondemandservices.Model.Rating;
import royal.ondemandservices.R;
import royal.ondemandservices.ServiceProviderFragment.AllJobFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourAllJobPostFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mHomeServiewDb;

    private DatabaseReference mTutionService;

    //Recycler view

    private RecyclerView mRecyclerHomeService;
    private RecyclerView mRecyclerTution;
    List<PostJob> homePostJob, tutionPostJob;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_your_all_job_post, container, false);

        mRecyclerHomeService=myview.findViewById(R.id.recycler_home_serview);
        mRecyclerTution=myview.findViewById(R.id.recycler_tution_service);

        //Firebase..
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        final String uid=mUser.getUid();

        //Home Service DB
        mHomeServiewDb= FirebaseConfig.saveJobPost();


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


        homePostJob = new ArrayList<>();
        mHomeServiewDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    homePostJob = new ArrayList<>();
                    tutionPostJob = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                            PostJob postJob = data.getValue(PostJob.class);
                            if (postJob.getUserId().equals(uid) && postJob.getCategory().equals(Category.HOME_JOBS)){
                                homePostJob.add(postJob);
                            }else if (postJob.getUserId().equals(uid) && postJob.getCategory().equals(Category.TUTION_JOBS)){
                                tutionPostJob.add(postJob);
                            }
                    }
                    if (homePostJob.size() > 0){
                        HomeJobPostAdapter adapter = new HomeJobPostAdapter(homePostJob);
                        mRecyclerHomeService.setAdapter(adapter);
                    }
                    if (tutionPostJob.size() > 0){
                        TutionJobPostAdapter adapter = new TutionJobPostAdapter(tutionPostJob);
                        mRecyclerTution.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
//Home Service class..


    private class HomeJobPostAdapter extends RecyclerView.Adapter<HomeJobPostAdapter.ViewHolder>{

        private List<PostJob> postJobs;

        public HomeJobPostAdapter(List<PostJob> postJobs) {
            this.postJobs = postJobs;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.job_post_item_design, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final PostJob model = postJobs.get(position);

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
                    ratingBar.setMax(5);
                    ratingBar.setNumStars(5);
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
            return postJobs.size();
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

    private class TutionJobPostAdapter extends RecyclerView.Adapter<TutionJobPostAdapter.ViewHolder>{

        private List<PostJob> postJobs;

        public TutionJobPostAdapter(List<PostJob> postJobs) {
            this.postJobs = postJobs;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.job_post_item_design, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final PostJob model = postJobs.get(position);

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
            return postJobs.size();
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


}

