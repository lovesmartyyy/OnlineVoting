package com.onlinevoting;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {
    ArrayList<OptionModel> options = new ArrayList<>();
    AppCompatButton addOption, updateData;
    EditText optionEditText, question, insturction;
    TextView startDateTextView, startTimeTextView, endDateTextView, endTimeTextView;
    Long startDate, startTime, endDate, endTime;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private final String currentUserUid = auth.getUid();
   // DatabaseReference ref = FirebaseDatabase.getInstance().getReference("votingData");
    FirebaseFirestore fRef = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        startDateTextView = findViewById(R.id.admin_start_date_text_view);
        startTimeTextView = findViewById(R.id.admin_start_time_text_view);
        endDateTextView = findViewById(R.id.admin_end_date_text_view);
        endTimeTextView = findViewById(R.id.admin_end_time_text_view);


        question = findViewById(R.id.admin_question);
        insturction = findViewById(R.id.admin_instruction);
        optionEditText = findViewById(R.id.admin_option_editText);
        addOption = findViewById(R.id.admin_add_option_buttom);
        updateData = findViewById(R.id.admin_update_dataa);
        RecyclerView optionRecycler = findViewById(R.id.admin_recycler_view);
        optionRecycler.setLayoutManager(new LinearLayoutManager(this));
        OptionModel model = new OptionModel(R.drawable.delete, "lovemee like you dooo");
        options.add(model);
        options.add(new OptionModel(R.drawable.delete, "second love me like you do"));

        RecyclerOptionAdapter adapter = new RecyclerOptionAdapter(AdminActivity.this, options);
        optionRecycler.setAdapter(adapter);

        addOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option = optionEditText.getText().toString();
                options.add(new OptionModel(R.drawable.delete, option));
                adapter.notifyItemInserted(options.size() - 1);
                optionEditText.setText("");
            }
        });
        

           updateData.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   if (checkempty()) {
                       updateData();


                   }
               }
           });




    }

    private boolean checkempty() {
        if (question.getText().toString().isEmpty()) {
            Toast.makeText(this, "enter question ", Toast.LENGTH_LONG).show();
            question.setHint("enter your question please");
            return false;
        } else if (insturction.getText().toString().isEmpty()) {
            insturction.setHint("please enter the instruction");
            Toast.makeText(this, "enter instruction ", Toast.LENGTH_LONG).show();
            insturction.setHint("enter your instruction please");
            return false;

        } else if (startDateTextView.getText().toString().isEmpty()) {
            Toast.makeText(this, "enter  start date ", Toast.LENGTH_LONG).show();
            return false;


        } else if (startTimeTextView.getText().toString().isEmpty()) {
            Toast.makeText(this, "enter start time please ", Toast.LENGTH_LONG).show();
            return false;
        } else if (endDateTextView.getText().toString().isEmpty()) {
            Toast.makeText(this, "enter end date please ", Toast.LENGTH_LONG).show();
            return false;

        } else if (endTimeTextView.getText().toString().isEmpty()) {
            Toast.makeText(this, "enter your end time please ", Toast.LENGTH_LONG).show();
            return false;

        } else if ((options.size() <= 2)) {
            optionEditText.setHint("enter more options");
            Toast.makeText(this, "enter more option ", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (checkDateAndTime()){
            return true;
        }
       return false;

    }

    private boolean checkDateAndTime() {
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
        Long currentDateInInt = (long) (Integer.parseInt(formattedYear)*365+Integer.parseInt(formattedMonth)*30+Integer.parseInt(formattedDay));
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        String formattedHour = hourFormat.format(currentDate);
        String formattedMinute = minuteFormat.format(currentDate);
        Long currentTimeInInt = (long) (Integer.parseInt(formattedHour)*60+Integer.parseInt(formattedMinute));




        if(currentDateInInt>startDate) {
            Toast.makeText(this, "increase date to minimum current date", Toast.LENGTH_LONG).show();
           return false;
        }else if(currentDateInInt.equals(startDate)){
           // Toast.makeText(this,"equal",Toast.LENGTH_LONG).show();
            if(currentTimeInInt>startTime){
                Toast.makeText(this,"increase time to current time++",Toast.LENGTH_LONG).show();
                return false;

            }
        }else if((currentDateInInt+30)<startDate){
            Toast.makeText(this,"date between current date or currentdate+30days only",Toast.LENGTH_LONG).show();
            return false;

        }
        if(endDate<startDate){
            Toast.makeText(AdminActivity.this,"increase date to more than equal to start date", Toast.LENGTH_LONG).show();
            return false;

        }else if(endDate.equals(startDate)){

            if(endTime<startTime){
                Toast.makeText(this,"increase time to starttime+30minutes",Toast.LENGTH_LONG).show();
                return false;

            }
        }else if(endDate-30>startDate){
            Toast.makeText(this,"decrease date to  startDate+30days",Toast.LENGTH_LONG).show();
            return false;

        }
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            date = LocalDate.now();
//        }
//        String dateStr = date.toString();
//
//        Toast.makeText(this,String.format("%s %s",dateStr,formattedTime), Toast.LENGTH_SHORT).show();
//         int compareStartDateToCurrentDate =formattedDate.compareTo(startDateTextView.getText().toString());
//         int compareStartDateToEndDate =(startDateTextView.getText().toString()).compareTo(endDateTextView.getText().toString());
//
//         if(compareStartDateToCurrentDate>0) {
//             Toast.makeText(this,String.format("increase date to current date or more to curent date"), Toast.LENGTH_SHORT).show();
//         }else if(compareStartDateToEndDate<0){
//             Toast.makeText(this,String.format("enter minimum one day gap between start and end date"), Toast.LENGTH_SHORT).show();
//         }
       //

        // Display date and time


        return true;
    }

    private void updateData() {
      String questionn = question.getText().toString();
      String instruction = insturction.getText().toString();

      //String idOfNode = ref.push().getKey();
       // DocumentReference documentReference =fRef.collection("votingData").document();

     // String  fRefId =  fRef.collection("votingData").document().getId();
      HashMap<String, Object> map = new HashMap<>();
      map.put("question",questionn);
      map.put("instruction",instruction);
      map.put("userId",currentUserUid);
     // ref.child(idOfNode).child("question").setValue(questionn);
     // ref.child(idOfNode).child("instruction").setValue(instruction);
    //  ref.child(idOfNode).child("userId").setValue(currentUserUid);
      for(int i=0;i<options.size();i++){

          String option =String.format("option%d",i);
          map.put(option,options.get(i).getOption());
          //ref.child(idOfNode).child(option).setValue(options.get(i).getOption());
      }
      map.put("optionCount",options.size());
      String strStartDate = startDateTextView.getText().toString();
      String strStartTime = startTimeTextView.getText().toString();
      String stEndDate = endDateTextView.getText().toString();
      String strEndTime = endTimeTextView.getText().toString();
      map.put("startDate",startDate);
      map.put("startTime",startTime);
      map.put("endDate",endDate);
      map.put("endTime",endTime);
      map.put("strStartDate",strStartDate);
      map.put("strStartTime",strStartTime);
      map.put("strEndDate",stEndDate);
      map.put("strEndTime",strEndTime);
        DocumentReference reference = fRef.collection("votingData").document();


                reference.set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle successful data upload
                        if(AdminActivity.this !=null) {
                            Toast.makeText(AdminActivity.this, "Data uploaded successfully", Toast.LENGTH_LONG).show();
                            question.setText("");
                            insturction.setText("");
                            startDateTextView.setText("");
                            startTimeTextView.setText("");
                            endDateTextView.setText("");
                            endTimeTextView.setText("");
                        }else {
                            Log.e("adminactivity","contextofadmin is null");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure to upload data
                        if(AdminActivity.this!=null) {
                            Toast.makeText(AdminActivity.this, "Error uploading data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }else {
                            Log.e("AdminActivity", "Error uploading data", e);
                        }
                    }
                });


        // fRef.collection("votingData").document(fRefId).set(map);
//      ref.child(idOfNode).child("startDate").setValue(startDate);
//      ref.child(idOfNode).child("startTime").setValue(startTime);
//      ref.child(idOfNode).child("endDate").setValue(endDate);
//      ref.child(idOfNode).child("endTime").setValue(endTime);


//      userRef.child(currentUserUid).child("addedVotes").child("index").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//          @Override
//          public void onComplete(@NonNull Task<DataSnapshot> task) {
//              DataSnapshot dataSnapshot = task.getResult();
//
//             String voteId = "vote";
//             Toast.makeText(AdminActivity.this,voteId,Toast.LENGTH_LONG).show();
//             userRef.child(currentUserUid).child("addedVotes").child(voteId).setValue(idOfNode);
//              userRef.child(currentUserUid).child("addedVotes").child("index").setValue("");
//          }
//      });



    }

    public void onClick(View v){
        if(v.getId()==R.id.admin_start_date_image_view)
            setDate(startDateTextView);
        else if(v.getId()==R.id.admin_end_date_image_view)
            setDate(endDateTextView);
        else if(v.getId()==R.id.admin_start_time_image_view)
            setTime(startTimeTextView);
        else if(v.getId()==R.id.admin_end_time_image_view)
            setTime(endTimeTextView);

   }

    private void setTime(TextView textView) {
        // Get current date and time
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        String formattedHour = hourFormat.format(currentDate);
        String formattedMinute = minuteFormat.format(currentDate);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(textView==findViewById(R.id.admin_start_time_text_view)){
                    startTime = (long) (hourOfDay*60+minute);

                }else{
                   // Toast.makeText(AdminActivity.this,"endtime",Toast.LENGTH_LONG).show();
                    endTime = (long) (hourOfDay*60+minute);
                }
                String time = String.valueOf(hourOfDay+":"+minute);
                textView.setText(time);

            }
        },Integer.parseInt(formattedHour),Integer.parseInt(formattedMinute),true);
        timePickerDialog.show();

    }

    private void setDate(TextView textView) {
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                if(textView==findViewById(R.id.admin_start_date_text_view)){
                    startDate = (long) (year*365+month*30+dayOfMonth);
                }else{
                    endDate = (long) (year*365+month*30+dayOfMonth);
                }
              String  date = String.valueOf(dayOfMonth+"/"+month+"/"+year);
              textView.setText(date);
            }
        },Integer.parseInt(formattedYear),Integer.parseInt(formattedMonth)-1,Integer.parseInt(formattedDay));
        datePickerDialog.show();
    }
}