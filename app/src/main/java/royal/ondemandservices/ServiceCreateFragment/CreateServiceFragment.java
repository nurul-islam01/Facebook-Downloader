package royal.ondemandservices.ServiceCreateFragment;


import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import royal.ondemandservices.R;
import royal.ondemandservices.ServiceCreateFragment.ServiceCatagoriesFragment.HomeServiceCreateFragment;
import royal.ondemandservices.ServiceCreateFragment.ServiceCatagoriesFragment.ItandSoftwareJobServiceCreateFragment;
import royal.ondemandservices.ServiceCreateFragment.ServiceCatagoriesFragment.TutionServiceCreateFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateServiceFragment extends Fragment {

    private Button btnHomeService;
    private Button btnTuitionService;
    private Button btnItanSoftwareJob;

    private HomeServiceCreateFragment homeServiceCreateFragment;
    private TutionServiceCreateFragment tutionServiceCreateFragment;
    private ItandSoftwareJobServiceCreateFragment itandSoftwareJobServiceCreateFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myview=inflater.inflate(R.layout.fragment_create, container, false);

        btnHomeService = myview.findViewById(R.id.home_Service_cat);
        btnTuitionService = myview.findViewById(R.id.tution_service);
        btnItanSoftwareJob = myview.findViewById(R.id.it_software_service);

        homeServiceCreateFragment = new HomeServiceCreateFragment();
        tutionServiceCreateFragment = new TutionServiceCreateFragment();
        itandSoftwareJobServiceCreateFragment = new ItandSoftwareJobServiceCreateFragment();

        setFragment(homeServiceCreateFragment);

        btnHomeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(homeServiceCreateFragment);
            }
        });

        btnTuitionService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(tutionServiceCreateFragment);
            }
        });

        btnItanSoftwareJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(itandSoftwareJobServiceCreateFragment);
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
