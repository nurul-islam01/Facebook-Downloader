package royal.ondemandservices.ClientViewFragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import royal.ondemandservices.ClientViewFragment.ClientViewJobSeekerServicesCatActivity.CatServciesHomeServiceActivity;
import royal.ondemandservices.ClientViewFragment.ClientViewJobSeekerServicesCatActivity.CatServciesTuitionServiceActivity;
import royal.ondemandservices.ClientViewFragment.ClientViewJobSeekerServicesCatActivity.CatServicesItandSoftwareActivity;
import royal.ondemandservices.FirebaseConfig;
import royal.ondemandservices.Model.Category;
import royal.ondemandservices.Model.PostJob;
import royal.ondemandservices.Model.Rating;
import royal.ondemandservices.Model.SellService;
import royal.ondemandservices.Model.Services;
import royal.ondemandservices.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllServicesFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference publicmHomeServiceCreate;

    private DatabaseReference PublicTuitionServiceCreate;


    //Recycler view

    private RecyclerView mRecyclerHomeService;
    private RecyclerView mRecyclerTution;

    //Button...

    private Button homeService;
    private Button tuitionService;
    private Button itandsoftwareService;
    private String userId;

    private List<Services> tutionList, homeList;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myview=inflater.inflate(R.layout.fragment_all_services, container, false);


        homeService=myview.findViewById(R.id.home_Service_cat);

        tuitionService=myview.findViewById(R.id.tution_service);

        itandsoftwareService=myview.findViewById(R.id.it_software_service);

        homeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CatServciesHomeServiceActivity.class));
            }
        });

        tuitionService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CatServciesTuitionServiceActivity.class));
            }
        });
        itandsoftwareService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CatServicesItandSoftwareActivity.class));
            }
        });


        mRecyclerHomeService=myview.findViewById(R.id.recycler_home_serview);
        mRecyclerTution=myview.findViewById(R.id.recycler_tution_service);
        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid=mUser.getUid();
        userId = uid;

        publicmHomeServiceCreate = FirebaseConfig.saveService();
        PublicTuitionServiceCreate = FirebaseConfig.saveService();


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


        FirebaseConfig.saveService().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    homeList = new ArrayList<>();
                    tutionList = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        Services model = data.getValue(Services.class);
                        if (model.getCategory().equals(Category.HOME_SERVICE)){
                            homeList.add(model);
                        }

                        if (model.getCategory().equals(Category.TUTION_SERVICE)){
                            tutionList.add(model);
                        }
                    }
                    if (homeList.size() > 0){
                        AllServiceAdapter adapter = new AllServiceAdapter(homeList);
                        mRecyclerHomeService.setAdapter(adapter);
                    }

                    if (tutionList.size() > 0){
                        AllServiceAdapter adapter = new AllServiceAdapter(tutionList);
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

//        FirebaseRecyclerAdapter<Services,AllServicesFragment.Allservice_HomeService> adapter=new FirebaseRecyclerAdapter<Services, AllServicesFragment.Allservice_HomeService>
//                (
//                        Services.class,
//                        R.layout.all_service_data_own_item,
//                        AllServicesFragment.Allservice_HomeService.class,
//                        publicmHomeServiceCreate
//
//                ) {
//            @Override
//            protected void populateViewHolder(final AllServicesFragment.Allservice_HomeService viewHolder,final Services model, int position) {
//
//                viewHolder.setTitle(model.getTitle());
//                viewHolder.setDescripton(model.getDescription());
//                viewHolder.setDate(model.getDate());
//                viewHolder.setBudget(model.getBudget());
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
//                Button buyButton = viewHolder.itemView.findViewById(R.id.buynowbtn);
//
//                buyButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
//
//                            }
//                        });
//
//                        builder.show();
//                    }
//                });
//            }
//        };
//        mRecyclerHomeService.setAdapter(adapter);

//
//
//        FirebaseRecyclerAdapter<Services, AllServicesFragment.Allservice_TutionService>tutionServiceAdapter = new FirebaseRecyclerAdapter<Services, AllServicesFragment.Allservice_TutionService>
//                (
//                        Services.class,
//                        R.layout.all_service_data_own_item,
//                        AllServicesFragment.Allservice_TutionService.class,
//                        PublicTuitionServiceCreate
//
//                ) {
//            @Override
//            protected void populateViewHolder(final AllServicesFragment.Allservice_TutionService viewHolder,final Services model, int position) {
//                viewHolder.setTitle(model.getTitle());
//                viewHolder.setDescripton(model.getDescription());
//                viewHolder.setDate(model.getDate());
//                viewHolder.setBudget(model.getBudget());
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
//
//
//                Button buyButton = viewHolder.itemView.findViewById(R.id.buynowbtn);
//
//                buyButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
//                buyNow.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
//                        final RatingBar ratingBar = new RatingBar(viewHolder.itemView.getContext());
//                        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
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
//            }
//        };
//
//        mRecyclerTution.setAdapter(tutionServiceAdapter);

    }

//    public static class Allservice_HomeService extends RecyclerView.ViewHolder{
//        View mHomeServiceView;
//        public Allservice_HomeService(View itemView) {
//            super(itemView);
//            mHomeServiceView=itemView;
//        }
//
//        public void setTitle(String title){
//            TextView mtitle=mHomeServiceView.findViewById(R.id.job_title);
//            mtitle.setText(title);
//        }
//
//        public void setBudget(String budget){
//            TextView mBudget=mHomeServiceView.findViewById(R.id.job_budget);
//            mBudget.setText("TK."+budget);
//        }
//
//        public void setDescripton(String descripton){
//            TextView mDescription=mHomeServiceView.findViewById(R.id.job_description);
//            mDescription.setText(descripton);
//        }
//
//        public void setDate(String date){
//            TextView mDate=mHomeServiceView.findViewById(R.id.job_post_Date);
//            mDate.setText(date);
//        }
//
//
//        public void setRating(float rating){
//            RatingBar  ratingbar =mHomeServiceView.findViewById(R.id.ratingbar);
//            ratingbar.setRating(rating);
//        }
//
//
//    }

//
//    public static class Allservice_TutionService extends RecyclerView.ViewHolder{
//        View mHomeServiceView;
//        public Allservice_TutionService(View itemView) {
//            super(itemView);
//            mHomeServiceView=itemView;
//        }
//
//        public void setTitle(String title){
//            TextView mtitle=mHomeServiceView.findViewById(R.id.job_title);
//            mtitle.setText(title);
//        }
//
//        public void setBudget(String budget){
//            TextView mBudget=mHomeServiceView.findViewById(R.id.job_budget);
//            mBudget.setText("TK."+budget);
//        }
//
//        public void setDescripton(String descripton){
//            TextView mDescription=mHomeServiceView.findViewById(R.id.job_description);
//            mDescription.setText(descripton);
//        }
//
//        public void setDate(String date){
//            TextView mDate=mHomeServiceView.findViewById(R.id.job_post_Date);
//            mDate.setText(date);
//        }
//
//        public void setRating(float rating){
//            RatingBar  ratingbar =mHomeServiceView.findViewById(R.id.ratingbar);
//            ratingbar.setRating(rating);
//        }
//
//
//
//    }


    private class AllServiceAdapter extends RecyclerView.Adapter<AllServiceAdapter.ViewHolder>{

        private List<Services> models;

        public AllServiceAdapter(List<Services> models) {
            this.models = models;
        }

        @NonNull
        @Override
        public AllServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.all_service_data_own_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final AllServiceAdapter.ViewHolder viewHolder, int i) {
            final Services model = models.get(i);


            viewHolder.mtitle.setText(model.getTitle());
            viewHolder.mDescription.setText(model.getDescription());
            viewHolder.mDate.setText(model.getDate());
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

            viewHolder.buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Are You Sure ?");
                    builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String pushKey = FirebaseConfig.sellService().push().getKey();
                            SellService sellService = new SellService(pushKey,model.getId(), model.getUserId(), userId);
                            FirebaseConfig.sellService().child(pushKey).setValue(sellService)
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

            viewHolder.buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                    final RatingBar ratingBar = new RatingBar(viewHolder.itemView.getContext());
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
            return models.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            RatingBar  ratingbar;
            Button buyButton;
            TextView mtitle, mDate, mDescription, mBudget, buyNow;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mtitle=itemView.findViewById(R.id.job_title);
                ratingbar =itemView.findViewById(R.id.ratingbar);
                mDate=itemView.findViewById(R.id.job_post_Date);
                 mDescription=itemView.findViewById(R.id.job_description);
                 mBudget=itemView.findViewById(R.id.job_budget);
                 buyButton = itemView.findViewById(R.id.buynowbtn);
                buyNow = itemView.findViewById(R.id.bynowTV);

            }
        }
    }

}
