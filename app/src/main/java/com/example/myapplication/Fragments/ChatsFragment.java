package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapter.UserAdapter;
import com.example.myapplication.Model.Chat;
import com.example.myapplication.Model.User;
import com.example.myapplication.Notifications.Token;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    DatabaseReference reference;
    FirebaseUser fuser;

    private List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats,container,false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        fuser= FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    /* current user vandhu... message anupuravana irundha msg yarku povdho avn peru add pannu */
                    assert chat != null;
                    assert fuser != null;
                    if(chat.getSender().equals(fuser.getUid())) {
                        usersList.add(chat.getReceiver());
                    }
                    /* ilaaa current user vandhu....msg ah receive panravana irundha msg anupunavan peru add pannu */
                    if(chat.getReceiver().equals(fuser.getUid())) {
                        usersList.add(chat.getSender());
                    }

                }

                Set<String> set = new LinkedHashSet<>();
                set.addAll(usersList);
                usersList.clear();
                usersList.addAll(set);

                readChats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    private void readChats() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);

                    for(String id: usersList) {
                        //assert user != null;
                        if(user.getId().equals(id)) {
                            mUsers.add(user);
                        }

                    }
                }
                //here it is declared as isChat to true
                userAdapter = new UserAdapter(getContext(),mUsers,true);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}