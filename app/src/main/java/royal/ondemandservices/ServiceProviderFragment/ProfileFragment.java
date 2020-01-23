package royal.ondemandservices.ServiceProviderFragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import royal.ondemandservices.ClientViewActivity.ClientViewHomeActivity;
import royal.ondemandservices.MainActivity;
import royal.ondemandservices.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    //Firebase auth..

    private FirebaseAuth mAuth;
    private Button btnSignOut;
    private TextView switchtoClientView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myview = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth=FirebaseAuth.getInstance();


        btnSignOut=myview.findViewById(R.id.btnSignout);

        switchtoClientView=myview.findViewById(R.id.switchtoclient);

        //Switch to client view

        switchtoClientView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ClientViewHomeActivity.class));
            }
        });



        //Sign out button..

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();

            }
        });


        return myview;

    }

}
