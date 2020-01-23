package royal.ondemandservices;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import royal.ondemandservices.Model.ProfileData;
import royal.ondemandservices.admin.AdminActivity;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;

    private Button btnLogin;
    private Button btnReg;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();

        mDialog = new ProgressDialog(MainActivity.this);

        loginFunction();
    }


    private void loginFunction(){

        email=findViewById(R.id.email_login);
        pass=findViewById(R.id.password_login);

        btnLogin=findViewById(R.id.btn_signiN);
        btnReg=findViewById(R.id.btn_registration);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mEmail=email.getText().toString().trim();
                String mPass=pass.getText().toString().trim();

                if (TextUtils.isEmpty(mEmail)){
                    email.setError("Required Field..");
                    return;
                }

                if (TextUtils.isEmpty(mPass)){
                    pass.setError("Required Field..");
                    return;
                }

                mDialog.setMessage("Processing..");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        String userId = mAuth.getUid();

                        if (userId.equals("4CRmYbLr8ldBMJMQZejOZmFYd5g2") || userId.equals("nvUd9whq8rekMVUL6z98kykySXO2") || userId.equals("RtB1zw48I4etS6eo2PJ9myeXSs53")){
                            startActivity(new Intent(MainActivity.this, AdminActivity.class));
                            finish();
                        } else {
                            FirebaseConfig.saveUsers().child(userId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        ProfileData data = dataSnapshot.getValue(ProfileData.class);
                                        if (data.isAdmin()){
                                            startActivity(new Intent(MainActivity.this, AdminActivity.class));
                                            finish();
                                        }else {
                                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(),"Problem..",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
                finish();
            }
        });
    }
}
