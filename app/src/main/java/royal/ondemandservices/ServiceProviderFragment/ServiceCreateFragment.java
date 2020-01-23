package royal.ondemandservices.ServiceProviderFragment;


import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import royal.ondemandservices.R;
import royal.ondemandservices.ServiceCreateFragment.CreateServiceFragment;
import royal.ondemandservices.ServiceCreateFragment.YourAllServiceFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceCreateFragment extends Fragment {

    private TextView allService;
    private TextView createService;

    //Fragment..

    private YourAllServiceFragment yourAllServiceFragment;
    private CreateServiceFragment createServiceFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myview=inflater.inflate(R.layout.fragment_service_create, container, false);


        yourAllServiceFragment=new YourAllServiceFragment();
        createServiceFragment=new CreateServiceFragment();

        allService=myview.findViewById(R.id.allServices);
        createService=myview.findViewById(R.id.createService);

        setFragment(yourAllServiceFragment);

        allService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(yourAllServiceFragment);
            }
        });

        createService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(createServiceFragment);
            }
        });




        return myview;
    }


    public void setFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_serviceFragment,fragment);
        fragmentTransaction.commit();

    }



}
