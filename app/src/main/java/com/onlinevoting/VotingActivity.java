package com.onlinevoting;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.common.value.qual.StringVal;

import java.util.ArrayList;
import java.util.HashMap;

public class VotingActivity extends AppCompatActivity implements RecyclerOptionAdapter.OnItemClickListener {
   String receivedVoteId;
   String voteId;
   String selectedOptionStr;
   static HashMap<String,Object> map = new HashMap<>();
   VotingData votingData = new VotingData();
    TextView strStartDate, strStartTime,strEndDate,strEndTime
            ,question,instruction;
    AppCompatButton confirmVote;
    ArrayList<OptionModel> options = new ArrayList<>();
    ArrayList<OptionModel> updatedData = new ArrayList<>();
    int postion;
    RecyclerView recyclerView;
    String userId = FirebaseAuth.getInstance().getUid();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voting);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        strStartDate = findViewById(R.id.voting_start_date_text_view);
        strStartTime = findViewById(R.id.voting_start_time_text_view);
        strEndDate = findViewById(R.id.voting_end_date_text_view);
        strEndTime = findViewById(R.id.voting_end_time_text_view);
        question = findViewById(R.id.voting_question);
        instruction = findViewById(R.id.voting_instruction);
        confirmVote = findViewById(R.id.voting_confirm_vote);
        confirmVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedOptionStr==null){
                    Toast.makeText(VotingActivity.this,"please select option before upload ",Toast.LENGTH_LONG).show();
                }else {
                    alerDialogForExit();
                }


            }
        });



        Intent intent = getIntent();
        receivedVoteId = intent.getStringExtra("voteId");
        voteId = receivedVoteId;
        if (receivedVoteId != null) {
            // Do something
            DocumentReference reference = firestore.collection("votingData").document(receivedVoteId);
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot snapshot = task.getResult();
                    if(task.isSuccessful()){
                       map = (HashMap<String, Object>) snapshot.getData();
                      // int optionCount = (int) snapshot.getData().get("optionCount");
                       if(map!=null){
                           Toast.makeText(VotingActivity.this,"sucessfully readed",Toast.LENGTH_LONG).show();
                       setData();

                       for (int i = 0; i < (Long)map.get("optionCount"); i++) {

                            String option = getOptionByIndex(map, i);
                           options.add(new OptionModel(R.drawable.edit, option));
                           Log.e("VotingActivity",option);
                       }
                           RecyclerOptionAdapter adapter = new RecyclerOptionAdapter(VotingActivity.this,options,"VotingActivity");
                           adapter.setOnItemClickListner(VotingActivity.this);
                           recyclerView=findViewById(R.id.voting_options_recycler_view);
                           recyclerView.setLayoutManager(new LinearLayoutManager(VotingActivity.this));
                           recyclerView.setAdapter(adapter);

                       }

                    }

                }
            });
//            firestore.collection("votingData").etaddSnapshotListener(new EventListener<QuerySnapshot>() {
//                @Override
//                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//
//                    for(DocumentSnapshot snapshot:value.getDocuments()){
//                        VotingData data = snapshot.toObject(VotingData.class);
//                        String idOfDocument = snapshot.getId();
//                        data.setVoteId(idOfDocument);
//                    }
//                }
//            });
        } else {

        }







    }

    private void alerDialogForExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Carefully");
        builder.setMessage("if you sure to vote click yes" +
                        "if you want to change click no");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String,Object> mmap= new HashMap<>();
                mmap.put("selectedOption",postion);
                mmap.put("selectionOptionString",selectedOptionStr);
                firestore.collection("votingData").document(voteId).collection("electedVoter").document(userId).set(mmap);
                // Exit the app
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void setData() {
        strStartDate.setText((CharSequence) map.get("strStartDate"));
        strStartTime.setText((CharSequence) map.get("strStartTime"));
        strEndDate.setText(map.get("strEndDate").toString());
        strEndTime.setText((CharSequence) map.get("strEndTime"));
        question.setText((CharSequence) map.get("question"));
        instruction.setText((CharSequence) map.get("instruction"));

    }
    private static String getOptionByIndex(HashMap<String,Object> map, int index) {
        switch (index) {
            case 0:
                return map.get("option0").toString();
            case 1:
                return map.get("option1").toString();
            case 2:
                return map.get("option2").toString();
            case 3:
                return map.get("option3").toString();
            case 4:
                return map.get("option4").toString();
            case 5:
                return map.get("option5").toString();
            case 6:
                return map.get("option6").toString();
            case 7:
                return map.get("option7").toString();
            case 8:
                return map.get("option8").toString();
            case 9:
                return map.get("option9").toString();

            // Continue for option3, option4, ..., option9
            default:
                return null; // Handle index out of bounds or other cases
        }
    }


    @Override
    public void onItemClick(ArrayList<OptionModel> data) {
      updatedData = data;
        for(int i=0;i<data.size();i++){
            if(data.get(i).image==R.drawable.check){
                postion =i;
                String str ="option"+String.valueOf(i);
                selectedOptionStr = data.get(i).option;

                Toast.makeText(VotingActivity.this,"perfect"+selectedOptionStr,Toast.LENGTH_LONG).show();

            }
        }
    }
}