package royal.ondemandservices;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import royal.ondemandservices.Model.Rating;
import royal.ondemandservices.Model.SellJob;

public class TutionJobDetailsActivity extends AppCompatActivity {


    private TextView budget;
    private TextView call;
    private TextView title;
    private TextView address;
    private TextView startDate;
    private TextView endDate;
    private TextView description, callAction;
    public static final int CALL_PERMISSION_REQUEST_CODE = 4;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tution_job_details);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        budget=findViewById(R.id.budget_dtls);
        call=findViewById(R.id.phone_call_dtls);
        callAction = findViewById(R.id.callAction);
        title=findViewById(R.id.job_title_dtls);
        address=findViewById(R.id.job_address_dtls);
        startDate=findViewById(R.id.job_start_date);
        endDate=findViewById(R.id.job_end_date);
        description=findViewById(R.id.job_description_dtls);


        Intent intent=getIntent();

        final String mId=intent.getStringExtra("id");
        String mBudget=intent.getStringExtra("budget");
        String mPhone=intent.getStringExtra("phone");
        String mtitle=intent.getStringExtra("title");
        String mAddress=intent.getStringExtra("address");
        String mStartDate=intent.getStringExtra("startDate");
        String mEndDate=intent.getStringExtra("endData");
        String mDescription=intent.getStringExtra("description");
        final String productuserId=intent.getStringExtra("productuserId");

        budget.setText("TK."+mBudget);
        call.setText(mPhone);
        title.setText(mtitle);
        address.setText(mAddress);
        startDate.setText(mStartDate);
        endDate.setText(mEndDate);
        description.setText(mDescription);


        callAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(TutionJobDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(TutionJobDetailsActivity.this, Manifest.permission.CALL_PHONE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(TutionJobDetailsActivity.this);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setMessage("Caller permission is necessary to make this call!!!");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(TutionJobDetailsActivity.this , new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
                            }
                        });
                    } else {
                        ActivityCompat.requestPermissions(TutionJobDetailsActivity.this , new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
                    }
                }else {
                    String phone = call.getText().toString();
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+Uri.encode(phone.trim())));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);
                }

            }
        });


        TextView buyNow = findViewById(R.id.bynowTV);

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(TutionJobDetailsActivity.this);
                final RatingBar ratingBar = new RatingBar(TutionJobDetailsActivity.this);
                builder.setView(ratingBar);
                ratingBar.setMax(5);
                ratingBar.setNumStars(5);
                builder.setTitle("Please Rate this");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        float rat = ratingBar.getRating();
                        String pushKey = FirebaseConfig.allRatings().push().getKey();
                        Rating rating = new Rating(pushKey, mId, rat);
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
        final RatingBar  ratingbar =findViewById(R.id.ratingbar);

        FirebaseConfig.allRatings().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    float allRatings = 0;
                    int count = 0;
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        Rating rating = data.getValue(Rating.class);
                        if (rating.getProductId().equals(mId)){
                            allRatings = allRatings + rating.getRating();
                            count++;
                        }
                    }
                    float rating = allRatings / count;
                    ratingbar.setRating(rating);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button buyButton = findViewById(R.id.buynowbtn);
        if (mId.equals(userId)){
            buyButton.setVisibility(View.GONE);
        }

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TutionJobDetailsActivity.this);
                builder.setTitle("Are You Sure ?");
                builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String pushKey = FirebaseConfig.sellService().push().getKey();
                        SellJob sellService = new SellJob(pushKey, mId, productuserId, userId);
                        FirebaseConfig.sellJobs().child(pushKey).setValue(sellService)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(TutionJobDetailsActivity.this, "Buying Complete", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TutionJobDetailsActivity.this, "Buying Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(TutionJobDetailsActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

    }

    public boolean checkPermissionForCall(){
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForExternalStorage(int requestCode){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)){
            Toast.makeText(this.getApplicationContext(), "Call permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},requestCode);
        }
    }
}
