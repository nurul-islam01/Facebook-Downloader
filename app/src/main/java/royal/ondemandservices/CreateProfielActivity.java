package royal.ondemandservices;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.util.Date;

import royal.ondemandservices.Model.ProfileData;

public class CreateProfielActivity extends AppCompatActivity {

    private EditText name;
    private EditText phone;
    private EditText district;
    private EditText address;

    private Button btnSave;

    //Frebase..

    private DatabaseReference mCreateProfileDatabase;
    private FirebaseAuth mAuth;

    private FirebaseUser mUser;
    private String uId;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profiel);

        mAuth=FirebaseAuth.getInstance();

        mUser=mAuth.getCurrentUser();

        uId=mUser.getUid();

        mCreateProfileDatabase= FirebaseConfig.saveUsers();

        mDialog=new ProgressDialog(CreateProfielActivity.this);

        createProfileFunc();
    }


    private void createProfileFunc(){


        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        district=findViewById(R.id.district);
        address=findViewById(R.id.address);

        btnSave=findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mName=name.getText().toString().trim();
                String mPhone=phone.getText().toString().trim();
                String mDistrict=district.getText().toString().trim();
                String mAddress=address.getText().toString().trim();

                if (TextUtils.isEmpty(mName)){
                    name.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(mPhone)){
                    phone.setError("Required Field..");
                    return;
                }

                if (TextUtils.isEmpty(mDistrict)){
                    district.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(mAddress)){
                    address.setError("Required Field..");
                    return;
                }

                mDialog.setMessage("Processing..");
                mDialog.show();

                String mDate = DateFormat.getDateTimeInstance().format(new Date());

                ProfileData profileData =new ProfileData(mName,mPhone,mDistrict,mAddress,uId, false);
                mCreateProfileDatabase.child(uId).setValue(profileData);

                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();

            }
        });





    }



}
