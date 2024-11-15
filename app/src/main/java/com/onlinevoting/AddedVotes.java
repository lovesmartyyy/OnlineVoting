package com.onlinevoting;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEvent;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddedVotes extends AppCompatActivity {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getUid();
    ArrayList<VotingData> listOfData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_added_votes);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        RecyclerView recylerView = findViewById(R.id.added_votes_recycler_view);
        recylerView.setLayoutManager(new LinearLayoutManager(AddedVotes.this));
        AddedVotesAdapter adapter = new AddedVotesAdapter(AddedVotes.this,listOfData,"AddedVotes");
        recylerView.setAdapter(adapter);
        firestore.collection("votingData").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int i=0;
                for(DocumentSnapshot snapshot:value.getDocuments()){
                    VotingData data = snapshot.toObject(VotingData.class);
                    String idOfDocument = snapshot.getId();
                    data.setVoteId(idOfDocument);
                    String currentUserIdOfData = data.getUserId();
                    if(currentUserIdOfData.equals(userId)){
                        listOfData.add(data);
                        adapter.notifyItemInserted(listOfData.size()-1);

                        Toast.makeText(AddedVotes.this,listOfData.get(i).getOption2(),Toast.LENGTH_LONG).show();
                        i++;
                    }
                }

            }
        });








    }


}