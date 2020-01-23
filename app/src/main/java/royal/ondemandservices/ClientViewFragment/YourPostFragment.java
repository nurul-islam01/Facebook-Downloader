package royal.ondemandservices.ClientViewFragment;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import royal.ondemandservices.PostJobCatagoryAllFragment.HomeServiceFragment;
import royal.ondemandservices.PostJobCatagoryAllFragment.ITJobsCreateFragment;
import royal.ondemandservices.PostJobCatagoryAllFragment.TutionServiceFragment;
import royal.ondemandservices.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourPostFragment extends Fragment {

    //Catbutton..

    private Button home_service;
    private Button it_software_service;
    private Button tution_service;

    private HomeServiceFragment homeServiceFragment;
    private TutionServiceFragment tutionServiceFragment;
    private ITJobsCreateFragment itJobsCreateFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_your_post, container, false);

        home_service=myview.findViewById(R.id.home_Service_cat);
        it_software_service =myview.findViewById(R.id.it_software_service);
        tution_service=myview.findViewById(R.id.tution_service);

        homeServiceFragment=new HomeServiceFragment();
        tutionServiceFragment=new TutionServiceFragment();
        itJobsCreateFragment=new ITJobsCreateFragment();

        setFragment(homeServiceFragment);

        //Home Service...

        home_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setFragment(homeServiceFragment);

            }
        });
        it_software_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setFragment(itJobsCreateFragment);

            }
        });

        //Tution Service

        tution_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(tutionServiceFragment);
            }
        });



        return myview;
    }


    public void setFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.post_your_job,fragment);
        fragmentTransaction.commit();

    }



}
