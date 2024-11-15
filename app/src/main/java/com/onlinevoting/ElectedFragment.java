package com.onlinevoting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class ElectedFragment extends Fragment {
    Long currentDateInInt, currentTimeInInt;
    ArrayList<VotingData> votingData = new ArrayList<>();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_elected, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.fragment_elected_reycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AddedVotesAdapter adapter = new AddedVotesAdapter(getContext(),votingData,"ElectedFragment");
        recyclerView.setAdapter(adapter);
        checkCurrentDataAndTime();
        firestore.collection("votingData").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int i=0;
                votingData.clear();
                adapter.notifyDataSetChanged();
                for(DocumentSnapshot snapshot:value.getDocuments()){
                    VotingData data = snapshot.toObject(VotingData.class);
                    String idOfDocument = snapshot.getId();
                    data.setVoteId(idOfDocument);
                    String dataofWhichUser = data.getUserId();
                    Long dataStartTime = data.getStartTime();
                    Long dataStartDate =data.getStartDate();
                    Long dataEndDate = data.getEndDate();
                    Long dataEndTime = data.getEndTime();
//                    if(currentUserIdOfData.equals(userId)){
//                        listOfData.add(data);
//                        adapter.notifyItemInserted(listOfData.size()-1);
//
//                        Toast.makeText(AddedVotes.this,listOfData.get(i).getOption2(),Toast.LENGTH_LONG).show();
//                        i++;
//                    }
                    // Toast.makeText(getContext(),"i am from firebase",Toast.LENGTH_LONG).show();

                    String userId = FirebaseAuth.getInstance().getUid();
                    FirebaseFirestore ref = FirebaseFirestore.getInstance();

                    ref.collection("votingData")
                            .document(idOfDocument)
                            .collection("electedVoter").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        HashMap<String,Object> map = (HashMap<String, Object>) documentSnapshot.getData();
                                        if(documentSnapshot.exists()){
                                            String id = documentSnapshot.getId();
                                            if(currentDateInInt>=dataStartDate){
                                                data.setSelectedOption(map.get("selectionOptionString").toString());
                                                votingData.add(data);
                                                adapter.notifyItemInserted(votingData.size()-1);
                                                // Toast.makeText(getContext(),"add first option",Toast.LENGTH_LONG).show();

                                            }

                                        }
                                    }
                                }
                            });




                }

            }
        });



        return v;
    }
    private void checkCurrentDataAndTime() {
        // Get current date and time
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Format date and time
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

        String formattedYear = yearFormat.format(currentDate);
        String formattedMonth = monthFormat.format(currentDate);
        String formattedDay = dayFormat.format(currentDate);
        currentDateInInt = (long) (Integer.parseInt(formattedYear)*365+Integer.parseInt(formattedMonth)*30+Integer.parseInt(formattedDay));
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        String formattedHour = hourFormat.format(currentDate);
        String formattedMinute = minuteFormat.format(currentDate);
        currentTimeInInt = (long) (Integer.parseInt(formattedHour)*60+Integer.parseInt(formattedMinute));
    }
}