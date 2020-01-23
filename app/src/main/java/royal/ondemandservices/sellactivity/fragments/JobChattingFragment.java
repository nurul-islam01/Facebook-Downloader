package royal.ondemandservices.sellactivity.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import royal.ondemandservices.FirebaseConfig;
import royal.ondemandservices.Model.Chat;
import royal.ondemandservices.Model.ProfileData;
import royal.ondemandservices.Model.SellJob;
import royal.ondemandservices.Model.User;
import royal.ondemandservices.R;
import royal.ondemandservices.adapter.ChatsAdapter;
import royal.ondemandservices.interfaces.SellJobsInterface;
import royal.ondemandservices.sellactivity.SellJobsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobChattingFragment extends Fragment implements SellJobsInterface {


    private Context context;
    private SellJob model;
    private List<Chat> chats;

    private Button button_chatbox_send;
    private EditText edittext_chatbox;

    private RecyclerView mMessageRecycler;
    private ChatsAdapter chatsAdapter;

    private String userId;

    public JobChattingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_chatting, container, false);

        userId = FirebaseConfig.getUserId();

        mMessageRecycler = view.findViewById(R.id.reyclerview_message_list);
        edittext_chatbox = view.findViewById(R.id.edittext_chatbox);
        button_chatbox_send = view.findViewById(R.id.button_chatbox_send);

        mMessageRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessageRecycler.setItemAnimator(new DefaultItemAnimator());


        FirebaseConfig.sellChats().child(model.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    chats = new ArrayList<>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        Chat chat = data.getValue(Chat.class);
                        chats.add(chat);
                    }
                    if (chats.size() > 0){
                        chatsAdapter = new ChatsAdapter(getActivity(), chats);
                        mMessageRecycler.setAdapter(chatsAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String text = edittext_chatbox.getText().toString().trim();
                    if (TextUtils.isEmpty(text)){
                        Toast.makeText(getActivity(), "chat empty", Toast.LENGTH_SHORT).show();
                        edittext_chatbox.setError("Empty");
                        return;
                    }else {
                        final String pushKey = FirebaseConfig.sellChats().push().getKey();
                        final String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                        FirebaseConfig.saveUsers().child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    ProfileData user = dataSnapshot.getValue(ProfileData.class);
                                    if (user != null){
                                        String name = user.getName();
                                        Chat chat = new Chat(pushKey, userId,name, text, currentTime);
                                        FirebaseConfig.sellChats().child(model.getId()).child(pushKey).setValue(chat);
                                        edittext_chatbox.setText("");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        ((SellJobsActivity) context).setSellJobsInterface(this);
    }

    @Override
    public void sellJobs(SellJob sellJob) {
        this.model = sellJob;
    }
}
