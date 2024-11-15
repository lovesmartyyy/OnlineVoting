package com.onlinevoting;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {
    ArrayList<VotingData> votingData = new ArrayList<>();

    Long currentDateInInt, currentTimeInInt,startDate,startTime,endDate,endTime;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance("https://online-voting-a8615-default-rtdb.firebaseio.com")
            .getReference("users");
    TextView name, email, gender,dob;
    CircleImageView image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        image = v.findViewById(R.id.home_fragment_profile_pic);
        name = v.findViewById(R.id.fragment_home_name);
        email = v.findViewById(R.id.fragment_home_email);
        gender = v.findViewById(R.id.framgment_home_gender);
        dob = v.findViewById(R.id.fragment_home_dob);
        setData();
        checkCurrentDataAndTime();
        RecyclerView recyclerView = v.findViewById(R.id.fragment_home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AddedVotesAdapter adapter = new AddedVotesAdapter(getContext(),votingData,"HomeFragment");
        recyclerView.setAdapter(adapter);
        String userId = FirebaseAuth.getInstance().getUid();



        
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
                    //Toast.makeText(getContext(),"i am from firebase",Toast.LENGTH_LONG).show();
                    if(dataStartDate<currentDateInInt&&currentDateInInt<dataEndDate){
                        votingData.add(data);
                        adapter.notifyItemInserted(votingData.size()-1);
                        //Toast.makeText(getContext(),"add first option",Toast.LENGTH_LONG).show();

                    }else if(dataStartDate.equals(currentDateInInt)&&dataStartTime<currentTimeInInt){
                        votingData.add(data);
                        adapter.notifyItemInserted(votingData.size()-1);
                        //Toast.makeText(getContext(),"second option",Toast.LENGTH_LONG).show();

                    }else if(dataEndDate.equals(currentDateInInt)&&dataEndTime>currentTimeInInt&&currentTimeInInt>dataStartTime){
                        votingData.add(data);
                        adapter.notifyItemInserted(votingData.size()-1);
                       // Toast.makeText(getContext()," third option",Toast.LENGTH_LONG).show();

                    }
                }

            }
        });



        return v;
    }

    private void setData() {

         HashMap<String,Object> mapp = UserDataHolder.getInstance().getSharedData();

        if (mapp !=null) {
            name.setText(mapp.get("name").toString());
            email.setText(mapp.get("email").toString());
            gender.setText(mapp.get("gender").toString());
            dob.setText(mapp.get("dob").toString());
        }
        Bitmap bitmapImage = UserDataHolder.getInstance().getBitmapUserImage();
        if(bitmapImage!=null){
            image.setImageBitmap(bitmapImage);
        }else{

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setData();
                }
            },100);
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