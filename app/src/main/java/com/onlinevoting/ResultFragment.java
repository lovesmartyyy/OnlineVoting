package com.onlinevoting;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class ResultFragment extends Fragment {


Long currentDateInInt, currentTimeInInt;
ArrayList<VotingData> votingData = new ArrayList<>();
FirebaseFirestore firestore = FirebaseFirestore.getInstance();
int[] mostmarks = new int[10];
    RecyclerView recyclerView ;

    AddedVotesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_result, container, false);
        adapter = new AddedVotesAdapter(getContext(),votingData,"ResultFragment");
        recyclerView= v.findViewById(R.id.fragment_result_reycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                    if(currentDateInInt>dataEndDate){
                        votingData.add(data);
                      todoSomethigForWhoGetFirst(votingData);

                        adapter.notifyItemInserted(votingData.size()-1);
                       // Toast.makeText(getContext(),"add first option",Toast.LENGTH_LONG).show();

                    }else if(dataEndDate.equals(currentDateInInt)&&dataEndTime<currentTimeInInt){
                        votingData.add(data);
                       todoSomethigForWhoGetFirst(votingData);
                        adapter.notifyItemInserted(votingData.size()-1);
                       // Toast.makeText(getContext(),"second option",Toast.LENGTH_LONG).show();

                    }
                }

            }
        });
        return v;
    }

    private void todoSomethigForWhoGetFirst(ArrayList<VotingData> votingData) {
        int num = votingData.size()-1;
        mostmarks = new int[mostmarks.length];




            String userId = FirebaseAuth.getInstance().getUid();
            FirebaseFirestore ref = FirebaseFirestore.getInstance();

        ref.collection("votingData")
                .document(votingData.get(num).getVoteId())
                .collection("electedVoter").addSnapshotListener(new EventListener<QuerySnapshot>() {


                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            DocumentSnapshot documentSnapshot = snapshot;
                            HashMap<String, Object> map = (HashMap<String, Object>) documentSnapshot.getData();
                            Log.e("error", String.valueOf(map) + snapshot.getId() + "   " + votingData.get(num).voteId);
                            if (map != null) {
                                String id = documentSnapshot.getId();
                                String selectedanswer = map.get("selectionOptionString").toString();
                                Long optionCountt = votingData.get(num).getOptionCount();
                                for (int i = 0; i < optionCountt; i++) {
                                    String optionNumber = "option" + String.valueOf(i);
                                    VotingData vd = votingData.get(num);
                                    String strr = getOptionByIndex(vd, i);

                                    if (strr.equals(selectedanswer)) {
                                        if (String.valueOf(mostmarks[i]).equals(null)) {
                                            int totolVote = mostmarks[i];
                                            mostmarks[i] = ++totolVote;
                                            Log.e("ResultFragment", Arrays.toString(mostmarks));
                                        } else {
                                            int totolVote = mostmarks[i];
                                            mostmarks[i] = ++totolVote;
                                            Log.e("ResultFrament", Arrays.toString(mostmarks));
                                        }

                                    }
                                }

                            }
                        }
                       String winner= checkWinner(votingData.get(num),votingData.size()-1);
                        votingData.get(num).setWinner(winner);
                        adapter.notifyDataSetChanged();
                        Log.e("error",winner);
                        winner ="";
                        mostmarks = new int[mostmarks.length];

                    }
                });


    }

    private String checkWinner(VotingData votingData,int size) {
        if (mostmarks == null || mostmarks.length == 0) {
            throw new IllegalArgumentException("Array must not be empty or null");
        }

        int maxIndex = 0; // Initialize the index of the maximum value as 0
        String winner = "";
        for (int i = 1; i < votingData.optionCount; i++) {
            if (mostmarks[i] > mostmarks[maxIndex]) {
                maxIndex = i; // Update the index of the maximum value
            }
            if(i==votingData.optionCount-1){

                winner =getOptionByIndex(votingData,maxIndex);
                return winner;
            }

        }

        return "noWinner";
    }



    private static String getOptionByIndex(VotingData map, int index) {
        switch (index) {
            case 0:
                return map.option0.toString();
            case 1:
                return map.option1.toString();
            case 2:
                return map.option2.toString();
            case 3:
                return map.option3.toString();
            case 4:
                return map.option4.toString();
            case 5:
                return map.option5.toString();
            case 6:
                return map.option6.toString();
            case 7:
                return map.option7.toString();
            case 8:
                return map.option8.toString();
            case 9:
                return map.option9.toString();

            // Continue for option3, option4, ..., option9
            default:
                return null; // Handle index out of bounds or other cases
        }
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