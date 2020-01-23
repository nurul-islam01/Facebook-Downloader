package royal.ondemandservices;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import royal.ondemandservices.Model.User;

public class RegistrationActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;

    private Button btnReg;
    private Button btnSignin;

    //Firebase..

    private FirebaseAuth mAuth;
    private DatabaseReference mUserList;

    //Progress dialog..
    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

        regFunc();

    }

    private void regFunc(){

        email=findViewById(R.id.email_registration);
        pass=findViewById(R.id.password_registration);

        btnReg=findViewById(R.id.btn_registration);

        btnSignin=findViewById(R.id.btn_signin);



        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String mEmail=email.getText().toString().trim();
                final String mPass=pass.getText().toString().trim();

                if (TextUtils.isEmpty(mEmail)){
                    email.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(mPass)){
                    pass.setError("Required Field..");
                    return;
                }

                mDialog=new ProgressDialog(RegistrationActivity.this);
                mDialog.setMessage("Processing..");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            startActivity(new Intent(getApplicationContext(),CreateProfielActivity.class));
                            mDialog.dismiss();

                        }else {
                            Toast.makeText(getApplicationContext(),"Problem..",Toast.LENGTH_LONG).show();
                        }

                    }
                });


            }
        });



        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


    }




}
